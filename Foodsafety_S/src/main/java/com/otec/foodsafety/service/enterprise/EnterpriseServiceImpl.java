package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseBaseChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import com.otec.foodsafety.mapper.system.SysInstitutionMapper;
import com.otec.foodsafety.mapper.system.SysUserInstitutionMapper;
import com.otec.foodsafety.service.system.SysInstitutionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public  class EnterpriseServiceImpl extends BaseServiceImpl<EnterpriseBase, Long> implements EnterpriseService {
    @Autowired
    private EnterpriseBaseMapper enterpriseBaseMapper;
    @Autowired
    private EnterpriseBaseChangeMapper enterpriseBaseChangeMapper;
    @Autowired
    private EnterpriseVerifyMapper enterpriseVerifyMapper;

    @Autowired
    public void setMapper(EnterpriseBaseMapper mapper) {
        setGenericMapper(mapper);
    }

    /*企业基本信息修改*/
    @Override
    public void modifyEnterpriseBase(EnterpriseBase enterpriseBase,Long userId,String reason) {
        //1.EnterpriseBaseChange企业基本信息变更表新增一条数据
        EnterpriseBaseChange enterpriseBaseChange = new EnterpriseBaseChange();
        BeanUtils.copyProperties(enterpriseBase,enterpriseBaseChange);
        enterpriseBaseChangeMapper.persist(enterpriseBaseChange);
        //2.EnterpriseVerify企业信息审核表新增一条数据
        EnterpriseVerify enterpriseVerify = new EnterpriseVerify();
        Date data = new Date();
        enterpriseVerify.setChangeId(enterpriseBaseChange.getChangeId());
        enterpriseVerify.setChangeType("2");//变更操作类型1.新增、2.修改、3.删除
        enterpriseVerify.setDataType("1");
        enterpriseVerify.setVerifyUserId(userId);
        enterpriseVerify.setChangeTime(data);
        enterpriseVerify.setEnterpriseId(enterpriseBase.getEnterpriseId());
        enterpriseVerify.setVerifyStatus("1");
        enterpriseVerify.setChangeReason(reason);
        enterpriseVerifyMapper.persist(enterpriseVerify);
    }
}
