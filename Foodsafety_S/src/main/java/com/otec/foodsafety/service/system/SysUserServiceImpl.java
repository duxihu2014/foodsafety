package com.otec.foodsafety.service.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.otec.foodsafety.entity.system.SysUserEnterprise;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import com.otec.foodsafety.mapper.system.SysUserEnterpriseMapper;
import com.otec.foodsafety.mapper.system.SysUserInstitutionMapper;
import com.otec.foodsafety.service.system.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysRoleUser;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.mapper.system.SysRoleUserMapper;
import com.otec.foodsafety.mapper.system.SysUserMapper;
import com.otec.foodsafety.util.MD5Encoder;

@Service
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, Long>
		implements SysUserService {

	@Autowired
	private SysRoleUserMapper roleUsermapper;
	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private SysUserInstitutionMapper sysUserInstitutionMapper;

	@Autowired
	private SysUserEnterpriseMapper sysUserEnterpriseMapper;

	@Autowired
	public void setMapper(SysUserMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public void persist(SysUser record) {
		record.setUserStatus("1");
//		record.setUserType(1);
		record.setCreateTime(new Date());
		// record.setUpdateTime(new Date());
		record.setUserPwd(MD5Encoder.encode(record.getUserPwd()));
		record.setUserType("1");
		super.persist(record);
	}

	@Override
	public int updateById(SysUser record) {
		record.setUpdateTime(new Date());
		int flag = super.updateById(record);
		return flag;
	}

    @Override
    public boolean checkUserPwd(SysUser record) {
        SysUser sysUser = super.findById(record.getUserId());
        if (sysUser.getUserPwd().equals(MD5Encoder.encode(record.getUserPwd_old())))
            return true;
        else
            return false;
    }

    @Override
	public void updateUserPwd(SysUser record) {
        SysUser sysUser = super.findById(record.getUserId());
        sysUser.setUserPwd(MD5Encoder.encode(record.getUserPwd()));
        sysUser.setUpdateTime(new Date());
		super.mergeById(sysUser);
	}

	public void deleteUserByIds(String[] userIds) {
		for (String userId : userIds) {
			// 删除用户
			removeById(Long.parseLong(userId));
			// 删除用户和角色的绑定关系
//			Map<String, Object> condition = new HashMap<String, Object>();
//			condition.put("userId", userId);
//			roleUsermapper.deleteByCondition(condition);
		}

	}

	@Override
	public List<SysUser> findUsersByRoleId(Long roleId) {

		return userMapper.findUsersByRoleId(roleId);
	}

	@Override
	public void modifyUsers(Long roleId, String userIds) {
		if (roleId != null) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("roleId", roleId);
			roleUsermapper.deleteByCondition(param);
			if (StringUtils.isNotBlank(userIds)) {
				String[] userIdArr = userIds.split(",");
				for (String userId : userIdArr) {
					SysRoleUser roleUser = new SysRoleUser();
					roleUser.setRoleId(roleId);
					roleUser.setUserId(Long.valueOf(userId));
					roleUsermapper.persist(roleUser);
				}
			}
		}

	}

	@Override
	public void batchChangeStatus(Long[] ids, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		map.put("status", status);
		userMapper.batchChangeStatus( map);
	}

	@Override
	public void add(SysUser sysUser) {
		userMapper.persist(sysUser);
		if((sysUser.getUserType().equals("2")||sysUser.getUserType().equals("3")) && sysUser.getInstitutionId()>0){
			SysUserInstitution userInstitution = new SysUserInstitution();
			userInstitution.setInstitutionId(Long.valueOf(sysUser.getInstitutionId()));
			userInstitution.setUserId(sysUser.getUserId());
			userInstitution.setAreaId(sysUser.getAreaId());
			sysUserInstitutionMapper.persist(userInstitution);
		}
		if(sysUser.getUserType().equals("4") &&  sysUser.getEnterpriseId()>0){
			SysUserEnterprise userEnterprise=new SysUserEnterprise();
			userEnterprise.setAreaId(sysUser.getAreaId());
			userEnterprise.setEnterpriseId(sysUser.getEnterpriseId());
			userEnterprise.setUserId(sysUser.getUserId());
			sysUserEnterpriseMapper.persist(userEnterprise);
		}
		SysRoleUser roleUser = new SysRoleUser();
		roleUser.setRoleId(Long.valueOf(sysUser.getRoleId()));
		roleUser.setUserId(sysUser.getUserId());
		roleUsermapper.persist(roleUser);

	}

	@Override
	public List<SysUser> getUserPage(Map map, int start, int pageSize) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return userMapper.getUserPage(map,rowBounds);
	}

	@Override
	public Integer getUserPageCount(Map map) { return  userMapper.getUserPageCount(map); }

	@Override
	public SysUser findByUserId(Long userId) {
		return userMapper.findByUserId(userId);
	}

	@Override
	public void update(SysUser sysUser) {
		if((sysUser.getUserType().equals("2")||sysUser.getUserType().equals("3")) && sysUser.getInstitutionId()>0){
			SysUserInstitution userInstitution = new SysUserInstitution();
			userInstitution.setInstitutionId(Long.valueOf(sysUser.getInstitutionId()));
			userInstitution.setUserId(sysUser.getUserId());
			userInstitution.setAreaId(sysUser.getAreaId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", sysUser.getUserId());
			List<SysUserInstitution> sysDeptUserList=sysUserInstitutionMapper.findEntitysByCondition(param);
			if(sysDeptUserList.size()>0){
				userInstitution.setUserInstitutionId(sysDeptUserList.get(0).getUserInstitutionId());
				sysUserInstitutionMapper.mergeById(userInstitution);

			}else{
				sysUserInstitutionMapper.persist(userInstitution);
			}
		}else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", sysUser.getUserId());
			sysUserInstitutionMapper.deleteByCondition(param);
		}

		if(sysUser.getUserType().equals("4") && sysUser.getEnterpriseId()>0){
			SysUserEnterprise userEnterprise=new SysUserEnterprise();
			userEnterprise.setAreaId(sysUser.getAreaId());
			userEnterprise.setEnterpriseId(sysUser.getEnterpriseId());
			userEnterprise.setUserId(sysUser.getUserId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", sysUser.getUserId());
			List<SysUserEnterprise> sysEnterUserList=sysUserEnterpriseMapper.findEntitysByCondition(param);
			if(sysEnterUserList.size()>0){
				userEnterprise.setUserEnterpriseId(sysEnterUserList.get(0).getUserEnterpriseId());
				sysUserEnterpriseMapper.mergeById(userEnterprise);

			}else{
				sysUserEnterpriseMapper.persist(userEnterprise);
			}
		}else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", sysUser.getUserId());
			sysUserEnterpriseMapper.deleteByCondition(param);
		}


		if(sysUser.getRoleId()>0){
			SysRoleUser roleUser = new SysRoleUser();
			roleUser.setRoleId(Long.valueOf(sysUser.getRoleId()));
			roleUser.setUserId(sysUser.getUserId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", sysUser.getUserId());
			List<SysRoleUser> sysRoleUserList=roleUsermapper.findEntitysByCondition(param);
			if(sysRoleUserList.size()>0){
				if(!String.valueOf(sysRoleUserList.get(0).getRoleId()).equals(sysUser.getRoleId())){
					roleUser.setRoleUserId(sysRoleUserList.get(0).getRoleUserId());
					roleUsermapper.mergeById(roleUser);
				}
			}else{
				roleUsermapper.persist(roleUser);
			}
		}else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", sysUser.getUserId());
			roleUsermapper.deleteByCondition(param);
		}
		userMapper.mergeById(sysUser);

	}


}
