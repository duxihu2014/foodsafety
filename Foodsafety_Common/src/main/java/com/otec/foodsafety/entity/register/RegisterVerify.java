package com.otec.foodsafety.entity.register;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_REGISTER_VERIFY", pkId = "verifyId", seqName = "",notColumn = {"gridId"})
public class RegisterVerify implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long verifyId;
	
	private long registerId;
	
	private long verifyInstitution;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date verifyTime;

	private String verifyResult;
	
	private String reason;

	private Long gridId;
	
	public void setVerifyId(long verifyId) {
		this.verifyId = verifyId;
	}
	 
	public long getVerifyId() {
		return this.verifyId;
	}

	public void setRegisterId(long registerId) {
		this.registerId = registerId;
	}
	 
	public long getRegisterId() {
		return this.registerId;
	}

	public void setVerifyInstitution(long verifyInstitution) {
		this.verifyInstitution = verifyInstitution;
	}
	 
	public long getVerifyInstitution() {
		return this.verifyInstitution;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	 
	public Date getVerifyTime() {
		return this.verifyTime;
	}

	public void setVerifyResult(String verifyResult) {
		this.verifyResult = verifyResult;
	}
	 
	public String getVerifyResult() {
		return this.verifyResult;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	 
	public String getReason() {
		return this.reason;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
}