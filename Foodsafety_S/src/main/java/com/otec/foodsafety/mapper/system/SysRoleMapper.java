package com.otec.foodsafety.mapper.system;

import java.util.List;

import org.apache.ibatis.annotation.myibatis.GenericMapper;

import com.otec.foodsafety.entity.system.SysRole;

public interface SysRoleMapper extends GenericMapper<SysRole,Long>{

	public List<SysRole>  selectRolesByUserId(Long userId);
}
