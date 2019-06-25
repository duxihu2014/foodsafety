package com.otec.foodsafety.entity.safety;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;

@Table(pkId = "itemId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ONSITE_VERIFICATION_RESULT_ITEM")
public class OnsiteVerificationResultItem implements Serializable{

	//
	private Long itemId;

	//
	private Long resultId;

	//现场核查的项目
	private Long projectId;

	//现场核查的项目内容
	private String inspectionContent; 

	//检查结果的合格项(单选)
	private String resultsQualified; 

	//检查结果的不合格项(多选)
	private String resultsUnqualified; 

	//回访的记录编号
	private Long revisitId;

	//回访的时间
	private Date revisitTime; 

	//检查结果不合格项是否已整改(0:未整改;1:部分已整改;2:整改完毕)
	private String isRectified; 

	//
	private Long photoResult;

	//
	private Long phoneRectified;

	public String  getInspectionContent(){
		return this.inspectionContent;
	}

	public void setInspectionContent(String inspectionContent){
		this.inspectionContent = inspectionContent == null ? null : inspectionContent.trim();
	}

	public String  getResultsQualified(){
		return this.resultsQualified;
	}

	public void setResultsQualified(String resultsQualified){
		this.resultsQualified = resultsQualified == null ? null : resultsQualified.trim();
	}

	public String  getResultsUnqualified(){
		return this.resultsUnqualified;
	}

	public void setResultsUnqualified(String resultsUnqualified){
		this.resultsUnqualified = resultsUnqualified == null ? null : resultsUnqualified.trim();
	}

	public Date  getRevisitTime(){
		return this.revisitTime;
	}

	public void setRevisitTime(Date revisitTime){
		this.revisitTime=revisitTime;
	}

	public String  getIsRectified(){
		return this.isRectified;
	}

	public void setIsRectified(String isRectified){
		this.isRectified = isRectified == null ? null : isRectified.trim();
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getRevisitId() {
		return revisitId;
	}

	public void setRevisitId(Long revisitId) {
		this.revisitId = revisitId;
	}

	public Long getPhotoResult() {
		return photoResult;
	}

	public void setPhotoResult(Long photoResult) {
		this.photoResult = photoResult;
	}

	public Long getPhoneRectified() {
		return phoneRectified;
	}

	public void setPhoneRectified(Long phoneRectified) {
		this.phoneRectified = phoneRectified;
	}
}
