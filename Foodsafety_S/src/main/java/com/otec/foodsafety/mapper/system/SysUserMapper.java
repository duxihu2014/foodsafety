package com.otec.foodsafety.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import com.otec.foodsafety.entity.system.SysUser;
import org.apache.ibatis.session.RowBounds;

public interface SysUserMapper extends GenericMapper<SysUser,Long>{

	public List<SysUser> findUsersByRoleId(@Param("roleId") Long roleId);

    void batchChangeStatus(Map<String, Object> map);

    List<SysUser> getUserPage(Map map, RowBounds rowBounds);

    Integer getUserPageCount(Map map);

    SysUser findByUserId(@Param("userId") Long id);
}
