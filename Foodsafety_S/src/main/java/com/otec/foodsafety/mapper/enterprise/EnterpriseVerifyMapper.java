package com.otec.foodsafety.mapper.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EnterpriseVerifyMapper extends GenericMapper<EnterpriseVerify, Long> {

    Integer getVerifyPageCount(Map map);

    List<EnterpriseVerify> getVerifyPage(Map map, RowBounds rowBounds);
}
