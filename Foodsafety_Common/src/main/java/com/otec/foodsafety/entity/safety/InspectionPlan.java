package com.otec.foodsafety.entity.safety;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_PLAN", pkId = "planId", seqName = "",notColumn = {""})
public class InspectionPlan implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long planId;
	
	private long areaId;
	
	private long gridId;
	
	private String townStreet;
	
	private String committeeVillage;
	
	private long monitors;
	
	private long assistants;
	
	private long standards;
	
	private long enterpriseId;
	
	private String enterpriseName;
	
	private String corporateRepresentative;
	
	private String registerAddress;
	
	private String productionAddress;
	
	private String subjectClassification;
	
	private String superviseClassification;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date inspectionDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	private String planStatus;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date taskDate;
	
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	 
	public long getPlanId() {
		return this.planId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	 
	public long getAreaId() {
		return this.areaId;
	}

	public void setGridId(long gridId) {
		this.gridId = gridId;
	}
	 
	public long getGridId() {
		return this.gridId;
	}

	public void setTownStreet(String townStreet) {
		this.townStreet = townStreet;
	}
	 
	public String getTownStreet() {
		return this.townStreet;
	}

	public void setCommitteeVillage(String committeeVillage) {
		this.committeeVillage = committeeVillage;
	}
	 
	public String getCommitteeVillage() {
		return this.committeeVillage;
	}

	public void setMonitors(long monitors) {
		this.monitors = monitors;
	}
	 
	public long getMonitors() {
		return this.monitors;
	}

	public void setAssistants(long assistants) {
		this.assistants = assistants;
	}
	 
	public long getAssistants() {
		return this.assistants;
	}

	public void setStandards(long standards) {
		this.standards = standards;
	}
	 
	public long getStandards() {
		return this.standards;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	 
	public String getEnterpriseName() {
		return this.enterpriseName;
	}

	public void setCorporateRepresentative(String corporateRepresentative) {
		this.corporateRepresentative = corporateRepresentative;
	}
	 
	public String getCorporateRepresentative() {
		return this.corporateRepresentative;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	 
	public String getRegisterAddress() {
		return this.registerAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}
	 
	public String getProductionAddress() {
		return this.productionAddress;
	}

	public void setSubjectClassification(String subjectClassification) {
		this.subjectClassification = subjectClassification;
	}
	 
	public String getSubjectClassification() {
		return this.subjectClassification;
	}

	public void setSuperviseClassification(String superviseClassification) {
		this.superviseClassification = superviseClassification;
	}
	 
	public String getSuperviseClassification() {
		return this.superviseClassification;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	 
	public Date getInspectionDate() {
		return this.inspectionDate;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	 
	public String getPlanStatus() {
		return this.planStatus;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	 
	public Date getTaskDate() {
		return this.taskDate;
	}


}