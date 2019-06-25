package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionTemplateProject;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.Map;

public interface InspectionTemplateProjectMapper extends GenericMapper<InspectionTemplateProject, Integer> {

    void unbanding(Map<String, Object> map);

    void setProjectScore(Map<String, Object> map);
}
