package com.otec.foodsafety.entity;

import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

public class CateringStaffeExt implements Serializable {

private static final long serialVersionUID = 1L;
	

	 private CateringStaff staff;

	 private CateringStaffCertificate certificate;


	public CateringStaff getStaff() {
		return staff;
	}

	public void setStaff(CateringStaff staff) {
		this.staff = staff;
	}

	public CateringStaffCertificate getCertificate() {
		return certificate;
	}

	public void setCertificate(CateringStaffCertificate certificate) {
		this.certificate = certificate;
	}
}