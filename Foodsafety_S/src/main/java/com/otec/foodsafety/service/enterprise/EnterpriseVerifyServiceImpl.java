package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/10/16 0016 下午 16:45
 */
@Service
@Transactional
public class EnterpriseVerifyServiceImpl extends BaseServiceImpl<EnterpriseVerify, Long> implements EnterpriseVerifyService{
    @Autowired
    public void setMapper(EnterpriseVerifyMapper mapper) {
        setGenericMapper(mapper);
    }
    @Autowired
    private EnterpriseVerifyMapper enterpriseVerifyMapper;

    /**
     * 根据区域id和企业数据变更类型查询审批记录总条数
     *
     * @param map
     * @return
     */
    @Override
    public Integer getEnterpriseVerifyCount(Map map) {
        return enterpriseVerifyMapper.getVerifyPageCount(map);
    }

    /**
     * 根据区域id和企业数据变更类型查询审批记录
     *
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<EnterpriseVerify> getEnterpriseVerify(Map map, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return enterpriseVerifyMapper.getVerifyPage(map,rowBounds);
    }
}
