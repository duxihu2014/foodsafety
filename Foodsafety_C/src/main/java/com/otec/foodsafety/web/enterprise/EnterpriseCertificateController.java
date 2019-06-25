package com.otec.foodsafety.web.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseCertificateService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.util.CompareObjectUtil;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/enterpriseCertificate")
public class EnterpriseCertificateController extends VueBaseController<EnterpriseCertificateService, EnterpriseCertificate, Long> {
    @Autowired
    private EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    private EnterpriseVerifyService enterpriseVerifyService;
    @Autowired
    private SessionFilter sessionFilter;

    @RequestMapping(value="/getPage")
    public GridDataModel getEnterpriseCertificatePage(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = enterpriseCertificateService.getEnterpriseCertificatePageCount(params);
        List<EnterpriseCertificate> enterpriseCertificateList = enterpriseCertificateService.getEnterpriseCertificatePage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(enterpriseCertificateList,total);
        return model;
    }

    /**
     *修改企业证照信息
     * @param multipartFile
     * @param enterpriseCertificateStr
     * @param reason
     * @return
     */
    @RequestMapping(value="/change" ,method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseCertificateChange> changrCertificate(@RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                                                             @RequestParam(value = "enterpriseCertificate") String enterpriseCertificateStr,
                                                                             @RequestParam(value = "reason") String reason) {
        try{
            SysUser sysUser = sessionFilter.getJWTUser(request);
            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
            EnterpriseCertificate enterpriseCertificate = JSONUtils.fromJson(enterpriseCertificateStr,EnterpriseCertificate.class);
            enterpriseCertificateService.modifyEnterpriseCertificate(uploadUrl,imageFolder,multipartFile,sysUser.getUserId(),reason,enterpriseCertificate,"2");
            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<EnterpriseCertificateChange> delete(@PathVariable Long id) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseCertificate enterpriseCertificate = enterpriseCertificateService.findById(id);
            enterpriseCertificateService.modifyEnterpriseCertificate("", "", null, sysUser.getUserId(), "注销证照", enterpriseCertificate,
                    "3");// 删除

            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("删除失败!");
            return resp;
        }
    }

    /**
     * 添加企业证照信息
     * @param multipartFile
     * @param enterpriseCertificateStr
     * @param reason
     * @return
     */
    @RequestMapping(value="/add" ,method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseCertificateChange> addCertificate(@RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                                                             @RequestParam(value = "enterpriseCertificate") String enterpriseCertificateStr,
                                                                             @RequestParam(value = "reason") String reason) {
        try{
            SysUser sysUser = sessionFilter.getJWTUser(request);
            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
            EnterpriseCertificate enterpriseCertificate = JSONUtils.fromJson(enterpriseCertificateStr,EnterpriseCertificate.class);
            enterpriseCertificateService.addEnterpriseCertificate(uploadUrl,imageFolder,multipartFile,sysUser.getUserId(),reason,enterpriseCertificate);
            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }
    /**
     * 根据证照id获取变更表数据
     * @param certificateId
     * @return
     */
    @RequestMapping(value="/getChangeByCertificateId",method = RequestMethod.GET)
    public ObjectRestResponse<EnterpriseCertificateChange> getChangeByCertificateId(@RequestParam("certificateId") String certificateId) {
        try{
            Map param = new HashMap();
            param.put("certificateId",certificateId);
            List<EnterpriseCertificateChange> enterpriseCertificateChangeList = enterpriseCertificateService.getChangeByCondition(param);
            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true).data(enterpriseCertificateChangeList);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }
    /**
     * 根据changeId获取企业证照修改数据表
     * @param id
     * @return
     */
    @RequestMapping(value="/getChange/{id}",method = RequestMethod.GET)
    public ObjectRestResponse<EnterpriseCertificateChange> getChange(@PathVariable Long id) {
        try{
            Map<String ,Object> result = new HashMap();
            List<Map<String ,Object>>  compareList=null;
            EnterpriseCertificateChange enterpriseCertificateChange = enterpriseCertificateService.getChangeById(id);
            //判断是新增还是修改操作
            if(enterpriseCertificateChange.getCertificateId()!=null){//修改
                EnterpriseCertificate enterpriseCertificate = enterpriseCertificateService.findById(enterpriseCertificateChange.getCertificateId());
               compareList = CompareObjectUtil.compareTwoClass(enterpriseCertificate,enterpriseCertificateChange);
            }
            result.put("formData",enterpriseCertificateChange);
            result.put("compareData",compareList);
            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true).data(result);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     * 证照变更审核
     * @param changeId
     * @param auditType
     * @param verifyConclusion
     * @param verifyId
     * @return
     */
    @RequestMapping(value="/verify",method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseVerify> auditEnterpriseCertificate(@RequestParam("changeId") String changeId,
                                                                           @RequestParam("auditType") String auditType,
                                                                           @RequestParam("verifyConclusion") String verifyConclusion,
                                                                           @RequestParam("verifyId") String verifyId) {
        try{
            EnterpriseCertificate enterpriseCertificate = new EnterpriseCertificate();
            EnterpriseCertificateChange enterpriseCertificateChange = enterpriseCertificateService.getChangeById(Long.valueOf(changeId));
            if(enterpriseCertificateChange.getCertificateId()!=null){
                enterpriseCertificate = enterpriseCertificateService.findById(enterpriseCertificateChange.getCertificateId());
            }
            EnterpriseVerify enterpriseVerify = enterpriseVerifyService.findById(Long.valueOf(verifyId));
            if(auditType.equals("0")){//审核不通过
                enterpriseVerify.setVerifyStatus("3");
            }else {//审核通过
                enterpriseVerify.setVerifyStatus("2");
            }
            SysUser sysUser = sessionFilter.getJWTUser(request);
            enterpriseVerify.setVerifyUserId(sysUser.getUserId());
            enterpriseVerify.setVerifyTime(new Date());
            enterpriseVerify.setVerifyConclusion(verifyConclusion);
            enterpriseCertificateService.verifyEnterpriseCertificate(enterpriseCertificate,enterpriseCertificateChange,enterpriseVerify);
            return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
        }catch (Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     * 企业证照信息审核查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/verify/query" ,method = RequestMethod.GET)
    public GridDataModel getCertificateChangePage(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = enterpriseCertificateService.getCertificateChangePageCount(params);
        List<Map<String,Object>> list = enterpriseCertificateService.getCertificateChangePage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }
    
    /**
     * 根据企业ID获取证照信息
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value="/getCertificateDetail/{id}",method = RequestMethod.GET)
    public ObjectRestResponse<List<EnterpriseCertificate>> getCertificateDetail(@PathVariable("id") Long id) {
        try{
            Map<String,Object> param = new HashMap();
            param.put("enterpriseId",id);
            List<EnterpriseCertificate> list = enterpriseCertificateService.findEntitysByCondition(param);
            return new ObjectRestResponse<List<EnterpriseCertificateChange>>().rel(true).data(list);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<List<EnterpriseCertificateChange>>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }
}
