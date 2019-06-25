package com.otec.foodsafety.web.purchase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.purchase.Materials;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.purchase.MaterialsService;
import com.otec.foodsafety.service.purchase.SupplierServiceService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.*;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/materials")
public class MaterialsController extends VueBaseController<MaterialsService, Materials, Long> {
    @Autowired
    private  MaterialsService materialsService;
    @Autowired
    SupplierServiceService supplierServiceService;
    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    SysAreaService sysAreaService;



    @Override
    public GridDataModel list(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }
        }
        else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());

        PageObject po = getPageObject(params);
        Integer total = materialsService.getPageCount(params);
        List<Materials> list = materialsService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<Materials> create(
            @RequestPart(value = "formData") Materials entity,
            @RequestPart(value = "productionCertificateFile", required = false) MultipartFile productionCertificateFile) {
        try {
            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
            materialsService.add(entity,productionCertificateFile,uploadUrl,imageFolder);
            return new ObjectRestResponse<Materials>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Materials>().rel(false);
            resp.setStatus(500);
            resp.setMessage("新增原料失败!");
            return resp;
        }
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<Materials> update(
            @RequestPart(value = "formData") Materials entity,
            @RequestPart(value = "productionCertificateFile", required = false) MultipartFile imageFile) {
        try {
            SysResource resource = null;
            if(imageFile!=null){
                resource=new SysResource();
                String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
                String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
                String fileName = imageFile.getOriginalFilename();
                String str = HttpURLConnectionUtils.sendMessage(uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,
                        imageFile.getBytes());
                JSONObject jSONObject = JSON.parseObject(str);
                String path = jSONObject.getString("imgUrl");
                String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                String rename = path.split("/")[path.split("/").length-1];

                resource.setResourceName(fileName);
                resource.setResourceRename(rename);
                resource.setResourceStorage(ResourceStorage.LOCAL.toString());
                resource.setResourceExtension(subfix);
                resource.setResourceStatus("1");
                resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
                resource.setResourceLength(imageFile.getSize());
                resource.setResourcePath( path );
            }
            materialsService.updateMaterial(entity,resource);
            return new ObjectRestResponse<Materials>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Materials>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新原料失败!");
            return resp;
        }
    }


    @Override
    public ObjectRestResponse<Materials> get(@PathVariable Long id) {
        Materials cs= materialsService.findByMaterialId(id);
        return new ObjectRestResponse<Materials>().rel(true) .data(cs);
    }



    @RequestMapping(value = "/getSuppliers/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
     public ObjectRestResponse<SupplierService>  getSuppliers(@PathVariable Long enterpriseId ) {
        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("supplierCategory", "1");//1供货商
            condition.put("enterpriseId", enterpriseId);
            List<SupplierService> list=supplierServiceService.getSuppliersByEnterpriseId(condition);
            return new ObjectRestResponse<SupplierService>().rel(true).data(list);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
            resp.setStatus(500);
            resp.setMessage("获取企业原料失败!");
            return resp;
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<Materials> changeStatus(@RequestParam Long[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<Materials>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                materialsService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<Materials>().rel(true);
            }
            return new ObjectRestResponse<Materials>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Materials>().rel(false);
            resp.setStatus(500);
            resp.setMessage("原料删除异常");
            return resp;
        }
    }

}

