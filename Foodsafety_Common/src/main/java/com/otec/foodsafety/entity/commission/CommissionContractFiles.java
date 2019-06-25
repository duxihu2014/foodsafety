package com.otec.foodsafety.entity.commission;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_COMMISSION_CONTRACT_FILES", pkId = "id", seqName = "",notColumn = {""})
public class CommissionContractFiles implements Serializable {

private static final long serialVersionUID = 1L;
	private long id;

	private long contractId;
	
	private long resourceId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}
	 
	public long getContractId() {
		return this.contractId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	 
	public long getResourceId() {
		return this.resourceId;
	}


}