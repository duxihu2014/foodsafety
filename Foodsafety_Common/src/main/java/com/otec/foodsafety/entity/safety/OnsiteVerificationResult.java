package com.otec.foodsafety.entity.safety;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(pkId = "resultId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ONSITE_VERIFICATION_RESULT")
public class OnsiteVerificationResult implements Serializable{

	//
	private Long resultId;

	//
	private Long enterpriseId;

	//
	private Long templateId;

	//
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date verificateTime; 

	//
	private Long verificateInstitution;

	//
	private Long verificateUser;

	//现场核查中发现的主要问题
	private String mainProblems; 

	//现场核查的结论(合格、一般不合格、严重不合格)
	private String verificateConclusion; 

	//下次回访的日期
	private Date revisitDate;

	private Long revisitPlanId;

	public Date  getVerificateTime(){
		return this.verificateTime;
	}

	public void setVerificateTime(Date verificateTime){
		this.verificateTime=verificateTime;
	}

	public String  getMainProblems(){
		return this.mainProblems;
	}

	public void setMainProblems(String mainProblems){
		this.mainProblems = mainProblems == null ? null : mainProblems.trim();
	}

	public String  getVerificateConclusion(){
		return this.verificateConclusion;
	}

	public void setVerificateConclusion(String verificateConclusion){
		this.verificateConclusion = verificateConclusion == null ? null : verificateConclusion.trim();
	}

	public Date  getRevisitDate(){
		return this.revisitDate;
	}

	public void setRevisitDate(Date revisitDate){
		this.revisitDate=revisitDate;
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getVerificateInstitution() {
		return verificateInstitution;
	}

	public void setVerificateInstitution(Long verificateInstitution) {
		this.verificateInstitution = verificateInstitution;
	}

	public Long getVerificateUser() {
		return verificateUser;
	}

	public void setVerificateUser(Long verificateUser) {
		this.verificateUser = verificateUser;
	}

	public Long getRevisitPlanId() {
		return revisitPlanId;
	}

	public void setRevisitPlanId(Long revisitPlanId) {
		this.revisitPlanId = revisitPlanId;
	}
}
