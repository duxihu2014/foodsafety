package com.otec.foodsafety.service.safety;

import java.util.List;
import java.util.Map;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.RevisitPlan;
import com.otec.foodsafety.mapper.safety.RevisitPlanMapper;
import com.otec.foodsafety.mapper.safety.RevisitResultMapper;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RevisitPlanServiceImpl extends BaseServiceImpl<RevisitPlan, Long> implements RevisitPlanService {
    @Autowired
    RevisitPlanMapper revisitPlanMapper;
    
    @Autowired
	 private RevisitResultMapper  revisitResultMapper;

    @Autowired
    public void setMapper(RevisitPlanMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public RevisitPlan createRevisitPlan(RevisitPlan revisitPlan) {
        revisitPlanMapper.persist(revisitPlan);
        return revisitPlan;
    }
    

	@Override
	public GridDataModel findRevisitPlan(PageObject po) {
		Integer totalCount = revisitPlanMapper.countRevisitPlan(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<RevisitPlan> results = revisitPlanMapper.findRevisitPlan(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

    @Override
    public List<Map<String, Object>> getRevisitPlanTask(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return revisitPlanMapper.getRevisitPlanTask(params, rowBounds);
    }

    @Override
    public int getRevisitPlanTaskCount(Map<String, String> params) {
        return revisitPlanMapper.getRevisitPlanTaskCount(params);
    }

    @Override
    public Map<String, Object> getRevisitPlan(Long planId) {
        return revisitPlanMapper.getRevisitPlan(planId);
    }
}
