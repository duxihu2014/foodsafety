package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.commission.CommissionContractFiles;
import com.otec.foodsafety.mapper.commission.CommissionContractFilesMapper;
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
public class CommissionContractFilesServiceImpl  extends BaseServiceImpl<CommissionContractFiles, Long> implements CommissionContractFilesService{


    @Autowired
    public void setMapper(CommissionContractFilesMapper mapper) {
        setGenericMapper(mapper);
    }
}
