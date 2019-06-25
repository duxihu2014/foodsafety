package com.otec.foodsafety.mapper.producesafety;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.producesafety.RecordMorningCheck;

public interface RecordMorningCheckMapper extends GenericMapper<RecordMorningCheck,Long>{
	
	List<RecordMorningCheck> findRecordMorningCheck(Map<String, Object> condition, RowBounds rowBounds);
	
	Integer countRecordMorningCheck(Map<String, Object> condition);

}
