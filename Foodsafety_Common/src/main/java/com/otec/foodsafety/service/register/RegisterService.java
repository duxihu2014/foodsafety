package com.otec.foodsafety.service.register;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.register.RegisterBase;
import com.otec.foodsafety.entity.register.RegisterCertificate;
import com.otec.foodsafety.entity.register.RegisterUser;
import org.springframework.remoting.service.annotation.RemoteService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RemoteService
public interface RegisterService extends BaseService<RegisterUser,Long> {
    public void addRegister(Map<String, Object> registerUserMap,
                            Map<String, Object> registerBaseMap,
                            Map<String, Object> registerCertificateMap ,
                            MultipartFile multipartFile,String uploadUrl,String imageFolder) throws Exception;

    public Boolean getRegisterUserByUserName(String userName);

    public Boolean getRegisterBaseByOrganizingInstitutionBarCode(String organizingInstitutionBarCode);

    public RegisterBase findRegisterBaseById(Long registerId);

    public RegisterCertificate findRegisterCertificateById(Long registerId);

    public void updateRegister(RegisterUser registerUser, MultipartFile multipartFile,String uploadUrl,String imageFolder) throws Exception;


    void addRegister(Map<String, Object> accountMap, Map<String, Object> enterprise, Map<String, Object> license);
}
