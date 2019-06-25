package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionCycle;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface InspectionCycleService extends BaseService<InspectionCycle, Integer> {

}
