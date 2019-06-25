package com.otec.foodsafety.mapper.producesafety;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;

public interface RecordWasteSituationMapper extends GenericMapper<RecordWasteSituation,Long>{

	Integer countRecordWasteSituation(Map<String, Object> condition);

	List<RecordWasteSituation> findRecordWasteSituation(
			Map<String, Object> condition, RowBounds rowBounds);

}
