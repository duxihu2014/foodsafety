package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionResult;
import com.otec.foodsafety.entity.safety.InspectionResultItem;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface InspectionResultService extends BaseService<InspectionResult, Long> {

    /**
     * 查询日常检查计划的检查记录
     * @param params
     * @return
     */
    List<Map<String, Object>> getInspectionResult(Map<String, String> params);

    /**
     * 创建日常检查记录
     * @param inspectionResult
     * @param inspectionResultItemList
     * @param SysResourceList
     */
    void create(InspectionResult inspectionResult, List<
            InspectionResultItem> inspectionResultItemList, List<SysResource> SysResourceList) throws Exception;

    /**
     * 创建日常检查记录
     * @param inspectionResult
     * @param inspectionResultItemList
     * @param resourceIds
     */
    void createByResourceId(InspectionResult inspectionResult, List<
            InspectionResultItem> inspectionResultItemList, List<Long> resourceIds) throws Exception;
}
