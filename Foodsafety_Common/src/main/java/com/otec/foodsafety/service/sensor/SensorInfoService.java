package com.otec.foodsafety.service.sensor;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.sensor.SensorInfo;

@RemoteService
public interface SensorInfoService extends BaseService<SensorInfo,Long>{

    GridDataModel getGridDataByCondition(PageObject po);

    GridDataModel getListCurrentMonitor(PageObject po);
}
