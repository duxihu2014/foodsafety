package com.otec.foodsafety.entity.delivery;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_SUPPLY_OBJECT_ENTERPRISE", pkId = "enterpriseSupplyObjectId", seqName = "", notColumn = {""})
public class SupplyObjectEnterprise implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long enterpriseSupplyObjectId;
	
	private long enterpriseId;
	
	private long supplyObjectId;
	
	public void setEnterpriseSupplyObjectId(long enterpriseSupplyObjectId) {
		this.enterpriseSupplyObjectId = enterpriseSupplyObjectId;
	}
	 
	public long getEnterpriseSupplyObjectId() {
		return this.enterpriseSupplyObjectId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setSupplyObjectId(long supplyObjectId) {
		this.supplyObjectId = supplyObjectId;
	}
	 
	public long getSupplyObjectId() {
		return this.supplyObjectId;
	}


}