package com.otec.foodsafety.entity.irregularity;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_IRREGULARITY_CASE", pkId = "irregularityId", seqName = "",notColumn = {"complainEvaluateId"})
public class IrregularityCase implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long irregularityId;
	
	private long enterpriseId;
	
	private String enterpriseName;
	
	private long areaId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date registerDate;
	
	private String registerAddress;
	
	private String productionAddress;
	
	private String organizingInstitutionBarCode;
	
	private String subjectClassification;
	
	private String superviseClassification;
	
	private String operationScope;
	
	private long productId;
	
	private String productName;
	
	private Long productType;
	
	private String licenseNo;
	
	private String specificationModel;
	
	private String registeredTrademark;
	
	private long productPicture;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date caseDate;
	
	private String caseCause;
	
	private String address;
	
	private String illegalType;
	
	private String amount;
	
	private String haveCasualty;
	
	private String illegalActivities;
	
	private String illegalFact;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	private long userId;
	
	private String caseStatus; //1已立案  2已处罚  3已结案

	private Integer complainEvaluateId;


	public Integer getComplainEvaluateId() {
		return complainEvaluateId;
	}

	public void setComplainEvaluateId(Integer complainEvaluateId) {
		this.complainEvaluateId = complainEvaluateId;
	}

	public void setIrregularityId(long irregularityId) {
		this.irregularityId = irregularityId;
	}
	 
	public long getIrregularityId() {
		return this.irregularityId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
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

	public void setSuperviseClassification(String superviseClassification) {
		this.superviseClassification = superviseClassification;
	}
	 
	public String getSuperviseClassification() {
		return this.superviseClassification;
	}

	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
	}
	 
	public String getOperationScope() {
		return this.operationScope;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
	 
	public long getProductId() {
		return this.productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	 
	public String getProductName() {
		return this.productName;
	}

	public void setProductType(Long productType) {
		this.productType = productType;
	}
	 
	public Long getProductType() {
		return this.productType;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	 
	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	 
	public String getSpecificationModel() {
		return this.specificationModel;
	}

	public void setRegisteredTrademark(String registeredTrademark) {
		this.registeredTrademark = registeredTrademark;
	}
	 
	public String getRegisteredTrademark() {
		return this.registeredTrademark;
	}

	public void setProductPicture(long productPicture) {
		this.productPicture = productPicture;
	}
	 
	public long getProductPicture() {
		return this.productPicture;
	}

	public void setCaseDate(Date caseDate) {
		this.caseDate = caseDate;
	}
	 
	public Date getCaseDate() {
		return this.caseDate;
	}

	public void setCaseCause(String caseCause) {
		this.caseCause = caseCause;
	}
	 
	public String getCaseCause() {
		return this.caseCause;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	 
	public String getAddress() {
		return this.address;
	}

	public void setIllegalType(String illegalType) {
		this.illegalType = illegalType;
	}
	 
	public String getIllegalType() {
		return this.illegalType;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	 
	public String getAmount() {
		return this.amount;
	}

	public void setHaveCasualty(String haveCasualty) {
		this.haveCasualty = haveCasualty;
	}
	 
	public String getHaveCasualty() {
		return this.haveCasualty;
	}

	public void setIllegalActivities(String illegalActivities) {
		this.illegalActivities = illegalActivities;
	}
	 
	public String getIllegalActivities() {
		return this.illegalActivities;
	}

	public void setIllegalFact(String illegalFact) {
		this.illegalFact = illegalFact;
	}
	 
	public String getIllegalFact() {
		return this.illegalFact;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	 
	public long getUserId() {
		return this.userId;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	 
	public String getCaseStatus() {
		return this.caseStatus;
	}


}