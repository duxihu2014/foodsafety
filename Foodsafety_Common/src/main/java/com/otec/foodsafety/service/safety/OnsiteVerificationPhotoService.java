package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.OnsiteVerificationPhoto;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface OnsiteVerificationPhotoService extends BaseService<OnsiteVerificationPhoto,Long>{

    OnsiteVerificationPhoto addPhoto(OnsiteVerificationPhoto photo);
}
