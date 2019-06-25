package com.otec.foodsafety.mapper.system;


import com.otec.foodsafety.entity.system.SysUserInstitution;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.Map;

public interface SysUserInstitutionMapper extends GenericMapper<SysUserInstitution, Long> {
    public int deleteByCondition(Map<String, Object> condition);

}