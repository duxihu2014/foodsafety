package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.commission.CommissionVerify;
import com.otec.foodsafety.mapper.commission.CommissionVerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:40
 */
@Service
@Transactional
public class CommissionVerifyServiceImpl extends BaseServiceImpl<CommissionVerify, Long> implements CommissionVerifyService{
    @Autowired
    public void setMapper(CommissionVerifyMapper mapper) {
        setGenericMapper(mapper);
    }
}
