package com.otec.foodsafety.web.system;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.entity.register.RegisterVerify;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import com.otec.foodsafety.service.enterprise.EnterpriseRegisterService;
import com.otec.foodsafety.service.register.RegisterVerifyService;
import com.otec.foodsafety.service.system.SysUserInstitutionService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/enterprise")
public class AdminEnterpriseController extends VueBaseController<EnterpriseRegisterService, RegisterUser, Long>  {
    @Autowired
    private EnterpriseRegisterService enterpriseRegisterService;

    @Autowired
    private DateLocalService dateLocalService;

    @Autowired
    private SessionFilter sessionFilter;

    @Autowired
    private SysUserInstitutionService sysUserInstitutionService;

    @Autowired
    private RegisterVerifyService registerVerifyService;

    /**
     *
     * 获取所有注册表中企业的信息
     * @param params
     * @return
     */
    @RequestMapping("/register/list")
    public GridDataModel getNoBindingHouse(@RequestParam Map<String, String> params) {
        // 查询列表数据
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("areaId", sysUser.getAreaId()+"");
        PageObject po = getPageObject(params);

        Integer total = enterpriseRegisterService.getRegisterEnterprisePageCount(params);
        List<RegisterUser> registerUserList = enterpriseRegisterService.getRegisterEnterprisePage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(registerUserList,total);
        return model;
    }
    /**
     *企业注册审批（0：未通过；1：已通过）
     */
    @RequestMapping(value="/register/audit", method = RequestMethod.POST)
    public ObjectRestResponse<RegisterVerify> add(@RequestBody RegisterVerify entity) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map param = new HashMap();
            param.put("userId",sysUser.getUserId());
            List<SysUserInstitution> sysInstitutions = sysUserInstitutionService.findEntitysByCondition(param);
            entity.setVerifyInstitution(sysInstitutions.get(0).getInstitutionId());
            entity.setVerifyTime(dateLocalService.getDataBaseDate());
            registerVerifyService.enterpriseRegisterAudit(entity);
            ObjectRestResponse resp = new ObjectRestResponse<>().rel(true);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysInstitution>().rel(false);
            resp.setStatus(500);
            resp.setMessage("操作失败!");
            return resp;
        }
    }

}
