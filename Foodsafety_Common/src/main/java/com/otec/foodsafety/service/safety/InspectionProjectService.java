package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionProject;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface InspectionProjectService extends BaseService<InspectionProject, Integer> {

    /**
     * 批量修改检查项目状态
     * @param ids
     * @param status
     */
    void batchChangeStatus(Integer[] ids, String status);

    int getTemplateProjectCount(Map<String, String> params);

    List<InspectionProject> getTemplateProject(Map<String, String> params);

    List<InspectionProject> getTemplateProject(Map<String, String> params, int start, int pageSize);

    int getUnbandingProjectCount(Map<String, String> params);

    List<InspectionProject> getUnbandingProject(Map<String, String> params, int start, int pageSize);
}
