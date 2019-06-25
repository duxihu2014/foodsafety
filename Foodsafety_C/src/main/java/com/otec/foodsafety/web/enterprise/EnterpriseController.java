package com.otec.foodsafety.web.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseBaseChange;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseService;
import com.otec.foodsafety.service.system.SysInstitutionService;
import com.otec.foodsafety.service.system.SysUserEnterpriseService;
import com.otec.foodsafety.service.system.SysUserInstitutionService;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/enterprise")
public class EnterpriseController extends VueBaseController<EnterpriseService, EnterpriseBase, Long> {
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private SessionFilter sessionFilter;
    /*获取企业基本信息*/
    @RequestMapping(value="/getBase" ,method = RequestMethod.GET)
    public ObjectRestResponse<EnterpriseBase> getBaseById(@RequestParam  Long enterpriseId) {
        try{
           return new ObjectRestResponse<EnterpriseBase>().rel(true).data(enterpriseService.findById(enterpriseId));
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBase>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }
    /*企业基本信息修改*/
    @RequestMapping(value="/changrBase" ,method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseBaseChange> changrBase(@RequestParam(value = "enterpriseBase") String enterpriseBaseStr, @RequestParam(value = "reason") String reason) {
        try{
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseBase enterpriseBase = JSONUtils.fromJson(enterpriseBaseStr,EnterpriseBase.class);
            enterpriseService.modifyEnterpriseBase(enterpriseBase,sysUser.getUserId(),reason);
            return new ObjectRestResponse<EnterpriseBaseChange>().rel(true);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBase>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }


    }
    /*获取企业证照信息*/
}

