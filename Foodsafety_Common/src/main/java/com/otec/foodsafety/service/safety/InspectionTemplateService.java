package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionTemplate;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface InspectionTemplateService extends BaseService<InspectionTemplate, Integer> {

    /**
     * 批量修改检查模板状态
     * @param ids
     * @param status
     */
    void batchChangeStatus(Integer[] ids, String status);
}
