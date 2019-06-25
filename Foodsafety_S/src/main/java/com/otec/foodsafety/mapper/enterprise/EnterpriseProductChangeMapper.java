package com.otec.foodsafety.mapper.enterprise;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;

public interface EnterpriseProductChangeMapper extends GenericMapper<EnterpriseProductChange, Long> {

	Integer getChangePageCount(Map<String, Object> condition);

	List<Map<String, Object>> getChangePage(Map<String, Object> condition, RowBounds rowBounds);

}
