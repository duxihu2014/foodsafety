package com.otec.foodsafety.web.enterprise;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseMaterialChangeService;
import com.otec.foodsafety.service.enterprise.EnterpriseMaterialService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.util.CompareObjectUtil;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

/**
 * 企业原材料及企业原材料变更
 */
@RestController
@RequestMapping("api/enterpriseMaterial")
public class EnterpriseMaterialController
		extends VueBaseController<EnterpriseMaterialService, EnterpriseMaterial, Long> {
	@Autowired
	EnterpriseMaterialService enterpriseMaterialService;

	@Autowired
	EnterpriseVerifyService enterpriseVerifyService;

	@Autowired
	SessionFilter sessionFilter;
	@Autowired
	EnterpriseMaterialChangeService emChangeService;

	/**
	 * 通过变更编号获取变更记录，对比历史记录返回同列内容不同的信息
	 * 
	 * @param id
	 * 
	 */
	@RequestMapping(value = "/getChange/{id}", method = RequestMethod.GET)
	private ObjectRestResponse<EnterpriseMaterialChange> getChange(@PathVariable Long id) {
		try {
			Map<String, Object> result = new HashMap();
			List<Map<String, Object>> compareList = null;
			EnterpriseMaterialChange emc = enterpriseMaterialService.findByChangeId(id);
			if ("2".equals(emc.getChangeType())) {// 修改操作
				EnterpriseMaterial enterpriseMaterial = enterpriseMaterialService
						.findById(Long.valueOf(emc.getMaterialId()));
				compareList = CompareObjectUtil.compareTwoClass(enterpriseMaterial, emc);
			}
			result.put("formData", emc);
			result.put("compareData", compareList);
			return new ObjectRestResponse<EnterpriseMaterialChange>().rel(true).data(result);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseMaterialChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 审批操作
	 * 
	 * @param changeId         变更记录Id
	 * @param auditType        0：审核不通过 1:审核通过
	 * @param verifyConclusion 审核批语
	 * @param verifyId         企业信息审批表主键
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseVerify> auditEnterpriseMaterial(@RequestParam("changeId") String changeId,
			@RequestParam("auditType") String auditType, @RequestParam("verifyConclusion") String verifyConclusion,
			@RequestParam("verifyId") String verifyId) {
		try {
			EnterpriseMaterial enterpriseMaterial = new EnterpriseMaterial();
			EnterpriseMaterialChange emc = enterpriseMaterialService.findByChangeId(Long.valueOf(changeId));
			if (emc.getMaterialId() != null) {
				enterpriseMaterial = enterpriseMaterialService.findById(Long.valueOf(emc.getMaterialId()));
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
			enterpriseMaterialService.verifyEnterpriseCertificate(enterpriseMaterial, emc, enterpriseVerify);
			return new ObjectRestResponse<EnterpriseMaterialChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseMaterialChange>().rel(false);
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
	@RequestMapping(value = "/getEnterpriseMaterial/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EnterpriseMaterialExt> getEnterpriseMaterial(@PathVariable Long id) {
		EnterpriseMaterialExt item = enterpriseMaterialService.getEnterpriseMaterialById(id);
		return new ObjectRestResponse<EnterpriseMaterialExt>().rel(true).data(item);
	}

	/**
	 * 添加企业原料信息
	 * @param enterpriseMaterialStr
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseMaterialChange> addEnterpriseMaterial(
			@RequestParam(value = "EnterpriseMaterial") String enterpriseMaterialStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);

			EnterpriseMaterial enterpriseMaterial = JSONUtils.fromJson(enterpriseMaterialStr, EnterpriseMaterial.class);

			enterpriseMaterialService.addEnterpriseMaterial(sysUser.getUserId(), reason, enterpriseMaterial);
			return new ObjectRestResponse<EnterpriseMaterialChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseMaterialChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 修改
	 * 
	 * @param enterpriseMaterialStr
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseMaterialChange> changeEnterpriseMaterial(
			@RequestParam(value = "EnterpriseMaterial") String enterpriseMaterialStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);

			EnterpriseMaterial enterpriseMaterial = JSONUtils.fromJson(enterpriseMaterialStr, EnterpriseMaterial.class);
			enterpriseMaterialService.modifyEnterpriseMaterial(sysUser.getUserId(), reason, enterpriseMaterial, "2");// 修改
			return new ObjectRestResponse<EnterpriseMaterialChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseMaterialChange>().rel(false);
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
			EnterpriseMaterial enterpriseMaterial = enterpriseMaterialService.findById(id);
			enterpriseMaterialService.modifyEnterpriseMaterial(sysUser.getUserId(), "将原材料永久停用", enterpriseMaterial,
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

	/**
	 * 企业原料信息审核查询
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/verify/query", method = RequestMethod.GET)
	public GridDataModel getCertificateChangePage(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = emChangeService.getGridDataModelByCondition(po);
		return model;
	}

}
