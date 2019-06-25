package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionProjectCategory;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.Map;

public interface InspectionProjectCategoryMapper extends GenericMapper<InspectionProjectCategory, Integer>{

    void batchChangeStatus(Map<String, Object> map);
}