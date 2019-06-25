package com.otec.foodsafety.service.producesafety;

import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;

@RemoteService
public interface RecordWasteSituationService extends BaseService<RecordWasteSituation,Long>{
	
	
	GridDataModel findRecordWasteSituation(PageObject po);

    void add(RecordWasteSituation rs, SysResource wastePhoto);

    void update(RecordWasteSituation rs, SysResource wastePhoto);
}
