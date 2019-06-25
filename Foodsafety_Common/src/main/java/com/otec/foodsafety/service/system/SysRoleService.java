package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysRole;
import com.otec.foodsafety.entity.system.SysRoleMenu;
import com.otec.foodsafety.entity.system.SysRoleUser;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface SysRoleService extends BaseService<SysRole,Long>{
	
	public List<SysRoleMenu> selectRoleMenuByCondition(Long roleId);	
	
	public List<SysRoleUser> selectSUserRoleByCondition(Long roleId);	
	
	public void  bindRoleMenu(Long roleId, String[] menuIds);

	public void  bindRoleUser(Long roleId, String[] userIds);
	
	public  void  deleteRoleByIds(String[] roleIds);
	
	public List<SysRole> selectRolesByUserId(Long userId);

	/**
	 * 修改绑定角色
	 */
	void modifyRoles(Long userId, String roleIds);
	/**
	 * 批量修改用户角色
	 * @param ids
	 * @param status
	 */
    void batchChangeStatus(Long[] ids, String status);
}
