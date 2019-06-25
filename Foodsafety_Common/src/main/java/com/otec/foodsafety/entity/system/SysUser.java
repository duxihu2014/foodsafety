package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Table(dataBase = DataBase.MYSQL, tabName = "T_S_USER", pkId = "userId", seqName = "",notColumn = {"personnelId","enterpriseName","institutionName","institutionId","roleId","userPwd_old","areaId","enterpriseId"})
public class SysUser implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long userId;
	
	private String userName;

    private String userPwd;

	private String userPwd_old;
	
	private String userType;
	
	private String realName;
	
	private String sex;
	
	private String mobile;
	
	private String email;
	
	private String wechat;

	private long roleId;

	private Long areaId;

	private Long institutionId;

	private Long enterpriseId;

	private String enterpriseName;
	private String institutionName;
	private long personnelId;//三员编号

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;
	
	private String loginIp;


	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date updateTime;
	
	private String userStatus;

	public long getPersonnelId() {
		return personnelId;
	}

	public void setPersonnelId(long personnelId) {
		this.personnelId = personnelId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	 
	public long getUserId() {
		return this.userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	 
	public String getUserName() {
		return this.userName;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	 
	public String getUserPwd() {
		return this.userPwd;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	 
	public String getUserType() {
		return this.userType;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	 
	public String getRealName() {
		return this.realName;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	 
	public String getSex() {
		return this.sex;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	 
	public String getMobile() {
		return this.mobile;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	 
	public String getEmail() {
		return this.email;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	 
	public String getWechat() {
		return this.wechat;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	 
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	 
	public String getLoginIp() {
		return this.loginIp;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	 
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	 
	public String getUserStatus() {
		return this.userStatus;
	}

    public String getUserPwd_old() {
        return userPwd_old;
    }

    public void setUserPwd_old(String userPwd_old) {
        this.userPwd_old = userPwd_old;
    }
	public long getRoleId() { return roleId; }

	public void setRoleId(long roleId) { this.roleId = roleId; }

	public Long getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Long institutionId) {
		this.institutionId = institutionId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
}