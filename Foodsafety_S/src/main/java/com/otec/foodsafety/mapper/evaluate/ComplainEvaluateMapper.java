package com.otec.foodsafety.mapper.evaluate;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.evaluate.ComplainEvaluate;

public interface ComplainEvaluateMapper extends GenericMapper<ComplainEvaluate,Long>{
	/**
	 * 根据条件统计企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	Integer countComplainEvaluateBycondition(Map<String, Object> condition);

	/**
	 * [分页]根据条件获得企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	List<ComplainEvaluate> findComplainEvaluateBycondition(Map<String, Object> condition, RowBounds rowBounds);

	/**
	 * 根据条件获得企业产品信息
	 * 
	 * @param condition
	 * @return
	 */
	List<ComplainEvaluate> findComplainEvaluateBycondition(Map<String, Object> condition);
}
