package com.otec.foodsafety.service.equipment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.equipment.EnterpriseGroup;
import com.otec.foodsafety.mapper.equipment.EnterpriseGroupMapper;

@Service
@Transactional
public class EnterpriseGroupServiceImpl  extends BaseServiceImpl<EnterpriseGroup,Long> implements EnterpriseGroupService{

	@Autowired
	public  void  setMapper(EnterpriseGroupMapper mapper){
		 setGenericMapper(mapper);
	}
}
