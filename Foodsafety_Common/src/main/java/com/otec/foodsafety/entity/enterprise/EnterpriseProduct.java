package com.otec.foodsafety.entity.enterprise;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_PRODUCT", pkId = "productId", seqName = "", notColumn = {
		"" })
public class EnterpriseProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productId;

	private String productName;

//	产品类型父类
	private Long productTypePId;
//	产品类型子类
	private Long productTypeId;
//	企业ID
	private Long enterpriseId;

	private String licenseNo;

	private String issuingInstitution;

	private String certificateState;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date issuingDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date validDate;

	private String specificationModel;

	private String productStandard;

	private String standardType;

	private String registeredTrademark;

	private String factoryInspection;

	private String salesArea;

	private String isExport;

	private Long productPicture;

	private String infoType;
//	产品状态暂定为  1正常、0停产
	private String productStatus;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getEnterpriseId() {
		return this.enterpriseId;
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

	public void setCertificateState(String certificateState) {
		this.certificateState = certificateState;
	}

	public String getCertificateState() {
		return this.certificateState;
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

	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}

	public String getSpecificationModel() {
		return this.specificationModel;
	}

	public void setProductStandard(String productStandard) {
		this.productStandard = productStandard;
	}

	public String getProductStandard() {
		return this.productStandard;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public String getStandardType() {
		return this.standardType;
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

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}

	public String getSalesArea() {
		return this.salesArea;
	}

	public void setIsExport(String isExport) {
		this.isExport = isExport;
	}

	public String getIsExport() {
		return this.isExport;
	}

	public void setProductPicture(Long productPicture) {
		this.productPicture = productPicture;
	}

	public Long getProductPicture() {
		return this.productPicture;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoType() {
		return this.infoType;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getProductStatus() {
		return this.productStatus;
	}

	public Long getProductTypePId() {
		return productTypePId;
	}

	public void setProductTypePId(Long productTypePId) {
		this.productTypePId = productTypePId;
	}

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	
}