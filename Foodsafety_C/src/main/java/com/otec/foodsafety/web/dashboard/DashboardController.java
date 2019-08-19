package com.otec.foodsafety.web.dashboard;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.collect.AlarmTotal;
import com.otec.foodsafety.entity.credit.CreditRecord;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordMorningCheck;
import com.otec.foodsafety.entity.safety.RevisitResult;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.catering.StaffCertificateService;
import com.otec.foodsafety.service.collect.AlarmTotalService;
import com.otec.foodsafety.service.credit.CreditRecordService;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.enterprise.EnterpriseCertificateService;
import com.otec.foodsafety.service.enterprise.EnterpriseRegisterService;
import com.otec.foodsafety.service.enterprise.EnterpriseSupervisionService;
import com.otec.foodsafety.service.evaluate.ComplainEvaluateService;
import com.otec.foodsafety.service.producesafety.RecordMorningCheckService;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import com.otec.foodsafety.service.safety.InspectionResultItemService;
import com.otec.foodsafety.service.safety.RevisitPlanService;
import com.otec.foodsafety.service.safety.RevisitResultService;
import com.otec.foodsafety.service.supervisory.ResponsibilityGridService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.DateUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/11/13 0013 上午 11:12
 */
@RestController
@RequestMapping("api/dashboard")
public class DashboardController extends VueBaseController<RevisitResultService, RevisitResult, Long> {
    @Autowired
    ResponsibilityGridService responsibilityGridService;
    @Autowired
    EnterpriseSupervisionService enterpriseSupervisionService;
    @Autowired
    private StaffCertificateService staffCertificateService;
    @Autowired
    private EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    InspectionPlanService inspectionPlanService;
    @Autowired
    RevisitPlanService revisitPlanService;
    @Autowired
    private EnterpriseBaseService enterpriseBaseService;
    @Autowired
    EnterpriseRegisterService enterpriseRegisterService;
    @Autowired
    CreditRecordService creditRecordService;
    @Autowired
    InspectionResultItemService inspectionResultItemService;
    @Autowired
    RecordMorningCheckService recordMorningCheckService;
    @Autowired
    AlarmService alarmService;
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    DateLocalService dateLocalService;
    @Autowired
    ComplainEvaluateService complainEvaluateService;
    @Autowired
    AlarmTotalService alarmTotalService;

