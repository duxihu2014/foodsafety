package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionProjectCategory;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface InspectionProjectCategoryService extends BaseService<InspectionProjectCategory, Integer> {

    /**
     * 批量修改检查项目类型状态
     * @param ids
     * @param status
     */
    void batchChangeStatus(Integer[] ids, String status);

}
