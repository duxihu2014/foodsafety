package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.EnterpriseBaseChangeExt;
import com.otec.foodsafety.entity.enterprise.*;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionPlan;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.*;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import com.otec.foodsafety.service.supervisory.ResponsibilityGridService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/enterprise")
@RestController
public class EnterpriseInterfafce extends BaseInterface {

    @Autowired
    EnterpriseBaseService enterpriseBaseService;

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    EnterpriseScaleService enterpriseScaleService;
    @Autowired
    EnterpriseCertificateService  enterpriseCertificateService;
    @Autowired
    InspectionPlanService inspectionPlanService;
    @Autowired
    SysResourceService sysResourceService;
    @Autowired
    SessionFilter sessionFilter;

     @Autowired
    ResponsibilityGridService responsibilityGridService;

     @Autowired
    EnterpriseSupervisionService enterpriseSupervisionService;

    /**
     *  查询登录用户地区内的企业列表
     * @param
     * @return
     */
    @RequestMapping(value="/query" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel query(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("finalAreaId",sysUser.getAreaId().toString());
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = enterpriseBaseService.getGridDataModelByCondition(po);
        return model;
    }


    /**
     *  查询监管人员所在网格内的企业列表
     * @param
     * @return
     */
    @RequestMapping(value="/jurisdictionEnterprise" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);

        Integer total = enterpriseBaseService.getJurisdictionEnterpriseCount(sysUser.getUserId());
        List<Map> list = enterpriseBaseService.getJurisdictionEnterprise(sysUser.getUserId(),po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;

    }


    /*获取企业基本信息*/
    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    public ResponseEntity getBaseById(@PathVariable Long enterpriseId) {
        Map<Object, Object> item = new HashMap<Object, Object>();
        try{
            EnterpriseBase base=enterpriseService.findById(enterpriseId);
            Map map=new HashMap<String ,Object>();
            map.put("enterpriseId",enterpriseId);
            List<EnterpriseScale> scaleList=enterpriseScaleService.findEntitysByCondition(map);
            if(scaleList.size()>0){
                item.put("enterpriseScale",scaleList.get(0).getEnterpriseScale());
                item.put("areaFloor",scaleList.get(0).getAreaFloor());
            }

            item.put("enterpriseName",base.getEnterpriseName());
            item.put("productionAddress",base.getProductionAddress());
            item.put("organizingInstitutionBarCode",base.getOrganizingInstitutionBarCode());
            item.put("registerDate",base.getRegisterDate());
            item.put("registeredCapital",base.getRegisteredCapital());
            item.put("economicNature",base.getEconomicNature());
            item.put("operationScope",base.getOperationScope());
            item.put("corporateRepresentative",base.getCorporateRepresentative());
            item.put("idCardNo",base.getIdCardNo());

            item.put("registerAddress",base.getRegisterAddress());
            item.put("contacts",base.getContacts());
            item.put("contactNumber",base.getContactNumber());
            item.put("inspectorMobile",base.getInspectorMobile());

            item.put("areaId",base.getAreaId());
            item.put("subjectClassification",base.getSubjectClassification());
            item.put("superviseClassification",base.getSuperviseClassification());
            item.put("contactMobile",base.getContactMobile());
            item.put("latitude",base.getLatitude());
            item.put("longitude",base.getLongitude());


            item.put("operatingTerm",base.getOperatingTerm());//经营期限
            item.put("operationScope",base.getOperationScope());//经营范围
            item.put("postalCode",base.getPostalCode());
            item.put("email",base.getEmail());
            item.put("fax",base.getFax());


            List<EnterpriseBaseChange> list=enterpriseBaseService.getBaseChange(map);
            if(list.size()>0){
                item.put("changeStatus",list.get(0).getVerifyStatus());
            }else{
                item.put("changeStatus",0);
            }
            map.put("certificateStatus",1);
            map.put("certificateType","3");

            List<EnterpriseCertificate> serList=enterpriseCertificateService.findEntitysByCondition(map);
            if(serList.size()>0){
                 item.put("certificateNo",serList.get(0).getCertificateNo());
            }

            InspectionPlan  inspectionPlan=inspectionPlanService.getLastInspectionPlan(enterpriseId);
            if(inspectionPlan!=null){
                item.put("inspectionDate",inspectionPlan.getInspectionDate());
            }

            item.put("regulatoryDate",base.getArchivedDate());//目前没有完成自动安全等级功能，故使用企业建档日期代替
            EnterpriseSupervision es=enterpriseSupervisionService.findById(enterpriseId);
            if(es!=null){
                item.put("regulatoryLevel",es.getRegulatoryLevel());//企业监管等级
            }

            item.put("state","1");
        //    item.put("info",enterpriseBaseService.findById(enterpriseId));
         //   item.put("changeInfo",enterpriseBaseService.getBaseChange(map).get(0));
            return ResponseEntity.ok(item);
        }catch(Exception e){
            e.printStackTrace();
            item.put("state","0");
            return ResponseEntity.ok(item);
        }
    }

    /**
     * 企业基本信息更新
     * @param
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<EnterpriseBaseChange> changeBase(@RequestBody EnterpriseBaseChangeExt ext) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseBase base=enterpriseBaseService.findById(ext.getBase().getEnterpriseId());
            ext.getBase().setEnterpriseStatus(base.getEnterpriseStatus());
            ext.getBase().setArchivedDate(base.getArchivedDate());//建档日期
            ext.getBase().setSuperviseClassification(base.getSuperviseClassification());//企业监管等级
            enterpriseBaseService.modifyEnterpriseBase(ext.getBase(), sysUser.getUserId(), ext.getReason());
            return new ObjectRestResponse<EnterpriseBaseChange>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBase>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }


}
