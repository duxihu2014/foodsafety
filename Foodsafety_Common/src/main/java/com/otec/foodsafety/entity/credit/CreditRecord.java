package com.otec.foodsafety.entity.credit;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_CREDIT_RECORD", pkId = "recordId", seqName = "",notColumn = {"cycleDate","enterpriseName"})
public class CreditRecord implements Serializable {

private static final long serialVersionUID = 1L;

    private long recordId;

	private long enterpriseId;

	private String  enterpriseName;
	
	private int cycleScore;
	
	private Integer creditScore;
	
	private Long creditGrade;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date cycleStart;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date cycleFinish;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date cycleDate;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
	
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setCycleScore(int cycleScore) {
		this.cycleScore = cycleScore;
	}
	 
	public int getCycleScore() {
		return this.cycleScore;
	}

	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}
	 
	public Integer getCreditScore() {
		return this.creditScore;
	}

	public void setCreditGrade(Long creditGrade) {
		this.creditGrade = creditGrade;
	}
	 
	public Long getCreditGrade() {
		return this.creditGrade;
	}

	public void setCycleStart(Date cycleStart) {
		this.cycleStart = cycleStart;
	}
	 
	public Date getCycleStart() {
		return this.cycleStart;
	}

	public void setCycleFinish(Date cycleFinish) {
		this.cycleFinish = cycleFinish;
	}
	 
	public Date getCycleFinish() {
		return this.cycleFinish;
	}

    public Date getCycleDate() {
        return cycleDate;
    }

    public void setCycleDate(Date cycleDate) {
        this.cycleDate = cycleDate;
    }

}