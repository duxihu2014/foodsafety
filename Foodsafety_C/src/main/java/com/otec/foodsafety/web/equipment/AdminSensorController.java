package com.otec.foodsafety.web.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.sensor.SensorInfo;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.sensor.SensorMonitor;
import com.otec.foodsafety.service.sensor.SensorInfoService;
import com.otec.foodsafety.service.sensor.SensorMonitorService;
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
@RequestMapping("api/admin/sensor")
public class AdminSensorController extends VueBaseController<SensorInfoService, SensorInfo, Long> {
	@Autowired
	private SensorInfoService sensorInfoService;
	@Autowired
	private SensorMonitorService sensorMonitorService;
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
		GridDataModel model = sensorInfoService.getGridDataByCondition(po);
		List<SensorInfo> list = (List<SensorInfo> )model.getRows();

		return model;

	}

	@RequestMapping(value = "/report", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> report(@RequestParam Map<String, String> params) {
		Map<String, Object> result = Maps.newHashMap();
		List<SensorMonitor> list = sensorMonitorService.getReport(params.get("sensorNo"));

		Map<String, Object> map = Maps.newHashMap();
		map.put("sensorNo", params.get("sensorNo"));
		List<SensorInfo> sensorList = sensorInfoService.findEntitysByCondition(map);
		result.put("list", list);
		result.put("sensor", sensorList.get(0));
		return result;
	}

	@RequestMapping(value = "/alarmReport", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> alarmReport(@RequestParam Map<String, String> params) {
		return sensorMonitorService.getReportByAlarm(Long.valueOf(params.get("alarmId")));
	}

	@RequestMapping(value = "/getCurrentMonitor", method = RequestMethod.GET)
	@ResponseBody
	public SensorMonitor getCurrentMonitor(@RequestParam Map<String, String> params) {
		List<SensorMonitor> list = sensorMonitorService.getCurrentMonitor(params.get("sensorNo"));
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 公共方法，添加实体
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<SensorInfo> add(@RequestBody SensorInfo entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		entity.setCreateOpId(userInfo.getUserId());

//		biz.persist(preHandler(entity));
		sensorInfoService.persist(preHandler(entity));
		return new ObjectRestResponse<SensorInfo>().rel(true).data(entity);
	}

	/**
	 * 公共方法，修改实体
	 *
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<SensorInfo> update(@RequestBody SensorInfo entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		entity.setModifyOpId(userInfo.getUserId());

//		biz.updateById(preHandler(entity));
		sensorInfoService.mergeById(preHandler(entity));
		return new ObjectRestResponse<SensorInfo>().rel(true);
	}

	/**
	 * 设备编码不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkSensorInfoNo", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existSensorInfoNo(@RequestParam Map<String, String> params) {
		String sensorNo = params.get("sensorNo");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("sensorNo", sensorNo);
//		cond.put("status", "1");
		cond.put("extendSql"," and STATUS = 1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	/**
	 * 设备名称不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkSensorInfoName", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existSensorInfoName(@RequestParam Map<String, String> params) {
		String audioName = params.get("audioName");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("audioName", audioName);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<SensorInfo> remove(@PathVariable("ids") String ids) {
		try {
			String token = request.getHeader("access-token");
			FrontUser userInfo = authService.getUserInfo(token);

			String[] aDeleteId = ids.split(",");
			for (String id : aDeleteId) {
				SensorInfo entity = biz.findById(Long.valueOf(id));
				entity.setStatus("0");
				entity.setModifyOpId(userInfo.getUserId());
				biz.updateById(preHandler(entity));
			}
			return new ObjectRestResponse<SensorInfo>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SensorInfo>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	/**
	 * 实体预处理
	 * 
	 * @param entity
	 * @return
	 */
	public SensorInfo preHandler(SensorInfo entity) {

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
