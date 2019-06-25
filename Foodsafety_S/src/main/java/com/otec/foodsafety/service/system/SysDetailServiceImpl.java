package com.otec.foodsafety.service.system;

import java.util.Date;

import com.otec.foodsafety.service.system.SysDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysDetail;
import com.otec.foodsafety.mapper.system.SysDetailMapper;
import com.otec.foodsafety.web.context.SystemCache;

@Service
@Transactional
public class SysDetailServiceImpl extends BaseServiceImpl<SysDetail, Long>
		implements SysDetailService {
	@Autowired
	public void setMapper(SysDetailMapper mapper) {
		super.setGenericMapper(mapper);
	}
	
	@Override
	public void persist(SysDetail record) {
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		record.setDetailStatus(1);
		super.persist(record);
		SystemCache.reCacheDict();
	}
	
	@Override
	public int removeById(Long detailId) {
		int flag = super.removeById(detailId);
		SystemCache.reCacheDict();
		return flag;
	}
}
