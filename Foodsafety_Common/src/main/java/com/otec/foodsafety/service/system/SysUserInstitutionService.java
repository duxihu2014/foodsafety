package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface SysUserInstitutionService extends BaseService<SysUserInstitution,Long> {

}