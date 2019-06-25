package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseBaseChange;
import com.otec.foodsafety.entity.system.SysInstitution;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface EnterpriseService extends BaseService<EnterpriseBase,Long> {
  /*企业基本信息修改*/
  public void modifyEnterpriseBase(EnterpriseBase enterpriseBase,Long userId,String reason);



 }
