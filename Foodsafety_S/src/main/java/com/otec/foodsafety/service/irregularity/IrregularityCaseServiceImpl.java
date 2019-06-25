package com.otec.foodsafety.service.irregularity;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductType;
import com.otec.foodsafety.entity.evaluate.ComplainEvaluate;
import com.otec.foodsafety.entity.irregularity.IrregularityCase;
import com.otec.foodsafety.entity.irregularity.IrregularityCaseClose;
import com.otec.foodsafety.entity.irregularity.IrregularityCaseExt;
import com.otec.foodsafety.entity.irregularity.IrregularityPunish;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseProductMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseProductTypeMapper;
import com.otec.foodsafety.mapper.evaluate.ComplainEvaluateMapper;
import com.otec.foodsafety.mapper.irregularity.IrregularityCaseCloseMapper;
import com.otec.foodsafety.mapper.irregularity.IrregularityCaseMapper;
import com.otec.foodsafety.mapper.irregularity.IrregularityPunishMapper;
import com.otec.foodsafety.mapper.util.DateMapper;
import com.otec.foodsafety.service.evaluate.ComplainEvaluateService;
import com.otec.foodsafety.service.util.DateLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class IrregularityCaseServiceImpl extends BaseServiceImpl<IrregularityCase, Long> implements IrregularityCaseService {


    @Autowired
    ComplainEvaluateMapper complainEvaluateMapper;
    @Autowired
    EnterpriseBaseMapper enterpriseBaseMapper;
    @Autowired
    EnterpriseProductMapper enterpriseProductMapper;
    @Autowired
    DateMapper dateMapper;
    @Autowired
    IrregularityCaseMapper irregularityCaseMapper;
    @Autowired
    IrregularityCaseCloseMapper irregularityCaseCloseMapper;
    @Autowired
    IrregularityPunishMapper irregularityPunishMapper;
    @Autowired
    EnterpriseProductTypeMapper enterpriseProductTypeMapper;

    @Autowired
    public void setMapper(IrregularityCaseMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void add(IrregularityCase irregularityCase) {
        EnterpriseBase base=enterpriseBaseMapper.findById(irregularityCase.getEnterpriseId());
        irregularityCase.setEnterpriseName(base.getEnterpriseName());
        irregularityCase.setAreaId(base.getAreaId());
        irregularityCase.setRegisterDate(base.getRegisterDate());
        irregularityCase.setRegisterAddress(base.getRegisterAddress());
        irregularityCase.setOrganizingInstitutionBarCode(base.getOrganizingInstitutionBarCode());
        irregularityCase.setSubjectClassification(base.getSubjectClassification());
        irregularityCase.setSuperviseClassification(base.getSuperviseClassification());
        irregularityCase.setOperationScope(base.getOperationScope());
        irregularityCase.setProductionAddress(base.getProductionAddress());

        EnterpriseProduct product=enterpriseProductMapper.findById(irregularityCase.getProductId());
        irregularityCase.setProductName(product.getProductName());
        irregularityCase.setProductType(product.getProductTypeId());
        irregularityCase.setLicenseNo(product.getLicenseNo());
        irregularityCase.setSpecificationModel(product.getSpecificationModel());
        irregularityCase.setRegisteredTrademark(product.getRegisteredTrademark());
        irregularityCase.setProductPicture(product.getProductPicture());

        irregularityCase.setCreateTime(dateMapper.getDataBaseDate());

        irregularityCaseMapper.persist(irregularityCase);

        if(irregularityCase.getComplainEvaluateId()!=null && irregularityCase.getComplainEvaluateId()>0){
            ComplainEvaluate complainEvaluate= complainEvaluateMapper.findById( irregularityCase.getComplainEvaluateId().longValue());
            if(complainEvaluate!=null){
                complainEvaluate.setIrregularityId(irregularityCase.getIrregularityId());
                complainEvaluateMapper.updateById(complainEvaluate);
            }
        }
    }

    @Override
    public void update(IrregularityCase irregularityCase) {
        EnterpriseBase base=enterpriseBaseMapper.findById(irregularityCase.getEnterpriseId());
        irregularityCase.setEnterpriseName(base.getEnterpriseName());
        irregularityCase.setAreaId(base.getAreaId());
        irregularityCase.setRegisterDate(base.getRegisterDate());
        irregularityCase.setRegisterAddress(base.getRegisterAddress());
        irregularityCase.setOrganizingInstitutionBarCode(base.getOrganizingInstitutionBarCode());
        irregularityCase.setSubjectClassification(base.getSubjectClassification());
        irregularityCase.setSuperviseClassification(base.getSuperviseClassification());
        irregularityCase.setOperationScope(base.getOperationScope());
        irregularityCase.setProductionAddress(base.getProductionAddress());

        EnterpriseProduct product=enterpriseProductMapper.findById(irregularityCase.getProductId());
        irregularityCase.setProductName(product.getProductName());
        irregularityCase.setProductType(product.getProductTypeId());
        irregularityCase.setLicenseNo(product.getLicenseNo());
        irregularityCase.setSpecificationModel(product.getSpecificationModel());
        irregularityCase.setRegisteredTrademark(product.getRegisteredTrademark());
        irregularityCase.setProductPicture(product.getProductPicture());

        irregularityCaseMapper.updateById(irregularityCase);
    }

    @Override
    public IrregularityCaseExt findByIrregularityId(Long id) {
        IrregularityCaseExt ext=new IrregularityCaseExt();
        IrregularityCase irregularityCase=irregularityCaseMapper.findById(id);
        if(irregularityCase.getProductType()!=null){
            EnterpriseProductType type=enterpriseProductTypeMapper.findById(irregularityCase.getProductType());
            ext.setProductPTypeId(type.getProductTypePId());
        }
        ext.setIrregularityCase(irregularityCase);

        HashMap<String,Object> params=new HashMap<>();
        params.put("irregularityId",id);
        if(irregularityCase.getCaseStatus()!=null){
            if (irregularityCase.getCaseStatus().equals("2")||irregularityCase.getCaseStatus().equals("3")){
                List<IrregularityPunish> punishList=irregularityPunishMapper.findEntitysByCondition(params);
                if(punishList.size()>0){
                    ext.setPunish(punishList.get(0));
                }
            }
            if (irregularityCase.getCaseStatus().equals("3")){
                List<IrregularityCaseClose> closeList=irregularityCaseCloseMapper.findEntitysByCondition(params);
                if(closeList.size()>0){
                    ext.setCaseClose(closeList.get(0));
                }
            }
        }
        return ext;
    }

    @Override
    public void saveCasePunish(IrregularityPunish punish) {
        if(punish.getPunishId()>0){
            irregularityPunishMapper.updateById(punish);
        }else{
            irregularityPunishMapper.persist(punish);
            IrregularityCase irregularityCase=irregularityCaseMapper.findById(punish.getIrregularityId());
            irregularityCase.setCaseStatus("2");
            irregularityCaseMapper.updateById(irregularityCase);
        }

    }

    @Override
    public void saveCaseClose(IrregularityCaseClose caseClose) {
        if(caseClose.getClosecaseId()>0){
            irregularityCaseCloseMapper.updateById(caseClose);
        }else {
            irregularityCaseCloseMapper.persist(caseClose);
            IrregularityCase irregularityCase=irregularityCaseMapper.findById(caseClose.getIrregularityId());
            irregularityCase.setCaseStatus("3");
            irregularityCaseMapper.updateById(irregularityCase);
        }
    }



}
