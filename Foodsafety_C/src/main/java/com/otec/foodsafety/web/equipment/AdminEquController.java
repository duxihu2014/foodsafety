package com.otec.foodsafety.web.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.equipment.EquipmentService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("api/admin/equipment")
public class AdminEquController extends VueBaseController<EquipmentService, Equipment, Long> {
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private AuthService authService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		po.getCondition().put("status", "1");
		if(StringUtils.equalsIgnoreCase(userInfo.getUserType(), "4")){
			po.getCondition().put("enterpriseId", userInfo.getEnterpriseId());
		}else if(!StringUtils.equalsIgnoreCase(userInfo.getUserType(), "1")){
			po.getCondition().put("areaId", userInfo.getAreaId());
		}
		GridDataModel model = equipmentService.getGridDataByCondition(po);
		List<Equipment> list = (List<Equipment> )model.getRows();

		return model;

	}

	/**
	 * 公共方法，添加实体
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<Equipment> add(@RequestBody Equipment entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		entity.setCreateOpId(userInfo.getUserId());

//		biz.persist(preHandler(entity));
		equipmentService.add(preHandler(entity));
		return new ObjectRestResponse<Equipment>().rel(true).data(entity);
	}

	/**
	 * 公共方法，修改实体
	 *
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<Equipment> update(@RequestBody Equipment entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		entity.setModifyOpId(userInfo.getUserId());

//		biz.updateById(preHandler(entity));
		equipmentService.update(preHandler(entity));
		return new ObjectRestResponse<Equipment>().rel(true);
	}

	/**
	 * 设备编码不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkEquipmentNo", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existEquipmentNo(@RequestParam Map<String, String> params) {
		String equipmentNo = params.get("equipmentNo");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("equipmentNo", equipmentNo);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	/**
	 * 设备名称不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkEquipmentName", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existEquipmentName(@RequestParam Map<String, String> params) {
		String audioName = params.get("audioName");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("audioName", audioName);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<Equipment> remove(@PathVariable("ids") String ids) {
		try {
			String token = request.getHeader("access-token");
			FrontUser userInfo = authService.getUserInfo(token);

			String[] aDeleteId = ids.split(",");
			for (String id : aDeleteId) {
				Equipment entity = biz.findById(Long.valueOf(id));
				entity.setStatus("0");
				entity.setModifyOpId(userInfo.getUserId());
				biz.updateById(preHandler(entity));
			}
			return new ObjectRestResponse<Equipment>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Equipment>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}
	@RequestMapping(value = "play/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<Map<String, Object>> play(@PathVariable Long id) {
		Map<String, Object> result = Maps.newHashMap();
		boolean canSee = false;
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		
		if(userInfo.getUserId() == 1){
			canSee = true;
		}else {
			PageObject pageObject = new PageObject();
			pageObject.setCurrPage(1);
			pageObject.setPageSize(2);
			pageObject.addCondition("equipmentId", id);			
			if(StringUtils.equalsIgnoreCase(userInfo.getUserType(), "4")){
				pageObject.getCondition().put("enterpriseId", userInfo.getEnterpriseId());
			}else if(!StringUtils.equalsIgnoreCase(userInfo.getUserType(), "1")){
				pageObject.getCondition().put("areaId", userInfo.getAreaId());
			}			
			
			pageObject.addCondition("status", 1);
			GridDataModel gm=equipmentService.getGridDataByCondition(pageObject);
			Integer count=gm.getTotal();
			if(count>0){
				canSee=true;
			}
		}

		result.put("flag", canSee);
		if(canSee){   
			String url = equipmentService.getPlayUrl(id);
			if(StringUtils.isNotBlank(url)){
				result.put("result", "success");
				result.put("url", url);
			}else {
				result.put("result", "error");
				result.put("url", "");
			}
		}
		return new ObjectRestResponse<Equipment>().rel(true).data(result);
	}
	/**
	 * 实体预处理
	 * 
	 * @param entity
	 * @return
	 */
	public Equipment preHandler(Equipment entity) {

		if (entity.getStatus() == null) {
			entity.setStatus("1");
		}
		if (entity.getCreateTime() == null) {
			entity.setCreateTime(new Date());
		} else {
			if (entity.getModifyTime() == null) {
				entity.setModifyTime(new Date());
			}
		}

		return entity;
	}
}
