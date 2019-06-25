package com.otec.foodsafety.entity.register;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_REGISTER_BASE", pkId = "registerId", seqName = "",notColumn = {""})
public class RegisterBase implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long registerId;
	
	private String enterpriseName;
	
	private long areaId;
	
	private String registerAddress;
	
	private String productionAddress;
	
	private String organizingInstitutionBarCode;
	
	private String subjectClassification;
	
	private String corporateRepresentative;
	
	private String idCardNo;
	
	private String registeredCapital;
	
	private String economicNature;
	
	private String operationScope;
	
	private String postalCode;
	
	private String contacts;
	
	private String contactNumber;
	
	private String contactMobile;
	
	private String inspectorMobile;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date registerDate;

	public void setRegisterId(long registerId) {
		this.registerId = registerId;
	}
	 
	public long getRegisterId() {
		return this.registerId;
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

	public void setInspectorMobile(String inspectorMobile) {
		this.inspectorMobile = inspectorMobile;
	}
	 
	public String getInspectorMobile() {
		return this.inspectorMobile;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}


}