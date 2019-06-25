package com.otec.foodsafety.entity.safety;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_REVISIT_PLAN", pkId = "planId", seqName = "",notColumn = {""})
public class RevisitPlan implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long planId;
	
	private String revisitSource;
	
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
	private Date revisitDate;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date inspectedTime;
	
	private long inspectedUser;
	
	private long inspectedInstitution;
	
	private long resultId;
	
	private String mainProblems;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date finishTime;
	
	private String planStatus;
	
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	 
	public long getPlanId() {
		return this.planId;
	}

	public void setRevisitSource(String revisitSource) {
		this.revisitSource = revisitSource;
	}
	 
	public String getRevisitSource() {
		return this.revisitSource;
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

	public void setInspectedTime(Date inspectedTime) {
		this.inspectedTime = inspectedTime;
	}
	 
	public Date getInspectedTime() {
		return this.inspectedTime;
	}

	public void setInspectedUser(long inspectedUser) {
		this.inspectedUser = inspectedUser;
	}
	 
	public long getInspectedUser() {
		return this.inspectedUser;
	}

	public void setInspectedInstitution(long inspectedInstitution) {
		this.inspectedInstitution = inspectedInstitution;
	}
	 
	public long getInspectedInstitution() {
		return this.inspectedInstitution;
	}

	public void setResultId(long resultId) {
		this.resultId = resultId;
	}
	 
	public long getResultId() {
		return this.resultId;
	}

	public void setMainProblems(String mainProblems) {
		this.mainProblems = mainProblems;
	}
	 
	public String getMainProblems() {
		return this.mainProblems;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	 
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	 
	public String getPlanStatus() {
		return this.planStatus;
	}

	public Date getRevisitDate() {
		return revisitDate;
	}

	public void setRevisitDate(Date revisitDate) {
		this.revisitDate = revisitDate;
	}
}