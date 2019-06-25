package com.otec.foodsafety.service.enterprise;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialChange;

@RemoteService
public interface EnterpriseMaterialChangeService extends BaseService<EnterpriseMaterialChange,Long>{

}
