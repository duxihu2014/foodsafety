package com.otec.foodsafety.web.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.*;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.enterprise.EnterpriseSupervisionService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.util.CompareObjectUtil;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/enterpriseBase")
public class EnterpriseBaseController extends VueBaseController<EnterpriseBaseService, EnterpriseBase, Long> {
	@Autowired
	private EnterpriseBaseService enterpriseBaseService;
	@Autowired
	private EnterpriseVerifyService enterpriseVerifyService;
	@Autowired
	private SessionFilter sessionFilter;
	@Autowired
	private EnterpriseSupervisionService enterpriseSupervisionService;
	@Autowired
	PersonnelThreeService personnelThreeService;
	@Autowired
	private AuthService authService;

	/* 获取企业基本信息 */
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public ObjectRestResponse<EnterpriseBase> getBaseById(@RequestParam Long enterpriseId) {
		try {
			return new ObjectRestResponse<EnterpriseBase>().rel(true)
					.data(enterpriseBaseService.findById(enterpriseId));
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBase>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/* 企业基本信息修改 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseBaseChange> changeBase(
			@RequestParam(value = "enterpriseBase") String enterpriseBaseStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			EnterpriseBase enterpriseBase = JSONUtils.fromJson(enterpriseBaseStr, EnterpriseBase.class);
			enterpriseBaseService.modifyEnterpriseBase(enterpriseBase, sysUser.getUserId(), reason);
			return new ObjectRestResponse<EnterpriseBaseChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBase>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 根据条件获取基本信息改变数据
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getChange", method = RequestMethod.GET)
	public ObjectRestResponse<EnterpriseBaseChange> getChangeBase(Map map) {
		try {
			List<EnterpriseBaseChange> enterpriseBaseChanges = enterpriseBaseService.getBaseChange(map);
			return new ObjectRestResponse<EnterpriseBaseChange>().rel(true).data(enterpriseBaseChanges);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBaseChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 根据changeId获取企业证照修改数据表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getChange/{id}", method = RequestMethod.GET)
	public ObjectRestResponse<EnterpriseBaseChange> getChange(@PathVariable Long id) {
		try {
			Map<String, Object> result = new HashMap();
			List<Map<String, Object>> compareList = null;
			EnterpriseBaseChange enterpriseBaseChange = enterpriseBaseService.getChangeById(id);
			EnterpriseBase enterpriseBase = enterpriseBaseService.findById(enterpriseBaseChange.getEnterpriseId());
			compareList = CompareObjectUtil.compareTwoClass(enterpriseBase, enterpriseBaseChange);
			result.put("formData", enterpriseBaseChange);
			result.put("compareData", compareList);
			return new ObjectRestResponse<EnterpriseBaseChange>().rel(true).data(result);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseBaseChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 企业基本信息审批
	 * 
	 * @param changeId
	 * @param auditType
	 * @param verifyConclusion
	 * @param verifyId
	 * @return
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseVerify> auditEnterpriseScale(@RequestParam("changeId") String changeId,
			@RequestParam("auditType") String auditType, @RequestParam("verifyConclusion") String verifyConclusion,
			@RequestParam("verifyId") String verifyId) {
		try {
			EnterpriseBase enterpriseBase = new EnterpriseBase();
			EnterpriseBaseChange enterpriseBaseChange = enterpriseBaseService.getChangeById(Long.valueOf(changeId));
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
			enterpriseBaseService.verifyEnterpriseBase(enterpriseBase, enterpriseBaseChange, enterpriseVerify);
			return new ObjectRestResponse<EnterpriseVerify>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseVerify>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	@RequestMapping(value = "/getEnterpriseByGrid", method = RequestMethod.GET)
	public List<EnterpriseBase> getEnterpriseByGrid(@RequestParam Long gridId) {
		return enterpriseBaseService.getEnterpriseByGrid(gridId);
	}

	/**
	 * 企业基本信息审核查询
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/verify/query", method = RequestMethod.GET)
	public GridDataModel getBaseChangePage(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		Integer total = enterpriseBaseService.getBaseChangePageCount(params);
		List<Map<String, Object>> list = enterpriseBaseService.getBaseChangePage(params, po.getOffset(),
				po.getPageSize());
		GridDataModel model = new GridDataModel(list, total);
		return model;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = enterpriseBaseService.getPage(po);
		return model;
	}

    @RequestMapping(value = "/getOnsiteTask", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getOnsiteTask(@RequestParam Map<String, String> params) {
        // 查询列表数据
        SysUser sysUser = sessionFilter.getJWTUser(request);
        Map map = new HashMap<String, Object>();
        map.put("userId", Long.valueOf(sysUser.getUserId()));
        List<PersonnelThree> list = personnelThreeService.findEntitysByCondition(map);
        PersonnelThree personnelThree = list.get(0);
        if (list != null && !list.isEmpty()) {
            params.put("personnelId", String.valueOf(personnelThree.getPersonnelId()));
            PageObject po = getPageObject(params);
            po.getCondition().putAll(params);
            GridDataModel model = enterpriseBaseService.getPage(po);
            return model;
        }
        return null;
    }

	/**
	 * 设备企业监管级别
	 * 
	 * @param enterpriseId
	 * @return
	 */
	@RequestMapping(value = "/addRegulatoryLevel", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<EnterpriseSupervision> create(
			@RequestParam(value = "enterpriseId") String enterpriseId,
			@RequestParam(value = "regulatoryLevel") String regulatoryLevel,
			@RequestParam(value = "lnglat") String lnglat) {
		try {
			EnterpriseSupervision enterpriseSupervision = enterpriseSupervisionService.findById(Long.valueOf(enterpriseId));
			enterpriseSupervision.setRegulatoryLevel(regulatoryLevel);
			enterpriseSupervisionService.updateEnterpriseSupervision(enterpriseSupervision,lnglat);
			return new ObjectRestResponse<EnterpriseSupervision>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseSupervision>().rel(false);
			resp.setStatus(500);
			resp.setMessage("企业建档失败");
			return resp;
		}
	}

	/**企业档案查询*/
    @RequestMapping(value = "/getEnterpriseAllInfo", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getEnterpriseAllInfo(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = enterpriseBaseService.getEnterpriseAllInfo(po);
        return model;

    }

    /***/
	@RequestMapping(value = "/getEnterpriseDetail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EnterpriseBase> getEnterpriseDetail(@PathVariable("id") Long id) {
		EnterpriseBase eb=enterpriseBaseService.findById(id);
		return new ObjectRestResponse<EnterpriseBase>().rel(true).data(eb);
	}

	/**
	 * 根据网格查询网格下的所有企业信息(分页查询)
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getByGrid", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel getEnterpriseByGridPage(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = enterpriseBaseService.getEnterpriseByGridPage(po);
		return model;
	}

	@ResponseBody
	@RequestMapping(value = "/getSubjectClassificationByGrid", method = RequestMethod.GET)
	public ObjectRestResponse< List<Map<String,Object>>> getSubjectClassificationByGrid(@RequestParam(required = false) Long gridId) {
		List<Map<String,Object>>  list=enterpriseBaseService.getSubjectClassificationByGrid(gridId);
		return new ObjectRestResponse<EnterpriseBase>().rel(true).data(list);
	}
}
