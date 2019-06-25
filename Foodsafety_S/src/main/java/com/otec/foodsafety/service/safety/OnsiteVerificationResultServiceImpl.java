package com.otec.foodsafety.service.safety;

import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.safety.OnsiteVerificationPhoto;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.safety.OnsiteVerificationResultMapper;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.enterprise.EnterpriseSupervisionService;
import com.otec.foodsafety.service.supervisory.ResponsibilityGridService;
import com.otec.foodsafety.service.system.SysResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.safety.OnsiteVerificationResult;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OnsiteVerificationResultServiceImpl  extends BaseServiceImpl<OnsiteVerificationResult,Long> implements OnsiteVerificationResultService{
	@Autowired
	private OnsiteVerificationResultMapper onsiteVerificationResultMapper;
	@Autowired
	SysResourceService sysResourceService;
	@Autowired
	EnterpriseBaseService enterpriseBaseService;
	@Autowired
	OnsiteVerificationResultItemService resultItemService;
	@Autowired
	OnsiteVerificationPhotoService resultPhotoService;
	@Autowired
	RevisitPlanService revisitPlanService;
	@Autowired
	EnterpriseSupervisionService enterpriseSupervisionService;
	@Autowired
	ResponsibilityGridService responsibilityGridService;

	@Autowired
	public  void  setMapper(OnsiteVerificationResultMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public List<Map<String, Object>> getOnsiteResult(Map<String, String> params) {
		return onsiteVerificationResultMapper.getResult(params);
	}

	@Override
	public void create(OnsiteVerificationResult onsiteVerificationResult,
                       List<OnsiteVerificationResultItem> resultItemList, List<SysResource> sysResourceList) {
		// 保存日常检查记录
		onsiteVerificationResultMapper.persist(onsiteVerificationResult);

		SysResource sysResource = null;
		OnsiteVerificationResultItem resultItem = null;
		OnsiteVerificationPhoto resultPhoto = null;
		for (int i = 0; i < resultItemList.size(); i++) {
            sysResource = sysResourceList.get(i);
            // 保存日常检查记录照片
            sysResource = sysResourceService.addResource(sysResource);

            resultPhoto = new OnsiteVerificationPhoto();
            resultPhoto.setResultId(onsiteVerificationResult.getResultId());
            resultPhoto.setResourceId(sysResource.getResourceId());
            resultPhoto = resultPhotoService.addPhoto(resultPhoto);

            resultItem = resultItemList.get(i);
            resultItem.setResultId(onsiteVerificationResult.getResultId());
            resultItem.setPhotoResult(resultPhoto.getPhotoId());
			// 保存日常检查项目记录
			resultItemService.addResultItem(resultItem);
		}
        // 更新企业状态
		EnterpriseBase enterpriseBase = enterpriseBaseService.findById(onsiteVerificationResult.getEnterpriseId());
		if(StringUtils.equalsIgnoreCase("1", onsiteVerificationResult.getVerificateConclusion())){
			enterpriseBase.setEnterpriseStatus("1");
		}else {
			enterpriseBase.setEnterpriseStatus("2");
		}
		enterpriseBaseService.updateById(enterpriseBase);
	}


    @Override
    public void createByResourceId(OnsiteVerificationResult onsiteVerificationResult, List<OnsiteVerificationResultItem> resultItemList, List<Long> resourceIds) {
        // 保存日常检查记录
        onsiteVerificationResultMapper.persist(onsiteVerificationResult);

   //     SysResource sysResource = null;
        OnsiteVerificationResultItem resultItem = null;
        OnsiteVerificationPhoto resultPhoto = null;
        for (int i = 0; i < resultItemList.size(); i++) {
    //        sysResource = resourceIds.get(i);
            // 保存日常检查记录照片
    //        sysResource = sysResourceService.getResourceById(resourceIds.get(i));

            resultPhoto = new OnsiteVerificationPhoto();
            resultPhoto.setResultId(onsiteVerificationResult.getResultId());
            resultPhoto.setResourceId(resourceIds.get(i));
            resultPhoto = resultPhotoService.addPhoto(resultPhoto);

            resultItem = resultItemList.get(i);
            resultItem.setResultId(onsiteVerificationResult.getResultId());
            resultItem.setPhotoResult(resultPhoto.getPhotoId());
            // 保存日常检查项目记录
            resultItemService.addResultItem(resultItem);
        }
        // 更新企业状态
        EnterpriseBase enterpriseBase = enterpriseBaseService.findById(onsiteVerificationResult.getEnterpriseId());
        if(StringUtils.equalsIgnoreCase("1", onsiteVerificationResult.getVerificateConclusion())){
            enterpriseBase.setEnterpriseStatus("1");
        }else {
            enterpriseBase.setEnterpriseStatus("2");
        }
        enterpriseBaseService.updateById(enterpriseBase);
    }

    @Override
    public void update(OnsiteVerificationResult onsiteVerificationResult,
                       List<OnsiteVerificationResultItem> resultItemList, List<SysResource> sysResourceList) {
        // 保存日常检查记录
        onsiteVerificationResultMapper.persist(onsiteVerificationResult);

        SysResource sysResource = null;
        OnsiteVerificationResultItem resultItem = null;
        OnsiteVerificationResultItem resultItem_orgin = null;
        OnsiteVerificationPhoto resultPhoto = null;
        for (int i = 0; i < resultItemList.size(); i++) {
            resultItem = resultItemList.get(i);
            resultItem.setResultId(onsiteVerificationResult.getResultId());
            resultItem_orgin = resultItemService.findById(resultItem.getItemId());

            sysResource = sysResourceList.get(i);

            if (StringUtils.isNotBlank(sysResource.getResourceName())
                    && resultItem.getPhoneRectified() == null) {
                // 保存日常检查记录照片
                sysResource = sysResourceService.addResource(sysResource);

                resultPhoto = new OnsiteVerificationPhoto();
                resultPhoto.setResultId(onsiteVerificationResult.getResultId());
                resultPhoto.setResourceId(sysResource.getResourceId());
                resultPhoto = resultPhotoService.addPhoto(resultPhoto);

                resultItem.setPhoneRectified(resultPhoto.getPhotoId());
            } else {
                resultItem.setPhoneRectified(resultItem_orgin.getPhoneRectified());
            }
            resultItem.setPhotoResult(resultItem_orgin.getPhotoResult());
            resultItem.setItemId(null);

            // 保存日常检查项目记录
            resultItemService.addResultItem(resultItem);
        }
        // 更新企业状态
        EnterpriseBase enterpriseBase = enterpriseBaseService.findById(onsiteVerificationResult.getEnterpriseId());
        if(StringUtils.equalsIgnoreCase("1", onsiteVerificationResult.getVerificateConclusion())){
            enterpriseBase.setEnterpriseStatus("1");
        }else {
            enterpriseBase.setEnterpriseStatus("2");
        }
        enterpriseBaseService.updateById(enterpriseBase);
    }
}
