package com.otec.foodsafety.service.delivery;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.delivery.SupplyObject;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface SupplyObjectService extends BaseService<SupplyObject, Long> {
     List<SupplyObject> getAllEnterprise();
     void add(SupplyObject supplyObject,Long enterpriseId);
}
