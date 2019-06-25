package com.otec.foodsafety.web.producesafety;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordDisinfection;
import com.otec.foodsafety.service.producesafety.RecordDisinfectionService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;

/***
 * @author Administrator
 * @version 餐具消毒管理 1.0
 * @date 2018年10月11日
 * **/
@RestController
@RequestMapping("api/admin/producesafety/recordDisinfection")
public class RecordDisinfectionController extends
		VueBaseController<RecordDisinfectionService, RecordDisinfection, Long> {

	private static final Logger log= LoggerFactory.getLogger(RecordDisinfectionController.class);
	
	@Autowired
	private RecordDisinfectionService recordDisinfectionService;
	
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
		GridDataModel model = recordDisinfectionService.findRecordDisinfection(po);
		return model;
	}
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<RecordDisinfection> get(@PathVariable("id") Long id) {
		RecordDisinfection entity=recordDisinfectionService.findById(id);
		return new ObjectRestResponse<RecordDisinfection>().rel(true).data(entity);
	}
	
	
	/**
	 * 新增
	 * */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<RecordDisinfection> add(@RequestBody RecordDisinfection entity) {
		entity.setCreateTime(new Date());
		recordDisinfectionService.persist(entity);
		return new ObjectRestResponse<RecordDisinfection>().rel(true);
	}
	
	/**
	 * 修改
	 * */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<RecordDisinfection> update(@RequestBody RecordDisinfection entity) {
		entity.setUpdateTime(new Date());
		recordDisinfectionService.updateById(entity);
		return new ObjectRestResponse<RecordDisinfection>().rel(true);
	}
	
}
