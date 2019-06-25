package com.otec.foodsafety.mapper.purchase;

import java.util.List;
import java.util.Map;

import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.purchase.MaterialsEnterprisePurchase;

public interface MaterialsEnterprisePurchaseMapper extends GenericMapper<MaterialsEnterprisePurchase, Long> {
	Integer countMaterialsEnterprisePurchaseByCondition(Map<String, Object> condition);

	List<MaterialsEnterprisePurchase> findMaterialsEnterprisePurchaseByCondition(Map<String, Object> condition,
			RowBounds rowBounds);

	List<MaterialsEnterprisePurchase> findMaterialsEnterprisePurchaseByCondition(Map<String, Object> condition);

	/**
	 * 根据ID，与原料类别查询唯一 的采购记录
	 * 
	 * @param condition
	 * @return
	 */
	MaterialsEnterprisePurchase getMaterialsEnterprisePurchaseById(Map<String, Object> condition);


	/**
	 * 获取某企业每种原料的最新进度记录
	 * @param condition
	 * @param rowBounds
	 * @return
	 */
	List<MaterialsEnterprisePurchase> getMaterialNewRecordPage(Map<String, String> condition, RowBounds rowBounds);

	Integer getMaterialNewRecordCount(Map<String, String> condition);
}
