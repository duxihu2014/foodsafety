package com.otec.foodsafety.web.producesafety;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import com.otec.foodsafety.util.SysInitConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseService;
import com.otec.foodsafety.service.producesafety.RecordWasteSituationService;
import com.otec.foodsafety.service.purchase.SupplierServiceService;
import com.otec.foodsafety.service.supervisory.ResponsibilityGridService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author minwei
 * @version  废弃物处理管理 1.0
 * @date 2018年10月10日
 * */
@RestController
@RequestMapping("api/admin/producesafety/recordWaste")
public class RecordWasteSituationController
		extends
		VueBaseController<RecordWasteSituationService, RecordWasteSituation, Long> {
	
	private static final Logger log = LoggerFactory
			.getLogger(RecordWasteSituationController.class);
	
	@Autowired
	private RecordWasteSituationService recordWasteSituationService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private  EnterpriseService enterpriseService;
	
	@Autowired
	private SupplierServiceService supplierServiceService;

	
    @Autowired
    SysAreaService sysAreaService;
    
    @Autowired
    ResponsibilityGridService responsibilityGridService;
    
    @Autowired
    SessionFilter sessionFilter;
    
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<RecordWasteSituation> get(@PathVariable("id") Long id) {
		RecordWasteSituation rs=recordWasteSituationService.findById(id);
		return new ObjectRestResponse<RecordWasteSituation>().rel(true).data(rs);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params ) {
		// 查询列表数据
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		if("4".equals(userInfo.getUserType())){//企业用户
			if(null!=userInfo.getEnterpriseId()&&!"".equals(userInfo.getEnterpriseId())){
				params.put("enterpriseId", userInfo.getEnterpriseId().toString());
			}else{
				return null;
			}
			
		}else if ("2".equals(userInfo.getUserType()) || "3".equals(userInfo.getUserType())){
			if(StringUtils.isNotEmpty(params.get("enterpriseId"))){
				params.put("enterpriseId", params.get("enterpriseId"));
			}else if(StringUtils.isNotEmpty(params.get("gridId"))){
				params.put("gridIds", params.get("gridId"));
			}else if(StringUtils.isNotEmpty(params.get("areaId"))){
				List list=new ArrayList<>();
				list=sysAreaService.listAllChildId(params.get("areaId"), list);
				params.put("areaIds",StringUtils.join(list.toArray(), ','));
			}else {
				List list=new ArrayList<>();
				list=sysAreaService.listAllChildId(String.valueOf(userInfo.getAreaId()), list);
				params.put("areaIds",StringUtils.join(list.toArray(), ','));
			}
		}
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = recordWasteSituationService.findRecordWasteSituation(po);
		return model;
	}
	
	@RequestMapping(value = "/getEnterpriseBaseMap", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getEnterpriseBaseMap(){
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		Map<String, Object> condititon = new HashMap<String, Object>();
		if("4".equals(userInfo.getUserType())){//企业用户
			if(null!=userInfo.getEnterpriseId()&&!"".equals(userInfo.getEnterpriseId())){
				condititon.put("enterpriseId", userInfo.getEnterpriseId().toString());
			}else{
				return null;
			}
			
		}else if("3".equals(userInfo.getUserType())){
			//condititon.put("areaId", "1074");
		}
		//企业
		List<EnterpriseBase> epist= enterpriseService.findEntitysByCondition(condititon);
		Hashtable<Object, List<ComboData>> epDict =new Hashtable<Object, List<ComboData>>();
		List<ComboData> lst = new ArrayList<ComboData>();
		for(EnterpriseBase v:epist){
			ComboData data=new ComboData(String.valueOf(v.getEnterpriseId()), v.getEnterpriseName());
			lst.add(data);
		}
		epDict.put("enterprise", lst);
		return epDict;
	}
	
	/**
	 * 获取相应类型的供应商key-value
	 * @param id(1供货商、2生产商、3餐饮用具消毒、4废弃物回收)
	 * */
	@RequestMapping(value = "/getSupplierMap/{id}", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getSupplierMap(@PathVariable("id") Long id){
		Hashtable<Object, List<ComboData>> epDict =new Hashtable<Object, List<ComboData>>();
		//供应商
		Map<String, String> map = new HashMap<String, String>();
		//map.put("supplierCategory", id);
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if("4".equals(sysUser.getUserType())){//企业用户，查询该企业关联的
        	 map.put("enterpriseId", String.valueOf(sysUser.getEnterpriseId()));
        }else if("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){//查全部
        	
        }
		map.put("extendSql", " and t3.ENTERPRISE_STATUS = 3 AND t1.SUPPLIER_CATEGORY = "+id);
		List<SupplierService> ssList=supplierServiceService.getSupplierServicePage(map,0,Integer.MAX_VALUE);
		List<ComboData> sslst = new ArrayList<ComboData>();
		for(SupplierService v:ssList){
			ComboData data=new ComboData(String.valueOf(v.getSupplierId()), v.getSupplierName());
			sslst.add(data);
		}
		epDict.put("supplier", sslst);
		return epDict;
	}
	
	/**
	 * 新增
	 * */
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = {"multipart/form-data"})
	@ResponseBody
	public ObjectRestResponse<RecordWasteSituation> add(@RequestPart RecordWasteSituation rs,
														@RequestPart(required = false) MultipartFile photo) {


		SysResource wastePhoto=null;

		try {
			if(photo!=null && !photo.isEmpty()){

				String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
				String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
				String response, fileName, fileRename, fileSubfix, filePath;

				fileName = photo.getOriginalFilename();
				fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀

 					response = HttpURLConnectionUtils.sendMessage(
							uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
							photo.getBytes());
					JSONObject jSONObject = JSON.parseObject(response);
					filePath = jSONObject.getString("imgUrl");
					fileRename = filePath.split("/")[filePath.split("/").length - 1];

					wastePhoto = new SysResource();
					wastePhoto.setResourceName(fileName);
					wastePhoto.setResourceRename(fileRename);
					wastePhoto.setResourceStorage(ResourceStorage.LOCAL.toString());
					wastePhoto.setResourceExtension(fileSubfix);
					wastePhoto.setResourceStatus("1");
					wastePhoto.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
					wastePhoto.setResourceContent(photo.getBytes());
					wastePhoto.setResourceLength(photo.getSize());
					wastePhoto.setResourcePath(filePath);
			}
			rs.setCreateTime(new Date());
		 	recordWasteSituationService.add(rs,wastePhoto);
			return new ObjectRestResponse<RecordWasteSituation>().rel(true);
		}catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
			resp.setStatus(500);
			resp.setMessage("回收记录创建异常");
			return resp;
		}


	}
	
	/**
	 * 修改
	 * */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = {"multipart/form-data"})
	@ResponseBody
	public ObjectRestResponse<RecordWasteSituation> update(@RequestPart RecordWasteSituation rs,
														   @RequestPart(required = false) MultipartFile photo) {

		SysResource wastePhoto=null;

		try {
			if(photo!=null && !photo.isEmpty()){

				String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
				String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
				String response, fileName, fileRename, fileSubfix, filePath;

				fileName = photo.getOriginalFilename();
				fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀

				response = HttpURLConnectionUtils.sendMessage(
						uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder,
						photo.getBytes());
				JSONObject jSONObject = JSON.parseObject(response);
				filePath = jSONObject.getString("imgUrl");
				fileRename = filePath.split("/")[filePath.split("/").length - 1];

				wastePhoto = new SysResource();
				wastePhoto.setResourceName(fileName);
				wastePhoto.setResourceRename(fileRename);
				wastePhoto.setResourceStorage(ResourceStorage.LOCAL.toString());
				wastePhoto.setResourceExtension(fileSubfix);
				wastePhoto.setResourceStatus("1");
				wastePhoto.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
				wastePhoto.setResourceContent(photo.getBytes());
				wastePhoto.setResourceLength(photo.getSize());
				wastePhoto.setResourcePath(filePath);
			}
			rs.setUpdateTime(new Date());
		 	recordWasteSituationService.update(rs,wastePhoto);
			return new ObjectRestResponse<RecordWasteSituation>().rel(true);
		}catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SupplierService>().rel(false);
			resp.setStatus(500);
			resp.setMessage("回收记录更新异常");
			return resp;
		}


	}	
}
