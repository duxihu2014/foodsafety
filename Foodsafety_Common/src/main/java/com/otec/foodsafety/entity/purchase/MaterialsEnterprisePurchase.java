package com.otec.foodsafety.entity.purchase;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 企业采购记录表
 * 
 * @author Administrator
 *
 */
@Table(pkId = "purchaseId", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_MATERIALS_ENTERPRISE_PURCHASE", notColumn = {"inspectionReportId",
		"inspectionReportFlag", "inspectionReportPhoto", "validDate","inspectionReportPhotoPath" })
public class MaterialsEnterprisePurchase implements Serializable {

	// 采购编号
	private Integer purchaseId;

	//
	private Integer enterpriseId;

	// 供货商的编号
	private Integer supplierId;

	// 供货商的名称
	private String supplierName;

	// 供货商的营业执照编码
	private String businessLicenceNoSupplier;

	// 原材料的编号
	private Integer materialId;

	// 原材料的名称
	private String materialName;

	// 原材料的类型(1 预包装食品、2散装食品、3食品相关产品、4食品添加剂、5农产品和其他产品)
	private String materialCategory;

	// 原材料的条码，散装食品和食用农产品没有条码，需要自己编写条码。
	private String barcodeCoding;

	// 原材料的规格
	private String specification;

	// 原材料计量单位
	private String unit;

	// 原材料的保质期(单位：天)
	private Integer shelfLife;

	// 原材料的产地
	private String originPlace;

	// 原材料的品牌
	private String brand;

	// 原材料的生产企业名称
	private String productionName;

	// 原材料生产企业的生产许可证编号
	private String productionCertificateNo;

	// 原材料的生产日期
	private Date productionDate;

	// 原材料的采购日期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date purchaseDate;

	// 原材料的采购数量
	private Long purchaseQuantity;

	// extends 质检报告相关：
	// 质检报告编号
	private boolean inspectionReportFlag;
	//报表ID
	private Integer inspectionReportId;
	// 质检报告图片
	private String inspectionReportPhoto;



	// 质检有效期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date validDate;
	// 质检报告图片访问路径
	private String inspectionReportPhotoPath;

	public String getInspectionReportPhotoPath() {
		return inspectionReportPhotoPath;
	}

	public void setInspectionReportPhotoPath(String inspectionReportPhotoPath) {
		this.inspectionReportPhotoPath = inspectionReportPhotoPath;
	}

	public Integer getPurchaseId() {
		return this.purchaseId;
	}

	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Integer getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName == null ? null : supplierName.trim();
	}

	public String getBusinessLicenceNoSupplier() {
		return this.businessLicenceNoSupplier;
	}

	public void setBusinessLicenceNoSupplier(String businessLicenceNoSupplier) {
		this.businessLicenceNoSupplier = businessLicenceNoSupplier == null ? null : businessLicenceNoSupplier.trim();
	}

	public Integer getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName == null ? null : materialName.trim();
	}

	public String getMaterialCategory() {
		return this.materialCategory;
	}

	public void setMaterialCategory(String materialCategory) {
		this.materialCategory = materialCategory == null ? null : materialCategory.trim();
	}

	public String getBarcodeCoding() {
		return this.barcodeCoding;
	}

	public void setBarcodeCoding(String barcodeCoding) {
		this.barcodeCoding = barcodeCoding == null ? null : barcodeCoding.trim();
	}

	public String getSpecification() {
		return this.specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification == null ? null : specification.trim();
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit == null ? null : unit.trim();
	}

	public Integer getShelfLife() {
		return this.shelfLife;
	}

	public void setShelfLife(Integer shelfLife) {
		this.shelfLife = shelfLife;
	}

	public String getOriginPlace() {
		return this.originPlace;
	}

	public void setOriginPlace(String originPlace) {
		this.originPlace = originPlace == null ? null : originPlace.trim();
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand == null ? null : brand.trim();
	}	

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public String getProductionCertificateNo() {
		return productionCertificateNo;
	}

	public void setProductionCertificateNo(String productionCertificateNo) {
		this.productionCertificateNo = productionCertificateNo;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Long getPurchaseQuantity() {
		return this.purchaseQuantity;
	}

	public void setPurchaseQuantity(Long purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	public String getInspectionReportPhoto() {
		return inspectionReportPhoto;
	}

	public void setInspectionReportPhoto(String inspectionReportPhoto) {
		this.inspectionReportPhoto = inspectionReportPhoto;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public boolean isInspectionReportFlag() {
		return inspectionReportFlag;
	}

	public void setInspectionReportFlag(boolean inspectionReportFlag) {
		this.inspectionReportFlag = inspectionReportFlag;
	}

	public Integer getInspectionReportId() {
		return inspectionReportId;
	}

	public void setInspectionReportId(Integer inspectionReportId) {
		this.inspectionReportId = inspectionReportId;
	}

}
