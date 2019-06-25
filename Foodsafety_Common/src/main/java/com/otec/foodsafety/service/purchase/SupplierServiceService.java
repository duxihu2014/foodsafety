package com.otec.foodsafety.service.purchase;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface SupplierServiceService extends BaseService<SupplierService, Long> {

    Integer getSupplierServiceCount(Map<String, String> params);

    List<SupplierService> getSupplierServicePage(
            Map<String, String> params, int start, int pageSize);

    /**
     * 批量修改供应商状态
     * @param ids
     * @param status
     */
    void batchChangeStatus(Long[] ids, String status);

    /**
     * 创建供应商
     * @param supplierService
     * @param idCardFront
     * @param idCardBack
     * @param businessLicence
     */
    void create(SupplierService supplierService, SysResource idCardFront,
                SysResource idCardBack, SysResource businessLicence, SupplierServiceEnterprise supplierServiceEnterprise);

    /**
     * 更新供应商
     * @param supplierService
     * @param idCardFront
     * @param idCardBack
     * @param businessLicence
     */
    void update(SupplierService supplierService, SysResource idCardFront,
                SysResource idCardBack, SysResource businessLicence) throws Exception;


    /**
     * 根据企业编号查询关联的供货商
     * @param map
     * @return
     */
//    List<SupplierService> getSuppliersByEnterpriseId(Long enterpriseId);
     List<SupplierService> getSuppliersByEnterpriseId(Map<String, Object> map);
}
