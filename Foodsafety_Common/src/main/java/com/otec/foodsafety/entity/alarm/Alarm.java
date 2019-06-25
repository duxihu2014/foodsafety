package com.otec.foodsafety.entity.alarm;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;

@Table(pkId = "id", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_ALARM")
public class Alarm implements Serializable {

	//
	private Long id;

	// 1:摄像头 2:传感器
	private String eventType;

	// 事件编码
	private String eventId;

	// 摄像头或传感器编码
	private String indexCode;

	//
	private String alarmSource;

	//
	private String alarmType;

	//
	private Date alarmTime;

	//
	private String content;

	//
	private String msg;

	//
	private String status;

	//
	private Date createTime;

	//
	private Date updateTime;
	private String imgUrl;
	private String videoUrl;

	//处理结果
	private String processingResult;

	//处理时间
	private Date processingTime;


	public String getProcessingResult() {
		return processingResult;
	}

	public void setProcessingResult(String processingResult) {
		this.processingResult = processingResult;
	}

	public Date getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(Date processingTime) {
		this.processingTime = processingTime;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventType() {
		return this.eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType == null ? null : eventType.trim();
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId == null ? null : eventId.trim();
	}

	public String getIndexCode() {
		return this.indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode == null ? null : indexCode.trim();
	}

	public String getAlarmSource() {
		return this.alarmSource;
	}

	public void setAlarmSource(String alarmSource) {
		this.alarmSource = alarmSource == null ? null : alarmSource.trim();
	}

	public String getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType == null ? null : alarmType.trim();
	}

	public Date getAlarmTime() {
		return this.alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg == null ? null : msg.trim();
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

}
