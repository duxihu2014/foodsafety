package com.otec.foodsafety.mapper.system;

import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;

import com.otec.foodsafety.entity.system.SysDetail;

public interface SysDetailMapper extends GenericMapper<SysDetail, Long> {

	public void deleteByCondition(Map<String, Object> map);

}
