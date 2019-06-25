package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.sensor.SensorMonitor;
import com.otec.foodsafety.service.sensor.SensorInfoService;
import com.otec.foodsafety.service.sensor.SensorMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/enterprise/sensor")
@RestController
public class SensorInterface  extends BaseInterface {


    @Autowired
    SensorInfoService sensorInfoService;
    @Autowired
    SensorMonitorService sensorMonitorService;


    /**
     * 获取传感器列表
     * @param params
     * @return
     */
    @RequestMapping(value="/query" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        po.getCondition().put("status", "1");
        GridDataModel model = sensorInfoService.getGridDataByCondition(po);
        return model;
    }

    /**
     * 获取传感器列表及最新温湿度
     * @param params
     * @return
     */
    @RequestMapping(value="/currentMonitor/query" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel listCurrentMonitor(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        po.getCondition().put("status", "1");
        GridDataModel model = sensorInfoService.getListCurrentMonitor(po);
        return model;
    }

    /**
     *  获取当日温湿度数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @ResponseBody
    public List<SensorMonitor> report(@RequestParam Map<String, String> params) {
        return sensorMonitorService.getReport(params.get("sensorNo"));
    }

    /**
     *  获取传感器最新温湿度数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCurrentMonitor", method = RequestMethod.GET)
    @ResponseBody
    public SensorMonitor getCurrentMonitor(@RequestParam Map<String, String> params) {

        List<SensorMonitor> list = sensorMonitorService.getCurrentMonitor(params.get("sensorNo"));
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 报警传感器温湿度监控统计
     * @param params  alarmId  报警编号
     *
     * @return
     */
    @RequestMapping(value = "/alarmReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> alarmReport(@RequestParam Map<String, String> params) {
        return sensorMonitorService.getAppReportByAlarm(Long.valueOf(params.get("alarmId")));
    }


}
