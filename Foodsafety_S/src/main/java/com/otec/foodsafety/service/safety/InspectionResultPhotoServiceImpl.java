package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionResultPhoto;
import com.otec.foodsafety.mapper.safety.InspectionResultPhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InspectionResultPhotoServiceImpl extends BaseServiceImpl<
        InspectionResultPhoto, Long> implements InspectionResultPhotoService {
    @Autowired
    InspectionResultPhotoMapper inspectionResultPhotoMapper;

    @Autowired
    public void setMapper(InspectionResultPhotoMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public InspectionResultPhoto addPhoto(InspectionResultPhoto inspectionResultPhoto) {
        inspectionResultPhotoMapper.persist(inspectionResultPhoto);
        return inspectionResultPhoto;
    }
}
