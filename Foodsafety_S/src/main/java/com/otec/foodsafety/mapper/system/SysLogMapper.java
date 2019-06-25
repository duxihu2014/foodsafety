package com.otec.foodsafety.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.system.SysLog;

public interface SysLogMapper extends GenericMapper<SysLog,Long>{
	
	public List<SysLog> findSysLog(Map<String, Object> param);
	public List<SysLog> findSysLog(Map<String, Object> param, RowBounds rowBounds);
}
