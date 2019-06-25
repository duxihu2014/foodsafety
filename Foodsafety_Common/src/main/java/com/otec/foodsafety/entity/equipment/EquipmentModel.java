package com.otec.foodsafety.entity.equipment;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;

@Table(pkId = "equModelId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_EQUIPMENT_MODEL")
public class EquipmentModel implements Serializable{

	//
	private Long equModelId; 

	//
	private String equModel; 

	//
	private Long venderId; 

	//有多个，则以逗号分隔
	private String otherParam; 

	//
	private Date createTime; 

	//
	private Long createOpId; 

	//
	private String status;
	//
	private String modelNo;

	public Long  getEquModelId(){
		return this.equModelId;
	}

	public void setEquModelId(Long equModelId){
		this.equModelId=equModelId;
	}

	public String  getEquModel(){
		return this.equModel;
	}

	public void setEquModel(String equModel){
		this.equModel = equModel == null ? null : equModel.trim();
	}

	public Long  getVenderId(){
		return this.venderId;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public String  getOtherParam(){
		return this.otherParam;
	}

	public void setOtherParam(String otherParam){
		this.otherParam = otherParam == null ? null : otherParam.trim();
	}

	public Date  getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Long  getCreateOpId(){
		return this.createOpId;
	}

	public void setCreateOpId(Long createOpId){
		this.createOpId=createOpId;
	}

	public String  getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status == null ? null : status.trim();
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}
}
