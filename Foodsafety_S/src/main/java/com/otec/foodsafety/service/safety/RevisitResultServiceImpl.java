package com.otec.foodsafety.service.safety;

import com.otec.foodsafety.entity.safety.*;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.service.util.DateLocalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.mapper.safety.RevisitResultMapper;

import java.util.List;

@Service
@Transactional
public class RevisitResultServiceImpl extends BaseServiceImpl<
		RevisitResult,Long> implements RevisitResultService{
    @Autowired
    RevisitPlanService revisitPlanService;
    @Autowired
    RevisitResultMapper revisitResultMapper;
    @Autowired
    InspectionResultPhotoService inspectionResultPhotoService;
    @Autowired
    InspectionResultItemService inspectionResultItemService;
    @Autowired
    SysResourceService sysResourceService;
    @Autowired
    DateLocalService dateLocalService;

    @Autowired
    public void setMapper(RevisitResultMapper mapper){
         setGenericMapper(mapper);
     }

    @Override
    public void create(RevisitResult revisitResult,
                       List<InspectionResultItem> resultItemList,
                       List<SysResource> sysResourceList) {
        // 保存回访情况记录
        revisitResultMapper.persist(revisitResult);
        SysResource sysResource = null;
        InspectionResultItem resultItem = null;
        InspectionResultPhoto resultPhoto = null;
        RevisitPlan revisitPlan = revisitPlanService.findById(revisitResult.getPlanId());

        for (int i = 0; i < resultItemList.size(); i++) {
            resultItem = resultItemList.get(i);
            sysResource = sysResourceList.get(i);

            if (StringUtils.isNotBlank(sysResource.getResourceName())
                    && resultItem.getPhoneRectified() == null) {
                // 保存现场核查记录照片
                sysResource = sysResourceService.addResource(sysResource);

                resultPhoto = new InspectionResultPhoto();
                resultPhoto.setResultId(revisitPlan.getResultId());
                resultPhoto.setResourceId(sysResource.getResourceId());
                resultPhoto = inspectionResultPhotoService.addPhoto(resultPhoto);

                resultItem.setResultId(revisitPlan.getResultId());
                resultItem.setPhoneRectified(resultPhoto.getPhotoId());
                resultItem.setRevisitId(revisitResult.getResultId());
                resultItem.setRevisitTime(revisitResult.getRevisitTime());
                // 更新现场核查记录明细
                inspectionResultItemService.mergeById(resultItem);
            }
        }
        // 如果需要再次回访，则重新新增一条回访计划
        if (revisitResult.getNextRevisitTime() != null) {

        } else {
            revisitPlan.setPlanStatus("1");
            revisitPlan.setFinishTime(dateLocalService.getDataBaseDate());
            revisitPlanService.updateById(revisitPlan);
        }
    }

}
