package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import com.otec.foodsafety.entity.safety.InspectionCycle;
import com.otec.foodsafety.entity.safety.InspectionPlan;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.mapper.safety.InspectionPlanMapper;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.enterprise.EnterpriseSupervisionService;
import com.otec.foodsafety.service.supervisory.ResponsibilityGridService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.CommonUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class InspectionPlanServiceImpl extends BaseServiceImpl<
        InspectionPlan, Long> implements InspectionPlanService {
    @Autowired
    InspectionPlanMapper inspectionPlanMapper;
    @Autowired
    EnterpriseBaseService enterpriseBaseService;
    @Autowired
    EnterpriseSupervisionService enterpriseSupervisionService;
    @Autowired
    ResponsibilityGridService responsibilityGridService;
    @Autowired
    InspectionCycleService inspectionCycleService;
    @Autowired
    DateLocalService dateLocalService;

    @Autowired
    public void setMapper(InspectionPlanMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public Long createInspectionPlan(Long enterpriseId) {
        // 查询企业基本信息
        EnterpriseBase enterpriseBase = enterpriseBaseService.findById(enterpriseId);
        // 查询企业监管信息
        EnterpriseSupervision enterpriseSupervision =
                enterpriseSupervisionService.findById(enterpriseId);
        // 查询企业所属责任区域信息
        ResponsibilityGrid responsibilityGrid =
                responsibilityGridService.findById(enterpriseSupervision.getGridId());
        // 查询责任区域相关机构的日常检查周期设置
        Map<String, Object> param = new HashMap<>();
        param.put("settingInstitution", responsibilityGrid.getInstitutionId());
        InspectionCycle inspectionCycle =
                inspectionCycleService.findEntitysByCondition(param).get(0);
        // 创建日常检查计划对象
        InspectionPlan inspectionPlan = new InspectionPlan();
        inspectionPlan.setAreaId(enterpriseBase.getAreaId());
        inspectionPlan.setGridId(responsibilityGrid.getGridId());
        inspectionPlan.setTownStreet(responsibilityGrid.getTownStreet());
        inspectionPlan.setCommitteeVillage(responsibilityGrid.getCommitteeVillage());
        inspectionPlan.setMonitors(responsibilityGrid.getMonitors());
        inspectionPlan.setAssistants(responsibilityGrid.getAssistants());
        inspectionPlan.setStandards(responsibilityGrid.getStandards());
        inspectionPlan.setEnterpriseId(enterpriseBase.getEnterpriseId());
        inspectionPlan.setEnterpriseName(enterpriseBase.getEnterpriseName());
        inspectionPlan.setCorporateRepresentative(enterpriseBase.getCorporateRepresentative());
        inspectionPlan.setRegisterAddress(enterpriseBase.getRegisterAddress());
        inspectionPlan.setProductionAddress(enterpriseBase.getProductionAddress());
        inspectionPlan.setSubjectClassification(enterpriseBase.getSubjectClassification());
        inspectionPlan.setSuperviseClassification(enterpriseBase.getSuperviseClassification());
        inspectionPlan.setCreateTime(dateLocalService.getDataBaseDate());
        inspectionPlan.setPlanStatus("0"); // 未完成
//        CommonUtil.copyBean(enterpriseBase, inspectionPlan);

        // 开始计算下次日常检查的日期
        Calendar calendar = Calendar.getInstance();
        // 查询当前企业最近一次的日常检查日期，如果没有进行过日常检查，则比较当年首次检查日期和建档日期。
        InspectionPlan lastInspectionPlan = getLastInspectionPlan(enterpriseId);
        if (lastInspectionPlan != null) {
            calendar.setTime(lastInspectionPlan.getInspectionDate());
        } else {
            // 如果当年日常检查首次检查日期小于企业建档日期，则以企业建档日期为基础，否则以首次检查日期为基础。
            if (inspectionCycle.getInitialInspection().compareTo(enterpriseBase.getArchivedDate()) < 0) {
                calendar.setTime(enterpriseBase.getArchivedDate());

                // 按企业的监管级别设置下次日常检查周期时间
                if ("A".equals(enterpriseSupervision.getRegulatoryLevel())) {
                    calendar.add(Calendar.MONTH, inspectionCycle.getInspectionCycleA());
                } else if ("B".equals(enterpriseSupervision.getRegulatoryLevel())) {
                    calendar.add(Calendar.MONTH, inspectionCycle.getInspectionCycleB());
                } else if ("C".equals(enterpriseSupervision.getRegulatoryLevel())) {
                    calendar.add(Calendar.MONTH, inspectionCycle.getInspectionCycleC());
                } else if ("D".equals(enterpriseSupervision.getRegulatoryLevel())) {
                    calendar.add(Calendar.MONTH, inspectionCycle.getInspectionCycleD());
                } else {
                    // 暂时不处理非A、B、C、D的监管等级的意外
                }
            } else {
                // 如果日常检查首次检查日期大于企业建档日期，则以日常检查首次检查日期。
                calendar.setTime(inspectionCycle.getInitialInspection());
            }
        }
        // 设置下次检查日期
        Date inspectionDate = calendar.getTime();
        inspectionPlan.setInspectionDate(inspectionDate);
        // 设置下次任务提醒日期
        calendar.add(Calendar.DAY_OF_MONTH, -inspectionCycle.getAheadDays());
        Date taskDate = calendar.getTime();
        inspectionPlan.setTaskDate(taskDate);
        // 保存日常检查计划
        inspectionPlanMapper.persist(inspectionPlan);
        return inspectionPlan.getPlanId();
    }

    @Override
    public InspectionPlan getLastInspectionPlan(Long enterpriseId) {
        List<InspectionPlan> list = inspectionPlanMapper.getInspectionPlanByEnterpriseId(enterpriseId);
        if (list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public List<Map<String, Object>> getEnterpriseInspectionPlanTask(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return inspectionPlanMapper.getEnterpriseInspectionPlanTask(params, rowBounds);
    }

    @Override
    public int getEnterpriseInspectionPlanTaskCount(Map<String, String> params) {
        return inspectionPlanMapper.getEnterpriseInspectionPlanTaskCount(params);
    }

    @Override
    public Map<String, Object> getInspectionPlan(Long planId) {
        return inspectionPlanMapper.getInspectionPlan(planId);
    }

    @Override
    public List<Map<String, Object>> getEnterpriseInspectionPlan(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return inspectionPlanMapper.getEnterpriseInspectionPlan(params, rowBounds);
    }

    @Override
    public int getEnterpriseInspectionPlanCount(Map<String, String> params) {
        return inspectionPlanMapper.getEnterpriseInspectionPlanCount(params);
    }


}
