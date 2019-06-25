package com.otec.foodsafety.entity.producesafety;

import java.io.Serializable;
import java.util.Date;

public class RecordAdditiveUseageExt extends RecordAdditiveUseage implements Serializable {
	// 企业名称
	private String enterpriseName;
	// 原料编号
	private String materialId;
	// 原料名称
	private String materialName;
	// 供货商编号
	private String supplierId;
	// 供货商名称
	private String supplierName;
	// 添加剂的采购日期
	private Date purchaseDate;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

}
