package com.otec.foodsafety.mapper.enterprise;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.enterprise.EnterpriseEquipment;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentExt;

public interface EnterpriseEquipmentMapper extends GenericMapper<EnterpriseEquipment, Long> {
	/**
	 * 根据条件统计企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	Integer countEnterpriseEquipmentByCondition(Map<String, Object> condition);

	/**
	 * [分页]根据条件获得企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	List<EnterpriseEquipmentExt> findEnterpriseEquipmentByCondition(Map<String, Object> condition, RowBounds rowBounds);

	/**
	 * 根据条件获得企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	List<EnterpriseEquipmentExt> findEnterpriseEquipmentByCondition(Map<String, Object> condition);
}
