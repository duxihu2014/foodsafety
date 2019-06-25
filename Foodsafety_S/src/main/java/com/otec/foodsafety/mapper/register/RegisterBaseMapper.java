package com.otec.foodsafety.mapper.register;

import com.otec.foodsafety.entity.register.RegisterBase;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface RegisterBaseMapper extends GenericMapper<RegisterBase, Long> {
    public void addRegisterBase(Map<String, Object> map);

    public RegisterBase getByOrganizingInstitutionBarCode(@Param("organizingInstitutionBarCode") String organizingInstitutionBarCode);
}
