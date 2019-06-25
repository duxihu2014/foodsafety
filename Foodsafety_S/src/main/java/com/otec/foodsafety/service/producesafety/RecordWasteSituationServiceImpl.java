package com.otec.foodsafety.service.producesafety;

import java.util.List;

import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;
import com.otec.foodsafety.mapper.producesafety.RecordWasteSituationMapper;

@Service
@Transactional
public class RecordWasteSituationServiceImpl  extends BaseServiceImpl<RecordWasteSituation,Long> implements RecordWasteSituationService{

	@Autowired
	RecordWasteSituationMapper mapper;
	@Autowired
	SysResourceMapper sysResourceMapper;
	
	@Autowired
	public  void  setMapper(RecordWasteSituationMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public GridDataModel findRecordWasteSituation(PageObject po) {
		Integer totalCount = mapper.countRecordWasteSituation(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<RecordWasteSituation> results = mapper.findRecordWasteSituation(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public void add(RecordWasteSituation rs, SysResource wastePhoto) {
		if(wastePhoto!=null){
			sysResourceMapper.persist(wastePhoto);
			rs.setWastePhoto(wastePhoto.getResourceId());
		}
		mapper.persist(rs);
	}

	@Override
	public void update(RecordWasteSituation rs, SysResource wastePhoto) {
		if(wastePhoto!=null){
			sysResourceMapper.persist(wastePhoto);
			rs.setWastePhoto(wastePhoto.getResourceId());
		}
		mapper.updateById(rs);
	}
}
