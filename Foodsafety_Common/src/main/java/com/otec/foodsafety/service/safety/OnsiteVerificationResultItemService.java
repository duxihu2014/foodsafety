package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface OnsiteVerificationResultItemService extends BaseService<OnsiteVerificationResultItem,Long>{
    OnsiteVerificationResultItem addResultItem(OnsiteVerificationResultItem onsiteVerificationResultItem);

    List<Map<String, Object>> getResultItem(Map<String, String> params);
}
