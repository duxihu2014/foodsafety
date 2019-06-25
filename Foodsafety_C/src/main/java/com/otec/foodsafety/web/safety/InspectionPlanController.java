package com.otec.foodsafety.web.safety;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionPlan;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/inspection/plan")
public class InspectionPlanController extends VueBaseController<
        InspectionPlanService, InspectionPlan, Long> {
    @Autowired
    InspectionPlanService inspectionPlanService;
    @Autowired
    PersonnelThreeService personnelThreeService;
    @Autowired
    SessionFilter sessionFilter;

    /**
     * 查询当前操作员未完成的日常检查计划
     * @return
     */
    @RequestMapping(value = "/getEnterpriseInspectionPlanTask", method = RequestMethod.GET)
    public GridDataModel getEnterpriseInspectionPlanTask(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        /*Map map = new HashMap<String, Object>(){
            { put("userId", Long.valueOf(sysUser.getUserId())); }
        };*/
        Map map = new HashMap<String, Object>();
        map.put("userId", Long.valueOf(sysUser.getUserId()));
        List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
        List<Map<String, Object>> planList = null;
        Integer total = 0;
        if (list != null && !list.isEmpty()) {
            PersonnelThree personnelThree = list.get(0);
            params.put("personnelId", String.valueOf(personnelThree.getPersonnelId()));
            PageObject po = getPageObject(params);
            total = inspectionPlanService.getEnterpriseInspectionPlanTaskCount(params);
            if (total > 0)
                planList = inspectionPlanService.getEnterpriseInspectionPlanTask(params, po.getOffset(), po.getPageSize());
        }
        GridDataModel model = new GridDataModel(planList, total);
        return model;
    }

    @RequestMapping(value = "/getInspectionPlan/{planId}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getInspectionPlan(@PathVariable Long planId) {
        return new ObjectRestResponse<Map<String, Object>>().rel(true)
                .data(inspectionPlanService.getInspectionPlan(planId));
    }

    @RequestMapping(value = "/getEnterpriseInspectionPlan", method = RequestMethod.GET)
    public GridDataModel getEnterpriseInspectionPlan(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if (StringUtils.isBlank(params.get("areaId")))
            params.put("areaId", String.valueOf(sysUser.getAreaId()));
        if (sysUser.getEnterpriseId() != null)
            params.put("enterpriseId", sysUser.getEnterpriseId().toString());
        PageObject po = getPageObject(params);
        List<Map<String, Object>> planList = new ArrayList<Map<String, Object>>();
        Integer total = inspectionPlanService.getEnterpriseInspectionPlanCount(params);
        if (total > 0)
            planList = inspectionPlanService.getEnterpriseInspectionPlan(params, po.getOffset(), po.getPageSize());
        GridDataModel model = new GridDataModel(planList, total);
        return model;
    }
}
