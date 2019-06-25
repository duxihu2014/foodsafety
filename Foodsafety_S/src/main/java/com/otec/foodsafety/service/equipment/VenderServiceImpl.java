package com.otec.foodsafety.service.equipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.equipment.Vender;
import com.otec.foodsafety.mapper.equipment.VenderMapper;

@Service
@Transactional
public class VenderServiceImpl extends BaseServiceImpl<Vender, Long>
		implements VenderService {
	
	@Autowired
	private VenderMapper mapper;
	
	@Autowired
	public void setMapper(VenderMapper mapper) {
		setGenericMapper(mapper);
	}
	

}
