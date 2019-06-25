package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.commission.CommissionEnterprise;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.mapper.commission.CommissionEnterpriseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:40
 */
@Service
@Transactional
public class CommissionEnterpriseServiceImpl extends BaseServiceImpl<CommissionEnterprise, Long> implements CommissionEnterpriseService{
    @Autowired
    public void setMapper(CommissionEnterpriseMapper mapper) {
        setGenericMapper(mapper);
    }
    @Autowired
    private CommissionEnterpriseMapper commissionEnterpriseMapper;
    @Autowired
    private EnterpriseBaseMapper enterpriseBaseMapper;

    @Override
    public List<CommissionEnterprise> getAllEnterprise() {
        //获取所有可以选择的委托企业信息（建档的企业+委托企业信息表中没有企业id的企业即省外企业）
        Map param = new HashMap();
        param.put("extendSql","ENTERPRISE_ID IS NULL");
        List<CommissionEnterprise> commissionEnterpriseList = commissionEnterpriseMapper.findEntitysByCondition(param);
        param.clear();
        List<EnterpriseBase> enterpriseBaseList = enterpriseBaseMapper.findEntitysByCondition(param);
        for(EnterpriseBase item :enterpriseBaseList){
            CommissionEnterprise commissionEnterprise = new CommissionEnterprise();
            BeanUtils.copyProperties(item,commissionEnterprise);
            commissionEnterpriseList.add(commissionEnterprise);
        }
        return commissionEnterpriseList;
    }
}
