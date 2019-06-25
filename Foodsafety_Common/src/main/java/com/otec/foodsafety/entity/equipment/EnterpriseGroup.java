package com.otec.foodsafety.entity.equipment;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(pkId = "enterpriseGroupId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ENTERPRISE_GROUP")
public class EnterpriseGroup implements Serializable{

	//
	private Long enterpriseGroupId;

	//
	private String enterpriseGroupName; 

	//
	private Integer enterpriseId; 

	//
	private String status;

	public Long getEnterpriseGroupId() {
		return enterpriseGroupId;
	}

	public void setEnterpriseGroupId(Long enterpriseGroupId) {
		this.enterpriseGroupId = enterpriseGroupId;
	}

	public String  getEnterpriseGroupName(){
		return this.enterpriseGroupName;
	}

	public void setEnterpriseGroupName(String enterpriseGroupName){
		this.enterpriseGroupName = enterpriseGroupName == null ? null : enterpriseGroupName.trim();
	}

	public Integer  getEnterpriseId(){
		return this.enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId){
		this.enterpriseId=enterpriseId;
	}

	public String  getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status == null ? null : status.trim();
	}
}
