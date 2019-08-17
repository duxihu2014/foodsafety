package com.otec.foodsafety.entity.enterprise;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_CERTIFICATE_CHANGE", pkId = "changeId", seqName = "",notColumn = {""})
public class EnterpriseCertificateChange implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long changeId;
	
	private Long certificateId;
	
	private Long enterpriseId;
	
	private String certificateType;
	
	private String certificateName;
	
	private String certificateNo;
	
	private String registerScope;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date issuingDate;
	
	private String issuingUnit;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date validDate;
	
	private long certificatePhoto;

	private String verifyStatus;

	private String changeType;
	
	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}
	 
	public long getChangeId() {
		return this.changeId;
	}

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public Long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	 
	public String getCertificateType() {
		return this.certificateType;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	 
	public String getCertificateName() {
		return this.certificateName;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	 
	public String getCertificateNo() {
		return this.certificateNo;
	}

	public void setRegisterScope(String registerScope) {
		this.registerScope = registerScope;
	}
	 
	public String getRegisterScope() {
		return this.registerScope;
	}

	public void setIssuingDate(Date issuingDate) {
		this.issuingDate = issuingDate;
	}
	 
	public Date getIssuingDate() {
		return this.issuingDate;
	}

	public void setIssuingUnit(String issuingUnit) {
		this.issuingUnit = issuingUnit;
	}
	 
	public String getIssuingUnit() {
		return this.issuingUnit;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	 
	public Date getValidDate() {
		return this.validDate;
	}

	public void setCertificatePhoto(long certificatePhoto) {
		this.certificatePhoto = certificatePhoto;
	}
	 
	public long getCertificatePhoto() {
		return this.certificatePhoto;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
}