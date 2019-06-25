package com.otec.foodsafety.entity.evaluate;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;
import java.util.List;

@Table(pkId = "complainEvaluateId", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_COMPLAIN_EVALUATE", notColumn = {
		"enterpriseName", "imgPaths" })
public class ComplainEvaluate implements Serializable {

	// 编号
	private Integer complainEvaluateId;

	// 1投诉2评价
	private Integer typeId;

	// 文字描述
	private String writing;

	// 评价投诉图片，最多上传4张图片，图片ID以逗号间隔
	private String imgPath;

	// 1颗星=2分，总分10分
	private Integer grade;

	// 提交姓名
	private String submitName;

	// 提交 电话
	private String submitTel;

	// 提交时间
	private Date createTime;

	// 1APP2微信扫码
	private String submitPlatform;

	// 回复文字描述
	private String writing1;

	// 回复时间
	private Date createTime1;

	// 回复人
	private Integer createOp1;
	private Integer enterpriseId;

	// 0:无效，1:已提交，2已回复
	// 0:作废，1:待受理，2已受理/待处理 3处理完成 4受理结束
	private String status;
	private String enterpriseName;
	private List<String> imgPaths; // 评论图片地址数组
	// 受理人ID
	private Integer acceptanceId;
	// 受理时间
	private Date acceptanceTime;
	
	// 责任人
	private String personLiable;
	// 责任人电话
	private String personLiableTel;
	// 处理人ID
	private Integer handleManId;
	// 处理结果
	private String handleResult;
	// 处理时间
	private Date handleTime;


	private long irregularityId;

	public void setIrregularityId(long irregularityId) {
		this.irregularityId = irregularityId;
	}

	public long getIrregularityId() {
		return this.irregularityId;
	}

	public List<String> getImgPaths() {
		return imgPaths;
	}

	public void setImgPaths(List<String> imgPaths) {
		this.imgPaths = imgPaths;
	}

	public Integer getComplainEvaluateId() {
		return this.complainEvaluateId;
	}

	public void setComplainEvaluateId(Integer complainEvaluateId) {
		this.complainEvaluateId = complainEvaluateId;
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getWriting() {
		return this.writing;
	}

	public void setWriting(String writing) {
		this.writing = writing == null ? null : writing.trim();
	}

	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath == null ? null : imgPath.trim();
	}

	public Integer getGrade() {
		return this.grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getSubmitName() {
		return this.submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName == null ? null : submitName.trim();
	}

	public String getSubmitTel() {
		return this.submitTel;
	}

	public void setSubmitTel(String submitTel) {
		this.submitTel = submitTel == null ? null : submitTel.trim();
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSubmitPlatform() {
		return this.submitPlatform;
	}

	public void setSubmitPlatform(String submitPlatform) {
		this.submitPlatform = submitPlatform == null ? null : submitPlatform.trim();
	}

	public String getWriting1() {
		return this.writing1;
	}

	public void setWriting1(String writing1) {
		this.writing1 = writing1 == null ? null : writing1.trim();
	}

	public Date getCreateTime1() {
		return this.createTime1;
	}

	public void setCreateTime1(Date createTime1) {
		this.createTime1 = createTime1;
	}

	public Integer getCreateOp1() {
		return this.createOp1;
	}

	public void setCreateOp1(Integer createOp1) {
		this.createOp1 = createOp1;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getPersonLiable() {
		return personLiable;
	}

	public void setPersonLiable(String personLiable) {
		this.personLiable = personLiable;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public Integer getAcceptanceId() {
		return acceptanceId;
	}

	public void setAcceptanceId(Integer acceptanceId) {
		this.acceptanceId = acceptanceId;
	}

	public Date getAcceptanceTime() {
		return acceptanceTime;
	}

	public void setAcceptanceTime(Date acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}

	public String getPersonLiableTel() {
		return personLiableTel;
	}

	public void setPersonLiableTel(String personLiableTel) {
		this.personLiableTel = personLiableTel;
	}

	public Integer getHandleManId() {
		return handleManId;
	}

	public void setHandleManId(Integer handleManId) {
		this.handleManId = handleManId;
	}

	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	
}
