package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysUserEnterprise;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import com.otec.foodsafety.mapper.system.SysUserEnterpriseMapper;
import com.otec.foodsafety.mapper.system.SysUserInstitutionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysUserEnterpriseServiceImpl extends BaseServiceImpl<SysUserEnterprise, Long> implements SysUserEnterpriseService {


    @Autowired
    public void setMapper(SysUserEnterpriseMapper mapper) {
        setGenericMapper(mapper);
    }


}