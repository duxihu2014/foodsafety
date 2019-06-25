package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionPlan;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface InspectionPlanMapper extends GenericMapper<InspectionPlan, Long> {

    List<InspectionPlan> getInspectionPlanByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    List<Map<String, Object>> getEnterpriseInspectionPlanTask(Map<String, String> params, RowBounds rowBounds);

    int getEnterpriseInspectionPlanTaskCount(Map<String, String> params);

    Map<String, Object> getInspectionPlan(@Param("planId") Long planId);

    List<Map<String, Object>> getEnterpriseInspectionPlan(Map<String, String> params, RowBounds rowBounds);

    int getEnterpriseInspectionPlanCount(Map<String, String> params);
}
