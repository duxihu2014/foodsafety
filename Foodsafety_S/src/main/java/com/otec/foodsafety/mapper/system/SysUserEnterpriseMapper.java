package com.otec.foodsafety.mapper.system;

import com.otec.foodsafety.entity.system.SysUserEnterprise;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.Map;

public interface SysUserEnterpriseMapper extends GenericMapper<SysUserEnterprise, Long> {

    public int deleteByCondition(Map<String, Object> param);
}