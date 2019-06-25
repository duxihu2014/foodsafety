package com.otec.foodsafety.web.system;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysRole;
import com.otec.foodsafety.entity.system.SysRoleMenu;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.system.SysRoleService;
import com.otec.foodsafety.service.system.SysUserService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 */
@RestController
@RequestMapping("api/admin/role")
public class AdminRoleController extends
        VueBaseController<SysRoleService, SysRole, Long> {

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysRoleService roleService;
	@Autowired
	private SessionFilter sessionFilter;

    @Override
	public ObjectRestResponse<SysRole> add(@RequestBody SysRole entity) {
		SysUser sysUser = sessionFilter.getJWTUser(request);

//		if(sysUser != null && !StringUtils.equalsIgnoreCase(sysUser.getSysType(), "1")){
//			entity.setSysType(sysUser.getSysType());
//		}
		entity.setRoleStatus("1");
		entity.setCreateTime(new Date());
		return super.add(entity);
	}

    @Override
    public ObjectRestResponse<SysRole> update(@RequestBody SysRole entity) {
        entity.setUpdateTime(new Date());
        return super.update(entity);
    }

    @Override
	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		//params.put("orderByClause", " UPDATE_TIME  desc ");
		return super.list(params);
	}

	@RequestMapping("/{id}/users")
	public ObjectRestResponse<SysUser> getUsers(@PathVariable("id") Long roleId) {

		List<SysUser> userList = userService.findUsersByRoleId(roleId);

		return new ObjectRestResponse<SysUser>().rel(true).data(userList);

	}

	@RequestMapping(value = "/{id}/users", method = RequestMethod.POST)
	public ObjectRestResponse<SysUser> modifyUsers(
			@PathVariable("id") Long roleId,
			@RequestParam(name = "userIds", required = false) String userIds) {
		userService.modifyUsers(roleId, userIds);
		return new ObjectRestResponse<SysUser>().rel(true);

	}

	/**
	 * 获取权限
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/{id}/authority", method = RequestMethod.GET)
	public ObjectRestResponse<SysRoleMenu> getMenuAuthority(
			@PathVariable("id") Long roleId) {

		return new ObjectRestResponse<SysRoleMenu>().rel(true).data(
				roleService.selectRoleMenuByCondition(roleId));

	}

	/**
	 * 修改权限
	 * 
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value = "/{id}/authority", method = RequestMethod.POST)
	public ObjectRestResponse<SysRoleMenu> modifyMenuAuthority(
			@PathVariable("id") Long roleId,
			@RequestParam(name = "menuIds", required = false) String menuIds) {
		if (StringUtils.isNotBlank(menuIds)) {
			roleService.bindRoleMenu(roleId, menuIds.split(","));
		}
		return new ObjectRestResponse<SysRoleMenu>().rel(true);

	}

	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<SysUser> removeByIds(@PathVariable String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				//检查角色是否被操作员引用，是否已绑定权限
				
				roleService.deleteRoleByIds(ids.split(","));
			}
			return new ObjectRestResponse<SysUser>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>()
					.rel(false);

			resp.setStatus(500);
			resp.setMessage("该角色有关联用户或菜单，无法删除");
			return resp;

		}
	}
	
	@RequestMapping(value="checkRoleName",method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> checkRoleName(@RequestParam Map<String,String> params){
		String userName = params.get("roleName");
		String userId = params.get("roleId");
				
		Map<String, Object> condititon = new HashMap<String, Object>();
		String extendSql= "role_status = 1 and role_name ='"+userName +"'";
		if(userId!= null) {
			extendSql = extendSql+" and role_id <> "+userId;
		}
		condititon.put("extendSql", extendSql);
		int flag = biz.countByCondition(condititon);
		return new ObjectRestResponse<Boolean>().rel(true).data(flag > 0 ? true : false);
	}


	@RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
	public ObjectRestResponse<Boolean> changeStatus(@RequestParam Long[] ids,@RequestParam String status) {
		try {
			if(ids==null || !(status.equals("1")||status.equals("0"))){
				return new ObjectRestResponse<Boolean>().rel(false);
			}
			if (ids!=null && ids.length!=0) {
				roleService.batchChangeStatus(ids,status);
				return new ObjectRestResponse<Boolean>().rel(true);

			}
			return new ObjectRestResponse<Boolean>().rel(false);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Boolean>().rel(false);
			resp.setStatus(500);
			resp.setMessage("地块删除异常");
			return resp;
		}
	}

}
