package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface EnterpriseSupervisionService extends BaseService<EnterpriseSupervision, Long> {

    void addEnterpriseSupervision(EnterpriseSupervision entity);
    void updateEnterpriseSupervision( EnterpriseSupervision entity,String lnglat);
    List<Map<String,Object>> getSupervisionByGrid(Long gridId );

}
