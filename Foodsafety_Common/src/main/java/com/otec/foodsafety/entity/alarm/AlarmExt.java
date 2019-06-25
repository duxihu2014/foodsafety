package com.otec.foodsafety.entity.alarm;

import java.io.Serializable;

public class AlarmExt extends Alarm implements Serializable {

	private Long enterpriseId;
	private String enterpriseName;
	private String organizingInstitutionBarCode;
	private String registerAddress;
	private String equName;

	public String getOrganizingInstitutionBarCode() {
		return organizingInstitutionBarCode;
	}

	public void setOrganizingInstitutionBarCode(String organizingInstitutionBarCode) {
		this.organizingInstitutionBarCode = organizingInstitutionBarCode;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getEquName() {
		return equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}
