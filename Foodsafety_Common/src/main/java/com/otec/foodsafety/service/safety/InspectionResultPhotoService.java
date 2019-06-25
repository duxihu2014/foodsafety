package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionResultPhoto;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface InspectionResultPhotoService extends BaseService<InspectionResultPhoto, Long> {

    InspectionResultPhoto addPhoto(InspectionResultPhoto inspectionResultPhoto);
}
