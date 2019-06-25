package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;

@Table(pkId = "roleMenuId", usingSeq = true, dataBase = DataBase.MYSQL, tabName = "T_S_ROLE_MENU", seqName = "")
public class SysRoleMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2088878484963884499L;

	//
	private Long roleMenuId;

	//
	private Long roleId;

	//
	private Long menuId;

	public Long getRoleMenuId() {
		return this.roleMenuId;
	}

	public void setRoleMenuId(Long roleMenuId) {
		this.roleMenuId = roleMenuId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
}
