package com.otec.foodsafety.service.purchase;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;

@RemoteService
public interface MaterialsInspectionReportService extends BaseService<MaterialsInspectionReport,Long>{
	
	GridDataModel findInspectionReport(PageObject po);

	MaterialsInspectionReport findByPurcharseId(Long purcharseId);

}
