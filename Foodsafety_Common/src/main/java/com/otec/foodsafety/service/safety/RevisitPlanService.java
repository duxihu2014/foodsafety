package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.RevisitPlan;

import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface RevisitPlanService extends BaseService<RevisitPlan, Long> {

    RevisitPlan createRevisitPlan(RevisitPlan revisitPlan);
    
    GridDataModel findRevisitPlan(PageObject po);

    List<Map<String, Object>> getRevisitPlanTask(Map<String, String> params, int start, int pageSize);

    int getRevisitPlanTaskCount(Map<String, String> params);

    Map<String, Object> getRevisitPlan(Long planId);
}
