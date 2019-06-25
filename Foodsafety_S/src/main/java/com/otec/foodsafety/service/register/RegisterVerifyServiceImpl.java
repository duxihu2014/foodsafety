package com.otec.foodsafety.service.register;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import com.otec.foodsafety.entity.register.RegisterBase;
import com.otec.foodsafety.entity.register.RegisterCertificate;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.entity.register.RegisterVerify;
import com.otec.foodsafety.entity.system.SysRoleUser;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.entity.system.SysUserEnterprise;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseCertificateMapper;
import com.otec.foodsafety.mapper.register.RegisterBaseMapper;
import com.otec.foodsafety.mapper.register.RegisterCertificateMapper;
import com.otec.foodsafety.mapper.register.RegisterUserMapper;
import com.otec.foodsafety.mapper.register.RegisterVerifyMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.mapper.system.SysRoleUserMapper;
import com.otec.foodsafety.mapper.system.SysUserEnterpriseMapper;
import com.otec.foodsafety.mapper.system.SysUserMapper;
import com.otec.foodsafety.service.enterprise.EnterpriseSupervisionService;
import com.otec.foodsafety.service.system.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class RegisterVerifyServiceImpl extends BaseServiceImpl<RegisterVerify, Long> implements RegisterVerifyService{
    @Autowired
    private RegisterVerifyMapper registerVerifyMapper;
    @Autowired
    private RegisterUserMapper registerUserMapper;
    @Autowired
    private RegisterBaseMapper registerBaseMapper;
    @Autowired
    private RegisterCertificateMapper registerCertificateMapper;
    @Autowired
    private EnterpriseBaseMapper enterpriseBaseMapper;
    @Autowired
    private EnterpriseCertificateMapper enterpriseCertificateMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private SysUserEnterpriseMapper sysUserEnterpriseMapper;
    @Autowired
    EnterpriseSupervisionService enterpriseSupervisionService;
    @Autowired
    SysDictService sysDictService;
    @Autowired
    public void setMapper(RegisterVerifyMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void enterpriseRegisterAudit(RegisterVerify registerVerify) {
        RegisterUser registerUser = registerUserMapper.findById(registerVerify.getRegisterId());
        RegisterBase registerBase = registerBaseMapper.findById(registerVerify.getRegisterId());
        RegisterCertificate registerCertificate =registerCertificateMapper.findById(registerVerify.getRegisterId());
        if("1".equals(registerVerify.getVerifyResult())){//审核通过，将注册表中的数据移到正式企业表、用户表、用户角色表
            //1.用户表
            SysUser sysUser = new SysUser();
            sysUser.setUserName(registerUser.getUserName());
            sysUser.setRealName(registerUser.getRealName());
            sysUser.setUserPwd(registerUser.getUserPwd());
            sysUser.setMobile(registerUser.getMobile());
            sysUser.setEmail(registerUser.getEmail());
            sysUser.setUserStatus("1");
            sysUser.setUserType("4");
            sysUser.setCreateTime(new Date());
            sysUserMapper.persist(sysUser);
            //2.企业角色表
            SysRoleUser sysRoleUser = new SysRoleUser();
            Map<String, String> map = sysDictService.getDetailNameMap("企业建档前角色");
            sysRoleUser.setRoleId(Long.valueOf(map.get("企业建档前角色")));
            sysRoleUser.setUserId(Long.valueOf(sysUser.getUserId()));
            sysRoleUserMapper.persist(sysRoleUser);
            //3.企业基本信息表
            EnterpriseBase enterpriseBase = new EnterpriseBase();
            enterpriseBase.setEnterpriseName(registerBase.getEnterpriseName());
            enterpriseBase.setCorporateRepresentative(registerBase.getCorporateRepresentative());
            enterpriseBase.setContactMobile(registerBase.getContactMobile());
            enterpriseBase.setContactNumber(registerBase.getContactNumber());
            enterpriseBase.setContacts(registerBase.getContacts());
            enterpriseBase.setAreaId(registerBase.getAreaId());
            enterpriseBase.setEconomicNature(registerBase.getEconomicNature());
            enterpriseBase.setEnterpriseStatus("0");
            enterpriseBase.setIdCardNo(registerBase.getIdCardNo());
            enterpriseBase.setSubjectClassification(registerBase.getSubjectClassification());
            enterpriseBase.setRegisterAddress(registerBase.getRegisterAddress());
            enterpriseBase.setProductionAddress(registerBase.getProductionAddress());
            enterpriseBase.setPostalCode(registerBase.getPostalCode());
            enterpriseBase.setInspectorMobile(registerBase.getInspectorMobile());
            enterpriseBase.setOrganizingInstitutionBarCode(registerBase.getOrganizingInstitutionBarCode());
            enterpriseBase.setOperationScope(registerBase.getOperationScope());
            enterpriseBase.setRegisteredCapital(registerBase.getRegisteredCapital());
            enterpriseBase.setRegisterDate(registerBase.getRegisterDate());
            enterpriseBaseMapper.persist(enterpriseBase);
            //4.用户企业表
            SysUserEnterprise sysUserEnterprise = new SysUserEnterprise();
            sysUserEnterprise.setAreaId(enterpriseBase.getAreaId());
            sysUserEnterprise.setEnterpriseId(enterpriseBase.getEnterpriseId());
            sysUserEnterprise.setUserId(sysUser.getUserId());
            sysUserEnterpriseMapper.persist(sysUserEnterprise);
            //5.证照表
            EnterpriseCertificate enterpriseCertificate = new EnterpriseCertificate();
            enterpriseCertificate.setCertificateName(registerCertificate.getCertificateName());
            enterpriseCertificate.setCertificateNo(registerCertificate.getCertificateNo());
            enterpriseCertificate.setCertificateType(registerCertificate.getCertificateType());
            enterpriseCertificate.setCertificatePhoto(registerCertificate.getCertificatePhoto());
            enterpriseCertificate.setIssuingDate(registerCertificate.getIssuingDate());
            enterpriseCertificate.setValidDate(registerCertificate.getValidDate());
            enterpriseCertificate.setCertificateStatus("1");//0：停用；1：启用；2：待启用
            enterpriseCertificate.setIssuingUnit(registerCertificate.getIssuingUnit());
            enterpriseCertificate.setRegisterScope(registerCertificate.getRegisterScope());
            enterpriseCertificate.setEnterpriseId( enterpriseBase.getEnterpriseId());
            enterpriseCertificateMapper.persist(enterpriseCertificate);
            // 企业责任区域表
            EnterpriseSupervision enterpriseSupervision = new EnterpriseSupervision();
            enterpriseSupervision.setEnterpriseId(enterpriseBase.getEnterpriseId());
            enterpriseSupervision.setGridId(registerVerify.getGridId());
            enterpriseSupervisionService.addEnterpriseSupervision(enterpriseSupervision);

            registerUser.setRegisterStatus("1");
            registerUserMapper.updateById(registerUser);
            registerVerifyMapper.persist(registerVerify);
        }else if("0".equals(registerVerify.getVerifyResult())){//审核不通过
            registerUser.setRegisterStatus("2");
            registerUserMapper.updateById(registerUser);
            registerVerifyMapper.persist(registerVerify);
        }else{ //作废操作，删除注册表数据
            registerCertificateMapper.removeById( registerVerify.getRegisterId());
            registerBaseMapper.removeById( registerVerify.getRegisterId());
            sysResourceMapper.removeById(registerCertificate.getCertificatePhoto());
            registerUserMapper.removeById( registerVerify.getRegisterId());
        }


    }
}
