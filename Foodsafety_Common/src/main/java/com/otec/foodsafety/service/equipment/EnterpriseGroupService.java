package com.otec.foodsafety.service.equipment;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.EnterpriseGroup;

@RemoteService
public interface EnterpriseGroupService extends BaseService<EnterpriseGroup,Long>{

}
