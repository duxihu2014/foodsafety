package com.otec.foodsafety.entity.purchase;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;

/**
 *  质检报告表实体
 * 
 * @author Administrator
 *
 */
@Table(pkId = "inspectionReportId", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_MATERIALS_INSPECTION_REPORT", notColumn = {
		"supplierName" })
public class MaterialsInspectionReport implements Serializable {

	// 原材料的出厂检验报告的编号
	private Integer inspectionReportId;

	// 原材料的出厂检验报告的照片
	private Integer inspectionReportPhoto;

	//
	private Integer materialId;

	// 原材料的名称
	private String materialName;

	// 原材料的类型(预包装食品、散装食品、食品相关产品、食品添加剂、农产品和其他产品)
	private String materialCategory;

	// 原材料的条码，散装食品和食用农产品没有条码，需要自己编写条码。
	private String barcodeCoding;

	// 原材料的生产日期
	private Date productionDate;

	// 原材料的有效期
	private Date validDate;
	//
	private Integer purchaseId;

	
	private String supplierName;
	
	public Integer getInspectionReportId() {
		return this.inspectionReportId;
	}

	public void setInspectionReportId(Integer inspectionReportId) {
		this.inspectionReportId = inspectionReportId;
	}

	public Integer getInspectionReportPhoto() {
		return this.inspectionReportPhoto;
	}

	public void setInspectionReportPhoto(Integer inspectionReportPhoto) {
		this.inspectionReportPhoto = inspectionReportPhoto;
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

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Integer getPurchaseId() {
		return this.purchaseId;
	}

	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}



	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
}
