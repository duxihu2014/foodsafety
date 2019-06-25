package com.otec.foodsafety.web.classification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.classification.ClassificationDetail;
import com.otec.foodsafety.entity.commission.CommissionContract;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.classification.ClassificationDetailService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: xiongjing
 * @Date:  2019/01/17 0011 下午 14:39
 */
@RestController
@RequestMapping(value = "api/classification/detail")
public class ClassificationDetailController extends VueBaseController<ClassificationDetailService, ClassificationDetail, Long> {


    @Autowired
    ClassificationDetailService classificationDetailService;
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    DateLocalService dateLocalService;

    @RequestMapping(value = "/changeClassification", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<ClassificationDetail> add(@RequestPart ClassificationDetail detail,
                                                        @RequestPart(required = false) MultipartFile photoImage) {
        SysResource photo=null;
        SysUser sysUser = sessionFilter.getJWTUser(request);

        try {
            if(photoImage!=null && !photoImage.isEmpty()){
                 String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
                String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
                String response, fileName, fileRename, fileSubfix, filePath;

                fileName = photoImage.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀

                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        photoImage.getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                photo = new SysResource();
                photo.setResourceName(fileName);
                photo.setResourceRename(fileRename);
                photo.setResourceStorage(ResourceStorage.LOCAL.toString());
                photo.setResourceExtension(fileSubfix);
                photo.setResourceStatus("1");
                photo.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                photo.setResourceContent(photoImage.getBytes());
                photo.setResourceLength(photoImage.getSize());
                photo.setResourcePath(filePath);
            }
            detail.setUserId(sysUser.getUserId());
            detail.setOperTime(dateLocalService.getDataBaseDate());
            classificationDetailService.add(detail,photo);
            return new ObjectRestResponse<ClassificationDetail>().rel(true);
        }catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ClassificationDetail>().rel(false);
            resp.setStatus(500);
            resp.setMessage("监管等级变更异常");
            return resp;
        }

    }

    /**
     * 查询企业监管等级变更记录
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/changeRecord/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ClassificationDetail> changeRecord(@PathVariable Long enterpriseId) {

        try{
            List<Map> list= classificationDetailService.getChangeRecords(enterpriseId);
            return new ObjectRestResponse<ClassificationDetail>().rel(true).data(list);
        }catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ClassificationDetail>().rel(false);
            resp.setStatus(500);
            resp.setMessage("监管等级变更异常");
            return resp;
        }

    }

}
