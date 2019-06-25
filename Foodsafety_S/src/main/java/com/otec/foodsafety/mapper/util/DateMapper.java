package com.otec.foodsafety.mapper.util;

import java.util.Date;

import org.apache.ibatis.annotations.Select;

public interface DateMapper {
	
	@Select("select now()")
	Date getDataBaseDate();
}
