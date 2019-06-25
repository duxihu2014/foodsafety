package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysUser;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface SysUserService extends BaseService<SysUser,Long>{

    boolean checkUserPwd(SysUser record);

	public void updateUserPwd(SysUser record);
	
	public  void deleteUserByIds(String[] userIds);

	/**
	 * 根据角色编号查找用户
	 * @param roleId
	 * @return
	 */
	public List<SysUser> findUsersByRoleId(Long roleId);

	/**
	 * 修改绑定用户
	 * @param roleId
	 * @param userIds
	 */
	public void modifyUsers(Long roleId, String userIds);

	/**
	 * 批量修改用户状态
	 * @param ids
	 * @param status
	 */
	public void batchChangeStatus(Long[] ids, String status);

	/**
	 * 添加用户
	 * @param sysUser
	 */
	public void add(SysUser sysUser);

	public List<SysUser> getUserPage(Map map, int start, int pageSize);

	public Integer getUserPageCount(Map map);

	public SysUser findByUserId(Long userId);

	public void update(SysUser sysUser);
}
