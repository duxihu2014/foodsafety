package com.otec.foodsafety.entity.commission;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_COMMISSION_CONTRACT", pkId = "contractId", seqName = "",notColumn = {""})
public class CommissionContract implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long contractId;
	
	private String contractCode;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date contractDate;
	
	private Long yield;
	
	private Long output;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date effectiveDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date contractPeriod;
	
	private long acceptId;
	
	private long clientEnterpriseId;
	
	private long consigneeEnterpriseId;
	
	private String contractStatus;
	
	public void setContractId(long contractId) {
		this.contractId = contractId;
	}
	 
	public long getContractId() {
		return this.contractId;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	 
	public String getContractCode() {
		return this.contractCode;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	 
	public Date getContractDate() {
		return this.contractDate;
	}

	public void setYield(Long yield) {
		this.yield = yield;
	}
	 
	public Long getYield() {
		return this.yield;
	}

	public void setOutput(Long output) {
		this.output = output;
	}
	 
	public Long getOutput() {
		return this.output;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	 
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setContractPeriod(Date contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	 
	public Date getContractPeriod() {
		return this.contractPeriod;
	}

	public void setAcceptId(long acceptId) {
		this.acceptId = acceptId;
	}
	 
	public long getAcceptId() {
		return this.acceptId;
	}

	public void setClientEnterpriseId(long clientEnterpriseId) {
		this.clientEnterpriseId = clientEnterpriseId;
	}
	 
	public long getClientEnterpriseId() {
		return this.clientEnterpriseId;
	}

	public void setConsigneeEnterpriseId(long consigneeEnterpriseId) {
		this.consigneeEnterpriseId = consigneeEnterpriseId;
	}
	 
	public long getConsigneeEnterpriseId() {
		return this.consigneeEnterpriseId;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	 
	public String getContractStatus() {
		return this.contractStatus;
	}


}