package com.otec.foodsafety.api.supervise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.enterprise.EnterpriseRegisterService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待办事项
 */
@RestController
@RequestMapping("/api/backlog")
public class BacklogInterface extends BaseInterface {

    @Autowired
    EnterpriseBaseService enterpriseBaseService;
    @Autowired
    private EnterpriseRegisterService enterpriseRegisterService;
    @Autowired
    EnterpriseVerifyService enterpriseVerifyService;
    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    PersonnelThreeService personnelThreeService;
    @Autowired
    InspectionPlanService inspectionPlanService;

    /**
     * 企业注册
     * @param params
     * @return
     */
    @RequestMapping("/register")
    public GridDataModel registerList(@RequestParam Map<String, String> params) {
        // 查询列表数据
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("areaId", sysUser.getAreaId()+"");//查询监管员地区下的企业
        PageObject po = getPageObject(params);

        params.put("registerStatus","0");
        Integer total = enterpriseRegisterService.getRegisterEnterprisePageCount(params);
        List<RegisterUser> registerUserList = enterpriseRegisterService.getRegisterEnterprisePage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(registerUserList,total);
        return model;
    }

    /**
     * 档案更新
     * @param params
     * @return
     */
    @RequestMapping(value="/changeCheck")
    public GridDataModel getEnterpriseVerifyPage(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("areaId", sysUser.getAreaId()+"");//查询监管员地区下的企业

         // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = enterpriseVerifyService.getEnterpriseVerifyCount(params);
        List<EnterpriseVerify> enterpriseVerifyList = enterpriseVerifyService.getEnterpriseVerify(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(enterpriseVerifyList,total);
        return model;
    }
    /**
     *  现场核查
     * @param params
     * @return
     */
    @RequestMapping(value="/verification")
    public GridDataModel getVerification(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);

      //  params.put("someStatus","0,2");//未核查
        params.put("someStatus","0");//未核查
        Map map = new HashMap<String, Object>();
        map.put("userId", Long.valueOf(sysUser.getUserId()));
        List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
        if (list != null && !list.isEmpty()) {
            PersonnelThree personnelThree = list.get(0);
            params.put("personnelId", String.valueOf(personnelThree.getPersonnelId()));
            PageObject po = getPageObject(params);
            po.getCondition().putAll(params);
            GridDataModel model = enterpriseBaseService.getPage(po);
            return model;
        }else
            return new GridDataModel(new ArrayList<>(),0);
    }

    /**
     *  日常检查 三员才可访问
     * @param params
     * @return
     */
    @RequestMapping(value="/inspection")
    public GridDataModel getEnterpriseInspectionPlan(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if(sysUser.getPersonnelId()>0) {

            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
            List<Map<String, Object>> planList = null;
            Integer total = 0;
            if (list != null && !list.isEmpty()) {
                PersonnelThree personnelThree = list.get(0);
                params.put("personnelId", String.valueOf(personnelThree.getPersonnelId()));
                PageObject po = getPageObject(params);
         //       params.put("planStatus", "0"); //未完成
                total = inspectionPlanService.getEnterpriseInspectionPlanTaskCount(params);
                if (total > 0)
                    planList = inspectionPlanService.getEnterpriseInspectionPlanTask(params, po.getOffset(), po.getPageSize());
            }
            GridDataModel model = new GridDataModel(planList, total);

            return model;
        }else
            return new GridDataModel(new ArrayList<>(), 0);
    }


}
