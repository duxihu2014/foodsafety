package com.otec.foodsafety.entity.safety;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_PROJECT", pkId = "projectId", seqName = "",notColumn = {""})
public class InspectionProject implements Serializable {

private static final long serialVersionUID = 1L;
	
	private int projectId;
	
	private int projectCategory;
	
	private String projectName;
	
	private String projectContent;
	
	private String importanceDegree;
	
	private String inspectionMethod;
	
	private String criterion;
	
	private String resultsQualified;
	
	private String resultsUnqualified;
	
	private String projectStatus;
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	 
	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectCategory(int projectCategory) {
		this.projectCategory = projectCategory;
	}
	 
	public int getProjectCategory() {
		return this.projectCategory;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	 
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectContent(String projectContent) {
		this.projectContent = projectContent;
	}
	 
	public String getProjectContent() {
		return this.projectContent;
	}

	public void setImportanceDegree(String importanceDegree) {
		this.importanceDegree = importanceDegree;
	}
	 
	public String getImportanceDegree() {
		return this.importanceDegree;
	}

	public void setInspectionMethod(String inspectionMethod) {
		this.inspectionMethod = inspectionMethod;
	}
	 
	public String getInspectionMethod() {
		return this.inspectionMethod;
	}

	public void setCriterion(String criterion) {
		this.criterion = criterion;
	}
	 
	public String getCriterion() {
		return this.criterion;
	}

	public void setResultsQualified(String resultsQualified) {
		this.resultsQualified = resultsQualified;
	}
	 
	public String getResultsQualified() {
		return this.resultsQualified;
	}

	public void setResultsUnqualified(String resultsUnqualified) {
		this.resultsUnqualified = resultsUnqualified;
	}
	 
	public String getResultsUnqualified() {
		return this.resultsUnqualified;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	 
	public String getProjectStatus() {
		return this.projectStatus;
	}


}