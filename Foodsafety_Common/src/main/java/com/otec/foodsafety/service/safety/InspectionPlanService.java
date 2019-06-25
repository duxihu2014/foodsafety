package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionPlan;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface InspectionPlanService extends BaseService<InspectionPlan, Long> {

    /**
     * 企业建档后创建下一次的日常检查计划
     * @param enterpriseId
     */
    Long createInspectionPlan(Long enterpriseId);

    /**
     * 查询企业最近的一次日常检查
     * @param enterpriseId
     * @return
     */
    InspectionPlan getLastInspectionPlan(Long enterpriseId);

    /**
     * 查询当前操作员未完成的日常检查计划
     * @param params
     * @return
     */
    List<Map<String, Object>> getEnterpriseInspectionPlanTask(Map<String, String> params, int start, int pageSize);

    int getEnterpriseInspectionPlanTaskCount(Map<String, String> params);

    Map<String, Object> getInspectionPlan(Long planId);

    List<Map<String, Object>> getEnterpriseInspectionPlan(Map<String, String> params, int start, int pageSize);

    int getEnterpriseInspectionPlanCount(Map<String, String> params);
}
