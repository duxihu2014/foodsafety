package com.otec.foodsafety.service.purchase;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;
import com.otec.foodsafety.mapper.purchase.MaterialsInspectionReportMapper;

@Service
@Transactional
public class MaterialsInspectionReportServiceImpl  extends BaseServiceImpl<MaterialsInspectionReport,Long> implements MaterialsInspectionReportService{
	
	@Autowired
	MaterialsInspectionReportMapper materialsInspectionReportMapper;
	
	@Autowired
	public  void  setMapper(MaterialsInspectionReportMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public GridDataModel findInspectionReport(PageObject po) {
		Integer totalCount = materialsInspectionReportMapper.countInspectionReport(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<MaterialsInspectionReport> results = materialsInspectionReportMapper.findInspectionReport(po.getCondition(),
				rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public MaterialsInspectionReport findByPurcharseId(Long purcharseId) {
		return materialsInspectionReportMapper.findByPurcharseId(purcharseId);
	}
}
