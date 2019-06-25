package com.otec.foodsafety.service.producesafety;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseage;

@RemoteService
public interface RecordAdditiveUseageService extends BaseService<RecordAdditiveUseage, Long> {
	/**
	 * 
	 * @param po
	 * @return
	 */
	GridDataModel getGridDataAdditiveUseageByCondition(PageObject po);
}
