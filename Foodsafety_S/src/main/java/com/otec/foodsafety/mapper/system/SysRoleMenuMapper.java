package com.otec.foodsafety.mapper.system;

import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;

import com.otec.foodsafety.entity.system.SysRoleMenu;

public interface SysRoleMenuMapper extends GenericMapper<SysRoleMenu,Long>{
	
	public int deleteByCondition(Map<String, Object> condition);

}
