package com.otec.foodsafety.web.personal;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.catering.StaffCertificateService;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.enterprise.EnterpriseCertificateService;
import com.otec.foodsafety.service.enterprise.EnterpriseRegisterService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import com.otec.foodsafety.service.safety.RevisitPlanService;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/personal")
public class PersonalController {

    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    private EnterpriseRegisterService enterpriseRegisterService;
    @Autowired
    private EnterpriseVerifyService enterpriseVerifyService;
    @Autowired
    private StaffCertificateService staffCertificateService;
    @Autowired
    private EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    PersonnelThreeService personnelThreeService;
    @Autowired
    InspectionPlanService inspectionPlanService;
    @Autowired
    RevisitPlanService revisitPlanService;
    @Autowired
    private EnterpriseBaseService enterpriseBaseService;
    @Autowired
    private AlarmService alarmService;


    @RequestMapping(value = "/enterprise/count", method = RequestMethod.GET)
    @ResponseBody
    public Map getEnterpriseCount(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if(!StringUtils.equals("4", sysUser.getUserType())){
            return null;
        }
        Map<String, Object> result = Maps.newHashMap();
        // 证照预警
        params.put("enterpriseId", sysUser.getEnterpriseId()+"");
        params.put("validStatus", "4");//即将过期以及已过期
        params.put("certificateStatus", "1");
        int count_cert_staff = staffCertificateService.getPageCount(params);
        int count_cert_enterprise = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);

        //报警提醒
        Map mapAlerm = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(params.get("alermStartTime"))) {
            mapAlerm.put("alermStartDate", params.get("alermStartTime"));
        }
        //企业
        mapAlerm.put("enterpriseId", sysUser.getEnterpriseId()+"");
        Integer count_alarm = alarmService.countAlarmByCondition(mapAlerm);

        result.put("count_cert", count_cert_staff+count_cert_enterprise);
        result.put("count_alarm", count_alarm);
        result.put("count_alarm_Time", new Date());//记录统计报警的当前时间
        return result;
    }

    @RequestMapping(value = "/supervise/count", method = RequestMethod.GET)
    @ResponseBody
    public Map getSuperviseCount(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if(!StringUtils.equals("3", sysUser.getUserType())){
            return null;
        }

        Map<String, Object> result = Maps.newHashMap();

        String areaId = sysUser.getAreaId()+"";
        params.put("areaId", areaId);
        params.put("registerStatus", "0");
        // 企业注册审核
        Integer total = enterpriseRegisterService.getRegisterEnterprisePageCount(params);
        result.put("count_register", total);
        // 企业资料审核
        total = enterpriseVerifyService.getEnterpriseVerifyCount(params);
        result.put("count_verify", total);
        // 证照预警
        params.put("validStatus", "4");//即将过期以及已过期
        params.put("certificateStatus", "1");
        int count_cert_staff = staffCertificateService.getPageCount(params);
        int count_cert_enterprise = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        result.put("count_cert", count_cert_staff+count_cert_enterprise);

        //现场核查统计
        Map map = new HashMap<String, Object>();
        map.put("userId", sysUser.getUserId());
        List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
        if(list.size() > 0){
            PersonnelThree personnelThree = list.get(0);
            params.put("personnelId", String.valueOf(personnelThree.getPersonnelId()));
            params.remove("userId");
            params.put("someStatus", "0,2");

            PageObject po = new PageObject();
            po.setCurrPage(1);
            po.setPageSize(1);
            po.getCondition().putAll(params);

            GridDataModel model = enterpriseBaseService.getPage(po);

            int inspection = inspectionPlanService.getEnterpriseInspectionPlanTaskCount(params);
            int revisit = revisitPlanService.getRevisitPlanTaskCount(params);
            result.put("count_onsite", model.getTotal()+inspection+revisit);
        }else {
            result.put("count_onsite", 0);
        }
        //报警提醒
        Map mapAlerm = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(params.get("alermStartTime"))) {
            mapAlerm.put("alermStartDate", params.get("alermStartTime"));
        }
        //监管
        mapAlerm.put("areaId", sysUser.getAreaId()+"");
        Integer count_alarm = alarmService.countAlarmByCondition(mapAlerm);
        result.put("count_alarm", count_alarm);
        result.put("count_alarm_Time", new Date());//记录统计报警的当前时间
        return result;
    }
}

