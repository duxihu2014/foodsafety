package com.otec.foodsafety.service.purchase;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import org.springframework.remoting.service.annotation.RemoteService;

@RemoteService
public interface SupplierServiceEnterpriseService extends BaseService<SupplierServiceEnterprise, Long> {
}
