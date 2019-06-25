package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionCycle;
import com.otec.foodsafety.mapper.safety.InspectionCycleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InspectionCycleServiceImpl extends BaseServiceImpl<
        InspectionCycle, Integer> implements InspectionCycleService {
    @Autowired
    InspectionCycleMapper inspectionCycleMapper;

    @Autowired
    public void setMapper(InspectionCycleMapper mapper) {
        setGenericMapper(mapper);
    }
}
