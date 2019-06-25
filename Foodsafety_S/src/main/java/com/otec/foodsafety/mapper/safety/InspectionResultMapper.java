package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionResult;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.List;
import java.util.Map;

public interface InspectionResultMapper extends GenericMapper<InspectionResult, Long> {

    List<Map<String, Object>> getInspectionResult(Map<String, String> params);
}
