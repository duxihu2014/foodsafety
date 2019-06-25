package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionTemplate;
import com.otec.foodsafety.mapper.safety.InspectionTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class InspectionTemplateServiceImpl extends BaseServiceImpl<
        InspectionTemplate, Integer> implements InspectionTemplateService {
    @Autowired
    InspectionTemplateMapper inspectionTemplateMapper;

    @Autowired
    public void setMapper(InspectionTemplateMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void batchChangeStatus(Integer[] ids, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("status", status);
        inspectionTemplateMapper.batchChangeStatus(map);
    }
}
