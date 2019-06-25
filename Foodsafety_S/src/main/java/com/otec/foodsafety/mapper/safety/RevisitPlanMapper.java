package com.otec.foodsafety.mapper.safety;

import java.util.List;
import java.util.Map;

import com.otec.foodsafety.entity.safety.RevisitPlan;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface RevisitPlanMapper extends GenericMapper<RevisitPlan, Long> {
	
	Integer countRevisitPlan(Map<String, Object> condition);

	List<RevisitPlan> findRevisitPlan(Map<String, Object> condition, RowBounds rowBounds);

	List<Map<String, Object>> getRevisitPlanTask(Map<String, String> params, RowBounds rowBounds);

	int getRevisitPlanTaskCount(Map<String, String> params);

    Map<String, Object> getRevisitPlan(@Param("planId") Long planId);

}
