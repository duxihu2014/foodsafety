package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Table(dataBase = DataBase.MYSQL, tabName = "T_S_ROLE", pkId = "roleId", seqName = "",notColumn = {""})
public class SysRole implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long roleId;
	
	private String roleName;
	
	private String roleDesc;
	
	private String roleStatus;
	
	private String roleType;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date updateTime;


	
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	 
	public long getRoleId() {
		return this.roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	 
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	 
	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}
	 
	public String getRoleStatus() {
		return this.roleStatus;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	 
	public String getRoleType() {
		return this.roleType;
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


}