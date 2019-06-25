package com.otec.foodsafety.service.impl.realm;

import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysMenu;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.entity.system.SysUserEnterprise;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.service.system.SysMenuService;
import com.otec.foodsafety.service.system.SysUserEnterpriseService;
import com.otec.foodsafety.service.system.SysUserInstitutionService;
import com.otec.foodsafety.service.system.SysUserService;
import com.otec.foodsafety.util.JwtTokenUtil;
import com.otec.foodsafety.util.MD5Encoder;
import com.otec.foodsafety.util.vo.FrontUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	@Autowired
    SysUserService sysUserService;
	@Autowired
    JwtTokenUtil jwtTokenUtil;
	@Autowired
    SysMenuService menuService;
	@Autowired
    SysUserInstitutionService sysUserInstitutionService;
	@Autowired
    SysUserEnterpriseService sysUserEnterpriseService;
	@Autowired
	PersonnelThreeService personnelThreeService;

	@Override
	public String login(String userName, String password) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", userName);
		param.put("userStatus", "1");
		List<SysUser> lst = sysUserService.findEntitysByCondition(param);
		String token = "";
		if (null != lst && lst.size() > 0) {
			SysUser info = lst.get(0);
//			System.out.println(MD5Encoder.encode(password));
//			System.out.println(info.getUserPwd());
			if (MD5Encoder.encode(password).equals(info.getUserPwd())) {
				token = jwtTokenUtil.generateToken(info);
			}
		}
		return token;
	}

	public SysUser getUser(String userName) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", userName);
		param.put("userStatus", "1");
		List<SysUser> lst = sysUserService.findEntitysByCondition(param);
		if (null != lst && lst.size() > 0) {
			SysUser user = lst.get(0);
            param.clear();
            param.put("userId", user.getUserId());
            // 查询机构信息
			if ("2".equals(user.getUserType()) || "3".equals(user.getUserType())) {
                List<SysUserInstitution> lst_institution = sysUserInstitutionService.findEntitysByCondition(param);
                if (null != lst_institution && lst_institution.size() > 0) {
                    SysUserInstitution userInstitution = lst_institution.get(0);
                    user.setInstitutionId(userInstitution.getInstitutionId());
                    user.setAreaId(userInstitution.getAreaId());
                }
            }
            //查询三员信息
			if ("3".equals(user.getUserType())) {
				List<PersonnelThree> pt = personnelThreeService.findEntitysByCondition(param);
				if (pt.size() > 0) {
					PersonnelThree personnelThree = pt.get(0);
					user.setPersonnelId(personnelThree.getPersonnelId());
				}
			}
            // 查询企业信息
            if ("4".equals(user.getUserType())) {
                List<SysUserEnterprise> lst_enterprise = sysUserEnterpriseService.findEntitysByCondition(param);
                if (null != lst_enterprise && lst_enterprise.size() > 0) {
                    SysUserEnterprise userEnterprise = lst_enterprise.get(0);
                    user.setEnterpriseId(userEnterprise.getEnterpriseId());
                    user.setAreaId(userEnterprise.getAreaId());
                }
			}
            return user;
		} else
			return null;
	}

	@Override
	public FrontUser getUserInfo(String token) {
		String username = jwtTokenUtil.getUsernameFromToken(token);
		if (username == null) {
			return null;
		}

		SysUser user = getUser(username);

		FrontUser frontUser = new FrontUser();
		BeanUtils.copyProperties(user, frontUser);

		List<SysMenu> permissionInfos = menuService
				.getPermissionByUsername(username);

		List<SysMenu> menus = new ArrayList<SysMenu>();

		for (SysMenu per : permissionInfos) {
			if (per.getMenuLevel() < 3) {
				menus.add(per);
			}
		}
		frontUser.setMenus(menus);

		return frontUser;
	}

	@Override
	public void invalid(String token) {
		jwtTokenUtil.invalid(token);

	}

	@Override
	public String loginSupervise(String username, String password) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", username);
		param.put("userStatus", "1");
		param.put("userType","3");
		List<SysUser> lst = sysUserService.findEntitysByCondition(param);
		String token = "";
		if (null != lst && lst.size() > 0) {
			SysUser info = lst.get(0);
//			System.out.println(MD5Encoder.encode(password));
//			System.out.println(info.getUserPwd());
			if (MD5Encoder.encode(password).equals(info.getUserPwd())) {
				token = jwtTokenUtil.generateToken(info);
			}
		}
		return token;
	}

	@Override
	public String loginEnterprise(String username, String password) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", username);
		param.put("userStatus", "1");
		param.put("userType","4");

		List<SysUser> lst = sysUserService.findEntitysByCondition(param);
		String token = "";
		if (null != lst && lst.size() > 0) {
			SysUser info = lst.get(0);
//			System.out.println(MD5Encoder.encode(password));
//			System.out.println(info.getUserPwd());
			if (MD5Encoder.encode(password).equals(info.getUserPwd())) {
				token = jwtTokenUtil.generateToken(info);
			}
		}
		return token;
	}
}
