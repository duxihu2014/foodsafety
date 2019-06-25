package com.otec.foodsafety.entity.safety;


import java.io.Serializable;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 企业回访记录表
 * */
@Table(pkId = "resultId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_REVISIT_RESULT")
public class RevisitResult implements Serializable{

	private static final long serialVersionUID = 1L;
	//
	private Long resultId; 

	//回访计划的编号
	private long planId;

	//回访的时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date revisitTime; 

	//回访的人员
	private long revisitUser;

	//回访的机构
	private long revisitInstitution;

	//回访的场所
	private String revisitPlace; 

	//回访时企业在场的人员
	private String enterprisePersonnel; 

	//回访时企业在场的人员的职务
	private String enterpriseDuty; 

	//回访后的处理措施
	private String treatmentMeasures; 

	//回访后的处理结论
	private String treatmentConclusion; 

	//需要再次回访的时间
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date nextRevisitTime;

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public Date getRevisitTime() {
		return revisitTime;
	}

	public void setRevisitTime(Date revisitTime) {
		this.revisitTime = revisitTime;
	}

	public long getRevisitUser() {
		return revisitUser;
	}

	public void setRevisitUser(long revisitUser) {
		this.revisitUser = revisitUser;
	}

	public long getRevisitInstitution() {
		return revisitInstitution;
	}

	public void setRevisitInstitution(long revisitInstitution) {
		this.revisitInstitution = revisitInstitution;
	}

	public String getRevisitPlace() {
		return revisitPlace;
	}

	public void setRevisitPlace(String revisitPlace) {
		this.revisitPlace = revisitPlace;
	}

	public String getEnterprisePersonnel() {
		return enterprisePersonnel;
	}

	public void setEnterprisePersonnel(String enterprisePersonnel) {
		this.enterprisePersonnel = enterprisePersonnel;
	}

	public String getEnterpriseDuty() {
		return enterpriseDuty;
	}

	public void setEnterpriseDuty(String enterpriseDuty) {
		this.enterpriseDuty = enterpriseDuty;
	}

	public String getTreatmentMeasures() {
		return treatmentMeasures;
	}

	public void setTreatmentMeasures(String treatmentMeasures) {
		this.treatmentMeasures = treatmentMeasures;
	}

	public String getTreatmentConclusion() {
		return treatmentConclusion;
	}

	public void setTreatmentConclusion(String treatmentConclusion) {
		this.treatmentConclusion = treatmentConclusion;
	}

	public Date getNextRevisitTime() {
		return nextRevisitTime;
	}

	public void setNextRevisitTime(Date nextRevisitTime) {
		this.nextRevisitTime = nextRevisitTime;
	} 

}
