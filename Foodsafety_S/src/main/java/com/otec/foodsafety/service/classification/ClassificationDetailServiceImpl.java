package com.otec.foodsafety.service.classification;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.classification.ClassificationDetail;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.classification.ClassificationDetailMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseSupervisionMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.service.system.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: xiongjing
 * @Date: 2019/01/17 0011 下午 14:39
 */
@Service
@Transactional
public class ClassificationDetailServiceImpl extends BaseServiceImpl<ClassificationDetail, Long> implements ClassificationDetailService{


    @Autowired
    EnterpriseSupervisionMapper enterpriseSupervisionMapper;
    @Autowired
    ClassificationDetailMapper classificationDetailMapper;
    @Autowired
    SysResourceMapper sysResourceMapper;
    @Autowired
    EnterpriseBaseMapper enterpriseBaseMapper;

    @Autowired
    public void setMapper(ClassificationDetailMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void add(ClassificationDetail detail, SysResource photo) {
        if(photo!=null){
            sysResourceMapper.persist(photo);
            detail.setResourceId(photo.getResourceId());
         }
        classificationDetailMapper.persist(detail);

        EnterpriseSupervision item = enterpriseSupervisionMapper.findById(detail.getEnterpriseId());
        item.setRegulatoryLevel(detail.getAfterClassification());
        enterpriseSupervisionMapper.updateById(item);

        EnterpriseBase enterpriseBase=enterpriseBaseMapper.findById(detail.getEnterpriseId());
        enterpriseBase.setSuperviseClassification(detail.getAfterClassification());
        enterpriseBaseMapper.updateById(enterpriseBase);
    }

    @Override
    public List<Map> getChangeRecords(Long enterpriseId) {
        return classificationDetailMapper.getChangeRecords(enterpriseId);
    }
}
