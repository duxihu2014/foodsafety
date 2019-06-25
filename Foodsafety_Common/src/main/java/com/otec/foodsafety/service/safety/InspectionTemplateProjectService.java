package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.InspectionTemplateProject;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.Map;

@RemoteService
public interface InspectionTemplateProjectService extends BaseService<InspectionTemplateProject, Integer> {

    void unbanding(Integer[] ids, Integer templateId);

    void setProjectScore(Integer id, Integer score);
}
