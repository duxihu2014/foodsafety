package com.otec.foodsafety.service.producesafety;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.producesafety.RecordDisinfection;
import com.otec.foodsafety.mapper.producesafety.RecordDisinfectionMapper;

@Service
@Transactional
public class RecordDisinfectionServiceImpl  extends BaseServiceImpl<RecordDisinfection,Long> implements RecordDisinfectionService{
	
	@Autowired
	RecordDisinfectionMapper mapper;
	
	@Autowired
	public  void  setMapper(RecordDisinfectionMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public GridDataModel findRecordDisinfection(PageObject po) {
		Integer totalCount = mapper.countRecordDisinfection(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<RecordDisinfection> results = mapper.findRecordDisinfection(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}
}
