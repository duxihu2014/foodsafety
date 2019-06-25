package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

/**
 *  企业原材料信息
 * 
 * @author Administrator
 *
 */
@Table(pkId = "materialId", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_MATERIAL")
public class EnterpriseMaterial implements Serializable {

	// 材料编号
	private Integer materialId;

	// 材料名称
	private String materialName;

	// 材料类型
	private String materialType;

	// 企业编号
	private Integer enterpriseId;

	// 原材料的规格型号
	private String specificationModel;

	// 原材料的年需求量
	private String annualDemand;

	// 原材料的执行标准代码
	private String productStandard;

	// 原材料的生产厂家
	private String manufacturer;

	// 原材料的生产国
	private String country;

	// 原材料的生产许可证编号
	private String licenseNo;

	//原料状态暂定为 1 正常、0 停用
	private String materialStatus;

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

	public String getMaterialType() {
		return this.materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType == null ? null : materialType.trim();
	}

	public Integer getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getSpecificationModel() {
		return this.specificationModel;
	}

	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel == null ? null : specificationModel.trim();
	}

	public String getAnnualDemand() {
		return this.annualDemand;
	}

	public void setAnnualDemand(String annualDemand) {
		this.annualDemand = annualDemand == null ? null : annualDemand.trim();
	}

	public String getProductStandard() {
		return this.productStandard;
	}

	public void setProductStandard(String productStandard) {
		this.productStandard = productStandard == null ? null : productStandard.trim();
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer == null ? null : manufacturer.trim();
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country == null ? null : country.trim();
	}

	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo == null ? null : licenseNo.trim();
	}

	public String getMaterialStatus() {
		return this.materialStatus;
	}

	public void setMaterialStatus(String materialStatus) {
		this.materialStatus = materialStatus == null ? null : materialStatus.trim();
	}
}
