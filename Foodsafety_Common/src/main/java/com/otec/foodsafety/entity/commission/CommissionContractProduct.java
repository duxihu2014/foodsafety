package com.otec.foodsafety.entity.commission;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_COMMISSION_CONTRACT_PRODUCT", pkId = "id", seqName = "",notColumn = {""})
public class CommissionContractProduct implements Serializable {

private static final long serialVersionUID = 1L;
	private long id;
	private long contractId;
	
	private long productId;
	
	private String productName;
	
	private String productType;
	
	private String licenseNo;
	
	private String specificationModel;
	
	private String registeredTrademark;
	
	private long productPicture;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}
	 
	public long getContractId() {
		return this.contractId;
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

	public void setProductType(String productType) {
		this.productType = productType;
	}
	 
	public String getProductType() {
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


}