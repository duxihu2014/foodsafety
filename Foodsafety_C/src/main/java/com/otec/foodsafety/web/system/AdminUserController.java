package com.otec.foodsafety.web.system;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.entity.system.SysRole;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.entity.system.SysUserEnterprise;
import com.otec.foodsafety.service.system.SysInstitutionService;
import com.otec.foodsafety.service.system.SysRoleService;
import com.otec.foodsafety.service.system.SysUserEnterpriseService;
import com.otec.foodsafety.service.system.SysUserService;
import com.otec.foodsafety.util.MD5Encoder;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作员信息管理
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 */
@RestController
@RequestMapping("api/admin/user")
public class AdminUserController extends VueBaseController<SysUserService, SysUser, Long> {
	private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysInstitutionService sysInstitutionService;
	@Autowired
	private SessionFilter sessionFilter;
	@Autowired
	private SysUserEnterpriseService sysUserEnterpriseService;

	@RequestMapping(value = "/updateUserPwd", method = RequestMethod.PUT)
	public ObjectRestResponse<SysUser> updateUserPwd(@RequestBody SysUser sysUser) {
		try {
			if (sysUser.getUserId() > 0) {
				if (sysUserService.checkUserPwd(sysUser)) {
					sysUserService.updateUserPwd(sysUser);
					return new ObjectRestResponse<SysUser>().rel(true);
				} else {
					throw new Exception("原密码不匹配，无法修改密码");
				}
			} else {
				throw new Exception("操作员不存在，无法修改密码");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
			resp.setStatus(500);
			resp.setMessage(e.getMessage());
			return resp;
		}
	}

	@RequestMapping(value = "/resetUserPwd", method = RequestMethod.PUT)
	public ObjectRestResponse<SysUser> resetUserPwd(@RequestBody SysUser sysUser) {
		try {
			if (sysUser.getUserId() > 0) {
				sysUser.setUserPwd(SysInitConfig.getInstance().get(SysInitConfig.CfgProp.DEFAULTPASSWORD));
				sysUserService.updateUserPwd(sysUser);
				return new ObjectRestResponse<SysUser>().rel(true);
			} else {
				throw new Exception("操作员不存在，无法重置密码");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
			resp.setStatus(500);
			resp.setMessage(e.getMessage());
			return resp;
		}
	}

	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<SysUser> removeByIds(@PathVariable String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				sysUserService.deleteUserByIds(ids.split(","));
			}
			return new ObjectRestResponse<SysUser>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
			resp.setStatus(500);
			resp.setMessage("操作员关联角色，无法删除");
			return resp;
		}
	}

	/**
	 * 添加用户
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<SysUser> add(@RequestBody SysUser entity) {
		entity.setUserPwd(MD5Encoder.encode(SysInitConfig.getInstance().get(SysInitConfig.CfgProp.DEFAULTPASSWORD)));
		entity.setCreateTime(new Date());
		entity.setUserStatus("1");
		if (StringUtils.isBlank(entity.getRealName()))
			entity.setRealName(entity.getUserName());
		sysUserService.add(entity);
		return new ObjectRestResponse<SysUser>().rel(true);
	}

	@RequestMapping(value = "checkUserName", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> checkUserName(@RequestParam String userName) {
		Map<String, Object> param = new HashMap<>();
		param.put("userName", userName);
		List<SysUser> sysUsers = sysUserService.findEntitysByCondition(param);
		if (sysUsers.size() > 0) {
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
			return resp;
		} else {
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(true);
			return resp;
		}
		/*
		 * String userName = params.get("userName"); String userId =
		 * params.get("userId");
		 * 
		 * Map<String, Object> condititon = new HashMap<String, Object>(); String
		 * extendSql = "user_status = 1 and user_name ='" + userName + "'"; if (userId
		 * != null) { extendSql = extendSql + " and user_id <> " + userId; }
		 * condititon.put("extendSql", extendSql); int flag =
		 * biz.countByCondition(condititon); return new
		 * ObjectRestResponse<Boolean>().rel(true).data(flag > 0 ? true : false);
		 */
	}

	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		SysUser sysUser = sessionFilter.getJWTUser(request);
