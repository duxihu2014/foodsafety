package com.otec.foodsafety.mapper.producesafety;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseage;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseageExt;

public interface RecordAdditiveUseageMapper extends GenericMapper<RecordAdditiveUseage, Long> {
	
	Integer countRecordAdditiveUseageByCondition(Map<String, Object> condition);

	List<RecordAdditiveUseageExt> findRecordAdditiveUseageByCondition(Map<String, Object> condition, RowBounds rowBounds);

	List<RecordAdditiveUseageExt> findRecordAdditiveUseageByCondition(Map<String, Object> condition);

}
