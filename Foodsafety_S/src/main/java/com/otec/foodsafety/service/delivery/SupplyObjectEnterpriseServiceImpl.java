package com.otec.foodsafety.service.delivery;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.delivery.SupplyObject;
import com.otec.foodsafety.entity.delivery.SupplyObjectEnterprise;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.mapper.delivery.SupplyObjectEnterpriseMapper;
import com.otec.foodsafety.mapper.delivery.SupplyObjectMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SupplyObjectEnterpriseServiceImpl extends BaseServiceImpl<SupplyObjectEnterprise, Long> implements SupplyObjectEnterpriseService {
    @Autowired
    SupplyObjectEnterpriseMapper supplyObjectEnterpriseMapper;
    @Autowired
    public void setMapper(SupplyObjectEnterpriseMapper mapper) {
        setGenericMapper(mapper);
    }


    @Override
    public Integer getPageCount(Map<String, String> params) {
        return supplyObjectEnterpriseMapper.getPageCount(params);
    }

    @Override
    public List<SupplyObject> getPage(Map<String, String> params, int offset, int pageSize) {
        RowBounds rowBounds = new RowBounds(offset, pageSize);
        return supplyObjectEnterpriseMapper.getPage(params,rowBounds);
    }
    @Override
    public List<SupplyObject> getAll(Map<String, Object> params) {
        return supplyObjectEnterpriseMapper.getPage(params);
    }

    @Override
    public Integer getPageCountHasSupplyObject(Map<String, String> params) {
        return supplyObjectEnterpriseMapper.getPageCountHasSupplyObject(params);
    }

    @Override
    public List<EnterpriseBase> getPageHasSupplyObject(Map<String, String> params, int offset, int pageSize) {
        RowBounds rowBounds = new RowBounds(offset, pageSize);
        return supplyObjectEnterpriseMapper.getPageHasSupplyObject(params,rowBounds);
    }

    @Override
    public List<EnterpriseBase> getAllHasSupplyObject(Map<String, String> params) {
        return supplyObjectEnterpriseMapper.getPageHasSupplyObject(params);
    }
}