//		if (sysUser != null && !StringUtils.equalsIgnoreCase(sysUser.getSysType(), "1")) {
//			params.put("sysType", sysUser.getSysType());
//		}
		return super.list(params);

	}

	@RequestMapping("/{id}/roles")
	public ObjectRestResponse<SysRole> getUsers(@PathVariable("id") Long userId) {

		List<SysRole> roleList = sysRoleService.selectRolesByUserId(userId);

		return new ObjectRestResponse<SysUser>().rel(true).data(roleList);

	}

	/**
	 * 分页查询所有的用户信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getUserList", method = RequestMethod.GET)
	public GridDataModel getUserList(@RequestParam Map<String, String> params) {
		// 查询列表数据
//		Map param=new HashMap();
//		param.put("userNameLike",params.get("userNameLike"));
//		param.put("userStatus",params.get("userStatus"));
		PageObject po = getPageObject(params);
		Integer total = sysUserService.getUserPageCount(params);
		List<SysUser> sysUserList = sysUserService.getUserPage(params, po.getOffset(), po.getPageSize());
		GridDataModel model = new GridDataModel(sysUserList, total);
		return model;
	}

	/**
	 * 根据用户id查询用户信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public ObjectRestResponse<SysUser> get(@PathVariable Long id) {
		SysUser sysUser = sysUserService.findByUserId(id);
		return new ObjectRestResponse<SysUser>().rel(true).data(sysUser);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param sysUser
	 * @return
	 */
	@Override
	public ObjectRestResponse<SysUser> update(@RequestBody SysUser sysUser) {
		try {
			sysUser.setUpdateTime(new Date());
			sysUserService.update(sysUser);
			return new ObjectRestResponse<SysUser>().rel(true);
		} catch (Exception e) {
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
			resp.setStatus(500);
			resp.setMessage("更新失败!");
			return resp;
		}
	}

	@RequestMapping(value = "/{id}/roles", method = RequestMethod.POST)
	public ObjectRestResponse<SysUser> modifyUsers(@PathVariable("id") Long userId,
			@RequestParam(name = "roleIds", required = false) String roleIds) {
		sysRoleService.modifyRoles(userId, roleIds);
		return new ObjectRestResponse<SysUser>().rel(true);
	}

	@RequestMapping("/{id}/depts")
	public ObjectRestResponse<SysUser> getUsersDept(@PathVariable("id") Long userId) {

		List<SysInstitution> deptList = sysInstitutionService.selectInstitutionsByUserId(userId);

		return new ObjectRestResponse<SysUser>().rel(true).data(deptList);

	}

	@RequestMapping(value = "/{id}/depts", method = RequestMethod.POST)
	public ObjectRestResponse<SysUser> modifyUsersDept(@PathVariable("id") Long userId,
			@RequestParam(name = "deptIds", required = false) String deptIds) {
		sysInstitutionService.modifyInstitutions(userId, deptIds);
		return new ObjectRestResponse<SysUser>().rel(true);
	}

	/**
	 * 检查指定公司ID(compID)是否设置了操作员
	 * 
	 * @param ids
	 * @param sysType 平台类型 1：平台
	 * @return 有返回true,反之 false
	 */
	@RequestMapping(value = "checkCompanyBindOperator/{ids}/{sysType}", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> haveRelationOperator(@PathVariable String ids, @PathVariable Integer sysType) {
		Boolean reFlag = false;
		String[] idsArr = ids.split(",");

		Map<String, Object> condititon = new HashMap<String, Object>();
		condititon.put("extendSql", "comp_id in (" + ids + ") and SYS_TYPE= " + sysType + " and user_status = 1");
		int flag = sysUserService.countByCondition(condititon);
		if (flag > 0) {
			reFlag = true;
		}
		return new ObjectRestResponse<Boolean>().rel(true).data(reFlag);
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
	public ObjectRestResponse<SysUser> changeStatus(@RequestParam Long[] ids, @RequestParam String status) {
		try {
			if (ids == null || !(status.equals("1") || status.equals("0"))) {
				return new ObjectRestResponse<SysUser>().rel(false);
			}
			if (ids != null && ids.length != 0) {
				sysUserService.batchChangeStatus(ids, status);
				return new ObjectRestResponse<SysUser>().rel(true);

			}
			return new ObjectRestResponse<SysUser>().rel(false);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
			resp.setStatus(500);
			resp.setMessage("地块删除异常");
			return resp;
		}
	}
	/*根据用户id获取用户企业信息*/
	@RequestMapping(value = "/getUserEnterprise", method = RequestMethod.GET  )
	public ObjectRestResponse<SysUserEnterprise> resetUserPwd(@RequestParam Long userId) {
		Map param = new HashMap();
		param.put("userId",userId);
		List<SysUserEnterprise> sysUserEnterpriseList = sysUserEnterpriseService.findEntitysByCondition(param);
		SysUserEnterprise sysUserEnterprise = sysUserEnterpriseList.get(0);
		return new ObjectRestResponse<SysUserEnterprise>().rel(true).data(sysUserEnterprise);
	}

	/**
	 * 得到所有用户信息 map
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAllUserMap", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String, Object>> getAllUserMap() {

		Map<String, Object> map2 = new HashMap<String, Object>();
		String extendSql = "and user_status = 1 ";
		map2.put("extendSql", extendSql);
		List<SysUser> sysUserList = sysUserService.findEntitysByCondition(map2);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(sysUserList.size());
		for (SysUser sysUser : sysUserList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", String.valueOf(sysUser.getUserId()));
			map.put("text", sysUser.getUserName());
			list.add(map);
		}
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(list);
	}

}
