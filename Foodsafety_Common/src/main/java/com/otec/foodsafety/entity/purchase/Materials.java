package com.otec.foodsafety.entity.purchase;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_MATERIALS", pkId = "materialId", seqName = "", notColumn = {
		"resourcePath", "supplierName", "enterpiseName", "businessLicenceNo" })
public class Materials implements Serializable {

	private static final long serialVersionUID = 1L;

	private long materialId;

	private String materialName;

	private String materialCategory;

	private String barcodeCoding;

	private String specification;

	private String unit;

	private int shelfLife;

	private String originPlace;

	private String brand;

	private long supplierId;

	private String productionCertificateNo;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date productionCertificateValid;

	private Long productionCertificatePhoto;

	private String resourcePath; // 图片路径

	private String materialStatus;// 原料使用状态 1：有效 0：无效

	private String productionName;

	private String supplierName;

	private String enterpiseName;
	// 营业执照号
	private String businessLicenceNo;

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getEnterpiseName() {
		return enterpiseName;
	}

	public void setEnterpiseName(String enterpiseName) {
		this.enterpiseName = enterpiseName;
	}

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public String getMaterialStatus() {
		return materialStatus;
	}

	public void setMaterialStatus(String materialStatus) {
		this.materialStatus = materialStatus;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}

	public long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialCategory(String materialCategory) {
		this.materialCategory = materialCategory;
	}

	public String getMaterialCategory() {
		return this.materialCategory;
	}

	public void setBarcodeCoding(String barcodeCoding) {
		this.barcodeCoding = barcodeCoding;
	}

	public String getBarcodeCoding() {
		return this.barcodeCoding;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getSpecification() {
		return this.specification;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public int getShelfLife() {
		return this.shelfLife;
	}

	public void setOriginPlace(String originPlace) {
		this.originPlace = originPlace;
	}

	public String getOriginPlace() {
		return this.originPlace;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getSupplierId() {
		return this.supplierId;
	}

	public void setProductionCertificateNo(String productionCertificateNo) {
		this.productionCertificateNo = productionCertificateNo;
	}

	public String getProductionCertificateNo() {
		return this.productionCertificateNo;
	}

	public void setProductionCertificateValid(Date productionCertificateValid) {
		this.productionCertificateValid = productionCertificateValid;
	}

	public Date getProductionCertificateValid() {
		return this.productionCertificateValid;
	}

	public void setProductionCertificatePhoto(Long productionCertificatePhoto) {
		this.productionCertificatePhoto = productionCertificatePhoto;
	}

	public Long getProductionCertificatePhoto() {
		return this.productionCertificatePhoto;
	}

	public String getBusinessLicenceNo() {
		return businessLicenceNo;
	}

	public void setBusinessLicenceNo(String businessLicenceNo) {
		this.businessLicenceNo = businessLicenceNo;
	}

}