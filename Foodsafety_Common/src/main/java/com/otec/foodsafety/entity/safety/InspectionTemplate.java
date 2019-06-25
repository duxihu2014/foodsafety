package com.otec.foodsafety.entity.safety;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_TEMPLATE", pkId = "templateId", seqName = "",notColumn = {""})
public class InspectionTemplate implements Serializable {

private static final long serialVersionUID = 1L;
	
	private int templateId;
	
	private String templateName;
	
	private String templateDesc;
	
	private long workoutInstitution;
	
	private String administrativeLevel;
	
	private String applicationScope;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date applicationDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date workoutTime;
	
	private long workoutUser;
	
	private String templateStatus;
	
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	 
	public int getTemplateId() {
		return this.templateId;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	 
	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}
	 
	public String getTemplateDesc() {
		return this.templateDesc;
	}

	public void setWorkoutInstitution(long workoutInstitution) {
		this.workoutInstitution = workoutInstitution;
	}
	 
	public long getWorkoutInstitution() {
		return this.workoutInstitution;
	}

	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
	}
	 
	public String getAdministrativeLevel() {
		return this.administrativeLevel;
	}

	public void setApplicationScope(String applicationScope) {
		this.applicationScope = applicationScope;
	}
	 
	public String getApplicationScope() {
		return this.applicationScope;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}
	 
	public Date getApplicationDate() {
		return this.applicationDate;
	}

	public void setWorkoutTime(Date workoutTime) {
		this.workoutTime = workoutTime;
	}
	 
	public Date getWorkoutTime() {
		return this.workoutTime;
	}

	public void setWorkoutUser(long workoutUser) {
		this.workoutUser = workoutUser;
	}
	 
	public long getWorkoutUser() {
		return this.workoutUser;
	}

	public void setTemplateStatus(String templateStatus) {
		this.templateStatus = templateStatus;
	}
	 
	public String getTemplateStatus() {
		return this.templateStatus;
	}


}