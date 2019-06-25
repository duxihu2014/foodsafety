package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 企业产品_变更 实体
 * 
 * @author Administrator
 *
 */
@Table(pkId = "changeId", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_PRODUCT_CHANGE")
public class EnterpriseProductChange implements Serializable {

	// 变更编号
	private Integer changeId;

	// 产品编号
	private Long productId;

	// 产品名称
	private String productName;

	// 产品分类----
//	private String productType;
//	产品类型父类
	private Long productTypePId;
//	产品类型子类
	private Long productTypeId;

	// 企业编号
	private Long enterpriseId;

	// 生产许可证编号
	private String licenseNo;

	// 生产许可证的发证机构
	private String issuingInstitution;

	// 生产许可证的证书状态 1正常 0失效
	private String certificateState;

	// 生产许可证的发证日期
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date issuingDate;

	// 生产许可证的有效期
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date validDate;

////	//产品的生产状态 1正常 0停产
//	private String productionState; 

	// 产品的规格型号
	private String specificationModel;

	// 产品的执行标准
	private String productStandard;

	// 产品执行标准的分类
	private String standardType;

	// 产品的注册商标
	private String registeredTrademark;

	// 产品的出厂检验方式 1自检 2免检
	private String factoryInspection;

	// 销售区域
	private String salesArea;

	// 是否为出口产品 1 是 2 否
	private String isExport;

	// 产品图片
	private Long productPicture;

	// 产品的信息类型 国家级名牌、省级名牌
	private String infoType;

	// 审批状态
	private String verifyStatus;

	// 变更的操作类型(新增、修改、删除)
	private String changeType;

	public Integer getChangeId() {
		return this.changeId;
	}

	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}

	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName == null ? null : productName.trim();
	}

	public Long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo == null ? null : licenseNo.trim();
	}

	public String getIssuingInstitution() {
		return this.issuingInstitution;
	}

	public void setIssuingInstitution(String issuingInstitution) {
		this.issuingInstitution = issuingInstitution == null ? null : issuingInstitution.trim();
	}

	public String getCertificateState() {
		return this.certificateState;
	}

	public void setCertificateState(String certificateState) {
		this.certificateState = certificateState == null ? null : certificateState.trim();
	}

	public Date getIssuingDate() {
		return this.issuingDate;
	}

	public void setIssuingDate(Date issuingDate) {
		this.issuingDate = issuingDate;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public String getSpecificationModel() {
		return this.specificationModel;
	}

	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel == null ? null : specificationModel.trim();
	}

	public String getProductStandard() {
		return this.productStandard;
	}

	public void setProductStandard(String productStandard) {
		this.productStandard = productStandard == null ? null : productStandard.trim();
	}

	public String getStandardType() {
		return this.standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType == null ? null : standardType.trim();
	}

	public String getRegisteredTrademark() {
		return this.registeredTrademark;
	}

	public void setRegisteredTrademark(String registeredTrademark) {
		this.registeredTrademark = registeredTrademark == null ? null : registeredTrademark.trim();
	}

	public String getFactoryInspection() {
		return this.factoryInspection;
	}

	public void setFactoryInspection(String factoryInspection) {
		this.factoryInspection = factoryInspection == null ? null : factoryInspection.trim();
	}

	public String getSalesArea() {
		return this.salesArea;
	}

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea == null ? null : salesArea.trim();
	}

	public String getIsExport() {
		return this.isExport;
	}

	public void setIsExport(String isExport) {
		this.isExport = isExport == null ? null : isExport.trim();
	}

	public Long getProductPicture() {
		return this.productPicture;
	}

	public void setProductPicture(Long productPicture) {
		this.productPicture = productPicture;
	}

	public String getInfoType() {
		return this.infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType == null ? null : infoType.trim();
	}

	public String getVerifyStatus() {
		return this.verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus == null ? null : verifyStatus.trim();
	}

	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType == null ? null : changeType.trim();
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
