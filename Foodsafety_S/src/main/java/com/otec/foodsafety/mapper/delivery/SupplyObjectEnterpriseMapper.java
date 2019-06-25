package com.otec.foodsafety.mapper.delivery;

import com.otec.foodsafety.entity.delivery.SupplyObject;
import com.otec.foodsafety.entity.delivery.SupplyObjectEnterprise;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SupplyObjectEnterpriseMapper extends GenericMapper<SupplyObjectEnterprise, Long> {
    List<SupplyObject> getPage(Map<String, String> params, RowBounds rowBounds);

    List<SupplyObject> getPage(Map<String, Object> params);

    Integer getPageCount(Map<String, String> params);

    List<EnterpriseBase> getPageHasSupplyObject(Map<String, String> params, RowBounds rowBounds);

    List<EnterpriseBase> getPageHasSupplyObject(Map<String, String> params);

    Integer getPageCountHasSupplyObject(Map<String, String> params);
}
