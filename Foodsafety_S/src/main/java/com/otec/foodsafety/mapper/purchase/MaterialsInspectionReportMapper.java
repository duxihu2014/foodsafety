package com.otec.foodsafety.mapper.purchase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;

public interface MaterialsInspectionReportMapper extends GenericMapper<MaterialsInspectionReport, Long> {
	/**
	 * 根据采购编号得到对应的质检信息数据
	 * 
	 * @param PurcharseId
	 * @return
	 */
	MaterialsInspectionReport findByPurcharseId(Long PurcharseId);
	/**
	 * 删除采购记录对应的质检信息
	 * @param PurcharseId
	 * @return
	 */
	void removeByPurchaseId(Long purcharseId);
	
	Integer countInspectionReport(Map<String, Object> condition);

	List<MaterialsInspectionReport> findInspectionReport(Map<String, Object> condition,
			RowBounds rowBounds);
}
