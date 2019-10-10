package com.otec.foodsafety.api.supervise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.catering.StaffCertificateService;
import com.otec.foodsafety.service.enterprise.EnterpriseCertificateService;
import com.otec.foodsafety.service.sensor.SensorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预警统计接口
 */
@RestController
@RequestMapping("/api/warning/statistics")
public class WarningStatisticsInterface extends BaseInterface {

    @Autowired
    EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    StaffCertificateService staffCertificateService;

    @Autowired
    private SensorInfoService sensorInfoService;

    @Autowired
    private AlarmService alarmService;


    /**
     * 安全预警--统计页面
     */
    @RequestMapping(value = "/enterprise/warningStatistics/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Integer> warningStatistics(@RequestParam Map<String, String> params,@PathVariable String enterpriseId) throws ParseException {


        Map<String, Integer> datamap = new HashMap<>();

        PageObject po =null;
        //企业预警 证件预警 查询已过期和即将过期的企业证照 （指定企业）
        params.put("enterpriseId",enterpriseId);
        params.put("certificateStatus","1");

        params.put("certificateType", "1"); // 1.营业执照；2.食品生产许可证；3.餐饮服务许可证
        params.put("validStatus","2");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total1 = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        datamap.put("enterpriseCertificate12",total1);//营业执照-即将过期


        params.put("certificateType", "1"); // 1.营业执照；2.食品生产许可证；3.餐饮服务许可证
        params.put("validStatus","3");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total2 = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        datamap.put("enterpriseCertificate13",total2);//营业执照-已过期


        params.put("certificateType", "2"); // 1.营业执照；2.食品生产许可证；3.餐饮服务许可证
        params.put("validStatus","2");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total3 = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        datamap.put("enterpriseCertificate22",total3);//食品生产许可证-即将过期


        params.put("certificateType", "2"); // 1.营业执照；2.食品生产许可证；3.餐饮服务许可证
        params.put("validStatus","3");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total4 = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        datamap.put("enterpriseCertificate23",total4);//食品生产许可证-已过期


        params.put("certificateType", "3"); // 1.营业执照；2.食品生产许可证；3.餐饮服务许可证
        params.put("validStatus","2");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total5 = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        datamap.put("enterpriseCertificate32",total5);//餐饮服务许可证-即将过期


        params.put("certificateType", "3"); // 1.营业执照；2.食品生产许可证；3.餐饮服务许可证
        params.put("validStatus","3");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total6 = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        datamap.put("enterpriseCertificate33",total6);//餐饮服务许可证-已过期




        //2-企业预警 员工健康证---查询已过期和即将过期的员工健康证（指定企业
        params.put("certificateStatus",null);
        params.put("certificateType", null);

        params.put("enterpriseId",enterpriseId);
        params.put("validStatus","2");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total7 = staffCertificateService.getPageCount(params);
        datamap.put("staffCertificate2",total7);//健康证-即将过期


        params.put("validStatus","3");//1:正常   2：即将过期  3：已过期
        po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total8 = staffCertificateService.getPageCount(params);
        datamap.put("staffCertificate3",total8);//健康证-已过期



//        //3---行为预警
//        datamap.put("behaviorWarning1",total8);//未戴口罩
//        datamap.put("behaviorWarning2",total8);//未穿工作服
//        datamap.put("behaviorWarning3",total8);//吸烟
//
//
//        //4---温湿度预警
//        datamap.put("temperatureWarning1",total8);//温度
//        datamap.put("temperatureWarning2",total8);//湿度
//        datamap.put("temperatureWarning3",total8);//水浸



//		69	报警类型	报警类型 10001温度高10002温度低10003湿度高10004湿度低	10005水浸
//		69	摄像头报警类型	1未戴安全帽2未戴口罩3未穿工作装4抽烟5老鼠出没6陌生人进入

        String warning_items_show[] = {"behaviorWarning1","behaviorWarning2","behaviorWarning3",
                "temperatureWarning1","temperatureWarning2","temperatureWarning3","temperatureWarning4","temperatureWarning5"};//预警项
//        String warning_items[] = {"未戴口罩","未穿工作装","抽烟",
//                "温度高","温度低","湿度高","湿度低","水浸"};//预警项
        String event_ids[] = {"2","3","4","10001","10002","10003","10004","10005"};//item编码
        String event_types[] = {"1","1","1","2","2","2","2","2"};//预警项  type 1-视频，2-传感器


        for (int i = 0; i <event_ids.length ; i++) {
            String event_id = event_ids[i];
            String event_type = event_types[i];

//			2.循环查询数据
//			SELECT count(1) FROM V_ALARM  where event_type = ${event_type} and EVENT_ID=${event_id}  and STATUS=?
//			EVENT_TYPE  1:摄像头 2:传感器
//			EVENT_ID    对应报警类型  温度10001 10002 湿度为10003  10004
//			STATUS


            Map mapAlerm = new HashMap<String, Object>();
            mapAlerm.put("enterpriseId",enterpriseId);
            mapAlerm.put("eventId", event_id);
            mapAlerm.put("eventType",event_type);
            mapAlerm.put("status","1");//1-未处理
            mapAlerm.put("startDate", this.getTwoMonthBefore());
            Integer un_deal = alarmService.countAlarmByCondition(mapAlerm);
            datamap.put(warning_items_show[i], un_deal);
        }
        return datamap;
    }



    /**
     * 行为报警明细
     */
    @RequestMapping(value = "/enterprise/behaviorWarningDetail/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel behaviorWarningDetail(@RequestParam Map<String, String> params,@PathVariable String enterpriseId
    ) throws ParseException {
        params.put("startDate", this.getTwoMonthBefore());
        params.put("status","1");//1-未处理
        params.put("enterpriseId",enterpriseId);
        params.put("extendSql", "and event_id in (2,3,4) and event_type=1 ");
        // 查询列表数据
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = alarmService.getGridDataModelByCondition(po);
        return model;
    }


    /**
     * 温湿度---报警明细
     */
    @RequestMapping(value = "/enterprise/temperatureWarningDetail/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel temperatureWarningDetail(@RequestParam Map<String, String> params,@PathVariable String enterpriseId
    ) throws ParseException {

        params.put("startDate", this.getTwoMonthBefore());
        params.put("status","1");//1-未处理
        params.put("enterpriseId",enterpriseId);
        params.put("extendSql", "and event_id in (10001,10002,10003,10004,10005) and event_type=2 ");
        // 查询列表数据
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = alarmService.getGridDataModelByCondition(po);
        return model;
    }


    // 获取当前日期2个月前的时间
    private  String getTwoMonthBefore()throws ParseException {

        // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(calendar.MONTH, -2); //设置为前2月，可根据需求进行修改
        Date date = calendar.getTime();//获取2个月前的时间

        return dateFormat.format(date);
    }




}
