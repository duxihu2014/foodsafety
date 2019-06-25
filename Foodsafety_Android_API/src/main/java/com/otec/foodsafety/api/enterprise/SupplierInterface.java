package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.CateringStaffeExt;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseEquipmentService;
import com.otec.foodsafety.service.purchase.SupplierServiceService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/enterprise/supplier")
public class SupplierInterface extends BaseInterface {


    @Autowired
    SupplierServiceService supplierServiceService;
    @Autowired
    SysResourceService sysResourceService;
    @Autowired
    SessionFilter sessionFilter;

    /**
     *  查询企业的供货商信息列表
     * @param
     * @return
     */
    @RequestMapping(value="/list/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {
        // 查询列表数据

        PageObject po = getPageObject(params);
        List<SupplierService> supplierServiceList = new ArrayList<SupplierService>();
        params.put("enterpriseId",enterpriseId.toString());
        params.put("supplierStatus","1");
        Integer total = supplierServiceService.getSupplierServiceCount(params);
        if (total > 0)
            supplierServiceList = supplierServiceService.getSupplierServicePage(params, po.getOffset(), po.getPageSize());
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (SupplierService supplierService:supplierServiceList){
            SysResource idCardNoPhotoFront=sysResourceService.findById(supplierService.getIdCardNoPhotoFront());
            if(supplierService.getIdCardNoPhotoFront()!=null){
                supplierService.setIdCardNoPhotoFrontPath(imageServerUrl+"/"+idCardNoPhotoFront.getResourcePath());
            }
            SysResource idCardNoPhotoBack=sysResourceService.findById(supplierService.getIdCardNoPhotoBack());
            if(idCardNoPhotoBack!=null){
                supplierService.setIdCardNoPhotoBackPath(imageServerUrl+"/"+idCardNoPhotoBack.getResourcePath());
            }
            SysResource businessLicencePhoto=sysResourceService.findById(supplierService.getBusinessLicencePhoto());
            if(businessLicencePhoto!=null){
                supplierService.setBusinessLicencePhotoPath(imageServerUrl+"/"+businessLicencePhoto.getResourcePath());
            }
        }

        GridDataModel model = new GridDataModel(supplierServiceList, total);
        return model;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity <SupplierService> get(@PathVariable Long id) {
        SupplierService supplierService=supplierServiceService.findById(id);
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);

        SysResource idCardNoPhotoFront=sysResourceService.findById(supplierService.getIdCardNoPhotoFront());
        if(idCardNoPhotoFront!=null){
            supplierService.setIdCardNoPhotoFrontPath(imageServerUrl+"/"+idCardNoPhotoFront.getResourcePath());
        }
        SysResource idCardNoPhotoBack=sysResourceService.findById(supplierService.getIdCardNoPhotoBack());
        if(idCardNoPhotoBack!=null){
            supplierService.setIdCardNoPhotoBackPath(imageServerUrl+"/"+idCardNoPhotoBack.getResourcePath());
        }
        SysResource businessLicencePhoto=sysResourceService.findById(supplierService.getBusinessLicencePhoto());
        if(businessLicencePhoto!=null){
            supplierService.setBusinessLicencePhotoPath(imageServerUrl+"/"+businessLicencePhoto.getResourcePath());
        }
        return ResponseEntity.ok(supplierService);
    }

    /**
     * 	供货商信息保存（添加、更新）
     * @param supplierService
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<SupplierService> save(@RequestBody SupplierService supplierService){
        try {

            if(supplierService.getSupplierId()>0){
                SupplierService old=supplierServiceService.findById(supplierService.getSupplierId());
                supplierService.setSupplierStatus(old.getSupplierStatus());
                supplierServiceService.updateById(supplierService);
            }else{
                SysUser sysUser = sessionFilter.getJWTUser(request);
                supplierService.setSupplierStatus("1");
                SupplierServiceEnterprise supplierServiceEnterprise = new SupplierServiceEnterprise();
                supplierServiceEnterprise.setEnterpriseId(sysUser.getEnterpriseId());
                supplierServiceService.create(supplierService,null,null,null,supplierServiceEnterprise);
            }
            return new ObjectRestResponse<SupplierService>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存供货商失败!");
            return resp;
        }
    }

    /**
     * 	供货商删除
     * @param supplierId
     * @return
     */
    @RequestMapping(value = "/delete/{supplierId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<SupplierService> delete(@PathVariable Long supplierId) {
        try {
            SupplierService supplierService = supplierServiceService.findById(supplierId);
            supplierService.setSupplierStatus("2");
            supplierServiceService.updateById(supplierService);
            return new ObjectRestResponse<SupplierService>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            resp.setStatus(500);
            resp.setMessage("供货商删除失败!");
            return resp;
        }
    }



}
