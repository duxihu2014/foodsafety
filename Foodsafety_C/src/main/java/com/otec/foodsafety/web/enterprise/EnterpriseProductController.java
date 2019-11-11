package com.otec.foodsafety.web.enterprise;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductType;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductTypeTree;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseProductChangeService;
import com.otec.foodsafety.service.enterprise.EnterpriseProductService;
import com.otec.foodsafety.service.enterprise.EnterpriseProductTypeService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.CompareObjectUtil;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/enterpriseProduct")
public class EnterpriseProductController extends VueBaseController<EnterpriseProductService, EnterpriseProduct, Long> {

	@Autowired
	EnterpriseProductService enterpriseProductService;

	@Autowired
	private EnterpriseVerifyService enterpriseVerifyService;
	@Autowired
	private SessionFilter sessionFilter;
	@Autowired
	private AuthService authService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	EnterpriseProductTypeService enterpriseProductTypeService;
	@Autowired
	private EnterpriseProductTypeService productTypeService;
	@Autowired
	EnterpriseProductChangeService epchangeService;


	@RequestMapping(value = "/getChange/{id}", method = RequestMethod.GET)
	private ObjectRestResponse<EnterpriseProductChange> getChange(@PathVariable Long id) {
		try {
			Map<String, Object> result = new HashMap();
			List<Map<String, Object>> compareList = null;
			EnterpriseProductChange epc = enterpriseProductService.findByChangeId(id);
			// 判断是新增还是修改操作
			if ("2".equals(epc.getChangeType())) {// 修改
				EnterpriseProduct enterpriseProduct = enterpriseProductService
						.findById(Long.valueOf(epc.getProductId()));
				compareList = CompareObjectUtil.compareTwoClass(enterpriseProduct, epc);
			}
			result.put("formData", epc);
			result.put("compareData", compareList);
			return new ObjectRestResponse<EnterpriseProductChange>().rel(true).data(result);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseProductChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 产品变更审核
	 * 
	 * @param changeId
	 * @param auditType
	 * @param verifyConclusion
	 * @param verifyId
	 * @return
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseVerify> auditEnterpriseProduct(@RequestParam("changeId") String changeId,
			@RequestParam("auditType") String auditType, @RequestParam("verifyConclusion") String verifyConclusion,
			@RequestParam("verifyId") String verifyId) {
		try {
			EnterpriseProduct enterpriseProduct = new EnterpriseProduct();
			EnterpriseProductChange epc = enterpriseProductService.findByChangeId(Long.valueOf(changeId));
			if (epc.getProductId() != null) {
				enterpriseProduct = enterpriseProductService.findById(Long.valueOf(epc.getProductId()));
			}
			EnterpriseVerify enterpriseVerify = enterpriseVerifyService.findById(Long.valueOf(verifyId));
			if (auditType.equals("0")) {// 审核不通过
				enterpriseVerify.setVerifyStatus("3");
			} else {// 审核通过
				enterpriseVerify.setVerifyStatus("2");
			}
			SysUser sysUser = sessionFilter.getJWTUser(request);
			enterpriseVerify.setVerifyUserId(sysUser.getUserId());
			enterpriseVerify.setVerifyTime(new Date());
			enterpriseVerify.setVerifyConclusion(verifyConclusion);
			enterpriseProductService.verifyEnterpriseCertificate(enterpriseProduct, epc, enterpriseVerify);
			return new ObjectRestResponse<EnterpriseCertificateChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseCertificateChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}

	}

	/**
	 * 公共方法，根据主键查询实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getEnterpriseProduct/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EnterpriseProductExt> getEnterpriseProduct(@PathVariable Long id) {
		EnterpriseProductExt item = enterpriseProductService.getEnterpriseProductById(id);
		return new ObjectRestResponse<EnterpriseProductExt>().rel(true).data(item);
	}

	/**
	 * 添加企业产品信息
	 * 
	 * @param multipartFile
	 * @param enterpriseProductStr
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseProductChange> addEnterpriseProduct(
			@RequestPart(value = "file", required = false) MultipartFile multipartFile,
			@RequestParam(value = "EnterpriseProduct") String enterpriseProductStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
			String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
			EnterpriseProduct enterpriseProduct = JSONUtils.fromJson(enterpriseProductStr, EnterpriseProduct.class);
			SysResource resource = new SysResource();
			resource.setResourceName(multipartFile.getOriginalFilename());
			resource.setResourceContent(multipartFile.getBytes());
			resource.setResourceLength(multipartFile.getSize());
			enterpriseProductService.addEnterpriseProduct(uploadUrl, imageFolder, resource, sysUser.getUserId(),
					reason, enterpriseProduct);
			return new ObjectRestResponse<EnterpriseProductChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseProductChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 修改企业证照信息
	 * 
	 * @param multipartFile
	 * @param enterpriseProductStr
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseProductChange> changeEnterpriseProduct(
			@RequestPart(value = "file", required = false) MultipartFile multipartFile,
			@RequestParam(value = "EnterpriseProduct") String enterpriseProductStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
			String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
			EnterpriseProduct enterpriseProduct = JSONUtils.fromJson(enterpriseProductStr, EnterpriseProduct.class);
			SysResource resource = null;
			if(multipartFile!=null){
				resource = new SysResource();
				resource.setResourceName(multipartFile.getOriginalFilename());
				resource.setResourceContent(multipartFile.getBytes());
				resource.setResourceLength(multipartFile.getSize());
			}
			enterpriseProductService.modifyEnterpriseProduct(uploadUrl, imageFolder, resource, sysUser.getUserId(),
					reason, enterpriseProduct, "2");// 修改
			return new ObjectRestResponse<EnterpriseProductChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseProductChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<EnterpriseProduct> delete(@PathVariable Long id) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			EnterpriseProduct eProduct = enterpriseProductService.findById(id);
			enterpriseProductService.modifyEnterpriseProduct("", "", null, sysUser.getUserId(), "将产品永久停产", eProduct,
					"3");// 删除

			return new ObjectRestResponse<EnterpriseProduct>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseProduct>().rel(false);
			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	@RequestMapping(value = "/getAllProductTypeMap", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String, Object>> getAllProductTypeMap(@RequestParam Map<String, Object> params) {

		List<EnterpriseProductType> datalist = productTypeService.findEntitysByCondition(params);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(datalist.size());
		for (EnterpriseProductType item : datalist) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", String.valueOf(item.getProductTypeId()));
			map.put("text", item.getProductTypeName());
			list.add(map);
		}
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(list);
	}

	@RequestMapping(value = "/productTypetree", method = RequestMethod.GET)
	public List<EnterpriseProductTypeTree> getTree(@RequestParam(required = false) String menuName) {
		return productTypeService.listTree(true, false);
	}

	/**
	 * 企业产品信息审核查询
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/verify/query", method = RequestMethod.GET)
	public GridDataModel getProductChangePage(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = epchangeService.getGridDataModelByCondition(po);
		return model;
	}

	/**
	 * 
	
	 * */
	@RequestMapping(value = "/getProductTypeMap", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getProductTypeMap() {
		Hashtable<Object, List<ComboData>> epDict = new Hashtable<Object, List<ComboData>>();
		Map<String, Object> map = new HashMap<String, Object>();

		List<EnterpriseProductType> ssList = enterpriseProductTypeService.findEntitysByCondition(map);
		List<ComboData> sslst = new ArrayList<ComboData>();
		for (EnterpriseProductType v : ssList) {
			ComboData data = new ComboData(String.valueOf(v.getProductTypeId()), v.getProductTypeName());
			sslst.add(data);
		}
		epDict.put("productType", sslst);
		return epDict;
	}

		@RequestMapping(value = "/getProductDetail/{id}", method = RequestMethod.GET)
	   	@ResponseBody
	   	public ObjectRestResponse<List<EnterpriseProduct>> getProductDetail(@PathVariable("id") Long id) {
	   		Map<String,Object> map=new HashMap<>();
	   		map.put("enterpriseId", id);
	   		List<EnterpriseProduct> list=enterpriseProductService.findEntitysByCondition(map);
	   		return new ObjectRestResponse<List<EnterpriseProduct>>().rel(true).data(list);
	   	}
	
}
