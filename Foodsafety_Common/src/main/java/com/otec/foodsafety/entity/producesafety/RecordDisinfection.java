package com.otec.foodsafety.entity.producesafety;

import java.io.Serializable;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(pkId = "recordId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_RECORD_DISINFECTION")
public class RecordDisinfection implements Serializable{

	//
	private Integer recordId; 

	//
	private String enterpriseId; 

	//消毒的物品
	private String disinfectionObject; 

	//消毒的物品的件数
	private Long disinfectionAmount; 

	//消毒的方式
	private String disinfectionMethod; 

	//使用的消毒剂的名称
	private String disinfectant; 

	//消毒剂的使用量
	private String disinfectantDosage; 

	//进行消毒的单位
	private String disinfectionUnit; 

	//进行消毒的人
	private String disinfectionPerson; 

	//消毒的时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date disinfectionTime; 

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

	public String  getDisinfectionObject(){
		return this.disinfectionObject;
	}

	public void setDisinfectionObject(String disinfectionObject){
		this.disinfectionObject = disinfectionObject == null ? null : disinfectionObject.trim();
	}

	public Long  getDisinfectionAmount(){
		return this.disinfectionAmount;
	}

	public void setDisinfectionAmount(Long disinfectionAmount){
		this.disinfectionAmount=disinfectionAmount;
	}

	public String  getDisinfectionMethod(){
		return this.disinfectionMethod;
	}

	public void setDisinfectionMethod(String disinfectionMethod){
		this.disinfectionMethod = disinfectionMethod == null ? null : disinfectionMethod.trim();
	}

	public String  getDisinfectant(){
		return this.disinfectant;
	}

	public void setDisinfectant(String disinfectant){
		this.disinfectant = disinfectant == null ? null : disinfectant.trim();
	}

	public String  getDisinfectantDosage(){
		return this.disinfectantDosage;
	}

	public void setDisinfectantDosage(String disinfectantDosage){
		this.disinfectantDosage = disinfectantDosage == null ? null : disinfectantDosage.trim();
	}

	public String  getDisinfectionUnit(){
		return this.disinfectionUnit;
	}

	public void setDisinfectionUnit(String disinfectionUnit){
		this.disinfectionUnit=disinfectionUnit;
	}

	public String  getDisinfectionPerson(){
		return this.disinfectionPerson;
	}

	public void setDisinfectionPerson(String disinfectionPerson){
		this.disinfectionPerson = disinfectionPerson == null ? null : disinfectionPerson.trim();
	}

	public Date  getDisinfectionTime(){
		return this.disinfectionTime;
	}

	public void setDisinfectionTime(Date disinfectionTime){
		this.disinfectionTime=disinfectionTime;
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
