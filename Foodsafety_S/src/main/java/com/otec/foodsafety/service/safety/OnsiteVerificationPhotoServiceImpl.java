package com.otec.foodsafety.service.safety;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.safety.OnsiteVerificationPhoto;
import com.otec.foodsafety.mapper.safety.OnsiteVerificationPhotoMapper;

@Service
@Transactional
public class OnsiteVerificationPhotoServiceImpl  extends BaseServiceImpl<OnsiteVerificationPhoto,Long> implements OnsiteVerificationPhotoService{
	@Autowired
	OnsiteVerificationPhotoMapper mapper;

	@Autowired
	public  void  setMapper(OnsiteVerificationPhotoMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public OnsiteVerificationPhoto addPhoto(OnsiteVerificationPhoto photo) {
		mapper.persist(photo);
		return photo;
	}
}
