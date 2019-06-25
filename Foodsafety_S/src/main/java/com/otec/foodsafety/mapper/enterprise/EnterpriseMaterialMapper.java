package com.otec.foodsafety.mapper.enterprise;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialExt;

public interface EnterpriseMaterialMapper extends GenericMapper<EnterpriseMaterial, Long> {
	/**
	 * 根据条件统计企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	Integer countEnterpriseMaterialByCondition(Map<String, Object> condition);

	/**
	 * [分页]根据条件获得企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	List<EnterpriseMaterialExt> findEnterpriseMaterialByCondition(Map<String, Object> condition, RowBounds rowBounds);

	/**
	 * 根据条件获得企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	List<EnterpriseMaterialExt> findEnterpriseMaterialByCondition(Map<String, Object> condition);

}
