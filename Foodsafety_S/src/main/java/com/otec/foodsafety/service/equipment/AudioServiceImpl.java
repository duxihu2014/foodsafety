package com.otec.foodsafety.service.equipment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.equipment.Audio;
import com.otec.foodsafety.mapper.equipment.AudioMapper;

@Service
@Transactional
public class AudioServiceImpl  extends BaseServiceImpl<Audio,Long> implements AudioService{

	@Autowired
	public  void  setMapper(AudioMapper mapper){
		 setGenericMapper(mapper);
	}
}
