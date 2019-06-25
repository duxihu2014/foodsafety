package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.register.RegisterUser;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface EnterpriseRegisterService extends BaseService<RegisterUser,Long> {
    Integer getRegisterEnterprisePageCount(Map param);

    List<RegisterUser> getRegisterEnterprisePage(Map map, int start, int pageSize);
}
