package com.otec.foodsafety.mapper.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseScaleChange;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EnterpriseScaleChangeMapper extends GenericMapper<EnterpriseScaleChange, Long> {

    Integer getScaleChangePageCount(Map map);

    List<Map<String,Object>> getScaleChangePage(Map map, RowBounds rowBounds);
}
