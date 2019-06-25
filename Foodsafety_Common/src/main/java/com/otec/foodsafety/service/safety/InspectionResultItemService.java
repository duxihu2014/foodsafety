package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionResultItem;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface InspectionResultItemService extends BaseService<InspectionResultItem, Long> {

    InspectionResultItem addInspectionResultItem(InspectionResultItem inspectionResultItem);

    List<Map<String, Object>> getInspectionResultItem(Map<String, Object> params);

    List<Map<String, Object>> statisticalInspectionResultItem(Map<String, Object> params);
}
