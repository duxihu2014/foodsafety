package com.otec.foodsafety.service.sensor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.sensor.SensorInfo;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.util.SortUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.sensor.SensorMonitor;
import com.otec.foodsafety.mapper.sensor.SensorMonitorMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SensorMonitorServiceImpl  extends BaseServiceImpl<SensorMonitor,Long> implements SensorMonitorService{
	@Autowired
	private SensorMonitorMapper mapper;
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private SensorInfoService sensorInfoService;

	@Autowired
	public  void  setMapper(SensorMonitorMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public List<SensorMonitor> getReport(String sensorNo){
		Map<String, Object> map = Maps.newHashMap();
		map.put("sensorNo", sensorNo);
		map.put("date", new Date());

		map.put("startTime", DateTime.parse(DateTime.now().toString("yyyy-MM-dd")).toDate());
		map.put("endTime", DateTime.parse(DateTime.now().plusDays(1).toString("yyyy-MM-dd")).toDate());
		return mapper.getReport(map);
	}

	@Override
	public List<SensorMonitor> getCurrentMonitor(String sensorNo){
		Map<String, Object> map = Maps.newHashMap();
		map.put("sensorNo", sensorNo);
		return mapper.getCurrentMonitor(map);
	}

	@Override
	public Map<String, Object> getReportByAlarm(Long alarmId) {
		Map<String, Object> result = Maps.newHashMap();
		Alarm alarm = alarmService.findById(alarmId);
		if(alarm == null){
			result.put("list", Lists.newArrayList());
			return result;
		}
		Map<String, Object> sensorMap = Maps.newHashMap();
		sensorMap.put("sensorNo", alarm.getIndexCode());
		List<SensorInfo> sensorList = sensorInfoService.findEntitysByCondition(sensorMap);
		result.put("sensor", sensorList.get(0));

		Map<String, Object> map = Maps.newHashMap();
		map.put("sensorNo", alarm.getIndexCode());
		map.put("startTime", alarm.getCreateTime());
		DateTime dateTime = new DateTime(alarm.getCreateTime());
		DateTime dayBefore7 = DateTime.now().minusDays(7);
		if(dateTime.getMillis() < dayBefore7.getMillis()){
			map.put("isHistory", "1");
		}
		map.put("endTime", alarm.getUpdateTime()==null?new Date():alarm.getUpdateTime());
		List<SensorMonitor> list = mapper.getReport(map);

		result.put("list", list);
		String type = null;
		if(StringUtils.equalsIgnoreCase("10001", alarm.getEventId()) || StringUtils.equalsIgnoreCase("10002", alarm.getEventId()) ){
			type = "wd";
		}else {
			type = "sd";
		}
		result.put("type", type);
		return result;
	}

	@Override
	public List<SensorMonitor> getSupplySensorMonitor(Map<String, Object> map) {
		return mapper.getSupplySensorMonitor(map);
	}

	/**
	 * app专用，对数据进行聚合，方便展示
	 * @param alarmId
	 * @return
	 */
	@Override
	public Map<String, Object> getAppReportByAlarm(Long alarmId) {
		Map<String, Object> result = Maps.newHashMap();
		Alarm alarm = alarmService.findById(alarmId);
		if(alarm == null){
			result.put("list", Lists.newArrayList());
			return result;
		}
		Map<String, Object> sensorMap = Maps.newHashMap();
		sensorMap.put("sensorNo", alarm.getIndexCode());
		List<SensorInfo> sensorList = sensorInfoService.findEntitysByCondition(sensorMap);
		result.put("sensor", sensorList.get(0));

		Map<String, Object> map = Maps.newHashMap();
		map.put("sensorNo", alarm.getIndexCode());
		map.put("startTime", alarm.getCreateTime());
		DateTime dateTime = new DateTime(alarm.getCreateTime());
		DateTime dayBefore7 = DateTime.now().minusDays(7);
		if(dateTime.getMillis() < dayBefore7.getMillis()){
			map.put("isHistory", "1");
		}

		map.put("endTime", alarm.getUpdateTime()==null?new Date():alarm.getUpdateTime());

		List<SensorMonitor> list = mapper.getReport(map);
		List<SensorMonitor> limitData = Lists.newArrayList();
		if(list.size() > 10){
			int mod = new BigDecimal(list.size()).divide(new BigDecimal(10), 0, BigDecimal.ROUND_HALF_UP).intValue();
			Set<Integer> indexSet = Sets.newHashSet();
			for (int i = 0; i < list.size(); i++){
				if(i % mod == 0){
					limitData.add(list.get(i));
					indexSet.add(i);
				}
			}
			if(limitData.size() < 10){
				for (int i = list.size()-1; i >= 0; i--){
					if(!indexSet.contains(i)){
						limitData.add(list.get(i));
						if(limitData.size() == 10){
							break;
						}
					}
				}
			}
			SortUtils<SensorMonitor> sortInstance = SortUtils.newBuilder(SensorMonitor.class);
			sortInstance.addAsc("createTime").zerosFirst().sortList(limitData);
			result.put("list", limitData);
		}else {
			result.put("list", list);
		}




		String type = null;
		if(StringUtils.equalsIgnoreCase("10001", alarm.getEventId()) || StringUtils.equalsIgnoreCase("10002", alarm.getEventId()) ){
			type = "wd";
		}else {
			type = "sd";
		}
		result.put("type", type);
		return result;
	}
}
