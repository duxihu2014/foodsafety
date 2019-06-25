package com.otec.foodsafety.web.safety;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionResult;
import com.otec.foodsafety.entity.safety.InspectionResultItem;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.safety.InspectionResultService;
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
@RequestMapping("api/admin/safety/inspection/result")
public class InspectionResultController extends VueBaseController<
        InspectionResultService, InspectionResult, Long> {
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    InspectionResultService inspectionResultService;
    @Autowired
    PersonnelThreeService personnelThreeService;

    @RequestMapping(value = "/getInspectionResult", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getInspectionResult(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> list = inspectionResultService.getInspectionResult(params);
        if (list != null && !list.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true)
                .data(list.get(0));
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<InspectionResult> create(
            @RequestPart(value = "resultData") InspectionResult inspectionResult,
            @RequestPart(value = "projectResultData") List<Map<String, Object>> inspectionResultItems,
            @RequestPart(value = "photoResultData") MultipartFile[] inspectionResultPhotos) {
        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        String response, fileName, fileRename, fileSubfix, filePath;
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
            PersonnelThree personnelThree = list.get(0);
            inspectionResult.setInspectedInstitution(personnelThree.getInstitutionId());
            inspectionResult.setInspectedUser(personnelThree.getPersonnelId());

            // 检查结果对象集合
            List<InspectionResultItem> inspectionResultItemList = new ArrayList<>();
            InspectionResultItem inspectionResultItem = null;
            Map<String, Object> inspectionResultItemMap = null;
            List<Object> resultsUnqualified = null;
            // 检查结果照片集合
            List<SysResource> sysResourceList = new ArrayList<>();
            SysResource sysResource = null;
            for (int i = 0; i < inspectionResultItems.size(); i++) {
                inspectionResultItemMap = inspectionResultItems.get(i);

                inspectionResultItem = new InspectionResultItem();
                inspectionResultItem.setProjectId(Integer.parseInt(
                        inspectionResultItemMap.get("projectId").toString()));
                inspectionResultItem.setInspectionContent(
                        inspectionResultItemMap.get("projectContent").toString());
                inspectionResultItem.setResultsQualified(
                        inspectionResultItemMap.get("resultsQualified").toString());
                resultsUnqualified = (List<Object>) inspectionResultItemMap.get("resultsUnqualified");
                if (resultsUnqualified.size() > 0) {
                    inspectionResultItem.setResultsUnqualified(
                            StringUtils.join(resultsUnqualified.toArray(), ' '));
                }

                inspectionResultItemList.add(inspectionResultItem);

                fileName = inspectionResultPhotos[i].getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                response = HttpURLConnectionUtils.sendMessage(
                        uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                        inspectionResultPhotos[i].getBytes());
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
                sysResource.setResourceContent(inspectionResultPhotos[i].getBytes());
                sysResource.setResourceLength(inspectionResultPhotos[i].getSize());
                sysResource.setResourcePath(filePath);

                sysResourceList.add(sysResource);
            }
            inspectionResultService.create(inspectionResult, inspectionResultItemList, sysResourceList);
            return new ObjectRestResponse<InspectionResult>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionResult>().rel(false);
            resp.setStatus(500);
            resp.setMessage("日常检查记录创建异常");
            return resp;
        }
    }
}
