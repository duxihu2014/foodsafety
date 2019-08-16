package com.otec.foodsafety.web.alarm;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *   报警管理
 * 
 * @author Administrator
 *
 */

@RestController
@RequestMapping("api/admin/alert")
public class AdminAlertController extends VueBaseController<AlarmService, Alarm, Long> {	
	Logger log = LoggerFactory.getLogger(AdminAlertController.class);
	@Autowired
	private AuthService authService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	AlarmService alarmService;
	@Autowired
	DateLocalService dateLocalService;
	
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
				params.put("gridId", params.get("gridId"));
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
		params.put("extendSql"," and STATUS <>0 ");
		
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);
		return model;
	}
	
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public Map count(@RequestParam Map<String,String > params) {
        Map<String, Object> result = Maps.newHashMap();
    	String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
	
        //报警提醒
        Map mapAlerm = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(params.get("alermStartDate"))) {
        	mapAlerm.put("alermStartDate", params.get("alermStartDate"));        	
        }       
        if(StringUtils.equalsIgnoreCase("4", userInfo.getUserType())){//企业
        	mapAlerm.put("enterpriseId", userInfo.getEnterpriseId()+"");
        }
        if(StringUtils.equalsIgnoreCase("3", userInfo.getUserType())){//监管
        	mapAlerm.put("areaId", userInfo.getAreaId()+"");
        }
        mapAlerm.put("eventType","1");
        Integer count_cemare = biz.countAlarmByCondition(mapAlerm);
        mapAlerm.put("eventType","2");
        Integer count_sensor = biz.countAlarmByCondition(mapAlerm);
        result.put("count_cemare", count_cemare);
        result.put("count_sensor", count_sensor);

        return result;
    }

	/**
	 * 处理报警
	 * @param id
	 * @param processingResult
	 * @return
	 */
	@RequestMapping(value = "/processing/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse processing(@PathVariable Long id, @RequestParam("processingResult") String processingResult) {
		Alarm alarm=alarmService.findById(id);
		alarm.setProcessingTime(dateLocalService.getDataBaseDate());
		alarm.setProcessingResult(processingResult);
		alarm.setStatus("2");
	 	alarmService.updateById(alarm);
		return new ObjectRestResponse<>().rel(true);
	}


	/**
	 * 公共方法，根据主键查询实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<Alarm> get(@PathVariable Long id) {

		Alarm alarm = alarmService.findById(id);
		if(!StringUtils.isEmpty(alarm.getMsg())){
			alarm.setMsg(alarm.getMsg().replaceAll("\"event_video\":\"","\"event_video\":\""+"http://101.132.144.237:8080/event_video/"));
		}
		if(!StringUtils.isEmpty(alarm.getVideoUrl())){
			alarm.setVideoUrl("http://101.132.144.237:8080/event_video/"+alarm.getVideoUrl());
		}
		return new ObjectRestResponse<Alarm>().rel(true)
				.data(alarm);
	}


	
}