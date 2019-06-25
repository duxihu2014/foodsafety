package com.otec.foodsafety.service.delivery;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.delivery.SupplyObject;
import com.otec.foodsafety.entity.delivery.SupplyObjectEnterprise;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.mapper.delivery.SupplyObjectEnterpriseMapper;
import com.otec.foodsafety.mapper.delivery.SupplyObjectMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SupplyObjectServiceImpl extends BaseServiceImpl<SupplyObject, Long> implements SupplyObjectService {
    @Autowired
    SupplyObjectMapper supplyObjectMapper;
    @Autowired
    public void setMapper(SupplyObjectMapper mapper) {
        setGenericMapper(mapper);
    }
    @Autowired
    private EnterpriseBaseMapper enterpriseBaseMapper;
    @Autowired
    private SupplyObjectEnterpriseMapper supplyObjectEnterpriseMapper;

    @Override
    public List<SupplyObject> getAllEnterprise() {
        //获取所有可以选择的企业信息（建档的企业+已添加到配送单位表中的企业）
        Map param = new HashMap();
        param.put("extendSql","ENTERPRISE_ID IS NULL");
        List<SupplyObject> supplyObjectList = supplyObjectMapper.findEntitysByCondition(param);
        param.clear();
        param.put("enterpriseStatus","3");
        List<EnterpriseBase> enterpriseBaseList = enterpriseBaseMapper.findEntitysByCondition(param);
        for(EnterpriseBase item :enterpriseBaseList){
            SupplyObject supplyObject = new SupplyObject();
            BeanUtils.copyProperties(item,supplyObject);
            supplyObjectList.add(supplyObject);
        }
        return supplyObjectList;
    }

    @Override
    public void add(SupplyObject supplyObject,Long enterpriseId) {
        //1.判断供应单位是否已经在供应单位表中，如果不存在则添加企业到供应单位表中
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizingInstitutionBarCode",supplyObject.getOrganizingInstitutionBarCode());
        if(supplyObjectMapper.findEntitysByCondition(param).size()==0){
            supplyObjectMapper.persist(supplyObject);
        }else{
            supplyObject = supplyObjectMapper.findEntitysByCondition(param).get(0);
        }
        //2.新增关系表
        param.clear();
        param.put("enterpriseId",enterpriseId);
        param.put("supplyObjectId",supplyObject.getSupplyObjectId());
        if( supplyObjectEnterpriseMapper.findEntitysByCondition(param).isEmpty()){
            SupplyObjectEnterprise supplyObjectEnterprise = new SupplyObjectEnterprise();
            supplyObjectEnterprise.setEnterpriseId(enterpriseId);
            supplyObjectEnterprise.setSupplyObjectId(supplyObject.getSupplyObjectId());
            supplyObjectEnterpriseMapper.persist(supplyObjectEnterprise);
        }
    }

}
