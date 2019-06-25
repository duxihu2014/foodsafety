package com.otec.foodsafety.api.supervise;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.*;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.safety.*;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.*;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 现场核查  和  日常检查  业务
 */
@RestController
@RequestMapping("/api/safe")
public class SafeInterface extends BaseInterface {


    @Autowired
    InspectionTemplateService inspectionTemplateService;

    @Autowired
    InspectionProjectService inspectionProjectService;

    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    PersonnelThreeService personnelThreeService;
    @Autowired
    OnsiteVerificationResultService onsiteResultService;
    @Autowired
    OnsiteVerificationResultItemService onsiteVerificationResultItemService;

    @Autowired
    InspectionResultService inspectionResultService;
    @Autowired
    SysResourceService sysResourceService;

    /**
     * 获取检查项目模板
     * @param params
     * @return
     */
    @RequestMapping(value = "/template/list", method = RequestMethod.GET)
    @ResponseBody
    public List list(@RequestParam Map<String, String> params) {
        List<Map> result=new ArrayList<Map>();

        params.put("templateStatus","1");
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        List<InspectionTemplate> list = inspectionTemplateService.findEntitysByCondition(po);
        for (InspectionTemplate template:list){
            Map templatemap=new HashMap();
            templatemap.put("templateId",template.getTemplateId());
            templatemap.put("projectStatus","1");
            List<InspectionProject> pros=inspectionProjectService.getTemplateProject(templatemap);
            templatemap.remove("projectStatus");
            templatemap.put("projects",pros);
            templatemap.put("templateName",template.getTemplateName());
            result.add(templatemap);
        }
        return result;
    }

    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST,consumes ={"multipart/form-data"} )
    @ResponseBody
    public ObjectRestResponse uploadImg(@RequestPart MultipartFile uploadImg)   {

        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        String fileName = uploadImg.getOriginalFilename();

        String fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀

        try {
            String response = HttpURLConnectionUtils.sendMessage(
                    uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                    uploadImg.getBytes());


            JSONObject jSONObject = JSON.parseObject(response);
            String filePath = jSONObject.getString("imgUrl");
            String fileRename = filePath.split("/")[filePath.split("/").length - 1];

            SysResource sysResource = new SysResource();
            sysResource.setResourceName(fileName);
            sysResource.setResourceRename(fileRename);
            sysResource.setResourceStorage(ResourceStorage.LOCAL.toString());
            sysResource.setResourceExtension(fileSubfix);
            sysResource.setResourceStatus("1");
            sysResource.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
            sysResource.setResourceContent(uploadImg.getBytes());
            sysResource.setResourceLength(uploadImg.getSize());
            sysResource.setResourcePath(filePath);
            sysResource=sysResourceService.addResource(sysResource);

            SysResource sysResource2= new SysResource();
            sysResource2.setResourceId(sysResource.getResourceId());
            String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
            sysResource2.setResourcePath(imageServerUrl+"/"+filePath);
            System.out.println(imageServerUrl+"/"+filePath);
            return new ObjectRestResponse().rel(true).data(sysResource2);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse().rel(false);
            resp.setStatus(500);
            resp.setMessage("图片上传异常");
            return resp;
        }
    }


    /**
     * 现场核查提交
     * @param onsiteResultJson
     * @param onsiteResultItemsJson
     * @return
     */
    @RequestMapping(value = "/verification/result/create", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<OnsiteVerificationResult> verificationCreate(
            @RequestParam(value = "resultData") String onsiteResultJson,
            @RequestParam(value = "projectResultData") String onsiteResultItemsJson ) {

        OnsiteVerificationResult onsiteResult= JSONUtils.fromJson(onsiteResultJson,OnsiteVerificationResult.class);
        List<Map<String, Object>> onsiteResultItems=JSONUtils.fromJson(onsiteResultItemsJson,List.class);
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);

            PersonnelThree personnelThree = null;
            if (list != null && !list.isEmpty()) {
                personnelThree = list.get(0);
            }else{
                ObjectRestResponse resp = new ObjectRestResponse<InspectionResult>().rel(false);
                resp.setStatus(500);
                resp.setMessage("无权限访问");
                return resp;
            }
            onsiteResult.setVerificateInstitution(personnelThree.getInstitutionId());
            onsiteResult.setVerificateUser(personnelThree.getPersonnelId());

            // 检查结果对象集合
            List<OnsiteVerificationResultItem> onsiteResultItemList = new ArrayList<>();
            OnsiteVerificationResultItem onsiteResultItem = null;
            Map<String, Object> onsiteResultItemMap = null;
            List<Object> resultsUnqualified = null;
            // 检查结果照片集合
            List<Long> resourceIds = new ArrayList<>();
            for (int i = 0; i < onsiteResultItems.size(); i++) {
                onsiteResultItemMap = onsiteResultItems.get(i);

                onsiteResultItem = new OnsiteVerificationResultItem();
                onsiteResultItem.setProjectId(Long.parseLong(
                        onsiteResultItemMap.get("projectId").toString()));
                onsiteResultItem.setInspectionContent(
                        onsiteResultItemMap.get("projectContent").toString());
                onsiteResultItem.setResultsQualified(
                        onsiteResultItemMap.get("resultsQualified").toString());
                if (onsiteResultItemMap.get("resultsUnqualified") != null) {
                    resultsUnqualified = Arrays.asList(
                            onsiteResultItemMap.get("resultsUnqualified").toString().split(","));
                    if (resultsUnqualified.size() > 0) {
                        onsiteResultItem.setResultsUnqualified(
                                StringUtils.join(resultsUnqualified.toArray(), ' '));
                    }
                }

                onsiteResultItemList.add(onsiteResultItem);

                resourceIds.add(Long.parseLong(onsiteResultItemMap.get("resourceId").toString()));
            }
            onsiteResultService.createByResourceId(onsiteResult, onsiteResultItemList, resourceIds);
            return new ObjectRestResponse<OnsiteVerificationResult>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<OnsiteVerificationResult>().rel(false);
            resp.setStatus(500);
            resp.setMessage("现场核查记录创建异常");
            return resp;
        }
    }


    /**
     * 日常检查结果提交
     * @param inspectionResultJson
     * @param inspectionResultItemsJson
     * @return
     */
    @RequestMapping(value = "/inspection/result/create", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<InspectionResult> inspectionCreate(
            @RequestParam(value = "resultData") String inspectionResultJson,
            @RequestParam(value = "projectResultData") String inspectionResultItemsJson) {

        InspectionResult inspectionResult= JSONUtils.fromJson(inspectionResultJson,InspectionResult.class);
        List<Map<String, Object>> inspectionResultItems=JSONUtils.fromJson(inspectionResultItemsJson,List.class);

        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
            PersonnelThree personnelThree=null;
            if (list != null && !list.isEmpty()) {
                 personnelThree = list.get(0);
            }else{
                ObjectRestResponse resp = new ObjectRestResponse<InspectionResult>().rel(false);
                resp.setStatus(500);
                resp.setMessage("无权限访问");
                return resp;
            }
            inspectionResult.setInspectedInstitution(personnelThree.getInstitutionId());
            inspectionResult.setInspectedUser(personnelThree.getPersonnelId());

            // 检查结果对象集合
            List<InspectionResultItem> inspectionResultItemList = new ArrayList<>();
            InspectionResultItem inspectionResultItem = null;
            Map<String, Object> inspectionResultItemMap = null;
            List<Object> resultsUnqualified = null;
            // 检查结果照片集合
            List<Long> resourceIds = new ArrayList<>();
            for (int i = 0; i < inspectionResultItems.size(); i++) {
                inspectionResultItemMap = inspectionResultItems.get(i);

                inspectionResultItem = new InspectionResultItem();
                inspectionResultItem.setProjectId(Integer.parseInt(
                        inspectionResultItemMap.get("projectId").toString()));
                inspectionResultItem.setInspectionContent(
                        inspectionResultItemMap.get("projectContent").toString());
                inspectionResultItem.setResultsQualified(
                        inspectionResultItemMap.get("resultsQualified").toString());
                if (inspectionResultItemMap.get("resultsUnqualified") != null) {
                    resultsUnqualified = Arrays.asList(
                            inspectionResultItemMap.get("resultsUnqualified").toString().split(","));
                    if (resultsUnqualified.size() > 0) {
                        inspectionResultItem.setResultsUnqualified(
                                StringUtils.join(resultsUnqualified.toArray(), ' '));
                    }
                }

                inspectionResultItemList.add(inspectionResultItem);

                resourceIds.add(Long.parseLong(inspectionResultItemMap.get("resourceId").toString()));
            }
            inspectionResultService.createByResourceId(inspectionResult, inspectionResultItemList, resourceIds);
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
