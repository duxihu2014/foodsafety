package com.otec.foodsafety.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;

import com.otec.foodsafety.entity.system.SysArea;
import org.apache.ibatis.annotations.Param;

public interface SysAreaMapper extends GenericMapper<SysArea, Long> {
	public int deleteByCondition(Map<String, Object> condition);

	List<SysArea> getParentsByAreaId(@Param(value = "areaId") long areaId);


}
