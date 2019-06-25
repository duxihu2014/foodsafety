package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.CateringStaffeExt;
import com.otec.foodsafety.entity.enterprise.*;
import com.otec.foodsafety.entity.equipment.Audio;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;
import com.otec.foodsafety.entity.purchase.Materials;
import com.otec.foodsafety.entity.purchase.MaterialsEnterprisePurchase;
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.safety.InspectionPlan;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.*;
import com.otec.foodsafety.service.producesafety.RecordAdditiveUseageService;
import com.otec.foodsafety.service.purchase.MaterialsEnterprisePurchaseService;
import com.otec.foodsafety.service.purchase.MaterialsInspectionReportService;
import com.otec.foodsafety.service.purchase.MaterialsService;
import com.otec.foodsafety.service.purchase.SupplierServiceService;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.StringUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.util.vo.FrontUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原料及采购管理
 */
@RequestMapping("/api/enterprise/material")
@RestController
public class EnterpriseMaterialInterfafce extends BaseInterface {



    @Autowired
    MaterialsEnterprisePurchaseService materialsEnterprisePurchaseService;
    @Autowired
    RecordAdditiveUseageService rAdditiveUserService;
    @Autowired
    MaterialsService materialsService;
    @Autowired
    SupplierServiceService supplierServiceService;

    @Autowired
    MaterialsInspectionReportService inspectionReportService;
    @Autowired
    SysResourceService sysResourceService;

    @Autowired
    MaterialsInspectionReportService materialsInspectionReportService;

    /**
     * 获取企业原料进货信息列表
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {
        // 查询列表数据
        params.put("enterpriseId",enterpriseId.toString());
        if(params.get("materialName")!=null){
            params.put("materialNameLike",params.get("materialName"));
            params.remove("materialName");
        }
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = materialsEnterprisePurchaseService.getGridDataModelByCondition(po);

        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (MaterialsEnterprisePurchase purchase:(List<MaterialsEnterprisePurchase>)model.getRows()){
            MaterialsInspectionReport report = inspectionReportService.findByPurcharseId(purchase.getPurchaseId().longValue());
            if(report!=null){
                purchase.setInspectionReportId(report.getInspectionReportId());
                purchase.setInspectionReportPhoto(report.getInspectionReportPhoto().toString());
                purchase.setValidDate(report.getValidDate());
                SysResource sysResource=sysResourceService.getResourceById(report.getInspectionReportPhoto().longValue());
                if(sysResource!=null){
                    purchase.setInspectionReportPhotoPath(imageServerUrl+"/"+sysResource.getResourcePath());
                }
            }
        }
        return model;
    }


    /**
     *  企业原料采购详细信息
     * @param purchaseId
     * @return
     */
    @RequestMapping(value = "/detail/{purchaseId}", method = RequestMethod.GET)
    public ResponseEntity<MaterialsEnterprisePurchase> get(@PathVariable Long purchaseId) {
        MaterialsEnterprisePurchase purchase= materialsEnterprisePurchaseService.findById(purchaseId);
        MaterialsInspectionReport report = inspectionReportService.findByPurcharseId(purchaseId);
        if(report!=null){
            purchase.setInspectionReportId(report.getInspectionReportId());
            purchase.setInspectionReportPhoto(report.getInspectionReportPhoto().toString());
            purchase.setValidDate(report.getValidDate());
            SysResource sysResource=sysResourceService.getResourceById(report.getInspectionReportPhoto().longValue());
            if(sysResource!=null){
                String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
                purchase.setInspectionReportPhotoPath(imageServerUrl+"/"+sysResource.getResourcePath());
            }
        }
        return ResponseEntity.ok(purchase);
    }

