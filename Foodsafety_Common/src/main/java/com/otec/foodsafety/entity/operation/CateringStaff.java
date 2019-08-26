package com.otec.foodsafety.entity.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(dataBase = DataBase.MYSQL, tabName = "T_CATERING_STAFF", pkId = "staffId", seqName = "",notColumn = {"enterpriseName","resourcePath","validStatus","certificateStatus","certificateId","certificatePhoto","issuingUnit","issuingDate","certificateNumber","examinationDate","examinationResult","validDate"})
public class CateringStaff implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long staffId;
	
	private long enterpriseId;
	
	private String staffName;
	
	private String sex;

	private String idType;

    private String idCardNo;
	
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date birthDate;
	
	private String homeAddress;
	
	private String contactNumber;
	
	private String education;
	
	private String major;
	
	private String department;
	
	private String employeeNumber;
	
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date employmentDate;
	
	private String workType;
	
	private String staffStatus;

	private String certificateNumber;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date examinationDate;  //体检时间

	private String examinationResult;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date validDate;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date issuingDate; //发证时间

	private String issuingUnit;  //发证机构

	private long certificatePhoto;  //健康证照片
	private String resourcePath;
	private long certificateId;
	private String certificateStatus;//1.有效 2.无效

	private String validStatus;//1:正常   2：即将过期  3：已过期

	private String enterpriseName;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}

	public String getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public Date getExaminationDate() {
		return examinationDate;
	}

	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
	}
	public String getExaminationResult() {
		return examinationResult;
	}

	public void setExaminationResult(String examinationResult) {
		this.examinationResult = examinationResult;
	}
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getIssuingDate() {
		return issuingDate;
	}

	public void setIssuingDate(Date issuingDate) {
		this.issuingDate = issuingDate;
	}

	public String getIssuingUnit() {
		return issuingUnit;
	}

	public void setIssuingUnit(String issuingUnit) {
		this.issuingUnit = issuingUnit;
	}

	public long getCertificatePhoto() {
		return certificatePhoto;
	}

	public void setCertificatePhoto(long certificatePhoto) {
		this.certificatePhoto = certificatePhoto;
	}


	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	 
	public long getStaffId() {
		return this.staffId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	 
	public String getStaffName() {
		return this.staffName;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	 
	public String getSex() {
		return this.sex;
	}

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	 
	public String getIdCardNo() {
		return this.idCardNo;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	 
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	 
	public String getHomeAddress() {
		return this.homeAddress;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	 
	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	 
	public String getEducation() {
		return this.education;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	 
	public String getMajor() {
		return this.major;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	 
	public String getDepartment() {
		return this.department;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	 
	public String getEmployeeNumber() {
		return this.employeeNumber;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}
	 
	public Date getEmploymentDate() {
		return this.employmentDate;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}
	 
	public String getWorkType() {
		return this.workType;
	}

	public void setStaffStatus(String staffStatus) {
		this.staffStatus = staffStatus;
	}
	 
	public String getStaffStatus() {
		return this.staffStatus;
	}


}