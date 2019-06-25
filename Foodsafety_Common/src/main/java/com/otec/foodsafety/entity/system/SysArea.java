package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author yy.w
 *
 */
@Table(pkId = "areaId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_S_AREA",notColumn = {"parentName"})
public class SysArea implements Serializable{

	//地区编号
	private Long areaId; 

	//地区名称
	private String areaName; 

	//地区编码
	private String areaCode; 

	//行政级别
	private String administrativeLevel; 

	//上级地区编号
	private Long pAreaId; 

	//记录状态   1:有效，2:无效
	private String areaStatus; 

	//排序编号
	private Integer sortId; 

	//创建时间
	private Date createTime; 

	//创建工号
	private Integer createUserId; 

	//修改时间
	private Date updateTime; 

	//修改工号
	private Integer updateUserId; 

	//备注
	private String comments; 

    //extend field
	private String parentName;	//上级行政区域的名称

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAdministrativeLevel() {
		return administrativeLevel;
	}

	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
	}

	public Long getpAreaId() {
		return pAreaId;
	}

	public void setpAreaId(Long pAreaId) {
		this.pAreaId = pAreaId;
	}

	public String getAreaStatus() {
		return areaStatus;
	}

	public void setAreaStatus(String areaStatus) {
		this.areaStatus = areaStatus;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments == null ? null : comments.trim();
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName == null ? null : parentName.trim();;
	}
	
}
