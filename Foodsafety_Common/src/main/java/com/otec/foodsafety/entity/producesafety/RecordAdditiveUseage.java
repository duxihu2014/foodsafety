package com.otec.foodsafety.entity.producesafety;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
/**
 * 添加剂使用情况表
 * @author Administrator
 *
 */
@Table(pkId = "useageId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_RECORD_ADDITIVE_USEAGE")
public class RecordAdditiveUseage implements Serializable{

	//
	private Integer useageId; 

	//
	private Integer enterpriseId; 

	//添加剂的采购编号
	private Integer purchaseId; 

	//添加剂的使用日期	
	private Date useDate; 

	//添加剂的使用人
	private String usePerson; 

	//添加剂的使用规定量(单位:克/千克)
	private Long requisiteAmount; 

	//添加剂的实际使用量(单位:克/千克)
	private Long useAmount; 

	//添加剂的使用用途
	private String purpose; 

	//
	private Date createTime; 

	//
	private Date updateTime; 

	public Integer  getUseageId(){
		return this.useageId;
	}

	public void setUseageId(Integer useageId){
		this.useageId=useageId;
	}

	public Integer  getEnterpriseId(){
		return this.enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId){
		this.enterpriseId=enterpriseId;
	}

	public Integer  getPurchaseId(){
		return this.purchaseId;
	}

	public void setPurchaseId(Integer purchaseId){
		this.purchaseId=purchaseId;
	}

	public Date  getUseDate(){
		return this.useDate;
	}

	public void setUseDate(Date useDate){
		this.useDate=useDate;
	}

	public String  getUsePerson(){
		return this.usePerson;
	}

	public void setUsePerson(String usePerson){
		this.usePerson = usePerson == null ? null : usePerson.trim();
	}

	public Long  getRequisiteAmount(){
		return this.requisiteAmount;
	}

	public void setRequisiteAmount(Long requisiteAmount){
		this.requisiteAmount=requisiteAmount;
	}

	public Long  getUseAmount(){
		return this.useAmount;
	}

	public void setUseAmount(Long useAmount){
		this.useAmount=useAmount;
	}

	public String  getPurpose(){
		return this.purpose;
	}

	public void setPurpose(String purpose){
		this.purpose = purpose == null ? null : purpose.trim();
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
