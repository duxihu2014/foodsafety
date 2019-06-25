package com.otec.foodsafety.mapper.system;

import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;

import com.otec.foodsafety.entity.system.SysRoleUser;

public interface SysRoleUserMapper extends GenericMapper<SysRoleUser,Long>{
	
	public int deleteByCondition(Map<String, Object> condition);

    void batchChangeStatus(Map<String, Object> map);
}
