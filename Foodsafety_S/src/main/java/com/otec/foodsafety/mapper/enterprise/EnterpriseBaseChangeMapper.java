package com.otec.foodsafety.mapper.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseBaseChange;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EnterpriseBaseChangeMapper extends GenericMapper<EnterpriseBaseChange, Long> {
    Integer getBaseChangePageCount(Map map);

    List<Map<String,Object>> getBaseChangePage(Map map, RowBounds rowBounds);
}
