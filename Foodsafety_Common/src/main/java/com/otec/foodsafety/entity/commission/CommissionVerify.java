package com.otec.foodsafety.entity.commission;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_COMMISSION_VERIFY", pkId = "verifyId", seqName = "",notColumn = {""})
public class CommissionVerify implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long verifyId;
	
	private long contractId;
	
	private String consigneeContractCode;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date consigneeExamineEffectiveDate;
	
	private String consigneeOpinion;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date consigneeRecordEffectiveDate;
	
	private String consigneeOperator;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date consigneeOperateDate;
	
	private String consigneeAuditor;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date consigneeAuditDate;
	
	private String consigneeApprover;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date consigneeApprovalDate;
	
	private String clientContractCode;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date clientExamineEffectiveDate;
	
	private String clientOpinion;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date clientRecordEffectiveDate;
	
	private String clientOperator;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date clientOperateDate;
	
	private String clientAuditor;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date clientAuditDate;
	
	private String clientApprover;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date clientApprovalDate;
	
	public void setVerifyId(long verifyId) {
		this.verifyId = verifyId;
	}
	 
	public long getVerifyId() {
		return this.verifyId;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}
	 
	public long getContractId() {
		return this.contractId;
	}

	public void setConsigneeContractCode(String consigneeContractCode) {
		this.consigneeContractCode = consigneeContractCode;
	}
	 
	public String getConsigneeContractCode() {
		return this.consigneeContractCode;
	}

	public void setConsigneeExamineEffectiveDate(Date consigneeExamineEffectiveDate) {
		this.consigneeExamineEffectiveDate = consigneeExamineEffectiveDate;
	}
	 
	public Date getConsigneeExamineEffectiveDate() {
		return this.consigneeExamineEffectiveDate;
	}

	public void setConsigneeOpinion(String consigneeOpinion) {
		this.consigneeOpinion = consigneeOpinion;
	}
	 
	public String getConsigneeOpinion() {
		return this.consigneeOpinion;
	}

	public void setConsigneeRecordEffectiveDate(Date consigneeRecordEffectiveDate) {
		this.consigneeRecordEffectiveDate = consigneeRecordEffectiveDate;
	}
	 
	public Date getConsigneeRecordEffectiveDate() {
		return this.consigneeRecordEffectiveDate;
	}

	public void setConsigneeOperator(String consigneeOperator) {
		this.consigneeOperator = consigneeOperator;
	}
	 
	public String getConsigneeOperator() {
		return this.consigneeOperator;
	}

	public void setConsigneeOperateDate(Date consigneeOperateDate) {
		this.consigneeOperateDate = consigneeOperateDate;
	}
	 
	public Date getConsigneeOperateDate() {
		return this.consigneeOperateDate;
	}

	public void setConsigneeAuditor(String consigneeAuditor) {
		this.consigneeAuditor = consigneeAuditor;
	}
	 
	public String getConsigneeAuditor() {
		return this.consigneeAuditor;
	}

	public void setConsigneeAuditDate(Date consigneeAuditDate) {
		this.consigneeAuditDate = consigneeAuditDate;
	}
	 
	public Date getConsigneeAuditDate() {
		return this.consigneeAuditDate;
	}

	public void setConsigneeApprover(String consigneeApprover) {
		this.consigneeApprover = consigneeApprover;
	}
	 
	public String getConsigneeApprover() {
		return this.consigneeApprover;
	}

	public void setConsigneeApprovalDate(Date consigneeApprovalDate) {
		this.consigneeApprovalDate = consigneeApprovalDate;
	}
	 
	public Date getConsigneeApprovalDate() {
		return this.consigneeApprovalDate;
	}

	public void setClientContractCode(String clientContractCode) {
		this.clientContractCode = clientContractCode;
	}
	 
	public String getClientContractCode() {
		return this.clientContractCode;
	}

	public void setClientExamineEffectiveDate(Date clientExamineEffectiveDate) {
		this.clientExamineEffectiveDate = clientExamineEffectiveDate;
	}
	 
	public Date getClientExamineEffectiveDate() {
		return this.clientExamineEffectiveDate;
	}

	public void setClientOpinion(String clientOpinion) {
		this.clientOpinion = clientOpinion;
	}
	 
	public String getClientOpinion() {
		return this.clientOpinion;
	}

	public void setClientRecordEffectiveDate(Date clientRecordEffectiveDate) {
		this.clientRecordEffectiveDate = clientRecordEffectiveDate;
	}
	 
	public Date getClientRecordEffectiveDate() {
		return this.clientRecordEffectiveDate;
	}

	public void setClientOperator(String clientOperator) {
		this.clientOperator = clientOperator;
	}
	 
	public String getClientOperator() {
		return this.clientOperator;
	}

	public void setClientOperateDate(Date clientOperateDate) {
		this.clientOperateDate = clientOperateDate;
	}
	 
	public Date getClientOperateDate() {
		return this.clientOperateDate;
	}

	public void setClientAuditor(String clientAuditor) {
		this.clientAuditor = clientAuditor;
	}
	 
	public String getClientAuditor() {
		return this.clientAuditor;
	}

	public void setClientAuditDate(Date clientAuditDate) {
		this.clientAuditDate = clientAuditDate;
	}
	 
	public Date getClientAuditDate() {
		return this.clientAuditDate;
	}

	public void setClientApprover(String clientApprover) {
		this.clientApprover = clientApprover;
	}
	 
	public String getClientApprover() {
		return this.clientApprover;
	}

	public void setClientApprovalDate(Date clientApprovalDate) {
		this.clientApprovalDate = clientApprovalDate;
	}
	 
	public Date getClientApprovalDate() {
		return this.clientApprovalDate;
	}


}