package com.otec.foodsafety.service.alarm;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.alarm.AlarmExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.mapper.alarm.AlarmMapper;
import com.otec.foodsafety.mapper.util.PushService;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AlarmServiceImpl extends BaseServiceImpl<Alarm, Long> implements AlarmService {
	@Autowired
	AlarmMapper mapper;
	@Autowired
	private PushService pushService;
	@Autowired
	private EnterpriseBaseService enterpriseBaseService;

	@Autowired
	public void setMapper(AlarmMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {

		String tabType = (String)po.getCondition().get("tabType");
		if (StringUtils.isNotBlank(tabType)) {
			if (StringUtils.equalsIgnoreCase("1", tabType)) {
				po.getCondition().put("eventType", "2");
				po.getCondition().put("sensorType", "1");
			} else if (StringUtils.equalsIgnoreCase("2", tabType)) {
				po.getCondition().put("eventType", "2");
				po.getCondition().put("sensorType", "2");
			} else if (StringUtils.equalsIgnoreCase("3", tabType)) {
				po.getCondition().put("eventType", "1");
				po.getCondition().put("eventId", "5");
			} else if (StringUtils.equalsIgnoreCase("4", tabType)) {
				po.getCondition().put("eventType", "1");
				po.getCondition().put("eventId", "6");
			} else if (StringUtils.equalsIgnoreCase("5", tabType)) {
				po.getCondition().put("eventType", "1");
				po.getCondition().put("extendSql", " and EVENT_ID in ('1', '2', '3', '4') ");
			}
		}

		// 得到所有有效的设备信息
		Integer totalCount = mapper.countAlarmByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<AlarmExt> results = mapper.findAlarmByCondition(po.getCondition(), rowBounds);

		//20190705解决web视频地址播放问题-修改
        for(AlarmExt alarmExt:results){
//            if(StringUtils.isNotBlank(alarmExt.getMsg())&&!alarmExt.getMsg().equals("null")){
//                alarmExt.setMsg(alarmExt.getMsg().replaceAll("\"event_video\":\"","\"event_video\":\""+"http://101.132.144.237:8080/event_video/"));
//            }
            if(StringUtils.isNotBlank(alarmExt.getVideoUrl())&&!alarmExt.getVideoUrl().equals("null")){
                alarmExt.setVideoUrl("/event_video/"+alarmExt.getVideoUrl());
            }
        }

//		for(AlarmExt alarmExt:results){
//			alarmExt.setMsg(alarmExt.getMsg().replaceAll("\"event_video\":\"","\"event_video\":\""+"http://101.132.144.237:8080/event_video/"));
//			alarmExt.setVideoUrl("/event_video/"+alarmExt.getVideoUrl());
//		}


		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public AlarmExt getAlarmById(Long productId) {
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("productId", productId);
		List<AlarmExt> item = mapper.findAlarmByCondition(cond);

		return item==null?null:item.get(0);
	}

	public void sendByAlarm(Alarm alarm){
		Map<String, Object> map = Maps.newHashMap();
		map.put("sensorNo", alarm.getIndexCode());
		List<AlarmExt> list = mapper.findAlarmByCondition(map);
		if(list != null && list.size() > 0){
			EnterpriseBase enterpriseBase = enterpriseBaseService.findById(list.get(0).getEnterpriseId());
			List<String> tags = Lists.newArrayList();
			if(enterpriseBase != null){
				tags.add("comp_"+enterpriseBase.getAreaId());
			}else {
				throw new RuntimeException("报警信息没有对应的公司，报警id="+list.get(0).getId());
			}
			if(tags.size() > 0){
				pushService.pushAlarm(alarm.getEventId(), alarm.getAlarmSource(), alarm.getAlarmType()+":"+alarm.getContent(), tags);
			}
		}
	}
	@Override
	public Integer countAlarmByCondition(Map<String, Object> condition) {
		return mapper.countAlarmByCondition(condition);

	}

	public List<Map<String, Object>> findAlarmReport(Map<String, Object> condition){
		return mapper.findAlarmReport(condition);
	}


	public List<Map<String, Object>> getCountByEventId(Map<String, Object> condition){
		return mapper.getCountByEventId(condition);
	}

	@Override
	public List<Map<String, Object>> getWarning(Long areaId) {
		return mapper.getWarning(areaId);
	}

	@Override
	public List<Map<String, Object>> getHealthWarning(Long enterpriseId) {
		return mapper.getHealthWarning(enterpriseId);
	}

	@Override
	public List<Map<String, Object>> getSensorTrend(Map<String, Object> params) {
		return mapper.getSensorTrend(params);
	}

	@Override
	public List<Map<String, Object>> getVideoTrend(Map<String, Object> params) {
		return mapper.getVideoTrend(params);

	}


	@Override
	public List<String> getEnterpriseName(String eventId) {
		return mapper.getEnterpriseNameByEventId(eventId);
	}


}
