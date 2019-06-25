package com.otec.foodsafety.service.enterprise;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;
import com.otec.foodsafety.mapper.enterprise.EnterpriseProductChangeMapper;

@Service
@Transactional
public class EnterpriseProductChangeServiceImpl extends BaseServiceImpl<EnterpriseProductChange, Long>
		implements EnterpriseProductChangeService {
	@Autowired
	EnterpriseProductChangeMapper mapper;

	@Autowired
	public void setMapper(EnterpriseProductChangeMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {
		// 得到所有有效的设备信息
		Integer totalCount = mapper.getChangePageCount(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<Map<String, Object>> results = mapper.getChangePage(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}
}
