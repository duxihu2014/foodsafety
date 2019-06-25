package com.otec.foodsafety.web.safety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.safety.*;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.service.safety.InspectionResultService;
import com.otec.foodsafety.service.safety.RevisitPlanService;
import com.otec.foodsafety.service.safety.RevisitResultService;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.service.system.SysInstitutionService;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.web.multipart.MultipartFile;

/**回访结果*/
@RestController
@RequestMapping("api/admin/safety/revisitResult")
public class RevisitResultController extends VueBaseController<RevisitResultService, RevisitResult, Long> {
	
	@Autowired
	RevisitResultService revisitResultService;
	@Autowired
	RevisitPlanService revisitPlanService;
    @Autowired
    PersonnelThreeService personnelThreeService;
    @Autowired
    SysInstitutionService sysInstitutionService;
    @Autowired
    InspectionResultService inspectionResultService;
    @Autowired
    SessionFilter sessionFilter;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
		if("4".equals(sysUser.getUserType())) {//企业用户
			if(sysUser.getEnterpriseId() != null) {
				params.put("enterpriseId", sysUser.getEnterpriseId().toString());
			}
		}
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = revisitPlanService.findRevisitPlan(po);
		return model;
	}

	@RequestMapping(value = "/getRevisitPlanTask", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel getRevisitPlanTask(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        Map map = new HashMap<String, Object>();
        map.put("userId", Long.valueOf(sysUser.getUserId()));
        List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
        List<Map<String, Object>> planList = null;
        Integer total = 0;
        if (list != null && !list.isEmpty()) {
            PersonnelThree personnelThree = list.get(0);
            params.put("personnelId", String.valueOf(personnelThree.getPersonnelId()));
            PageObject po = getPageObject(params);
            total = revisitPlanService.getRevisitPlanTaskCount(params);
            if (total > 0)
                planList = revisitPlanService.getRevisitPlanTask(params, po.getOffset(), po.getPageSize());
        }
		GridDataModel model = new GridDataModel(planList, total);
		return model;
	}
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<RevisitResult> get(@PathVariable("id") Long id) {
		RevisitResult entity=revisitResultService.findById(id);
		return new ObjectRestResponse<RevisitResult>().rel(true).data(entity);
	}
	
	/**
	 * 获取监管人员
	 * @param
	 * */
	@RequestMapping(value = "/getPersonnelMap", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getPersonnelMap(){
		Hashtable<Object, List<ComboData>> epDict =new Hashtable<Object, List<ComboData>>();
        SysUser sysUser = sessionFilter.getJWTUser(request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaId", sysUser.getAreaId());
		List<PersonnelThree> ssList=personnelThreeService.findEntitysByCondition(map);
		List<ComboData> sslst = new ArrayList<ComboData>();
		for(PersonnelThree v:ssList){
			ComboData data=new ComboData(String.valueOf(v.getPersonnelId()), v.getPersonnelName());
			sslst.add(data);
		}
		epDict.put("personnel", sslst);
		return epDict;
	}
	
	/**
	 * 获取回访机构
	 * @param
	 * */
	@RequestMapping(value = "/getInstitutionMap", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getInstitutionMap(){
		Hashtable<Object, List<ComboData>> epDict =new Hashtable<Object, List<ComboData>>();
        SysUser sysUser = sessionFilter.getJWTUser(request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaId", sysUser.getAreaId());
		List<SysInstitution> ssList=sysInstitutionService.findEntitysByCondition(map);
		List<ComboData> sslst = new ArrayList<ComboData>();
		for(SysInstitution v:ssList){
			ComboData data=new ComboData(String.valueOf(v.getInstitutionId()), v.getInstitutionName());
			sslst.add(data);
		}
		epDict.put("institution", sslst);
		return epDict;
	}
	
	/**计划获取结果*/
	@RequestMapping(value = "getRevisitResultByPlan/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<RevisitResult> getRevisitResultByPlan(@PathVariable("id") Long id) {
		Map<String,Object> map=new HashMap<>();
		map.put("extendSql", "PLAN_ID ="+id);
		RevisitResult revisitResult = null;
		List<RevisitResult> list=revisitResultService.findEntitysByCondition(map);
		if(list.size()>0){
            revisitResult = list.get(0);
		}
		return new ObjectRestResponse<RevisitResult>().rel(true).data(revisitResult);
	}
	
	/**获取回访计划*/
	@RequestMapping(value = "getRevisitPlan/{planId}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<RevisitPlan> getRevisitPlan(@PathVariable("planId") Long planId) {
        return new ObjectRestResponse<Map<String, Object>>().rel(true)
                .data(revisitPlanService.getRevisitPlan(planId));
	}

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ObjectRestResponse<RevisitResult> create(
            @RequestPart(value = "resultData") RevisitResult revisitResult,
            @RequestPart(value = "projectResultData") List<Map<String, Object>> revisitResultItems,
            @RequestPart(value = "photoRectifiedData") MultipartFile[] revisitRectifiedPhotos) {
        String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
        String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
        String response, fileName, fileRename, fileSubfix, filePath;
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Map map = new HashMap<String, Object>();
            map.put("userId", Long.valueOf(sysUser.getUserId()));
            List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
            PersonnelThree personnelThree = list.get(0);
            revisitResult.setRevisitInstitution(personnelThree.getInstitutionId());
            revisitResult.setRevisitUser(personnelThree.getPersonnelId());

            // 检查结果对象集合
            List<InspectionResultItem> inspectionResultItemList = new ArrayList<>();
            InspectionResultItem inspectionResultItem = null;
            Map<String, Object> revisitResultItemMap = null;
            // 检查结果照片集合
            List<SysResource> sysResourceList = new ArrayList<>();
            SysResource sysResource = null;
            int index = 0;
            for (int i = 0; i < revisitResultItems.size(); i++) {
                revisitResultItemMap = revisitResultItems.get(i);

                inspectionResultItem = new InspectionResultItem();
                inspectionResultItem.setItemId(Long.parseLong(
                        revisitResultItemMap.get("itemId").toString()));
                inspectionResultItem.setProjectId(Integer.parseInt(
                        revisitResultItemMap.get("projectId").toString()));
                inspectionResultItem.setIsRectified(revisitResultItemMap.get("isRectified") != null ?
                        revisitResultItemMap.get("isRectified").toString() : null);
                if (((List) revisitResultItemMap.get("phoneRectified")).isEmpty() ||
                        ((List) revisitResultItemMap.get("phoneRectified")).get(0) instanceof Integer) {
                    sysResource = new SysResource();
                } else {
                    fileName = revisitRectifiedPhotos[index].getOriginalFilename();
                    fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                    response = HttpURLConnectionUtils.sendMessage(
                            uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
                            revisitRectifiedPhotos[index].getBytes());
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
                    sysResource.setResourceContent(revisitRectifiedPhotos[index].getBytes());
                    sysResource.setResourceLength(revisitRectifiedPhotos[index].getSize());
                    sysResource.setResourcePath(filePath);

                    index++;
                }
                inspectionResultItemList.add(inspectionResultItem);
                sysResourceList.add(sysResource);
            }
            revisitResultService.create(revisitResult, inspectionResultItemList, sysResourceList);
            return new ObjectRestResponse<RevisitResult>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RevisitResult>().rel(false);
            resp.setStatus(500);
            resp.setMessage("回访情况记录创建异常");
            return resp;
        }
    }
	
}
