package com.otec.foodsafety.service.delivery;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.delivery.SupplyObject;
import com.otec.foodsafety.entity.delivery.SupplyObjectEnterprise;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface SupplyObjectEnterpriseService extends BaseService<SupplyObjectEnterprise, Long> {
    Integer getPageCount(Map<String, String> params);

    List<SupplyObject> getPage(Map<String, String> params, int offset, int pageSize);

    List<SupplyObject> getAll(Map<String, Object> params);

    Integer getPageCountHasSupplyObject(Map<String, String> params);

    List<EnterpriseBase> getPageHasSupplyObject(Map<String, String> params, int offset, int pageSize);

     List<EnterpriseBase> getAllHasSupplyObject(Map<String, String> params);
}
