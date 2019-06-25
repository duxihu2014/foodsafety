package com.otec.foodsafety.web.purchase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.purchase.SupplierServiceService;
import com.otec.foodsafety.util.*;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/purchase/materials/supplier")
public class SupplierServiceController extends VueBaseController<
        SupplierServiceService, SupplierService, Long> {
    @Autowired
    SupplierServiceService supplierServiceService;
    @Autowired
    SessionFilter sessionFilter;

    @Override
    public GridDataModel list(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("enterpriseId", String.valueOf(sysUser.getEnterpriseId()));
        params.put("extendSql", " and t3.ENTERPRISE_STATUS = 3 ");
        PageObject po = getPageObject(params);
        List<SupplierService> supplierServiceList = new ArrayList<SupplierService>();
        Integer total = supplierServiceService.getSupplierServiceCount(params);
        if (total > 0)
            supplierServiceList = supplierServiceService.getSupplierServicePage(params, po.getOffset(), po.getPageSize());
        GridDataModel model = new GridDataModel(supplierServiceList, total);
        return model;
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel listAll(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if (StringUtils.isBlank(params.get("areaId")))
            params.put("areaId", String.valueOf(sysUser.getAreaId()));
        params.put("extendSql", " and t3.ENTERPRISE_STATUS = 3 ");
        PageObject po = getPageObject(params);
        List<SupplierService> supplierServiceList = new ArrayList<SupplierService>();
        Integer total = supplierServiceService.getSupplierServiceCount(params);
        if (total > 0)
            supplierServiceList = supplierServiceService.getSupplierServicePage(params, po.getOffset(), po.getPageSize());
        GridDataModel model = new GridDataModel(supplierServiceList, total);
        return model;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<SupplierService> create(
            @RequestPart(value = "formData") SupplierService entity,
            @RequestPart(value = "idCardFrontImgFile", required = false) MultipartFile idCardFrontImgFile,
            @RequestPart(value = "idCardBackImgFile", required = false) MultipartFile idCardBackImgFile,
            @RequestPart(value = "businessLicenceImgFile", required = false) MultipartFile businessLicenceImgFile) {
        //SupplierService entity = JSONUtils.fromJson(formData, SupplierService.class);
        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        SysResource idCardFront = null, idCardBack = null, businessLicence = null;
        String response, fileName, fileRename, fileSubfix, filePath;
        try {
            // 上传身份证正面图片
            if (idCardFrontImgFile != null && !idCardFrontImgFile.isEmpty()) {
                fileName = idCardFrontImgFile.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        idCardFrontImgFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                idCardFront = new SysResource();
                idCardFront.setResourceName(fileName);
                idCardFront.setResourceRename(fileRename);
                idCardFront.setResourceStorage(ResourceStorage.LOCAL.toString());
                idCardFront.setResourceExtension(fileSubfix);
                idCardFront.setResourceStatus("1");
                idCardFront.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                idCardFront.setResourceContent(idCardFrontImgFile.getBytes());
                idCardFront.setResourceLength(idCardFrontImgFile.getSize());
                idCardFront.setResourcePath(filePath);
            }

            // 上传身份证背面图片
            if (idCardBackImgFile != null && !idCardBackImgFile.isEmpty()) {
                fileName = idCardBackImgFile.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        idCardBackImgFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                idCardBack = new SysResource();
                idCardBack.setResourceName(fileName);
                idCardBack.setResourceRename(fileRename);
                idCardBack.setResourceStorage(ResourceStorage.LOCAL.toString());
                idCardBack.setResourceExtension(fileSubfix);
                idCardBack.setResourceStatus("1");
                idCardBack.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                idCardBack.setResourceContent(idCardBackImgFile.getBytes());
                idCardBack.setResourceLength(idCardBackImgFile.getSize());
                idCardBack.setResourcePath(filePath);
            }

            // 上传营业执照图片
            if (businessLicenceImgFile != null && !businessLicenceImgFile.isEmpty()) {
                fileName = businessLicenceImgFile.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        businessLicenceImgFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                businessLicence = new SysResource();
                businessLicence.setResourceName(fileName);
                businessLicence.setResourceRename(fileRename);
                businessLicence.setResourceStorage(ResourceStorage.LOCAL.toString());
                businessLicence.setResourceExtension(fileSubfix);
                businessLicence.setResourceStatus("1");
                businessLicence.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                businessLicence.setResourceContent(businessLicenceImgFile.getBytes());
                businessLicence.setResourceLength(businessLicenceImgFile.getSize());
                businessLicence.setResourcePath(filePath);
            }
            SysUser sysUser = sessionFilter.getJWTUser(request);
            SupplierServiceEnterprise supplierServiceEnterprise = new SupplierServiceEnterprise();
            supplierServiceEnterprise.setEnterpriseId(sysUser.getEnterpriseId());
            entity.setSupplierStatus("1");
            supplierServiceService.create(entity, idCardFront, idCardBack, businessLicence, supplierServiceEnterprise);
            return new ObjectRestResponse<SupplierService>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            resp.setStatus(500);
            resp.setMessage("供货商创建异常");
            return resp;
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<SupplierService> update(
            @RequestPart(value = "formData") SupplierService entity,
            @RequestParam(value = "idCardFront") String idCardFrontFlag,
            @RequestParam(value = "idCardBack") String idCardBackFlag,
            @RequestParam(value = "businessLicence") String businessLicenceFlag,
            @RequestPart(value = "idCardFrontImgFile", required = false) MultipartFile idCardFrontImgFile,
            @RequestPart(value = "idCardBackImgFile", required = false) MultipartFile idCardBackImgFile,
            @RequestPart(value = "businessLicenceImgFile", required = false) MultipartFile businessLicenceImgFile) {
        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        SysResource idCardFront = null, idCardBack = null, businessLicence = null;
        String response, fileName, fileRename, fileSubfix, filePath;
        try {
            // 更新身份证正面图片
            if ("1".equals(idCardFrontFlag)) {
                fileName = idCardFrontImgFile.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        idCardFrontImgFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                idCardFront = new SysResource();
                idCardFront.setResourceName(fileName);
                idCardFront.setResourceRename(fileRename);
                idCardFront.setResourceStorage(ResourceStorage.LOCAL.toString());
                idCardFront.setResourceExtension(fileSubfix);
                idCardFront.setResourceStatus("1");
                idCardFront.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                idCardFront.setResourceContent(idCardFrontImgFile.getBytes());
                idCardFront.setResourceLength(idCardFrontImgFile.getSize());
                idCardFront.setResourcePath(filePath);
            } else if ("-1".equals(idCardFrontFlag)) {
                // 删除身份证正面图片
                idCardFront = new SysResource();
            } else {
                // 不更新身份证正面图片
                idCardFront = null;
            }
            // 更新身份证背面图片
            if ("1".equals(idCardBackFlag)) {
                fileName = idCardBackImgFile.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        idCardBackImgFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                idCardBack = new SysResource();
                idCardBack.setResourceName(fileName);
                idCardBack.setResourceRename(fileRename);
                idCardBack.setResourceStorage(ResourceStorage.LOCAL.toString());
                idCardBack.setResourceExtension(fileSubfix);
                idCardBack.setResourceStatus("1");
                idCardBack.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                idCardBack.setResourceContent(idCardBackImgFile.getBytes());
                idCardBack.setResourceLength(idCardBackImgFile.getSize());
                idCardBack.setResourcePath(filePath);
            } else if ("-1".equals(idCardBackFlag)) {
                // 删除身份证背面图片
                idCardBack = new SysResource();
            } else {
                // 不更新身份证背面图片
                idCardBack = null;
            }
            // 更新营业执照图片
            if ("1".equals(businessLicenceFlag)) {
                fileName = businessLicenceImgFile.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        businessLicenceImgFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                businessLicence = new SysResource();
                businessLicence.setResourceName(fileName);
                businessLicence.setResourceRename(fileRename);
                businessLicence.setResourceStorage(ResourceStorage.LOCAL.toString());
                businessLicence.setResourceExtension(fileSubfix);
                businessLicence.setResourceStatus("1");
                businessLicence.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                businessLicence.setResourceContent(businessLicenceImgFile.getBytes());
                businessLicence.setResourceLength(businessLicenceImgFile.getSize());
                businessLicence.setResourcePath(filePath);
            } else if ("-1".equals(businessLicenceFlag)) {
                // 删除营业执照图片
                businessLicence = new SysResource();
            } else {
                // 不更新营业执照图片
                businessLicence = null;
            }
            supplierServiceService.update(entity, idCardFront, idCardBack, businessLicence);
            return new ObjectRestResponse<SupplierService>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            resp.setStatus(500);
            resp.setMessage("供货商创建异常");
            return resp;
        }
    }

    /**
     * @TODO 检查供货商名称是否重复
     * @param supplierName
     * @return
     */
    @RequestMapping(value = "/checkSupplierName", method = RequestMethod.GET)
    public ObjectRestResponse<SupplierService> checkSupplierName(@RequestParam String supplierName) {
        Map<String ,Object> param = new HashMap<>();
        param.put("supplierName", supplierName);
        List<SupplierService> supplierService = supplierServiceService.findEntitysByCondition(param);
        if(supplierService.size() > 0){
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            return resp;
        } else {
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(true);
            return resp;
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<SupplierService> changeStatus(@RequestParam Long[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<SupplierService>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                supplierServiceService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<SupplierService>().rel(true);
            }
            return new ObjectRestResponse<SupplierService>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            resp.setStatus(500);
            resp.setMessage("供货商删除异常");
            return resp;
        }
    }
}
