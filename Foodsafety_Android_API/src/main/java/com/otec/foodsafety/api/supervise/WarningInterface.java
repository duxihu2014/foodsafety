package com.otec.foodsafety.api.supervise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.catering.StaffCertificateService;
import com.otec.foodsafety.service.enterprise.EnterpriseCertificateService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证件过期预警
 */
@RestController
@RequestMapping("/api/warning")
public class WarningInterface extends BaseInterface {

    @Autowired
    EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    StaffCertificateService staffCertificateService;
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    SysResourceService sysResourceService;
    /**
     * 企业预警 员工健康证 查询已过期和即将过期的员工健康证（指定企业）
     */
    @RequestMapping(value = "/enterprise/staffcert/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel staffcert(@RequestParam Map<String, String> params,@PathVariable String enterpriseId) {

     //   SysUser sysUser = sessionFilter.getJWTUser(request);

        params.put("enterpriseId",enterpriseId);
        params.put("validStatus","4");
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total = staffCertificateService.getPageCount(params);
        List<CateringStaff> list = staffCertificateService.getPage(params,po.getOffset(),po.getPageSize());
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);

        for (CateringStaff cs:list){
            SysResource resource=sysResourceService.findById(cs.getCertificatePhoto());
            if(resource!=null){
                cs.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
            }
        }
        GridDataModel model = new GridDataModel(list,total);

        return model;
    }

    /**
     * 企业预警 餐饮服务许可证 查询已过期和即将过期的企业证照 （指定企业）
     * @return
     */
    @RequestMapping(value = "/enterprise/enterprisecert/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel enterprisecert(@RequestParam Map<String, String> params,@PathVariable String enterpriseId) {

   //     SysUser sysUser = sessionFilter.getJWTUser(request);

        params.put("enterpriseId",enterpriseId);
        params.put("certificateStatus","1");
        params.put("validStatus","4");
  //      params.put("certificateType", "3"); // 餐饮服务许可证
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        List<EnterpriseCertificate> list = enterpriseCertificateService.getEnterpriseCertificatePage(params,po.getOffset(),po.getPageSize());
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (EnterpriseCertificate cer:list){
            SysResource resource=sysResourceService.findById(cer.getCertificatePhoto());
            if(resource!=null){
                cer.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
            }
        }
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }

    /**
     * 工作预警 员工健康证 查询已过期和即将过期的员工健康证（监管责任区域内的企业）
     */
    @RequestMapping(value = "/staffcert", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel staffcerts(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);

        params.put("areaId",sysUser.getAreaId()+"");
        params.put("validStatus","4");
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total = staffCertificateService.getPageCount(params);
        List<CateringStaff> list = staffCertificateService.getPage(params,po.getOffset(),po.getPageSize());
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);

        for (CateringStaff cs:list){
            SysResource resource=sysResourceService.findById(cs.getCertificatePhoto());
            if(resource!=null){
                cs.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
            }
        }
        GridDataModel model = new GridDataModel(list,total);

        return model;
    }

    /**
     * 工作预警 餐饮服务许可证 查询已过期和即将过期的企业证照 （监管责任区域内的企业）
     * @return
     */
    @RequestMapping(value = "/enterprisecert", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel enterprisecerts(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);

        params.put("areaId",sysUser.getAreaId()+"");
        params.put("certificateStatus","1");
        params.put("validStatus","4");
     //   params.put("certificateType", "3"); // 餐饮服务许可证
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        List<EnterpriseCertificate> list = enterpriseCertificateService.getEnterpriseCertificatePage(params,po.getOffset(),po.getPageSize());
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (EnterpriseCertificate cer:list){
            SysResource resource=sysResourceService.findById(cer.getCertificatePhoto());
            if(resource!=null){
                cer.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
            }
        }
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }



}
