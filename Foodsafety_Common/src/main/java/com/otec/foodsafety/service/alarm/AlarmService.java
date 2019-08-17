package com.otec.foodsafety.service.alarm;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.alarm.AlarmExt;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface AlarmService extends BaseService<Alarm,Long>{
	/**
	 * 得到页面列表分页数据
	 */
	GridDataModel getGridDataModelByCondition(PageObject po);

	/**
	 * 根据Id得到企业产品信息
	 * 
	 * @param productId
	 * @return
	 */
	AlarmExt getAlarmById(Long productId);

	void sendByAlarm(Alarm alarm);
	Integer countAlarmByCondition(Map<String, Object> condition);

	List<Map<String, Object>> findAlarmReport(Map<String, Object> condition);

	List<Map<String, Object>> getCountByEventId(Map<String, Object> condition);

    List<Map<String, Object>> getWarning(Long areaId);

    List<Map<String, Object>> getSensorTrend(Map<String, Object> params);

    List<Map<String,Object>> getVideoTrend(Map<String, Object> params);

    List<Map<String,Object>> getHealthWarning(Long enterpriseId);

	List<String> getEnterpriseName(String eventId);
}
