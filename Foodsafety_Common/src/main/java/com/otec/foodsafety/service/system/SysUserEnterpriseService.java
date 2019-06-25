package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysUserEnterprise;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface SysUserEnterpriseService extends BaseService<SysUserEnterprise,Long> {
}