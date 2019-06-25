package com.otec.foodsafety.entity.safety;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_TEMPLATE_PROJECT", pkId = "id", seqName = "",notColumn = {""})
public class InspectionTemplateProject implements Serializable {

private static final long serialVersionUID = 1L;
	
	private int id;
	
	private int projectId;
	
	private int projectOrder;
	
	private int templateId;
	
	private int score;
	
	public void setId(int id) {
		this.id = id;
	}
	 
	public int getId() {
		return this.id;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	 
	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectOrder(int projectOrder) {
		this.projectOrder = projectOrder;
	}
	 
	public int getProjectOrder() {
		return this.projectOrder;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	 
	public int getTemplateId() {
		return this.templateId;
	}

	public void setScore(int score) {
		this.score = score;
	}
	 
	public int getScore() {
		return this.score;
	}


}