    /**
     * 根据条件查询网格下的企业统计数据
     * @param areaId
     * @return
     */
    @RequestMapping(value = "/grid/enterpriseStatistics", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ResponsibilityGrid> getGridsEnterpriseStatistics(@RequestParam(required = false) Long areaId) {
        Map param = new HashMap();
        param.put("areaId",areaId);
        List<ResponsibilityGrid> list=responsibilityGridService.findEntitysByCondition(param);
        List<Map<String,Object>> result = new ArrayList<>();
        for(ResponsibilityGrid grid :list){
            Map<String,Object> map = new HashMap<>();
            map.put("grid",grid);
            Map<String,Object> param1 = new HashMap<>();
            param1.put("gridId",grid.getGridId());
            param1.put("extendSql", "AND REGULATORY_LEVEL IS NOT NULL ");
            int count = enterpriseSupervisionService.countByCondition(param1);
            map.put("count",count);
            result.add(map);
        }
        return new ObjectRestResponse<ResponsibilityGrid>().rel(true).data(result);
    }

    /**
     * 根据条件查询网格下的预警统计数据
     * @param areaId
     * @return
     */
    @RequestMapping(value = "/grid/certificateStatistics", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ResponsibilityGrid> getGridsCertificateStatistics(@RequestParam(required = false) Long areaId) {
        Map param = new HashMap();
        param.put("areaId",areaId);
        List<ResponsibilityGrid> list=responsibilityGridService.findEntitysByCondition(param);
        List<Map<String,Object>> result = new ArrayList<>();
        for(ResponsibilityGrid grid :list){
            Map<String,Object> map = new HashMap<>();
            map.put("grid",grid);
            Map<String,String> param1 = new HashMap<>();
            param1.put("validStatus", "2");//即将过期
            param1.put("certificateStatus", "1");
            param1.put("certificateType", "3"); //餐饮服务许可证
            param1.put("gridId",grid.getGridId()+"");
            int count_cert_staff = staffCertificateService.getPageCount(param1);
            int count_cert_enterprise = enterpriseCertificateService.getEnterpriseCertificatePageCount(param1);
            map.put("count",count_cert_staff+count_cert_enterprise);
            result.add(map);
        }
        return new ObjectRestResponse<ResponsibilityGrid>().rel(true).data(result);
    }

    /**
     * 根据条件查询网格下的事务统计数据
     * @param areaId
     * @return
     */
    @RequestMapping(value = "/grid/taskStatistics", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ResponsibilityGrid> getGridsTaskStatistics(@RequestParam(required = false) Long areaId) {
        Map param = new HashMap();
        param.put("areaId",areaId);
        List<ResponsibilityGrid> list=responsibilityGridService.findEntitysByCondition(param);
        List<Map<String,Object>> result = new ArrayList<>();
        for(ResponsibilityGrid grid :list){
            Map<String,Object> map = new HashMap<>();
            map.put("grid",grid);
            Map<String,String> param1 = new HashMap<>();
            param1.put("someStatus", "0,2");
            param1.put("gridId",grid.getGridId()+"");
            PageObject po = new PageObject();
            po.setCurrPage(1);
            po.setPageSize(1);
            po.getCondition().putAll(param1);
            GridDataModel model = enterpriseBaseService.getPage(po);
            int inspection = inspectionPlanService.getEnterpriseInspectionPlanTaskCount(param1);
            int revisit = revisitPlanService.getRevisitPlanTaskCount(param1);
            map.put("count",model.getTotal()+inspection+revisit);
            result.add(map);
        }
        return new ObjectRestResponse<ResponsibilityGrid>().rel(true).data(result);
    }

    /**
     *
     * 查询当前网格未完成的回访事件
     * @param params
     * @return
     */
    @RequestMapping(value = "/getRevisitPlanTask", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getRevisitPlanTask(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> planList = null;
        Integer total = 0;
        PageObject po = getPageObject(params);
        total = revisitPlanService.getRevisitPlanTaskCount(params);
        if (total > 0)
            planList = revisitPlanService.getRevisitPlanTask(params, po.getOffset(), po.getPageSize());
        GridDataModel model = new GridDataModel(planList, total);
        return model;
    }


    /**
     * 查询当前网格未完成的日常检查计划
     * @return
     */
    @RequestMapping(value = "/getEnterpriseInspectionPlanTask", method = RequestMethod.GET)
    public GridDataModel getEnterpriseInspectionPlanTask(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> planList = null;
        Integer total = 0;
        PageObject po = getPageObject(params);
        total = inspectionPlanService.getEnterpriseInspectionPlanTaskCount(params);
        if (total > 0)
            planList = inspectionPlanService.getEnterpriseInspectionPlanTask(params, po.getOffset(), po.getPageSize());
        GridDataModel model = new GridDataModel(planList, total);
        return model;
    }

    /**
     * 查询当前网格未完成的现场核查事件
     * @param params
     * @return
     */
    @RequestMapping(value = "/getOnsiteTask", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getOnsiteTask(@RequestParam Map<String, String> params) {
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = enterpriseBaseService.getPage(po);
        return model;
    }
    @RequestMapping(value = "/getEnterpriseCountBySubject", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse< List<Map<String,Object>>> getEnterpriseCountBySubject(@RequestParam(required = false) Long areaId) {
        List<Map<String,Object>>  list=enterpriseBaseService.getEnterpriseCountBySubject(areaId);
        return new ObjectRestResponse<>().rel(true).data(list);
    }

    @RequestMapping(value = "/getEnterpriseCountBySupervise", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse< List<Map<String,Object>>> getEnterpriseCountBySupervise(@RequestParam(required = false) Long areaId) {
        List<Map<String,Object>>  list=enterpriseBaseService.getEnterpriseCountBySupervise(areaId);

        for(Map<String, Object> datamap:list){
            System.out.println(datamap.get("regulatoryLevel"));
            System.out.println(datamap.get("count"));

            Map<String,Object> mydatamap = new HashMap<>();
            mydatamap.put("superviseClassification",datamap.get("regulatoryLevel"));
            mydatamap.put("areaId",areaId);
            List<String> lists = enterpriseBaseService.getMyEnterpriseName(mydatamap);
            datamap.put("companeyName",lists);
        }



        return new ObjectRestResponse<>().rel(true).data(list);
    }

    @RequestMapping(value = "/getEnterpriseTotal", method = RequestMethod.GET)
    @ResponseBody
    public Integer getEnterpriseTotal(@RequestParam Map<String, Object> params) {
       return enterpriseBaseService.countByCondition(params);
    }

    @RequestMapping(value = "/getRegisterTotal", method = RequestMethod.GET)
    @ResponseBody
    public Integer getRegisterTotal(@RequestParam Map<String, String> params) {
        return enterpriseRegisterService.getRegisterEnterprisePageCount(params);
    }

    @RequestMapping(value = "/getGridEnterpriseCount", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse< List<Map<String,Object>>> getGridEnterpriseCount(@RequestParam Map<String, Object> param) {
        param.put("gridStatus","1");        //启用的区域
        List<ResponsibilityGrid> list=responsibilityGridService.findEntitysByCondition(param);
        List<Map<String,Object>> result = new ArrayList<>();
        for(ResponsibilityGrid grid :list){
            Map<String,Object> map = new HashMap<>();
            Map<String,Object> param1 = new HashMap<>();
            param1.put("gridId",grid.getGridId());
            param1.put("extendSql", "AND REGULATORY_LEVEL IS NOT NULL ");
            int count = enterpriseSupervisionService.countByCondition(param1);

            Map<String,Object> datamap1 = new HashMap<>();
            datamap1.put("gridId",grid.getGridId());
            List<EnterpriseSupervision> list1 = enterpriseSupervisionService.findEntitysByCondition(datamap1);
            List<String> mylist = new ArrayList<>();
            for(EnterpriseSupervision enterpriseSupervision:list1){
                EnterpriseBase enterpriseBase = enterpriseBaseService.findById(enterpriseSupervision.getEnterpriseId());
                if(enterpriseBase!=null){
                    mylist.add(enterpriseBase.getEnterpriseName());
                }
            }
            map.put("companyName",mylist);
            map.put("name",grid.getGridName());
            map.put("value",count);
            result.add(map);
        }
        return new ObjectRestResponse<>().rel(true).data(result);
    }

    /**
     * 获取安全检查表格数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSafetyGrid", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse< List<Map<String,Object>>> getSafetyGrid(@RequestParam Map<String, Object> param) {
        List<Map<String,Object>> result = new ArrayList<>();
        //1.现场核查企业总数
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","现场核查");
        Map<String, Object> param1=param;
        param1.put("someStatus", "0,1,2");
        int t1 = enterpriseBaseService.countByCondition(param1);
        map1.put("total", t1 );
        //2.现场核查企业通过总数
        Map<String, Object> param2=param;
        param1.put("enterpriseStatus", "1");
        int t2 = enterpriseBaseService.countByCondition(param2);
        map1.put("finish",t2);
        result.add(map1);
        //3.日常检查总数
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","日常检查");
        Map<String, Object> param3=param;
        int t3 = inspectionPlanService.countByCondition(param3);
        map2.put("total",t3);
        //4.日常检查完成总数
        Map<String, Object> param4=param;
        param4.put("status","1");
        int t4 = inspectionPlanService.countByCondition(param4);
        map2.put("finish",t4);
        result.add(map2);
        //5.事件回访总数
        Map<String,Object> map3 = new HashMap<>();
        map3.put("name","事件回访");
        Map<String, Object> param5=param;
        int t5 = revisitPlanService.countByCondition(param5);
        map3.put("total",t5);
        //6.事件回访完成总数
        Map<String, Object> param6=param;
        param6.put("planStatus","1");
        int t6 = revisitPlanService.countByCondition(param6);
        map3.put("finish",t6);
        result.add(map3);
        return new ObjectRestResponse<>().rel(true).data(result);
    }

    /**
     * 查询当前地区企业的积分排名
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCycleScore", method = RequestMethod.GET)
    @ResponseBody
    public List<CreditRecord> getCycleScoreRanking(@RequestParam Map<String, String> params) {
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        List<CreditRecord> model = creditRecordService.getCycleScoreRanking(po);
        return model;
    }




    /**
     * 查询当前地区预警数量
     * @return
     */
    @RequestMapping(value = "/getWarning", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getWarning(@RequestParam Long areaId) {
//        Map result = new HashMap();
//
//        Map<String, String> params=new HashMap<>();
//        params.put("areaId",areaId+"");
//        params.put("validStatus","4");
//        PageObject po = getPageObject(params);
//        po.getCondition().putAll(params);
//
//        //员工健康证
//        Integer stafftotal = staffCertificateService.getPageCount(params);
//
//        params.put("certificateStatus","1");
//        params.put("validStatus","4");
//        params.put("certificateType", "3");
//        //企业证照  餐饮服务许可证
//        Integer total = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
//        result.put("staffcer",stafftotal);
//        result.put("enterprisecer",total);
        List<Map<String, Object>> result = alarmService.getWarning(areaId);
        return result;
    }
    /**
     * 查询当前企业健康证预警数量
     * @return
     */
    @RequestMapping(value = "/getHealthWarning", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getHealthWarning(@RequestParam Long enterpriseId) {
//        Map result = new HashMap();
//
//        Map<String, String> params=new HashMap<>();
//        params.put("areaId",areaId+"");
//        params.put("validStatus","4");
//        PageObject po = getPageObject(params);
//        po.getCondition().putAll(params);
//
//        //员工健康证
//        Integer stafftotal = staffCertificateService.getPageCount(params);
//
//        params.put("certificateStatus","1");
//        params.put("validStatus","4");
//        params.put("certificateType", "3");
//        //企业证照  餐饮服务许可证
//        Integer total = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
//        result.put("staffcer",stafftotal);
//        result.put("enterprisecer",total);
        List<Map<String, Object>> result = alarmService.getHealthWarning(enterpriseId);
        return result;
    }

    /**
     * 根据条件获取企业信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/getEnterpriseInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse< List<Map<String,Object>>> getEnterpriseInfo(@RequestParam Map<String, Object> param) {
        List<EnterpriseBase> enterpriseBaseList = enterpriseBaseService.findEntitysByCondition(param);
        List<Map<String,Object>> result = new ArrayList<>();
        for(EnterpriseBase enterpriseBase : enterpriseBaseList) {
            Map<String, Object> map = new HashMap<>();
            Map param1 = new HashMap<>();
            param1.put("validStatus", "4");//即将过期以及已过期
            param1.put("certificateStatus", "1");
            param1.put("certificateType", "3"); //餐饮服务许可证
            param1.put("enterpriseId", enterpriseBase.getEnterpriseId());
            // 查询健康证
            int count_cert_staff = staffCertificateService.getPageCount(param1);
            // 查询餐饮服务许可证
            int count_cert_enterprise = enterpriseCertificateService.getEnterpriseCertificatePageCount(param1);
            // 查询企业报警

            int count_alarm = alarmService.countAlarmByCondition(param1);
            map.put("enterpriseInfo", enterpriseBase);
            map.put("alarmCount", count_cert_staff + count_cert_enterprise + count_alarm);
            result.add(map);
        }
        return new ObjectRestResponse<>().rel(true).data(result);
    }

    /**
     * 根据条件获取网格信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/getGridsInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ResponsibilityGrid> getGridsInfo(@RequestParam Map<String, Object> param) {
        List<ResponsibilityGrid> list = responsibilityGridService.findEntitysByCondition(param);
        return new ObjectRestResponse<>().rel(true).data(list);
    }

    /**
     * 日常检查结果
     * @param params
     * @return
     */
    @RequestMapping(value = "/inspectionResult", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> statisticalInspectionResultItem(@RequestParam Map<String, Object> params) {
        List<Map<String, Object>> list = inspectionResultItemService.statisticalInspectionResultItem(params);
        if (list != null && !list.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true)
                    .data(list);
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }

    @RequestMapping(value = "/getAlarmCount", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getAlarmCount(@RequestParam Map<String, Object> params) {
        Calendar c= Calendar.getInstance();
        c.setTime(dateLocalService.getDataBaseDate());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();

        params.put("alermStartDate", DateUtils.getDateString(m,"yyyy-MM-dd"));
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())) {
            params.put("areaId", sysUser.getAreaId() + "");
        } else if ("4".equals(sysUser.getUserType()))
            params.put("enterpriseId", sysUser.getEnterpriseId().toString());
        //1.获取已处理的报警数量
        params.put("status", "2");
        List<Map<String, Object>> list1 = alarmService.getCountByEventId(params);
        //2.获取未处理的报警数量
        params.put("status", "1");
        List<Map<String, Object>> list2 = alarmService.getCountByEventId(params);
        Map result = new HashMap();
        result.put("treated", list1);
        result.put("untreated", list2);


        for(Map<String, Object> datamap:list2){
            System.out.println(datamap.get("eventId"));
            System.out.println(datamap.get("count"));
            List<String> lists = alarmService.getEnterpriseName(datamap.get("eventId").toString());
            datamap.put("companeyName",lists);

        }

        if (result != null && !result.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true).data(result);
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }

    /**
     * 根据条件查询温湿度报警数量 （一个月内）
     * @param params
     * @return
     */
    @RequestMapping(value = "/sensor/count", method = RequestMethod.GET)
    @ResponseBody
    public Map count(@RequestParam Map<String,Object > params) {
        Map<String, Object> result = Maps.newHashMap();

        Calendar c= Calendar.getInstance();
        c.setTime(dateLocalService.getDataBaseDate());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();

        params.put("alermStartDate", DateUtils.getDateString(m,"yyyy-MM-dd"));
        params.put("eventType","2");

        params.put("extendSql","and EVENT_ID in ('10001','10002') and PROCESSING_TIME IS NULL");        //温度未处理数量
        Integer count_wd = alarmService.countAlarmByCondition(params);
        params.put("extendSql","and EVENT_ID in ('10003','10004') and PROCESSING_TIME IS NULL");        //湿度未处理数量
        Integer count_sd = alarmService.countAlarmByCondition(params);
        result.put("count_wd", count_wd);
        result.put("count_sd", count_sd);
        return result;
    }
    /**
     * 根据条件查询温湿度报警数量一周内趋势
     * @param params
     * @return
     */
    @RequestMapping(value = "/sensor/trend", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> trend(@RequestParam Map<String,Object > params) {
        List<Map<String, Object>> list= alarmService.getSensorTrend(params);
        return list;
    }
    /**
     * 根据条件查询视频报警数量一周内趋势
     * @param params
     * @return
     */
    @RequestMapping(value = "/video/trend", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getVideoTrend(@RequestParam Map<String,Object > params) {
        List<Map<String, Object>> list= alarmService.getVideoTrend(params);
        return list;
    }

    /**
     * 获取企业报警统计数据
     * @param params
     * @return
     */
    @RequestMapping(value="/getEnterpriseAlarmCount",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getEnterpriseAlarmCount(@RequestParam Map<String, Object> params) {
        Map result = new HashMap();
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("enterpriseId", sysUser.getEnterpriseId().toString());
        //1.本日
        params.put("alermStartDate", DateUtils.getDateString(dateLocalService.getDataBaseDate(),"yyyy-MM-dd"));
        params.put("enterpriseId", sysUser.getEnterpriseId().toString());
        List<Map<String, Object>> dateData = alarmService.getCountByEventId(params);
        result.put("dateData", dateData);
        //2.本周
        Calendar c1=Calendar.getInstance();
        c1.setTime(dateLocalService.getDataBaseDate());
        c1.add(Calendar.WEEK_OF_MONTH, 0);
        c1.set(Calendar.DAY_OF_WEEK, 2);
        Date week=c1.getTime();
        params.put("alermStartDate", DateUtils.getDateString(week,"yyyy-MM-dd"));
        List<Map<String, Object>> weekData = alarmService.getCountByEventId(params);
        result.put("weekData", weekData);
        //3.本月
        Calendar c2=Calendar.getInstance();
        c2.setTime(dateLocalService.getDataBaseDate());
        c2.add(Calendar.MONTH, 0);
        c2.set(Calendar.DAY_OF_MONTH, 1);
        Date month=c2.getTime();
        params.put("alermStartDate", DateUtils.getDateString(month,"yyyy-MM-dd"));
        List<Map<String, Object>> monthData = alarmService.getCountByEventId(params);
        result.put("monthData", monthData);
        //4.本年
        params.put("alermStartDate", DateUtils.getDateString(dateLocalService.getDataBaseDate(),"yyyy")+"-01-01");
        List<Map<String, Object>> yearData = alarmService.getCountByEventId(params);
        result.put("yearData", yearData);
        if (result != null && !result.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true).data(result);
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }

    /**
     * 查询企业晨检情况
     * @param params
     * @return
     */
    @RequestMapping(value="/getRecordMorningCheck",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<RecordMorningCheck> getRecordMorningCheck(@RequestParam Map<String, Object> params) {
        RecordMorningCheck recordMorningCheck = null;
        List<RecordMorningCheck> list = recordMorningCheckService.findEntitysByCondition(params);
        if(list!=null&&list.size()>0){
             recordMorningCheck = list.get(list.size()-1);
        }
        return new ObjectRestResponse<RecordMorningCheck>().rel(true).data(recordMorningCheck);
    }


    /**
     * 立案统计
     * @param params
     * @return
     */
    @RequestMapping(value = "/getComplainCount", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getComplainCount(@RequestParam Map<String, Object> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())) {
            params.put("areaId", sysUser.getAreaId() );
        } else if ("4".equals(sysUser.getUserType()))
            params.put("enterpriseId", sysUser.getEnterpriseId().toString());

        params.put("typeId", "1");
        params.put("submitPlatform", "1");//平台上报

        params.put("status", "2");
        Integer count1 = complainEvaluateService.countByCondition(params);
        params.put("status", "3");
        Integer count2 = complainEvaluateService.countByCondition(params);

        params.put("submitPlatform", "2");//公众上报
        params.put("status", "2");
        Integer count3 = complainEvaluateService.countByCondition(params);
        params.put("status", "3");
        Integer count4 = complainEvaluateService.countByCondition(params);


        Map result = new HashMap();
        result.put("count1", count1);
        result.put("count2", count2);
        result.put("count3", count3);
        result.put("count4", count4);


        if (result != null && !result.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true).data(result);
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }


    /**
     * TODO 查询企业报警排名
     * @param param
     * @return
     */
    @RequestMapping(value = "/getAlarmRanking", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<AlarmTotal> getAlarmRanking(@RequestParam Map<String, Object> param) {
        List<AlarmTotal> list = alarmTotalService.queryAlarmRankingCurrentMonth(param);
        return new ObjectRestResponse<>().rel(true).data(list);
    }
}
