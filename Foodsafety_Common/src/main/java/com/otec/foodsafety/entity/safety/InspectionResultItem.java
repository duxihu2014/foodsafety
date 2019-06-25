package com.otec.foodsafety.entity.safety;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_RESULT_ITEM", pkId = "itemId", seqName = "",notColumn = {""})
public class InspectionResultItem implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long itemId;
	
	private long resultId;
	
	private int projectId;
	
	private String inspectionContent;
	
	private String resultsQualified;
	
	private String resultsUnqualified;
	
	private Long revisitId;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date revisitTime;
	
	private String isRectified;
	
	private Long photoResult;
	
	private Long phoneRectified;
	
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	 
	public long getItemId() {
		return this.itemId;
	}

	public void setResultId(long resultId) {
		this.resultId = resultId;
	}
	 
	public long getResultId() {
		return this.resultId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	 
	public int getProjectId() {
		return this.projectId;
	}

	public void setInspectionContent(String inspectionContent) {
		this.inspectionContent = inspectionContent;
	}
	 
	public String getInspectionContent() {
		return this.inspectionContent;
	}

	public void setResultsQualified(String resultsQualified) {
		this.resultsQualified = resultsQualified;
	}
	 
	public String getResultsQualified() {
		return this.resultsQualified;
	}

	public void setResultsUnqualified(String resultsUnqualified) {
		this.resultsUnqualified = resultsUnqualified;
	}
	 
	public String getResultsUnqualified() {
		return this.resultsUnqualified;
	}

	public void setRevisitId(Long revisitId) {
		this.revisitId = revisitId;
	}
	 
	public Long getRevisitId() {
		return this.revisitId;
	}

	public void setRevisitTime(Date revisitTime) {
		this.revisitTime = revisitTime;
	}
	 
	public Date getRevisitTime() {
		return this.revisitTime;
	}

	public void setIsRectified(String isRectified) {
		this.isRectified = isRectified;
	}
	 
	public String getIsRectified() {
		return this.isRectified;
	}

	public void setPhotoResult(Long photoResult) {
		this.photoResult = photoResult;
	}
	 
	public Long getPhotoResult() {
		return this.photoResult;
	}

	public void setPhoneRectified(Long phoneRectified) {
		this.phoneRectified = phoneRectified;
	}
	 
	public Long getPhoneRectified() {
		return this.phoneRectified;
	}


}