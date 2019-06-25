package com.otec.foodsafety.entity.classification;

import java.util.Date;
import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_CLASSIFICATION_DETAIL", pkId = "detailId", seqName = "",notColumn = {""})
public class ClassificationDetail implements Serializable {

    private static final long serialVersionUID = 1L;
	
	private long detailId;
	
	private String cause;
	
	private String gist;
	
	private long userId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date operTime;
	
	private String comment;
	
	private Long resourceId;
	
	private long enterpriseId;
	
	private String beforeClassification;
	
	private String afterClassification;
	
	public void setDetailId(long detailId) {
		this.detailId = detailId;
	}
	 
	public long getDetailId() {
		return this.detailId;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	 
	public String getCause() {
		return this.cause;
	}

	public void setGist(String gist) {
		this.gist = gist;
	}
	 
	public String getGist() {
		return this.gist;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	 
	public long getUserId() {
		return this.userId;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	 
	public Date getOperTime() {
		return this.operTime;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	 
	public String getComment() {
		return this.comment;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	 
	public Long getResourceId() {
		return this.resourceId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setBeforeClassification(String beforeClassification) {
		this.beforeClassification = beforeClassification;
	}
	 
	public String getBeforeClassification() {
		return this.beforeClassification;
	}

	public void setAfterClassification(String afterClassification) {
		this.afterClassification = afterClassification;
	}
	 
	public String getAfterClassification() {
		return this.afterClassification;
	}


}