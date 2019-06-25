package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.mapper.register.RegisterUserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnterpriseRegisterServiceImpl extends BaseServiceImpl<RegisterUser, Long> implements EnterpriseRegisterService{
    @Autowired
    public void setMapper(RegisterUserMapper mapper) {
        setGenericMapper(mapper);
    }

    @Autowired
    private RegisterUserMapper registerUserMapper;
    @Override
    public Integer getRegisterEnterprisePageCount(Map param) {
        return registerUserMapper.getRegisterEnterprisePageCount(param);
    }

    @Override
    public List<RegisterUser> getRegisterEnterprisePage(Map map, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return registerUserMapper.getRegisterEnterprisePage(map,rowBounds);

    }

}
