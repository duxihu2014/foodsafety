package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.commission.CommissionContract;
import com.otec.foodsafety.entity.commission.CommissionContractFiles;
import com.otec.foodsafety.entity.commission.CommissionContractProduct;
import com.otec.foodsafety.entity.commission.CommissionEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:33
 */
@RemoteService
public interface CommissionContractService extends BaseService<CommissionContract, Long> {
    public void save(List<CommissionEnterprise> commissionEnterpriseList, CommissionContract contract, List<SysResource> sysResources);
}
