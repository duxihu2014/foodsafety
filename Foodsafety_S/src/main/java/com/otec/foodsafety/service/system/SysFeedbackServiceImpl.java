package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysFeedback;
import com.otec.foodsafety.mapper.system.SysFeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysFeedbackServiceImpl extends BaseServiceImpl<SysFeedback, Long>
		implements SysFeedbackService {

	@Autowired
	private SysFeedbackMapper sysFeedbackMapper;


	@Autowired
	public void setMapper(SysFeedbackMapper mapper) {
		setGenericMapper(mapper);
	}



}
