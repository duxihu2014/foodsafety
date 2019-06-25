package com.otec.foodsafety.service.safety;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;
import com.otec.foodsafety.mapper.safety.OnsiteVerificationResultItemMapper;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OnsiteVerificationResultItemServiceImpl  extends BaseServiceImpl<OnsiteVerificationResultItem,Long> implements OnsiteVerificationResultItemService{
	@Autowired
	private OnsiteVerificationResultItemMapper onsiteVerificationResultItemMapper;

	@Autowired
	public  void  setMapper(OnsiteVerificationResultItemMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public OnsiteVerificationResultItem addResultItem(OnsiteVerificationResultItem onsiteVerificationResultItem) {
		onsiteVerificationResultItemMapper.persist(onsiteVerificationResultItem);
		return onsiteVerificationResultItem;
	}

	@Override
	public List<Map<String, Object>> getResultItem(Map<String, String> params) {
		return onsiteVerificationResultItemMapper.getResultItem(params);
	}
}
