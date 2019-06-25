package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(pkId = "menuId", usingSeq = true, dataBase = DataBase.MYSQL, tabName = "T_S_MENU", seqName = "", notColumn = {
		"children", "parentName" })
public class SysMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1477424495390058816L;

	//
	private Long menuId;

	//
	private String menuName;

	//
	private String menuDesc;

	//
	private String menuUrl;

	//
	private Long menuPid;

	//
	private Integer menuType;

	//
	private Integer menuStatus;

	//
	private Integer menuLevel;

	//
	private String menuIcon;

	//
	private Date createTime;

	//
	private Date updateTime;

	//
	private Integer menuOrder;

	private List<SysMenu> children;

	private String parentName;

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName == null ? null : menuName.trim();
	}

	public String getMenuDesc() {
		return this.menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc == null ? null : menuDesc.trim();
	}

	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl == null ? null : menuUrl.trim();
	}

	public Long getMenuPid() {
		return this.menuPid;
	}

	public void setMenuPid(Long menuPid) {
		this.menuPid = menuPid;
	}

	public Integer getMenuType() {
		return this.menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getMenuStatus() {
		return this.menuStatus;
	}

	public void setMenuStatus(Integer menuStatus) {
		this.menuStatus = menuStatus;
	}

	public Integer getMenuLevel() {
		return this.menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getMenuIcon() {
		return this.menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon == null ? null : menuIcon.trim();
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getMenuOrder() {
		return this.menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	
}
