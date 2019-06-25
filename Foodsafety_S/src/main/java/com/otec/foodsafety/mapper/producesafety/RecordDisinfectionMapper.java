package com.otec.foodsafety.mapper.producesafety;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;
import com.otec.foodsafety.entity.producesafety.RecordDisinfection;

public interface RecordDisinfectionMapper extends GenericMapper<RecordDisinfection,Long>{
	
	Integer countRecordDisinfection(Map<String, Object> condition);

	List<RecordDisinfection> findRecordDisinfection(
			Map<String, Object> condition, RowBounds rowBounds);

}
