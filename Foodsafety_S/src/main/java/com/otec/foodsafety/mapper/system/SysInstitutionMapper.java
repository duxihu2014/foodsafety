package com.otec.foodsafety.mapper.system;

import com.otec.foodsafety.entity.system.SysInstitution;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.List;
import java.util.Map;

public interface SysInstitutionMapper extends GenericMapper<SysInstitution, Long> {

    SysInstitution findByInstitutionName(String name);

    public List<SysInstitution> selectInstitutionsByUserId(Long userId);

    void batchChangeStatus(Map<String, Object> map);
}