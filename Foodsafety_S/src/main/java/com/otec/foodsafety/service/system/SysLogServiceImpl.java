package com.otec.foodsafety.service.system;

import java.util.List;

import com.otec.foodsafety.service.system.SysLogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysLog;
import com.otec.foodsafety.mapper.system.SysLogMapper;

@Service
@Transactional
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, Long> implements
        SysLogService {
	@Autowired
	private SysLogMapper mapper;
	@Autowired
	public void setMapper(SysLogMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public GridDataModel findSysLog(PageObject po) {
		// TODO Auto-generated method stub
		Integer total=mapper.countByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());		
		List<SysLog> results =mapper.findSysLog(po.getCondition(), rowBounds);
		
		return new GridDataModel(results,total);		
	}
}
