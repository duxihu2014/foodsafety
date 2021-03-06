package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.commission.CommissionContractProduct;
import org.springframework.remoting.service.annotation.RemoteService;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:35
 */
@RemoteService
public interface CommissionContractProductService extends BaseService<CommissionContractProduct, Long> {
}
