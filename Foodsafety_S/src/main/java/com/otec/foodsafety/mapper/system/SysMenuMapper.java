package com.otec.foodsafety.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import com.otec.foodsafety.entity.system.SysMenu;

public interface SysMenuMapper extends GenericMapper<SysMenu, Long> {

	public int deleteByCondition(Map<String, Object> condition);

	public List<SysMenu> getAllMenus(Long userId);

	public List<SysMenu> getPermissionByUsername(
            @Param("userName") String username);

	public SysMenu getMenuById(@Param("id") Long id);

	List<SysMenu> getByRoot(String rootId);
}
