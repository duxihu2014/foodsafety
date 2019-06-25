package com.otec.foodsafety.service.producesafety;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.producesafety.RecordMorningCheck;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;
import com.otec.foodsafety.mapper.producesafety.RecordMorningCheckMapper;

@Service
@Transactional
public class RecordMorningCheckServiceImpl  extends BaseServiceImpl<RecordMorningCheck,Long> implements RecordMorningCheckService{
	
	@Autowired
	RecordMorningCheckMapper mapper;
	
	@Autowired
	public  void  setMapper(RecordMorningCheckMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public GridDataModel findRecordMorningCheck(PageObject po) {
		Integer totalCount = mapper.countRecordMorningCheck(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<RecordMorningCheck> results = mapper.findRecordMorningCheck(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}
}
