package com.otec.foodsafety.mapper.purchase;

import com.otec.foodsafety.entity.purchase.SupplierService;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SupplierServiceMapper extends GenericMapper<SupplierService, Long> {

    Integer getSupplierServiceCount(Map<String, String> params);

    List<SupplierService> getSupplierServicePage(Map<String, String> params, RowBounds rowBounds);

    void batchChangeStatus(Map<String, Object> map);

//    List<SupplierService> getSuppliersByEnterpriseId( Long enterpriseId);
    List<SupplierService> getSuppliersByEnterpriseId( Map<String, Object> map);
}
