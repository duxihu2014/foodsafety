package com.otec.foodsafety.entity.irregularity;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_IRREGULARITY_CASE_CLOSE", pkId = "closecaseId", seqName = "",notColumn = {""})
public class IrregularityCaseClose implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long closecaseId;
	
	private long irregularityId;
	
	private String undertakeUnit;
	
	private String processingResult;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date closeDate;
	
	private String isClose;
	
	private String transferUnit;
	
	private String transferReason;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date transferDate;
	
	public void setClosecaseId(long closecaseId) {
		this.closecaseId = closecaseId;
	}
	 
	public long getClosecaseId() {
		return this.closecaseId;
	}

	public void setIrregularityId(long irregularityId) {
		this.irregularityId = irregularityId;
	}
	 
	public long getIrregularityId() {
		return this.irregularityId;
	}

	public void setUndertakeUnit(String undertakeUnit) {
		this.undertakeUnit = undertakeUnit;
	}
	 
	public String getUndertakeUnit() {
		return this.undertakeUnit;
	}

	public void setProcessingResult(String processingResult) {
		this.processingResult = processingResult;
	}
	 
	public String getProcessingResult() {
		return this.processingResult;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	 
	public Date getCloseDate() {
		return this.closeDate;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
	 
	public String getIsClose() {
		return this.isClose;
	}

	public void setTransferUnit(String transferUnit) {
		this.transferUnit = transferUnit;
	}
	 
	public String getTransferUnit() {
		return this.transferUnit;
	}

	public void setTransferReason(String transferReason) {
		this.transferReason = transferReason;
	}
	 
	public String getTransferReason() {
		return this.transferReason;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	 
	public Date getTransferDate() {
		return this.transferDate;
	}


}