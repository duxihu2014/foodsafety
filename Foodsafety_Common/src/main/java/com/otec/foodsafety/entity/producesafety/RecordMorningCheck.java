package com.otec.foodsafety.entity.producesafety;

import java.io.Serializable;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(pkId = "recordId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_RECORD_MORNING_CHECK")
public class RecordMorningCheck implements Serializable{

	//
	private Integer recordId; 

	//
	private String enterpriseId; 

	//进行晨检的人
	private String checkPerson; 

	//进行晨检的时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date checkTime; 

	//进行晨检的内容
	private String checkContent; 

	//进行晨检的结果
	private String checkResult; 

	//进行晨检中发现的主要问题
	private String mainProblems; 

	//针对发现的问题的处理措施
	private String treatmentMeasures; 

	//
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime; 

	//
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime; 

	public Integer  getRecordId(){
		return this.recordId;
	}

	public void setRecordId(Integer recordId){
		this.recordId=recordId;
	}

	public String  getEnterpriseId(){
		return this.enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId){
		this.enterpriseId=enterpriseId;
	}

	public String  getCheckPerson(){
		return this.checkPerson;
	}

	public void setCheckPerson(String checkPerson){
		this.checkPerson = checkPerson == null ? null : checkPerson.trim();
	}

	public Date  getCheckTime(){
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime){
		this.checkTime=checkTime;
	}

	public String  getCheckContent(){
		return this.checkContent;
	}

	public void setCheckContent(String checkContent){
		this.checkContent = checkContent == null ? null : checkContent.trim();
	}

	public String  getCheckResult(){
		return this.checkResult;
	}

	public void setCheckResult(String checkResult){
		this.checkResult = checkResult == null ? null : checkResult.trim();
	}

	public String  getMainProblems(){
		return this.mainProblems;
	}

	public void setMainProblems(String mainProblems){
		this.mainProblems = mainProblems == null ? null : mainProblems.trim();
	}

	public String  getTreatmentMeasures(){
		return this.treatmentMeasures;
	}

	public void setTreatmentMeasures(String treatmentMeasures){
		this.treatmentMeasures = treatmentMeasures == null ? null : treatmentMeasures.trim();
	}

	public Date  getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date  getUpdateTime(){
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
