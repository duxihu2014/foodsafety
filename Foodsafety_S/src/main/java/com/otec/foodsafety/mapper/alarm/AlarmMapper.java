package com.otec.foodsafety.mapper.alarm;

import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.alarm.AlarmExt;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;


public interface AlarmMapper extends GenericMapper<Alarm,Long>{
	/**
	 * 根据条件统计企业产品信息
	 *
	 * @param condition
	 * @return
	 */
	Integer countAlarmByCondition(Map<String, Object> condition);

	/**
	 * [分页]根据条件获得企业产品信息
	 *
	 * @param condition
	 * @return
	 */
	List<AlarmExt> findAlarmByCondition(Map<String, Object> condition, RowBounds rowBounds);

	/**
	 * 根据条件获得企业产品信息
	 *
	 * @param condition
	 * @return
	 */
	List<AlarmExt> findAlarmByCondition(Map<String, Object> condition);

	List<Map<String, Object>> findAlarmReport(Map<String, Object> condition);


	List<Map<String, Object>> getCountByEventId(Map<String, Object> condition);

    List<Map<String, Object>> getWarning(@Param("areaId") Long areaId);

	/**
	 * 根据条件查询温湿度报警数量一周内趋势
	 * @param params
	 * @return
	 */
    List<Map<String,Object>> getSensorTrend(Map<String, Object> params);

    List<Map<String,Object>> getVideoTrend(Map<String, Object> params);

	List<Map<String,Object>> getHealthWarning(@Param("enterpriseId")Long enterpriseId);
}
