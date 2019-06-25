package com.otec.foodsafety.service.safety;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.credit.CreditRecord;
import com.otec.foodsafety.entity.safety.*;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.safety.InspectionResultMapper;
import com.otec.foodsafety.service.credit.CreditRecordService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.CommonUtil;
import com.otec.foodsafety.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InspectionResultServiceImpl extends BaseServiceImpl<
        InspectionResult, Long> implements InspectionResultService {
    @Autowired
    SysResourceService sysResourceService;
    @Autowired
    InspectionPlanService inspectionPlanService;
    @Autowired
    InspectionResultMapper inspectionResultMapper;
    @Autowired
    InspectionResultItemService inspectionResultItemService;
    @Autowired
    InspectionResultPhotoService inspectionResultPhotoService;
    @Autowired
    RevisitPlanService revisitPlanService;
    @Autowired
    InspectionTemplateProjectService inspectionTemplateProjectService;
    @Autowired
    CreditRecordService creditRecordService;

    @Autowired
    public void setMapper(InspectionResultMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public List<Map<String, Object>> getInspectionResult(Map<String, String> params) {
        return inspectionResultMapper.getInspectionResult(params);
    }

    @Override
    public void create(InspectionResult inspectionResult, List<
            InspectionResultItem> inspectionResultItemList, List<SysResource> sysResourceList) throws Exception {
        // 保存日常检查记录
        inspectionResultMapper.persist(inspectionResult);

        Map<String, Object> params = new HashMap<>();
        params.put("templateId", inspectionResult.getTemplateId());

        int score = 0;
        SysResource sysResource = null;
        CreditRecord creditRecord = null;
        InspectionTemplateProject inspectionTemplateProject;
        InspectionResultItem inspectionResultItem = null;
        InspectionResultPhoto inspectionResultPhoto = null;
        for (int i = 0; i < inspectionResultItemList.size(); i++) {
            sysResource = sysResourceList.get(i);
            // 保存日常检查记录照片
            sysResource = sysResourceService.addResource(sysResource);

            inspectionResultPhoto = new InspectionResultPhoto();
            inspectionResultPhoto.setResultId(inspectionResult.getResultId());
            inspectionResultPhoto.setResourceId(sysResource.getResourceId());
            inspectionResultPhoto = inspectionResultPhotoService.addPhoto(inspectionResultPhoto);

            inspectionResultItem = inspectionResultItemList.get(i);
            inspectionResultItem.setResultId(inspectionResult.getResultId());
            inspectionResultItem.setPhotoResult(inspectionResultPhoto.getPhotoId());
            // 保存日常检查项目记录
            inspectionResultItemService.addInspectionResultItem(inspectionResultItem);
            // 累加不合格项目的记分
            if (!StringUtils.isBlankString(inspectionResultItem.getResultsUnqualified())) {
                params.put("projectId", inspectionResultItem.getProjectId());
                inspectionTemplateProject = inspectionTemplateProjectService.findEntitysByCondition(params).get(0);
                score += inspectionTemplateProject.getScore();
            }
        }

        InspectionPlan inspectionPlan = inspectionPlanService.findById(inspectionResult.getPlanId());

        // 如果不合格记分则需要保存
        if (score > 0) {
            // 判断该企业本年有无初始化记分
            creditRecord = creditRecordService.getCreditRecordCurrentYear(inspectionPlan.getEnterpriseId());
            // 如果没有初始化记分，进行本年记分初始化，然后更新记分。
            if (creditRecord == null) {
                creditRecord = new CreditRecord();
                creditRecord.setEnterpriseId(inspectionPlan.getEnterpriseId());
                creditRecord = creditRecordService.initCreditRecordCurrentYear(creditRecord);
            }
            creditRecord.setCycleScore(score);
            creditRecordService.updateCreditRecordCurrentYear(creditRecord);
        }

        // 如果日常检查结果的回访时间不为空，则需要创建回访事件。
        if (inspectionResult.getRevisitDate() != null) {
            RevisitPlan revisitPlan = new RevisitPlan();
            // 复制日常检查计划数据到回访事件对象
            CommonUtil.copyBean(inspectionPlan, revisitPlan);

            revisitPlan.setRevisitSource("1"); // 回访来源为日常检查
            revisitPlan.setRevisitDate(inspectionResult.getRevisitDate());
            revisitPlan.setInspectedTime(inspectionResult.getInspectedTime());
            revisitPlan.setInspectedInstitution(inspectionResult.getInspectedInstitution());
            revisitPlan.setInspectedUser(inspectionResult.getInspectedUser());
            revisitPlan.setResultId(inspectionResult.getResultId());
            revisitPlan.setMainProblems(inspectionResult.getMainProblems());
            revisitPlan.setPlanStatus("0");
            revisitPlanService.createRevisitPlan(revisitPlan);

            // 更新日常检查结果的回访事件编号
            inspectionResult.setRevisitPlanId(revisitPlan.getPlanId());
            inspectionResultMapper.updateById(inspectionResult);
        }
        // 更新日常检查计划状态
        inspectionPlan.setPlanStatus("1");
        inspectionPlanService.updateById(inspectionPlan);
    }

    @Override
    public void createByResourceId(InspectionResult inspectionResult, List<InspectionResultItem> inspectionResultItemList, List<Long> resourceIds) throws Exception {
        // 保存日常检查记录
        inspectionResultMapper.persist(inspectionResult);

        Map<String, Object> params = new HashMap<>();
        params.put("templateId", inspectionResult.getTemplateId());

        int score = 0;
    //    SysResource sysResource = null;
        CreditRecord creditRecord = null;
        InspectionTemplateProject inspectionTemplateProject;
        InspectionResultItem inspectionResultItem = null;
        InspectionResultPhoto inspectionResultPhoto = null;
        for (int i = 0; i < inspectionResultItemList.size(); i++) {
         //   sysResource = sysResourceList.get(i);
            // 保存日常检查记录照片
        //    sysResource = sysResourceService.addResource(sysResource);

            inspectionResultPhoto = new InspectionResultPhoto();
            inspectionResultPhoto.setResultId(inspectionResult.getResultId());
            inspectionResultPhoto.setResourceId(resourceIds.get(i));
            inspectionResultPhoto = inspectionResultPhotoService.addPhoto(inspectionResultPhoto);

            inspectionResultItem = inspectionResultItemList.get(i);
            inspectionResultItem.setResultId(inspectionResult.getResultId());
            inspectionResultItem.setPhotoResult(inspectionResultPhoto.getPhotoId());
            // 保存日常检查项目记录
            inspectionResultItemService.addInspectionResultItem(inspectionResultItem);
            // 累加不合格项目的记分
            if (!StringUtils.isBlankString(inspectionResultItem.getResultsUnqualified())) {
                params.put("projectId", inspectionResultItem.getProjectId());
                inspectionTemplateProject = inspectionTemplateProjectService.findEntitysByCondition(params).get(0);
                score += inspectionTemplateProject.getScore();
            }
        }

        InspectionPlan inspectionPlan = inspectionPlanService.findById(inspectionResult.getPlanId());

        // 如果不合格记分则需要保存
        if (score > 0) {
            // 判断该企业本年有无初始化记分
            creditRecord = creditRecordService.getCreditRecordCurrentYear(inspectionPlan.getEnterpriseId());
            // 如果没有初始化记分，进行本年记分初始化，然后更新记分。
            if (creditRecord == null) {
                creditRecord = new CreditRecord();
                creditRecord.setEnterpriseId(inspectionPlan.getEnterpriseId());
                creditRecord = creditRecordService.initCreditRecordCurrentYear(creditRecord);
            }
            creditRecord.setCycleScore(score);
            creditRecordService.updateCreditRecordCurrentYear(creditRecord);
        }

        // 如果日常检查结果的回访时间不为空，则需要创建回访事件。
        if (inspectionResult.getRevisitDate() != null) {
            RevisitPlan revisitPlan = new RevisitPlan();
            // 复制日常检查计划数据到回访事件对象
            CommonUtil.copyBean(inspectionPlan, revisitPlan);

            revisitPlan.setRevisitSource("1"); // 回访来源为日常检查
            revisitPlan.setRevisitDate(inspectionResult.getRevisitDate());
            revisitPlan.setInspectedTime(inspectionResult.getInspectedTime());
            revisitPlan.setInspectedInstitution(inspectionResult.getInspectedInstitution());
            revisitPlan.setInspectedUser(inspectionResult.getInspectedUser());
            revisitPlan.setResultId(inspectionResult.getResultId());
            revisitPlan.setMainProblems(inspectionResult.getMainProblems());
            revisitPlan.setPlanStatus("0");
            revisitPlanService.createRevisitPlan(revisitPlan);

            // 更新日常检查结果的回访事件编号
            inspectionResult.setRevisitPlanId(revisitPlan.getPlanId());
            inspectionResultMapper.updateById(inspectionResult);
        }
        // 更新日常检查计划状态
        inspectionPlan.setPlanStatus("1");
        inspectionPlanService.updateById(inspectionPlan);
    }
}
