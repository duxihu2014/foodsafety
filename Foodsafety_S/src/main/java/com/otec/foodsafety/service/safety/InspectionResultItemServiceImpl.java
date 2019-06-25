package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionResultItem;
import com.otec.foodsafety.mapper.safety.InspectionResultItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InspectionResultItemServiceImpl extends BaseServiceImpl<
        InspectionResultItem, Long> implements InspectionResultItemService {
    @Autowired
    InspectionResultItemMapper inspectionResultItemMapper;

    @Autowired
    public void setMapper(InspectionResultItemMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public InspectionResultItem addInspectionResultItem(InspectionResultItem inspectionResultItem) {
        inspectionResultItemMapper.persist(inspectionResultItem);
        return inspectionResultItem;
    }

    @Override
    public List<Map<String, Object>> getInspectionResultItem(Map<String, Object> params) {
        return inspectionResultItemMapper.getInspectionResultItem(params);
    }

    @Override
    public List<Map<String, Object>> statisticalInspectionResultItem(Map<String, Object> params) {
        return inspectionResultItemMapper.statisticalInspectionResultItem(params);
    }
}
