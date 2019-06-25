package com.otec.foodsafety.web.producesafety;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.producesafety.RecordMorningCheck;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.service.catering.CateringStaffService;
import com.otec.foodsafety.service.producesafety.RecordMorningCheckService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;

/**
 * @author minwei
 * @version 晨检记录 1.0
 * @date 2018年10月10日
 * */
@RestController
@RequestMapping("api/admin/producesafety/recordMorningCheck")
public class RecordMorningCheckController extends
		VueBaseController<RecordMorningCheckService, RecordMorningCheck, Long> {
	
	private static final Logger log=LoggerFactory.getLogger(RecordMorningCheckController.class);
	
	@Autowired
	private  RecordMorningCheckService recordMorningCheckService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	SysAreaService sysAreaService;
	
	@Autowired
	CateringStaffService cateringStaffService;
	
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
		GridDataModel model = recordMorningCheckService.findRecordMorningCheck(po);
		return model;
	}
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<RecordMorningCheck> get(@PathVariable("id") Long id) {
		RecordMorningCheck entity=recordMorningCheckService.findById(id);
		return new ObjectRestResponse<RecordMorningCheck>().rel(true).data(entity);
	}

	/**
	 * 新增
	 * */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<RecordMorningCheck> add(@RequestBody RecordMorningCheck entity) {
		entity.setCreateTime(new Date());
		recordMorningCheckService.persist(entity);
		return new ObjectRestResponse<RecordMorningCheck>().rel(true);
	}
	
	/**
	 * 修改
	 * */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<RecordMorningCheck> update(@RequestBody RecordMorningCheck entity) {
		entity.setUpdateTime(new Date());
		recordMorningCheckService.updateById(entity);
		return new ObjectRestResponse<RecordMorningCheck>().rel(true);
	}
	
	/**
	 * 获取从业人员
	 * @param id
	 * */
	@RequestMapping(value = "/getCateringStaffMap", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getCateringStaffMap(@RequestParam(value = "enterpriseId", required = false) String enterpriseId){
		Hashtable<Object, List<ComboData>> epDict =new Hashtable<Object, List<ComboData>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(enterpriseId)){
			map.put("enterpriseId", Long.valueOf(enterpriseId));
		}
		//map.put("staffStatus", "1");
		List<CateringStaff> ssList=cateringStaffService.findEntitysByCondition(map);
		List<ComboData> sslst = new ArrayList<ComboData>();
		for(CateringStaff v:ssList){
			ComboData data=new ComboData(String.valueOf(v.getStaffId()), v.getStaffName());
			sslst.add(data);
		}
		epDict.put("staff", sslst);
		return epDict;
	}
}
