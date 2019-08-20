package com.otec.foodsafety.web.sensor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.constant.EventTypeConstants;
import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.sensor.SensorInfo;
import com.otec.foodsafety.entity.sensor.SensorMonitor;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.sensor.SensorInfoService;
import com.otec.foodsafety.service.sensor.SensorMonitorService;
import com.otec.foodsafety.service.system.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/sensor")
public class SyncController {
    protected static Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private SensorMonitorService sensorMonitorService;
    @Autowired
    private SensorInfoService sensorInfoService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private AlarmService alarmService;

    @RequestMapping(value = "sync/monitor", method = RequestMethod.POST)
    public void syncMonitor(HttpServletRequest request, HttpServletResponse response) {
        try {
//			byte[] msgBytes = IOUtils.toByteArray(request.getInputStream());
//			String content = new String(msgBytes, "utf-8");
            String content = request.getParameter("jsonData");
            if(StringUtils.equalsIgnoreCase(content, "close")){
                return;
            }
            System.out.println("--------data="+content);
            //{"t": "30", "id": "th001", "h": "34"}
            JSONObject jsonObject = JSON.parseObject(content);
            String type = jsonObject.getString("type");
            if(StringUtils.equalsIgnoreCase(type, "1")){
                handleSensor1(jsonObject, content);
            }else if(StringUtils.equalsIgnoreCase(type, "2")){
                handleSensor2(jsonObject, content);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 温湿度传感器处理
    private void handleSensor1(JSONObject jsonObject, String content){
        SensorMonitor sensorMonitor = new SensorMonitor();
        sensorMonitor.setMsg(content);
        sensorMonitor.setHumidity(jsonObject.getFloat("t"));//温度
        sensorMonitor.setDampness(jsonObject.getFloat("h"));//湿度
        if(sensorMonitor.getHumidity() == null || sensorMonitor.getDampness() == null){
            logger.warn("监控数据非法："+content);
            return;
        }
        sensorMonitor.setCreateTime(new Date());
        sensorMonitor.setSensorNo(jsonObject.getString("id"));

        sensorMonitor.setLat(jsonObject.getString("lat"));
        sensorMonitor.setLon(jsonObject.getString("lon"));

        Map<String, String> eventMap = sysDictService.getDetailValueMap("IOT报警类型");
//            System.out.println(eventMap);{温度低=10002, 温度高=10001, 湿度低=10004, 湿度高=10003}
        Map<String, Object> map = Maps.newHashMap();
        int alarmType = 0;
        if(StringUtils.isNotBlank(sensorMonitor.getSensorNo())){
            map.put("sensorNo", sensorMonitor.getSensorNo());
            List<SensorInfo> sensorInfoList = sensorInfoService.findEntitysByCondition(map);
            if(sensorInfoList.size() > 0){
                SensorInfo sensorInfo = sensorInfoList.get(0);
                if(sensorInfo.getHumidityHigh() != null){
                    if(sensorMonitor.getHumidity() > sensorInfo.getHumidityHigh()){
                        saveAlarm("10001", sensorInfo, eventMap, sensorMonitor.getHumidity());
                        alarmType++;
                    }
                }
                if(sensorInfo.getHumidityLow() != null){
                    if(sensorMonitor.getHumidity() < sensorInfo.getHumidityLow()){
                        saveAlarm("10002", sensorInfo, eventMap, sensorMonitor.getHumidity());
                        alarmType++;
                    }
                }
                if(sensorInfo.getDampnessHigh() != null){
                    if(sensorMonitor.getDampness() > sensorInfo.getDampnessHigh()){
                        saveAlarm("10003", sensorInfo, eventMap, sensorMonitor.getDampness());
                        alarmType = alarmType + 10;
                    }
                }
                if(sensorInfo.getDampnessLow() != null){
                    if(sensorMonitor.getDampness() < sensorInfo.getDampnessLow()){
                        saveAlarm("10004", sensorInfo, eventMap, sensorMonitor.getDampness());
                        alarmType = alarmType + 10;
                    }
                }

            }
        }
        Date createTime = sensorMonitor.getCreateTime();
        DateTime dateTime = new DateTime(createTime);
        int sec = dateTime.getSecondOfMinute();
        if(alarmType > 0){
            if(alarmType == 1){
                sensorMonitor.setWdAlarmSec(sec+60);
                sensorMonitor.setSdAlarmSec(sec);
            }else if(alarmType == 10){
                sensorMonitor.setWdAlarmSec(sec);
                sensorMonitor.setSdAlarmSec(sec+60);
            }else if(alarmType == 11){
                sensorMonitor.setWdAlarmSec(sec+60);
                sensorMonitor.setSdAlarmSec(sec+60);
            }
        }else {
            sensorMonitor.setWdAlarmSec(sec);
            sensorMonitor.setSdAlarmSec(sec);
        }
        sensorMonitorService.persist(sensorMonitor);
    }

    //水浸传感器处理
    private void handleSensor2(JSONObject jsonObject, String content){
        String sensorNo = jsonObject.getString("id");
        String alarmContent = jsonObject.getString("s");
        Alarm alarm = new Alarm();
        alarm.setIndexCode(sensorNo);
        alarm.setContent(alarmContent);
        alarm.setEventId("10005");
        alarm.setEventType(EventTypeConstants.SENSOR);
//        alarm.setAlarmType(alarmName);
        alarm.setAlarmTime(new Date());
        alarm.setAlarmSource("水浸报警");
        alarm.setStatus("1");
        alarm.setCreateTime(new Date());
        alarmService.persist(alarm);
    }

    private void saveAlarm(String eventType, SensorInfo sensorInfo, Map<String, String> eventMap, float value){

        String alarmName = eventMap.get(eventType);
        if(StringUtils.isBlank(alarmName)){
            alarmName = "未知的报警类型";
        }
        //当前温度30°C，预设温度高25°C、低10°C，超过高温预警。
        String content = null;
        if(StringUtils.equalsIgnoreCase(eventType, "10001")){
            content = "当前温度"+value+"°C, 预设温度高"+sensorInfo.getHumidityHigh()+"°C 低"+sensorInfo.getHumidityLow()+"°C, 超过高温预警。";
        }else if(StringUtils.equalsIgnoreCase(eventType, "10002")){
            content = "当前温度"+value+"°C, 预设温度高"+sensorInfo.getHumidityHigh()+"°C 低"+sensorInfo.getHumidityLow()+"°C, 低于高温预警。";
        }else if(StringUtils.equalsIgnoreCase(eventType, "10003")){
            content = "当前湿度"+value+"%, 预设湿度高"+sensorInfo.getDampnessHigh()+"% 低"+sensorInfo.getDampnessLow()+"%, 超过湿度预警。";
        }else if(StringUtils.equalsIgnoreCase(eventType, "10004")){
            content = "当前湿度"+value+"%, 预设湿度高"+sensorInfo.getDampnessHigh()+"% 低"+sensorInfo.getDampnessLow()+"%, 低于湿度预警。";
        }
        String sensorNo = sensorInfo.getSensorNo();
        Alarm alarm = new Alarm();
        alarm.setIndexCode(sensorNo);
        alarm.setContent(content);
        alarm.setEventId(eventType);
        alarm.setEventType(EventTypeConstants.SENSOR);
//        alarm.setAlarmType(alarmName);
        alarm.setAlarmTime(new Date());
        alarm.setAlarmSource("传感器报警");
        alarm.setStatus("1");
        alarm.setCreateTime(new Date());

        Map<String, Object> msgParam = Maps.newHashMap();
        msgParam.put("sensorNo", sensorNo);
        msgParam.put("orderByClause", "CREATE_TIME desc limit 1");
        boolean isNew = false;
        List<SensorMonitor> lastMsgList = sensorMonitorService.findEntitysByCondition(msgParam);
        if(lastMsgList.size() == 0){
            isNew = true;
        }else {
            SensorMonitor lastMsg = lastMsgList.get(0);
            if(lastMsg.getHumidity() > sensorInfo.getHumidityHigh() || lastMsg.getHumidity() < sensorInfo.getHumidityLow()
                    || lastMsg.getDampness() > sensorInfo.getDampnessHigh() || lastMsg.getDampness() < sensorInfo.getDampnessLow()){
                isNew = false;
            }else {
                isNew = true;
            }
        }

        Map<String, Object> param = Maps.newHashMap();
        param.put("sensorNo", sensorNo);
        param.put("eventId", alarm.getEventId());
        param.put("startDate", new Date());
        param.put("endDate", new Date());
        param.put("orderByClause", "CREATE_TIME desc limit 1");
        List<Alarm> olds = alarmService.findEntitysByCondition(param);
        if(isNew || olds.size() == 0){
            alarm.setUpdateTime(new Date());
            alarmService.persist(alarm);
            alarmService.sendByAlarm(alarm);
        }else {
            Alarm old = olds.get(0);
            old.setUpdateTime(new Date());
            alarmService.mergeById(old);
        }

    }

    @RequestMapping(value = "sync/alarm", method = RequestMethod.POST)
    public void syncAlarm(HttpServletRequest request, HttpServletResponse response) {
        try {
//			byte[] msgBytes = IOUtils.toByteArray(request.getInputStream());
//			String content = new String(msgBytes, "utf-8");
            String content = request.getParameter("jsonData");
            System.out.println("--------alarm data="+content);
            JSONObject jsonObject = JSON.parseObject(content);
            Alarm alarm = new Alarm();
            alarm.setEventType(EventTypeConstants.CAMERA);
            alarm.setIndexCode(jsonObject.getString("camera_num"));
            alarm.setEventId(jsonObject.getString("event_type"));
            alarm.setAlarmSource("摄像头报警");
            Map<String, String> eventMap = sysDictService.getDetailValueMap("摄像头报警类型");
            String c = eventMap.get(jsonObject.getString("event_type"));
            if(StringUtils.isNotBlank(c)){
                alarm.setContent(c);
            }else {
                alarm.setContent("未知报警");
            }

            alarm.setAlarmTime(jsonObject.getDate("event_time"));
            alarm.setVideoUrl(jsonObject.getString("event_video"));
            alarm.setImgUrl(jsonObject.getString("result_image"));
            alarm.setStatus("1");
            alarm.setCreateTime(new Date());
            alarm.setMsg(content);
            alarmService.persist(alarm);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "sync/list", method = RequestMethod.GET)
    @ResponseBody
    public List<SensorInfo> list(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = new PageObject();
        po.setCurrPage(1);
        po.setPageSize(10000);
        po.getCondition().putAll(params);
        po.getCondition().put("status", "1");

        GridDataModel model = sensorInfoService.getGridDataByCondition(po);
        List<SensorInfo> list = (List<SensorInfo> )model.getRows();
        return list;

    }
}
