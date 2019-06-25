package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionTemplate;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.Map;

public interface InspectionTemplateMapper extends GenericMapper<InspectionTemplate, Integer> {

    void batchChangeStatus(Map<String, Object> map);
}
