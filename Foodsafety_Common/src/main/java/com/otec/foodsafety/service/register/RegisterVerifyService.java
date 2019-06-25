package com.otec.foodsafety.service.register;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.register.RegisterVerify;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface RegisterVerifyService extends BaseService<RegisterVerify,Long> {
    public void enterpriseRegisterAudit(RegisterVerify registerVerify);
}
