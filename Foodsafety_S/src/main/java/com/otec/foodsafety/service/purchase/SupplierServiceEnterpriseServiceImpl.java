package com.otec.foodsafety.service.purchase;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import com.otec.foodsafety.mapper.purchase.SupplierServiceEnterpriseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SupplierServiceEnterpriseServiceImpl extends BaseServiceImpl<
        SupplierServiceEnterprise, Long> implements SupplierServiceEnterpriseService {
    @Autowired
    SupplierServiceEnterpriseMapper supplierServiceEnterpriseMapper;

    @Autowired
    public void setMapper(SupplierServiceEnterpriseMapper mapper) {
        setGenericMapper(mapper);
    }
}
