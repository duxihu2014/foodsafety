package com.otec.foodsafety.entity.supervisory;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_PERSONNEL_THREE", pkId = "personnelId", seqName = "",notColumn = {"userName"})
public class PersonnelThree implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long personnelId;
	
	private String personnelName;
	
	private String personnelType;
	
	private String idCardNo;
	
	private String unit;
	
	private String contactNumber;
	
	private String postalAddress;
	
	private long institutionId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	private long areaId;
	
	private long userId;
	
	private String personnelStatus;

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPersonnelId(long personnelId) {
		this.personnelId = personnelId;
	}
	 
	public long getPersonnelId() {
		return this.personnelId;
	}

	public void setPersonnelName(String personnelName) {
		this.personnelName = personnelName;
	}
	 
	public String getPersonnelName() {
		return this.personnelName;
	}

	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}
	 
	public String getPersonnelType() {
		return this.personnelType;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	 
	public String getIdCardNo() {
		return this.idCardNo;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	 
	public String getUnit() {
		return this.unit;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	 
	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}
	 
	public String getPostalAddress() {
		return this.postalAddress;
	}

	public void setInstitutionId(long institutionId) {
		this.institutionId = institutionId;
	}
	 
	public long getInstitutionId() {
		return this.institutionId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	 
	public long getAreaId() {
		return this.areaId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	 
	public long getUserId() {
		return this.userId;
	}

	public void setPersonnelStatus(String personnelStatus) {
		this.personnelStatus = personnelStatus;
	}
	 
	public String getPersonnelStatus() {
		return this.personnelStatus;
	}


}