    /**
     *  企业原料采购信息保存
     * @param purchase
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<MaterialsEnterprisePurchase> save(@RequestBody MaterialsEnterprisePurchase purchase){
        try {
            Materials materials=materialsService.findByMaterialId(purchase.getMaterialId().longValue());

            SupplierService supplierService=supplierServiceService.findById(materials.getSupplierId());

            purchase.setSupplierName(materials.getSupplierName());
            purchase.setBusinessLicenceNoSupplier(supplierService.getBusinessLicenceNo());
            purchase.setMaterialName(materials.getMaterialName());
            purchase.setMaterialCategory(materials.getMaterialCategory());
            purchase.setBarcodeCoding(materials.getBarcodeCoding());
            purchase.setSpecification(materials.getSpecification());
            purchase.setUnit(materials.getUnit());
            purchase.setOriginPlace(materials.getOriginPlace());
            purchase.setBrand(materials.getBrand());
            purchase.setProductionName(materials.getProductionName());
            purchase.setProductionCertificateNo(materials.getProductionCertificateNo());

            if(purchase.getPurchaseId()==null || purchase.getPurchaseId().intValue()<=0){
                materialsEnterprisePurchaseService.add(purchase);
            }
            else{
                materialsEnterprisePurchaseService.update(purchase);
            }
            return new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }
    }

    /**
     * 	原料采购记录删除
     * @param purchaseId
     * @return
     */
    @RequestMapping(value = "/delete/{purchaseId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<MaterialsEnterprisePurchase> remove(@PathVariable Long purchaseId) {
        try {
            Map<String, Object> cond = new HashMap<String, Object>();
            cond.put("extendSql", " PURCHASE_ID  in (" + purchaseId + ")");

            Integer num = rAdditiveUserService.countByCondition(cond);
            if (num > 0) {
                ObjectRestResponse resp = new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(false);
                resp.setStatus(500);
                resp.setMessage("采购信息下有添加剂使用信息,无法删除!");
                return resp;
            } else {
                //物理删除
                materialsEnterprisePurchaseService.deleteById(purchaseId);
            }
            return new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(false);

            resp.setStatus(500);
            resp.setMessage("删除失败!");
            return resp;
        }
    }

    /**
     * 获取质检报告列表
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/reports/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel reportList(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {
        params.put("enterpriseId", enterpriseId.toString());

        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);

        GridDataModel model = materialsInspectionReportService.findInspectionReport(po);
        List<HashMap> results =(List<HashMap>)model.getRows();

        for (HashMap report:results){
            Integer photoId=(Integer)report.get("inspectionReportPhoto");
            SysResource sysResource=sysResourceService.getResourceById(photoId.longValue());
            if(sysResource!=null){
                String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
                report.put("inspectionReportPhotoPath",imageServerUrl+"/"+sysResource.getResourcePath());
            }
        }
        return model;
    }


    /**
     *  获取企业原料管理列表
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value="/manage/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getMangelist(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {

        params.put("enterpriseId",enterpriseId.toString());
        params.put("materialStatus","1");
        PageObject po = getPageObject(params);
        Integer total = materialsService.getPageCount(params);
        List<Materials> list = materialsService.getPage(params,po.getOffset(),po.getPageSize());
        for (Materials materials:list){
            SysResource sysResource=sysResourceService.getResourceById(materials.getProductionCertificatePhoto());
            if(sysResource!=null){
                String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
                materials.setResourcePath(imageServerUrl+"/"+sysResource.getResourcePath());
            }
        }
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }

    /**
     *  企业原料信息保存
     * @param entity
     * @return
     */
    @RequestMapping(value = "/manage/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Materials> manageSave(@RequestBody  Materials entity){
        try {

            if(entity.getMaterialId()>0){
                Materials old=materialsService.findById(entity.getMaterialId());
                entity.setMaterialStatus(old.getMaterialStatus());
                materialsService.updateById(entity);
            }else{
                entity.setMaterialStatus("1");
                materialsService.persist(entity);
            }
            return new ObjectRestResponse<Materials>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Materials>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存原料失败!");
            return resp;
        }
    }
    /**
     *  获取原料详细信息
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/manage/detail/{materialId}", method = RequestMethod.GET)
    public ResponseEntity<Materials> getMaterials(@PathVariable Long  materialId) {
        Materials materials=materialsService.findByMaterialId(materialId);
        return ResponseEntity.ok(materials);
    }

    /**
     * 	企业原料信息删除
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/manage/delete/{materialId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Materials> delete(@PathVariable Long materialId) {
        try {
            Materials materials=materialsService.findById(materialId);
            materials.setMaterialStatus("0");
            materialsService.updateById(materials);
            return new ObjectRestResponse<Materials>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Materials>().rel(false);
            resp.setStatus(500);
            resp.setMessage("删除原料失败!");
            return resp;
        }
    }
}
