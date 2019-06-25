package com.otec.foodsafety.entity.register;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_REGISTER_CERTIFICATE", pkId = "registerId", seqName = "",notColumn = {""})
public class RegisterCertificate implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long registerId;
	
	private String certificateType;
	
	private String certificateName;
	
	private String certificateNo;
	
	private String registerScope;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date issuingDate;
	
	private String issuingUnit;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date validDate;
	
	private long certificatePhoto;
	
	public void setRegisterId(long registerId) {
		this.registerId = registerId;
	}
	 
	public long getRegisterId() {
		return this.registerId;
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


}