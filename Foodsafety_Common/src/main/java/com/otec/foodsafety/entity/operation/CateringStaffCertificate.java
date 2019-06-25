package com.otec.foodsafety.entity.operation;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_CATERING_STAFF_CERTIFICATE", pkId = "certificateId", seqName = "",notColumn = {})
public class CateringStaffCertificate implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long certificateId;
	
	private long staffId;
	
	private String certificateNumber;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date examinationDate;  //体检时间
	
	private String examinationResult;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date validDate;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date issuingDate; //发证时间
	
	private String issuingUnit;  //发证机构
	
	private long certificatePhoto;  //健康证照片

	private String certificateStatus;//1.正常 2.过期


	public String getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}
	 
	public long getCertificateId() {
		return this.certificateId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	 
	public long getStaffId() {
		return this.staffId;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	 
	public String getCertificateNumber() {
		return this.certificateNumber;
	}

	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
	}
	 
	public Date getExaminationDate() {
		return this.examinationDate;
	}

	public void setExaminationResult(String examinationResult) {
		this.examinationResult = examinationResult;
	}
	 
	public String getExaminationResult() {
		return this.examinationResult;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	 
	public Date getValidDate() {
		return this.validDate;
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

	public void setCertificatePhoto(long certificatePhoto) {
		this.certificatePhoto = certificatePhoto;
	}
	 
	public long getCertificatePhoto() {
		return this.certificatePhoto;
	}


}