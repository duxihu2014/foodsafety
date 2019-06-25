package com.otec.foodsafety.service.system;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysLog;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface SysLogService extends BaseService<SysLog,Long>{
	public GridDataModel findSysLog(PageObject po);
}
