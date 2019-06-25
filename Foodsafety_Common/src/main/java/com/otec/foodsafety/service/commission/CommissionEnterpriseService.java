package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.commission.CommissionEnterprise;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:35
 */
@RemoteService
public interface CommissionEnterpriseService extends BaseService<CommissionEnterprise, Long> {
     List<CommissionEnterprise> getAllEnterprise();
}
