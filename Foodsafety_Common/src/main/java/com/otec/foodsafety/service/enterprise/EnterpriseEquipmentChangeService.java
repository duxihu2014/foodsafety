package com.otec.foodsafety.service.enterprise;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentChange;

@RemoteService
public interface EnterpriseEquipmentChangeService extends BaseService<EnterpriseEquipmentChange,Long>{

}
