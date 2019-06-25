package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.commission.CommissionContractProduct;
import com.otec.foodsafety.mapper.commission.CommissionContractProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:39
 */
@Service
@Transactional
public class CommissionContractProductServiceImpl extends BaseServiceImpl<CommissionContractProduct, Long> implements CommissionContractProductService{
    @Autowired
    public void setMapper(CommissionContractProductMapper mapper) {
        setGenericMapper(mapper);
    }
}
