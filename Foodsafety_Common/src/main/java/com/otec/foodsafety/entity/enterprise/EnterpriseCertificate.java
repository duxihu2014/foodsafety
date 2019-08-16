package com.otec.foodsafety.entity.enterprise;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_CERTIFICATE", pkId = "certificateId", seqName = "",notColumn = {"registerAddress","enterpriseName","resourcePath","changeStatus","verifyConclusion","changeId"})
public class EnterpriseCertificate implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long certificateId;
	
	private Long enterpriseId;
	
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
	
	private String certificateStatus;

	private String changeStatus;

	private String resourcePath;

	private String enterpriseName;

	private String registerAddress;  //企业的注册地址

	private String verifyConclusion;	 //退回原因

    private Long changeId;            //变更Id

    public Long getChangeId() {
        return changeId;
    }

    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }

    public String getVerifyConclusion() {
        return verifyConclusion;
    }

    public void setVerifyConclusion(String verifyConclusion) {
        this.verifyConclusion = verifyConclusion;
    }

    public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
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

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}
	 
	public String getCertificateStatus() {
		return this.certificateStatus;
	}

	public String getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}
}