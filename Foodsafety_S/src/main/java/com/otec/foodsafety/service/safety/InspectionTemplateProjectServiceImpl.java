package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionTemplateProject;
import com.otec.foodsafety.mapper.safety.InspectionTemplateProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class InspectionTemplateProjectServiceImpl extends BaseServiceImpl<
        InspectionTemplateProject, Integer> implements InspectionTemplateProjectService {
    @Autowired
    InspectionTemplateProjectMapper inspectionTemplateProject;

    @Autowired
    public void setMapper(InspectionTemplateProjectMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void unbanding(Integer[] ids, Integer templateId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("templateId", templateId);
        inspectionTemplateProject.unbanding(map);
    }

    @Override
    public void setProjectScore(Integer id, Integer score) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("score", score);
        inspectionTemplateProject.setProjectScore(map);
    }
}
