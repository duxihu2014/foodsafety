package com.otec.foodsafety.entity.delivery;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_SUPPLY_OBJECT", pkId = "supplyObjectId", seqName = "", notColumn = {"companyName","enterpriseSupplyObjectId"})
public class SupplyObject implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long supplyObjectId;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private long areaId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date registerDate;
	
	private String registerAddress;
	
	private String productionAddress;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date operatingTerm;
	
	private String organizingInstitutionBarCode;
	
	private String subjectClassification;
	
	private String corporateRepresentative;
	
	private String idCardNo;
	
	private String registeredCapital;
	
	private String economicNature;
	
	private String operationScope;
	
	private String postalCode;
	
	private String email;
	
	private String fax;
	
	private String contacts;
	
	private String contactNumber;
	
	private String contactMobile;
	
	private String latitude;
	
	private String longitude;

	private String companyName;
	private String enterpriseSupplyObjectId;
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEnterpriseSupplyObjectId() {
		return enterpriseSupplyObjectId;
	}

	public void setEnterpriseSupplyObjectId(String enterpriseSupplyObjectId) {
		this.enterpriseSupplyObjectId = enterpriseSupplyObjectId;
	}

	public void setSupplyObjectId(long supplyObjectId) {
		this.supplyObjectId = supplyObjectId;
	}
	 
	public long getSupplyObjectId() {
		return this.supplyObjectId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public Long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	 
	public String getEnterpriseName() {
		return this.enterpriseName;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	 
	public long getAreaId() {
		return this.areaId;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	 
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	 
	public String getRegisterAddress() {
		return this.registerAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}
	 
	public String getProductionAddress() {
		return this.productionAddress;
	}

	public void setOperatingTerm(Date operatingTerm) {
		this.operatingTerm = operatingTerm;
	}
	 
	public Date getOperatingTerm() {
		return this.operatingTerm;
	}

	public void setOrganizingInstitutionBarCode(String organizingInstitutionBarCode) {
		this.organizingInstitutionBarCode = organizingInstitutionBarCode;
	}
	 
	public String getOrganizingInstitutionBarCode() {
		return this.organizingInstitutionBarCode;
	}

	public void setSubjectClassification(String subjectClassification) {
		this.subjectClassification = subjectClassification;
	}
	 
	public String getSubjectClassification() {
		return this.subjectClassification;
	}

	public void setCorporateRepresentative(String corporateRepresentative) {
		this.corporateRepresentative = corporateRepresentative;
	}
	 
	public String getCorporateRepresentative() {
		return this.corporateRepresentative;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	 
	public String getIdCardNo() {
		return this.idCardNo;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}
	 
	public String getRegisteredCapital() {
		return this.registeredCapital;
	}

	public void setEconomicNature(String economicNature) {
		this.economicNature = economicNature;
	}
	 
	public String getEconomicNature() {
		return this.economicNature;
	}

	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
	}
	 
	public String getOperationScope() {
		return this.operationScope;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	 
	public String getPostalCode() {
		return this.postalCode;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	 
	public String getEmail() {
		return this.email;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	 
	public String getFax() {
		return this.fax;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	 
	public String getContacts() {
		return this.contacts;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	 
	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	 
	public String getContactMobile() {
		return this.contactMobile;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	 
	public String getLatitude() {
		return this.latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	 
	public String getLongitude() {
		return this.longitude;
	}


}