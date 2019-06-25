package com.otec.foodsafety.entity.safety;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_RESULT", pkId = "resultId", seqName = "",notColumn = {""})
public class InspectionResult implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long resultId;
	
	private long planId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date inspectedTime;
	
	private long inspectedInstitution;
	
	private long inspectedUser;
	
	private long templateId;
	
	private String mainProblems;
	
	private String treatmentMeasures;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date revisitDate;
	
	private Long revisitPlanId;
	
	public void setResultId(long resultId) {
		this.resultId = resultId;
	}
	 
	public long getResultId() {
		return this.resultId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}
	 
	public long getPlanId() {
		return this.planId;
	}

	public void setInspectedTime(Date inspectedTime) {
		this.inspectedTime = inspectedTime;
	}
	 
	public Date getInspectedTime() {
		return this.inspectedTime;
	}

	public void setInspectedInstitution(long inspectedInstitution) {
		this.inspectedInstitution = inspectedInstitution;
	}
	 
	public long getInspectedInstitution() {
		return this.inspectedInstitution;
	}

	public void setInspectedUser(long inspectedUser) {
		this.inspectedUser = inspectedUser;
	}
	 
	public long getInspectedUser() {
		return this.inspectedUser;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	 
	public long getTemplateId() {
		return this.templateId;
	}

	public void setMainProblems(String mainProblems) {
		this.mainProblems = mainProblems;
	}
	 
	public String getMainProblems() {
		return this.mainProblems;
	}

	public void setTreatmentMeasures(String treatmentMeasures) {
		this.treatmentMeasures = treatmentMeasures;
	}
	 
	public String getTreatmentMeasures() {
		return this.treatmentMeasures;
	}

	public void setRevisitDate(Date revisitDate) {
		this.revisitDate = revisitDate;
	}
	 
	public Date getRevisitDate() {
		return this.revisitDate;
	}

	public void setRevisitPlanId(Long revisitPlanId) {
		this.revisitPlanId = revisitPlanId;
	}
	 
	public Long getRevisitPlanId() {
		return this.revisitPlanId;
	}


}