package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionResultItem;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.List;
import java.util.Map;

public interface InspectionResultItemMapper extends GenericMapper<InspectionResultItem, Long> {

    List<Map<String, Object>> getInspectionResultItem(Map<String, Object> params);

    List<Map<String, Object>> statisticalInspectionResultItem(Map<String, Object> params);
}
