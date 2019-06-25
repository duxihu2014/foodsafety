package com.otec.foodsafety.mapper.safety;

import com.otec.foodsafety.entity.safety.InspectionProject;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface InspectionProjectMapper extends GenericMapper<InspectionProject, Integer>{

    void batchChangeStatus(Map<String, Object> map);

    int getTemplateProjectCount(Map<String, String> map);

    List<InspectionProject> getTemplateProject(Map<String, String> map, RowBounds rowBounds);

    int getUnbandingProjectCount(Map<String, String> map);

    List<InspectionProject> getUnbandingProject(Map<String, String> map, RowBounds rowBounds);
}