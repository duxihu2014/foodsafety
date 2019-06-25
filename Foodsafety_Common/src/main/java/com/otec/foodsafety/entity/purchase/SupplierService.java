package com.otec.foodsafety.entity.purchase;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(dataBase = DataBase.MYSQL, tabName = "T_SUPPLIER_SERVICE", pkId = "supplierId", seqName = "",
		notColumn = {"idCardNoPhotoFrontPath","idCardNoPhotoBackPath","businessLicencePhotoPath"})
public class SupplierService implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long supplierId;
	
	private String supplierName;
	
	private String supplierCategory;
	
	private String supplierType;
	
	private String leader;
	
	private String contactNumber;
	
	private String operatingAddress;
	
	private String operationScope;
	
	private String idCardNo;
	
	private Long idCardNoPhotoFront;
	
	private Long idCardNoPhotoBack;
	
	private String businessLicenceNo;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date businessLicenceValid;
	
	private Long businessLicencePhoto;
	
//	private String productionCertificateNo;
//
//	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
//	private Date productionCertificateValid;
//
//	private long productionCertificatePhoto;

	private String supplierStatus;

	private String idCardNoPhotoFrontPath;
	private String idCardNoPhotoBackPath;
	private String businessLicencePhotoPath;

	public String getIdCardNoPhotoFrontPath() {
		return idCardNoPhotoFrontPath;
	}

	public void setIdCardNoPhotoFrontPath(String idCardNoPhotoFrontPath) {
		this.idCardNoPhotoFrontPath = idCardNoPhotoFrontPath;
	}

	public String getIdCardNoPhotoBackPath() {
		return idCardNoPhotoBackPath;
	}

	public void setIdCardNoPhotoBackPath(String idCardNoPhotoBackPath) {
		this.idCardNoPhotoBackPath = idCardNoPhotoBackPath;
	}

	public String getBusinessLicencePhotoPath() {
		return businessLicencePhotoPath;
	}

	public void setBusinessLicencePhotoPath(String businessLicencePhotoPath) {
		this.businessLicencePhotoPath = businessLicencePhotoPath;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	 
	public long getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	 
	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierCategory(String supplierCategory) {
		this.supplierCategory = supplierCategory;
	}
	 
	public String getSupplierCategory() {
		return this.supplierCategory;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}
	 
	public String getSupplierType() {
		return this.supplierType;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}
	 
	public String getLeader() {
		return this.leader;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	 
	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setOperatingAddress(String operatingAddress) {
		this.operatingAddress = operatingAddress;
	}
	 
	public String getOperatingAddress() {
		return this.operatingAddress;
	}

	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
	}
	 
	public String getOperationScope() {
		return this.operationScope;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	 
	public String getIdCardNo() {
		return this.idCardNo;
	}

	public void setIdCardNoPhotoFront(Long idCardNoPhotoFront) {
		this.idCardNoPhotoFront = idCardNoPhotoFront;
	}
	 
	public Long getIdCardNoPhotoFront() {
		return this.idCardNoPhotoFront;
	}

	public void setIdCardNoPhotoBack(Long idCardNoPhotoBack) {
		this.idCardNoPhotoBack = idCardNoPhotoBack;
	}
	 
	public Long getIdCardNoPhotoBack() {
		return this.idCardNoPhotoBack;
	}

	public void setBusinessLicenceNo(String businessLicenceNo) {
		this.businessLicenceNo = businessLicenceNo;
	}
	 
	public String getBusinessLicenceNo() {
		return this.businessLicenceNo;
	}

	public void setBusinessLicenceValid(Date businessLicenceValid) {
		this.businessLicenceValid = businessLicenceValid;
	}
	 
	public Date getBusinessLicenceValid() {
		return this.businessLicenceValid;
	}

	public void setBusinessLicencePhoto(Long businessLicencePhoto) {
		this.businessLicencePhoto = businessLicencePhoto;
	}
	 
	public Long getBusinessLicencePhoto() {
		return this.businessLicencePhoto;
	}

//	public void setProductionCertificateNo(String productionCertificateNo) {
//		this.productionCertificateNo = productionCertificateNo;
//	}
//
//	public String getProductionCertificateNo() {
//		return this.productionCertificateNo;
//	}
//
//	public void setProductionCertificateValid(Date productionCertificateValid) {
//		this.productionCertificateValid = productionCertificateValid;
//	}
//
//	public Date getProductionCertificateValid() {
//		return this.productionCertificateValid;
//	}
//
//	public void setProductionCertificatePhoto(long productionCertificatePhoto) {
//		this.productionCertificatePhoto = productionCertificatePhoto;
//	}
//
//	public long getProductionCertificatePhoto() {
//		return this.productionCertificatePhoto;
//	}

    public String getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }
}