package com.otec.foodsafety.mapper.register;

import com.otec.foodsafety.entity.register.RegisterCertificate;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.Map;

public interface RegisterCertificateMapper extends GenericMapper<RegisterCertificate, Long> {
    public void addRegisterCertificate(Map<String, Object> map);
}

