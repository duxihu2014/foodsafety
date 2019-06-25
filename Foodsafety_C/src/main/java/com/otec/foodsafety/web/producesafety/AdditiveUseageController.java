package com.otec.foodsafety.web.producesafety;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseage;
import com.otec.foodsafety.service.producesafety.RecordAdditiveUseageService;
import com.otec.foodsafety.service.purchase.MaterialsEnterprisePurchaseService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;

/**
 * 添加剂
 * @author Administrator
 *
 */
@RestController
@RequestMapping("api/admin/additiveUseage")
public class AdditiveUseageController
		extends VueBaseController<RecordAdditiveUseageService, RecordAdditiveUseage, Long> {
	@Autowired
	MaterialsEnterprisePurchaseService purchaseService;
	@Autowired
	private AuthService authService;
	@Autowired
	SysAreaService sysAreaService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		if ("4".equals(userInfo.getUserType())) {// 企业用户
			if (null != userInfo.getEnterpriseId() && !"".equals(userInfo.getEnterpriseId())) {
				params.put("enterpriseId", userInfo.getEnterpriseId().toString());
			} else {
				return null;
			}

		} else if ("2".equals(userInfo.getUserType()) || "3".equals(userInfo.getUserType())) {
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
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataAdditiveUseageByCondition(po);
		return model;

	}

	/**
	 * 逻辑删除多条记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<RecordAdditiveUseage> remove(@PathVariable("ids") String ids) {
		try {
			String[] aDeleteId = ids.split(",");
			for (String id : aDeleteId) {
				biz.removeById(Long.valueOf(id));
			}

			return new ObjectRestResponse<RecordAdditiveUseage>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<RecordAdditiveUseage>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	/**
	 * 得到指定企业下的采购编号信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getPurchaseCombo", method = RequestMethod.GET)
	public ObjectRestResponse<List<ComboData>> getPurchaseCombo(@RequestParam Map<String, String> params) {
		String enterpriseId = params.get("enterpriseId");

		Map<String, Object> cond = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(enterpriseId)) {
			cond.put("enterpriseId", enterpriseId);
		}
		List<ComboData> comboList = purchaseService.findPurchaseComboByCondition(cond);

		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(comboList);
	}

	/**
	 * 实体预处理
	 * 
	 * @param entity
	 * @return
	 */
	public RecordAdditiveUseage preHandler(RecordAdditiveUseage entity) {
		if (entity.getCreateTime() == null) {
			entity.setCreateTime(new Date());
		} else {
			if (entity.getUpdateTime() == null) {
				entity.setUpdateTime(new Date());
			}
		}

		return entity;
	}
}
