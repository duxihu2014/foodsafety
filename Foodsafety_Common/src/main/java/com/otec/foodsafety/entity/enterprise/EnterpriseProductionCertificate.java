package com.otec.foodsafety.entity.enterprise;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_PRODUCTION_CERTIFICATE", pkId = "certId", seqName = "",notColumn = {""})
public class EnterpriseProductionCertificate implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long certId;
	
	private long enterpriseId;
	
	private String productName;
	
	private String productType;
	
	private String productStandard;
	
	private String variety;
	
	private String registeredTrademark;
	
	private String factoryInspection;
	
	private String licenseNo;
	
	private String issuingInstitution;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date issuingDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date validDate;
	
	private String licenseStatus;
	
	private String remarks;
	
	public void setCertId(long certId) {
		this.certId = certId;
	}
	 
	public long getCertId() {
		return this.certId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	 
	public String getProductName() {
		return this.productName;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	 
	public String getProductType() {
		return this.productType;
	}

	public void setProductStandard(String productStandard) {
		this.productStandard = productStandard;
	}
	 
	public String getProductStandard() {
		return this.productStandard;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}
	 
	public String getVariety() {
		return this.variety;
	}

	public void setRegisteredTrademark(String registeredTrademark) {
		this.registeredTrademark = registeredTrademark;
	}
	 
	public String getRegisteredTrademark() {
		return this.registeredTrademark;
	}

	public void setFactoryInspection(String factoryInspection) {
		this.factoryInspection = factoryInspection;
	}
	 
	public String getFactoryInspection() {
		return this.factoryInspection;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	 
	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setIssuingInstitution(String issuingInstitution) {
		this.issuingInstitution = issuingInstitution;
	}
	 
	public String getIssuingInstitution() {
		return this.issuingInstitution;
	}

	public void setIssuingDate(Date issuingDate) {
		this.issuingDate = issuingDate;
	}
	 
	public Date getIssuingDate() {
		return this.issuingDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	 
	public Date getValidDate() {
		return this.validDate;
	}

	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}
	 
	public String getLicenseStatus() {
		return this.licenseStatus;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	 
	public String getRemarks() {
		return this.remarks;
	}


}