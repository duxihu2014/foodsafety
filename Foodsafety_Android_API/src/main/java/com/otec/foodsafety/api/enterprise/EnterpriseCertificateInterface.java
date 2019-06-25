package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.EnterpriseCertificateChangeExt;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseCertificateService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/enterprise/certificate")
@RestController
public class EnterpriseCertificateInterface extends BaseInterface {
    @Autowired
    EnterpriseCertificateService enterpriseCertificateService;

    @Autowired
    SysResourceService sysResourceService;

    @Autowired
    SessionFilter sessionFilter;
    /**
     * 获取企业证照列表
     * @param params
     * @return
     */
    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    public GridDataModel getCertificate(@RequestParam Map<String, String> params, @PathVariable Long enterpriseId) {
        // 查询列表数据
        params.put("enterpriseId",enterpriseId.toString());
        params.put("certificateStatus","1");
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        List<EnterpriseCertificate> enterpriseCertificateList = enterpriseCertificateService.getEnterpriseCertificatePage(params,po.getOffset(),po.getPageSize());

        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);

        for (EnterpriseCertificate cer:enterpriseCertificateList){
            SysResource resource=sysResourceService.findById(cer.getCertificatePhoto());
            if(resource!=null){
                cer.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
            }
        }

        GridDataModel model = new GridDataModel(enterpriseCertificateList,total);

        return model;
    }

    /**
     * 获取企业证照详细信息
     * @param certificateId
     * @return
     */
    @RequestMapping(value="/getDetail/{certificateId}" ,method = RequestMethod.GET)
    public ResponseEntity<EnterpriseCertificate> getDetail(@PathVariable Long certificateId) {
        EnterpriseCertificate cer= enterpriseCertificateService.findById(certificateId);

        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        SysResource resource=sysResourceService.findById(cer.getCertificatePhoto());
        if(resource!=null){
            cer.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
        }
        return   ResponseEntity.ok(cer);
    }

    /**
     *  企业证照保存
     *
     * @param ext
     * @return
     */
    @RequestMapping(value="/save" ,method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseCertificateChange> changrCertificate(@RequestBody EnterpriseCertificateChangeExt ext) {
        try{
            SysUser sysUser = sessionFilter.getJWTUser(request);

            if(ext.getCertificate().getCertificateId() !=null && ext.getCertificate().getCertificateId().intValue()>0){
                EnterpriseCertificate enterpriseCertificate = enterpriseCertificateService.findById(ext.getCertificate().getCertificateId());
                ext.getCertificate().setCertificateStatus(enterpriseCertificate.getCertificateStatus());
                ext.getCertificate().setCertificateType(enterpriseCertificate.getCertificateType());
                 enterpriseCertificateService.modifyEnterpriseCertificate("", "", null,sysUser.getUserId(),ext.getReason(),ext.getCertificate(),"2");
            }else{
               enterpriseCertificateService.addEnterpriseCertificate("","",null,sysUser.getUserId(),ext.getReason(),ext.getCertificate());
            }

           return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("修改企业证照信息失败");
            return resp;
        }
    }



    /**
     *
     * @param certificateId
     * @return
     */
    @RequestMapping(value = "/delete/{certificateId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<EnterpriseCertificateChange> delete(@PathVariable Long certificateId) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseCertificate enterpriseCertificate = enterpriseCertificateService.findById(certificateId);
            enterpriseCertificateService.modifyEnterpriseCertificate("", "", null, sysUser.getUserId(), "注销证照", enterpriseCertificate,
                    "3");// 删除
            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("企业证照删除失败!");
            return resp;
        }
    }

}
