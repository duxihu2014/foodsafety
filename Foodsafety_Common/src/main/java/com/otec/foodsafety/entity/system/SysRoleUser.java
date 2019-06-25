package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;

@Table(pkId = "roleUserId", usingSeq = true, dataBase = DataBase.MYSQL, tabName = "T_S_ROLE_USER", seqName = "")
public class SysRoleUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1160097577626992986L;

	//
	private Long roleUserId;

	//
	private Long userId;

	//
	private Long roleId;

	public Long getRoleUserId() {
		return this.roleUserId;
	}

	public void setRoleUserId(Long roleUserId) {
		this.roleUserId = roleUserId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
