package com.otec.foodsafety.service.producesafety;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseage;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseageExt;
import com.otec.foodsafety.mapper.producesafety.RecordAdditiveUseageMapper;

@Service
@Transactional
public class RecordAdditiveUseageServiceImpl extends BaseServiceImpl<RecordAdditiveUseage, Long>
		implements RecordAdditiveUseageService {
	@Autowired
	RecordAdditiveUseageMapper mapper;

	@Autowired
	public void setMapper(RecordAdditiveUseageMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public GridDataModel getGridDataAdditiveUseageByCondition(PageObject po) {
		Integer totalCount = mapper.countRecordAdditiveUseageByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<RecordAdditiveUseageExt> results = mapper.findRecordAdditiveUseageByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}
}
