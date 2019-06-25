package com.otec.foodsafety.web.safety;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResult;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.safety.OnsiteVerificationResultService;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/onsite/result")
public class OnsiteVerificationResultController extends VueBaseController<
        OnsiteVerificationResultService, OnsiteVerificationResult, Long> {
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    OnsiteVerificationResultService onsiteResultService;
    @Autowired
    PersonnelThreeService personnelThreeService;

    @RequestMapping(value = "/getOnsiteVerificationResult", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getOnsiteVerificationResult(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> list = onsiteResultService.getOnsiteResult(params);
        if (list != null && !list.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true)
                .data(list.get(0));
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<OnsiteVerificationResult> create(
            @RequestPart(value = "resultData") OnsiteVerificationResult onsiteResult,
            @RequestPart(value = "projectResultData") List<Map<String, Object>> onsiteResultItems,
            @RequestPart(value = "photoResultData") MultipartFile[] onsiteResultPhotos) {
        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        String response, fileName, fileRename, fileSubfix, filePath;
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
            PersonnelThree personnelThree = list.get(0);
            onsiteResult.setVerificateInstitution(personnelThree.getInstitutionId());
            onsiteResult.setVerificateUser(personnelThree.getPersonnelId());

            // 检查结果对象集合
            List<OnsiteVerificationResultItem> onsiteResultItemList = new ArrayList<>();
            OnsiteVerificationResultItem onsiteResultItem = null;
            Map<String, Object> onsiteResultItemMap = null;
            List<Object> resultsUnqualified = null;
            // 检查结果照片集合
            List<SysResource> sysResourceList = new ArrayList<>();
            SysResource sysResource = null;
            for (int i = 0; i < onsiteResultItems.size(); i++) {
                onsiteResultItemMap = onsiteResultItems.get(i);

                onsiteResultItem = new OnsiteVerificationResultItem();
                onsiteResultItem.setProjectId(Long.parseLong(
                        onsiteResultItemMap.get("projectId").toString()));
                onsiteResultItem.setInspectionContent(
                        onsiteResultItemMap.get("projectContent").toString());
                onsiteResultItem.setResultsQualified(
                        onsiteResultItemMap.get("resultsQualified").toString());
                resultsUnqualified = (List<Object>) onsiteResultItemMap.get("resultsUnqualified");
                if (resultsUnqualified.size() > 0) {
                    onsiteResultItem.setResultsUnqualified(
                            StringUtils.join(resultsUnqualified.toArray(), ' '));
                }

                onsiteResultItemList.add(onsiteResultItem);

                fileName = onsiteResultPhotos[i].getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        onsiteResultPhotos[i].getBytes());
                JSONObject jSONObject = JSON.parseObject(response);
                filePath = jSONObject.getString("imgUrl");
                fileRename = filePath.split("/")[filePath.split("/").length - 1];

                sysResource = new SysResource();
                sysResource.setResourceName(fileName);
                sysResource.setResourceRename(fileRename);
                sysResource.setResourceStorage(ResourceStorage.LOCAL.toString());
                sysResource.setResourceExtension(fileSubfix);
                sysResource.setResourceStatus("1");
                sysResource.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                sysResource.setResourceContent(onsiteResultPhotos[i].getBytes());
                sysResource.setResourceLength(onsiteResultPhotos[i].getSize());
                sysResource.setResourcePath(filePath);

                sysResourceList.add(sysResource);
            }
            onsiteResultService.create(onsiteResult, onsiteResultItemList, sysResourceList);
            return new ObjectRestResponse<OnsiteVerificationResult>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<OnsiteVerificationResult>().rel(false);
            resp.setStatus(500);
            resp.setMessage("现场核查记录创建异常");
            return resp;
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<OnsiteVerificationResult> update(
            @RequestPart(value = "resultData") OnsiteVerificationResult onsiteResult,
            @RequestPart(value = "projectResultData") List<Map<String, Object>> onsiteResultItems,
            @RequestPart(value = "photoRectifiedData") MultipartFile[] onsiteRectifiedPhotos) {
        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        String response, fileName, fileRename, fileSubfix, filePath;
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
            PersonnelThree personnelThree = list.get(0);
            onsiteResult.setVerificateInstitution(personnelThree.getInstitutionId());
            onsiteResult.setVerificateUser(personnelThree.getPersonnelId());

            // 检查结果对象集合
            List<OnsiteVerificationResultItem> onsiteResultItemList = new ArrayList<>();
            OnsiteVerificationResultItem onsiteResultItem = null;
            Map<String, Object> onsiteResultItemMap = null;
            List<Object> resultsUnqualified = null;
            // 检查结果照片集合
            List<SysResource> sysResourceList = new ArrayList<>();
            SysResource sysResource = null;
            int index = 0;
            for (int i = 0; i < onsiteResultItems.size(); i++) {
                onsiteResultItemMap = onsiteResultItems.get(i);

                onsiteResultItem = new OnsiteVerificationResultItem();
                onsiteResultItem.setItemId(Long.parseLong(
                        onsiteResultItemMap.get("itemId").toString()));
                onsiteResultItem.setProjectId(Long.parseLong(
                        onsiteResultItemMap.get("projectId").toString()));
                onsiteResultItem.setInspectionContent(
                        onsiteResultItemMap.get("projectContent").toString());
                onsiteResultItem.setResultsQualified(
                        onsiteResultItemMap.get("resultsQualified").toString());
                if (onsiteResultItemMap.get("resultsUnqualified") != null) {
                    resultsUnqualified = (List<Object>) onsiteResultItemMap.get("resultsUnqualified");
                    if (resultsUnqualified.size() > 0) {
                        onsiteResultItem.setResultsUnqualified(
                                StringUtils.join(resultsUnqualified.toArray(), ' '));
                    }
                }

                if (onsiteResultItemMap.get("isRectified") != null)
                    onsiteResultItem.setIsRectified(onsiteResultItemMap.get("isRectified").toString());

                if (((List) onsiteResultItemMap.get("phoneRectified")).isEmpty() ||
                        ((List) onsiteResultItemMap.get("phoneRectified")).get(0) instanceof Integer) {
                    // 未修改整改后图片
                    sysResource = new SysResource();
                } else {
                    fileName = onsiteRectifiedPhotos[index].getOriginalFilename();
                    fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                    response = HttpURLConnectionUtils.sendMessage(
                            uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                            onsiteRectifiedPhotos[index].getBytes());
                    JSONObject jSONObject = JSON.parseObject(response);
                    filePath = jSONObject.getString("imgUrl");
                    fileRename = filePath.split("/")[filePath.split("/").length - 1];

                    sysResource = new SysResource();
                    sysResource.setResourceName(fileName);
                    sysResource.setResourceRename(fileRename);
                    sysResource.setResourceStorage(ResourceStorage.LOCAL.toString());
                    sysResource.setResourceExtension(fileSubfix);
                    sysResource.setResourceStatus("1");
                    sysResource.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                    sysResource.setResourceContent(onsiteRectifiedPhotos[index].getBytes());
                    sysResource.setResourceLength(onsiteRectifiedPhotos[index].getSize());
                    sysResource.setResourcePath(filePath);

                    index++;
                }
                onsiteResultItemList.add(onsiteResultItem);
                sysResourceList.add(sysResource);
            }
            onsiteResultService.update(onsiteResult, onsiteResultItemList, sysResourceList);
            return new ObjectRestResponse<OnsiteVerificationResult>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<OnsiteVerificationResult>().rel(false);
            resp.setStatus(500);
            resp.setMessage("现场核查记录更新异常");
            return resp;
        }
    }
}
