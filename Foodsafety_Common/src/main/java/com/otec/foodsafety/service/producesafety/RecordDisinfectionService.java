package com.otec.foodsafety.service.producesafety;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.producesafety.RecordDisinfection;

@RemoteService
public interface RecordDisinfectionService extends BaseService<RecordDisinfection,Long>{
	
	GridDataModel findRecordDisinfection(PageObject po);

}
