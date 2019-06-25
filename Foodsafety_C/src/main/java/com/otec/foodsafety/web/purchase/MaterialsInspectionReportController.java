package com.otec.foodsafety.web.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;
import com.otec.foodsafety.service.purchase.MaterialsInspectionReportService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;


/**
 * 质检报告
 * */
@RestController
@RequestMapping("api/admin/purchase/materialsInspectionReport")
public class MaterialsInspectionReportController extends
		VueBaseController<MaterialsInspectionReportService, MaterialsInspectionReport, Long> {
	
	@Autowired
	MaterialsInspectionReportService materialsInspectionReportService;
	@Autowired
	private AuthService authService;
	@Autowired
	SysAreaService sysAreaService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		if("4".equals(userInfo.getUserType())){//企业用户
			if(null!=userInfo.getEnterpriseId()&&!"".equals(userInfo.getEnterpriseId())){
				params.put("enterpriseId", userInfo.getEnterpriseId().toString());
			}else{
				return null;
			}
		}else if ("2".equals(userInfo.getUserType()) || "3".equals(userInfo.getUserType())) {
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
		GridDataModel model = materialsInspectionReportService.findInspectionReport(po);
		return model;
	}
	
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<MaterialsInspectionReport> get(@PathVariable("id") Long id) {
		MaterialsInspectionReport entity=materialsInspectionReportService.findById(id);
		return new ObjectRestResponse<MaterialsInspectionReport>().rel(true).data(entity);
	}

}
