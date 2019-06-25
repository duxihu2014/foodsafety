package com.otec.foodsafety.mapper.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnterpriseSupervisionMapper extends GenericMapper<EnterpriseSupervision, Long> {
    List<Map<String,Object>> getSupervisionByGrid(@Param("gridId") Long gridId);
    void addEnterpriseSupervision(EnterpriseSupervision entity);
}
