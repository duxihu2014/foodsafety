package com.otec.foodsafety.web.purchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.equipment.Audio;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.purchase.MaterialsEnterprisePurchase;
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;
import com.otec.foodsafety.entity.purchase.Materials;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.producesafety.RecordAdditiveUseageService;
import com.otec.foodsafety.service.purchase.MaterialsEnterprisePurchaseService;
import com.otec.foodsafety.service.purchase.MaterialsService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

/**
 * 采购
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("api/admin/purchase/prepackaging")
public class MaterialsEnterprisePurchaseController
		extends VueBaseController<MaterialsEnterprisePurchaseService, MaterialsEnterprisePurchase, Long> {
	@Autowired
	MaterialsService materialsService;
	@Autowired
	SessionFilter sessionFilter;
	@Autowired
	MaterialsEnterprisePurchaseService mePurchaseService;
	@Autowired
	RecordAdditiveUseageService rAdditiveUserService;
	@Autowired
	private AuthService authService;
	@Autowired
	SysAreaService sysAreaService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);

		if ("2".equals(userInfo.getUserType()) || "3".equals(userInfo.getUserType())) {
			if (StringUtils.isNotEmpty(params.get("enterpriseId"))) {
				params.put("enterpriseId", params.get("enterpriseId"));
			} else if (StringUtils.isNotEmpty(params.get("gridId"))) {
				params.put("gridIds", params.get("gridId"));
			} else if (StringUtils.isNotEmpty(params.get("areaId"))) {
				List list = new ArrayList<>();
				list = sysAreaService.listAllChildId(params.get("areaId"), list);
				params.put("areaIds", StringUtils.join(list.toArray(), ','));
			} else {
				List list = new ArrayList<>();
				list = sysAreaService.listAllChildId(String.valueOf(userInfo.getAreaId()), list);
				params.put("areaIds", StringUtils.join(list.toArray(), ','));
			}
		}

		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);
		return model;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public ObjectRestResponse<MaterialsEnterprisePurchase> create(
			@RequestPart(value = "formData") MaterialsEnterprisePurchase entity,
			@RequestPart(value = "inspectionReportPhotoFile", required = false) MultipartFile inspectionReportPhotoFile) {

		String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
		String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
		SysResource inspectionReport = null;

		String response, fileName, fileRename, fileSubfix, filePath;
		try {
			// 上传营业执照图片
			if (inspectionReportPhotoFile != null && !inspectionReportPhotoFile.isEmpty()) {
				fileName = inspectionReportPhotoFile.getOriginalFilename();
				fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());// 文件后缀
				response = HttpURLConnectionUtils.sendMessage(
						uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
						inspectionReportPhotoFile.getBytes());
				JSONObject jSONObject = JSON.parseObject(response);
				filePath = jSONObject.getString("imgUrl");
				fileRename = filePath.split("/")[filePath.split("/").length - 1];

				inspectionReport = new SysResource();
				inspectionReport.setResourceName(fileName);
				inspectionReport.setResourceRename(fileRename);
				inspectionReport.setResourceStorage(ResourceStorage.LOCAL.toString());
				inspectionReport.setResourceExtension(fileSubfix);
				inspectionReport.setResourceStatus("1");
				inspectionReport.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
				inspectionReport.setResourceContent(inspectionReportPhotoFile.getBytes());
				inspectionReport.setResourceLength(inspectionReportPhotoFile.getSize());
				inspectionReport.setResourcePath(filePath);
			}

			mePurchaseService.add(preHandler(entity), inspectionReport);
			return new ObjectRestResponse<SupplierService>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(false);
			resp.setStatus(500);
			resp.setMessage("采购记录创建异常");
			return resp;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public ObjectRestResponse<MaterialsEnterprisePurchase> update(
			@RequestPart(value = "formData") MaterialsEnterprisePurchase entity,
			@RequestParam(value = "inspectionReportPhoto") String inspectionReportPhotoFlag,
			@RequestPart(value = "inspectionReportPhotoFile", required = false) MultipartFile inspectionReportPhotoFile) {
		String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
		String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
		SysResource inspectionReport = null;
		String response, fileName, fileRename, fileSubfix, filePath;
		try {

			// 更新营业执照图片
			if ("1".equals(inspectionReportPhotoFlag)) {
				fileName = inspectionReportPhotoFile.getOriginalFilename();
				fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());// 文件后缀
				response = HttpURLConnectionUtils.sendMessage(
						uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
						inspectionReportPhotoFile.getBytes());
				JSONObject jSONObject = JSON.parseObject(response);
				filePath = jSONObject.getString("imgUrl");
				fileRename = filePath.split("/")[filePath.split("/").length - 1];

				inspectionReport = new SysResource();
				inspectionReport.setResourceName(fileName);
				inspectionReport.setResourceRename(fileRename);
				inspectionReport.setResourceStorage(ResourceStorage.LOCAL.toString());
				inspectionReport.setResourceExtension(fileSubfix);
				inspectionReport.setResourceStatus("1");
				inspectionReport.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
				inspectionReport.setResourceContent(inspectionReportPhotoFile.getBytes());
				inspectionReport.setResourceLength(inspectionReportPhotoFile.getSize());
				inspectionReport.setResourcePath(filePath);
			} else if ("-1".equals(inspectionReportPhotoFlag)) {
				// 删除营业执照图片
				inspectionReport = new SysResource();
			} else {
				// 不更新营业执照图片
				inspectionReport = null;
			}
			mePurchaseService.update(entity, inspectionReport);
			return new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<MaterialsEnterprisePurchase>().rel(false);
			resp.setStatus(500);
			resp.setMessage("供货商创建异常");
			return resp;
		}
	}

	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<MaterialsEnterprisePurchase> remove(@PathVariable("ids") String ids) {
		try {
			Map<String, Object> cond = new HashMap<String, Object>();
			cond.put("extendSql", " PURCHASE_ID  in (" + ids + ")");

			Integer num = rAdditiveUserService.countByCondition(cond);
			if (num > 0) {
				ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);
				resp.setStatus(500);
				resp.setMessage("采购信息下有添加剂使用信息,无法删除");
				return resp;
			} else {
				String[] aDeleteId = ids.split(",");
				for (String id : aDeleteId) {
					mePurchaseService.deleteById(Long.valueOf(id));
				}
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
	 * 根据企业id+原料类型得到企业下指定原料类型的原料信息
	 * 
	 * @param enterpriseId
	 * @param materialCategory
	 * @return
	 */
	@RequestMapping(value = "/getMaterialsMap/{enterpriseId}/{materialCategory}", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String, Object>> getMaterialsMap(@PathVariable Long enterpriseId,
			@PathVariable String materialCategory) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("materialStatus", "1");
		condition.put("enterpriseId", enterpriseId);
		condition.put("materialCategory", materialCategory);

		List<Materials> datalist = materialsService.findMaterialsByCondition(condition);
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(datalist);
	}


	@RequestMapping(value = "/getMaterialsMapByConditon", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String, Object>> getMaterialsMap(@RequestParam Map<String, String> params) {
		String enterpriseId = params.get("enterpriseId");
		String materialCategory = params.get("materialCategory");

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("materialStatus", "1");
		if (StringUtils.isNotBlank(enterpriseId)) {
			condition.put("enterpriseId", enterpriseId);
		}
		if (StringUtils.isNotBlank(materialCategory)) {
			condition.put("materialCategory", materialCategory);
		}

		List<Materials> datalist = materialsService.findMaterialsByCondition(condition);
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(datalist);
	}

	/**
	 * 根据原料id得到原料的信息
	 * 
	 * @param id
	 * @param materialCategory 原料类型
	 * @return
	 */
	@RequestMapping(value = "getMaterialObj/{materialId}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<Materials> getMaterialObj(@PathVariable Long materialId) {

		return new ObjectRestResponse<Materials>().rel(true).data(materialsService.findByMaterialId(materialId));
	}

}
