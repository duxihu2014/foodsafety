package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionProject;
import com.otec.foodsafety.mapper.safety.InspectionProjectCategoryMapper;
import com.otec.foodsafety.mapper.safety.InspectionProjectMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InspectionProjectServiceImpl extends BaseServiceImpl<
        InspectionProject, Integer> implements InspectionProjectService {
    @Autowired
    InspectionProjectMapper inspectionProjectMapper;

    @Autowired
    public void setMapper(InspectionProjectMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void batchChangeStatus(Integer[] ids, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("status", status);
        inspectionProjectMapper.batchChangeStatus(map);
    }

    @Override
    public int getTemplateProjectCount(Map<String, String> params) {
        return inspectionProjectMapper.getTemplateProjectCount(params);
    }

    @Override
    public List<InspectionProject> getTemplateProject(Map<String, String> params) {
        return getTemplateProject(params, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<InspectionProject> getTemplateProject(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return inspectionProjectMapper.getTemplateProject(params, rowBounds);
    }

    @Override
    public int getUnbandingProjectCount(Map<String, String> params) {
        return inspectionProjectMapper.getUnbandingProjectCount(params);
    }

    @Override
    public List<InspectionProject> getUnbandingProject(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return inspectionProjectMapper.getUnbandingProject(params, rowBounds);
    }
}
