package com.otec.foodsafety.service.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.otec.foodsafety.service.system.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysRole;
import com.otec.foodsafety.entity.system.SysRoleMenu;
import com.otec.foodsafety.entity.system.SysRoleUser;
import com.otec.foodsafety.mapper.system.SysRoleMapper;
import com.otec.foodsafety.mapper.system.SysRoleMenuMapper;
import com.otec.foodsafety.mapper.system.SysRoleUserMapper;

@Service
@Transactional
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole, Long>
		implements SysRoleService {

	@Autowired
	private SysRoleMenuMapper sRoleMenuMapper;
	@Autowired
	private SysRoleUserMapper sRoleUserMapper;
	@Autowired
	private SysRoleMapper sRoleMapper;

	@Autowired
	public void setMapper(SysRoleMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public List<SysRoleMenu> selectRoleMenuByCondition(Long roleId) {
		Map<String, Object> condititon = new HashMap<String, Object>();
		condititon.put("roleId", roleId);
		return sRoleMenuMapper.findEntitysByCondition(condititon);
	}

	@Override
	public List<SysRoleUser> selectSUserRoleByCondition(Long roleId) {
		Map<String, Object> condititon = new HashMap<String, Object>();
		condititon.put("roleId", roleId);
		return sRoleUserMapper.findEntitysByCondition(condititon);
	}

	public List<SysRole> selectRolesByUserId(Long userId) {
		return sRoleMapper.selectRolesByUserId(userId);
	}

	public void bindRoleMenu(Long roleId, String[] menuIds) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("roleId", roleId);
		sRoleMenuMapper.deleteByCondition(condition);
		for (int i = 0; i < menuIds.length; i++) {
			SysRoleMenu record = new SysRoleMenu();
			record.setRoleId(roleId);
			record.setMenuId(Long.valueOf(menuIds[i]));
			sRoleMenuMapper.persist(record);
		}
	}

	public void bindRoleUser(Long roleId, String[] userIds) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("roleId", roleId);
		sRoleUserMapper.deleteByCondition(condition);
		for (int i = 0; i < userIds.length; i++) {
			SysRoleUser record = new SysRoleUser();
			record.setRoleId(roleId);
			record.setUserId(Long.valueOf(userIds[i]));
			sRoleUserMapper.persist(record);
		}
	}

	public void deleteRoleByIds(String[] roleIds) {
		for (String roleId : roleIds) {
			// 删除角色
			removeById(Long.parseLong(roleId));
//			Map<String, Object> condition = new HashMap<String, Object>();
//			condition.put("roleId", roleId);
//			// 删除角色和菜单的绑定关系
//			sRoleMenuMapper.deleteByCondition(condition);
//			// 删除角色和用户的绑定关系
//			sRoleUserMapper.deleteByCondition(condition);
		}
	}

	@Override
	public int removeById(Long id) {

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("roleId", id);
		// 删除角色和菜单的绑定关系
//		sRoleMenuMapper.deleteByCondition(condition);
//		// 删除角色和用户的绑定关系
//		sRoleUserMapper.deleteByCondition(condition);
		return sRoleMapper.removeById(id);
	}

	@Override
	public void modifyRoles(Long userId, String roleIds) {
		if (userId != null) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			sRoleUserMapper.deleteByCondition(param);
			if (StringUtils.isNotBlank(roleIds)) {
				String[] roleIdArr = roleIds.split(",");
				for (String roleId : roleIdArr) {
					SysRoleUser roleUser = new SysRoleUser();
					roleUser.setRoleId(Long.valueOf(roleId));
					roleUser.setUserId(userId);
					sRoleUserMapper.persist(roleUser);
				}
			}
		}

	}
	@Override
	public void batchChangeStatus(Long[] ids, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		map.put("status", status);
		sRoleUserMapper.batchChangeStatus( map);
	}

}
