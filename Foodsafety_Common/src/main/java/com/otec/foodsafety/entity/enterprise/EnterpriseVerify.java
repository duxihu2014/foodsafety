package com.otec.foodsafety.entity.enterprise;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_VERIFY", pkId = "verifyId", seqName = "",notColumn = {"enterpriseName","organizingInstitutionBarCode","changeUserName"})
public class EnterpriseVerify implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long verifyId;
	//	1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
	private String dataType;
//	操作类型  1.新增；2.修改；3.删除
	private String changeType;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date changeTime;
	
	private long changeUserId;
	
	private String changeReason;
	
	private long changeId;
	
	private long enterpriseId;
	
	private String verifyStatus;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date verifyTime;
	
	private Long verifyUserId;
	
	private String verifyConclusion;

	private String enterpriseName;

	private String organizingInstitutionBarCode;

	private String changeUserName;
	
	public void setVerifyId(long verifyId) {
		this.verifyId = verifyId;
	}
	 
	public long getVerifyId() {
		return this.verifyId;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	 
	public String getDataType() {
		return this.dataType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	 
	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
	 
	public Date getChangeTime() {
		return this.changeTime;
	}

	public void setChangeUserId(long changeUserId) {
		this.changeUserId = changeUserId;
	}
	 
	public long getChangeUserId() {
		return this.changeUserId;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	 
	public String getChangeReason() {
		return this.changeReason;
	}

	public void setChangeId(long changeId) {
		this.changeId = changeId;
	}
	 
	public long getChangeId() {
		return this.changeId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	 
	public String getVerifyStatus() {
		return this.verifyStatus;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	 
	public Date getVerifyTime() {
		return this.verifyTime;
	}

	public void setVerifyUserId(Long verifyUserId) {
		this.verifyUserId = verifyUserId;
	}
	 
	public Long getVerifyUserId() {
		return this.verifyUserId;
	}

	public void setVerifyConclusion(String verifyConclusion) {
		this.verifyConclusion = verifyConclusion;
	}
	 
	public String getVerifyConclusion() {
		return this.verifyConclusion;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getOrganizingInstitutionBarCode() {
		return organizingInstitutionBarCode;
	}

	public void setOrganizingInstitutionBarCode(String organizingInstitutionBarCode) {
		this.organizingInstitutionBarCode = organizingInstitutionBarCode;
	}

	public String getChangeUserName() {
		return changeUserName;
	}

	public void setChangeUserName(String changeUserName) {
		this.changeUserName = changeUserName;
	}
}