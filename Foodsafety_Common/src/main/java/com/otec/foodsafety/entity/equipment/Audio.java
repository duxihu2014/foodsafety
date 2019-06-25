package com.otec.foodsafety.entity.equipment;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(pkId = "audioId", usingSeq = true, seqName = "", dataBase = DataBase.MYSQL, tabName = "T_AUDIO", notColumn = {
		"cameraFlag" })
public class Audio implements Serializable {

	// 音频ID
	private Long audioId;

	// 音频编码
	private String audioNo;

	// 音频名称
	private String audioName;

	// 设备描述
	private String audioDescribe;

	// 管理url
	private String namagerUrl;

	// 设备规格型号
	private Long equModelId;

	// 状态 1有效 0 无效
	private String status;

	// 安装时间
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private Date installTime;

	// 创建时间
	private Date createTime;

	// 创建工号
	private Long createOpId;

	// 修改时间
	private Date modifyTime;

	// 修改工号
	private Long modifyOpId;

	// 设备状态 0未知 1在线 2离线
	private String equipmentStatus;

	// 是否志用 1是 0否
	private String specialStatus;

	// 经度
	private String lon;

	// 纬度
	private String lat;

	// 市编码
	private String areaCode2;

	// 省编码
	private String areaCode1;

	// 区编码
	private String areaCode3;

	// 企业ID
	private Long enterpriseId;

	// extend
	private boolean cameraFlag;// 是否有喜大普奔摄像头 true ,false

	public Long getAudioId() {
		return this.audioId;
	}

	public void setAudioId(Long audioId) {
		this.audioId = audioId;
	}

	public String getAudioNo() {
		return this.audioNo;
	}

	public void setAudioNo(String audioNo) {
		this.audioNo = audioNo == null ? null : audioNo.trim();
	}

	public String getAudioName() {
		return this.audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName == null ? null : audioName.trim();
	}

	public String getAudioDescribe() {
		return this.audioDescribe;
	}

	public void setAudioDescribe(String audioDescribe) {
		this.audioDescribe = audioDescribe == null ? null : audioDescribe.trim();
	}

	public String getNamagerUrl() {
		return this.namagerUrl;
	}

	public void setNamagerUrl(String namagerUrl) {
		this.namagerUrl = namagerUrl == null ? null : namagerUrl.trim();
	}

	public Long getEquModelId() {
		return this.equModelId;
	}

	public void setEquModelId(Long equModelId) {
		this.equModelId = equModelId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public Date getInstallTime() {
		return this.installTime;
	}

	public void setInstallTime(Date installTime) {
		this.installTime = installTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateOpId() {
		return this.createOpId;
	}

	public void setCreateOpId(Long createOpId) {
		this.createOpId = createOpId;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getModifyOpId() {
		return this.modifyOpId;
	}

	public void setModifyOpId(Long modifyOpId) {
		this.modifyOpId = modifyOpId;
	}

	public String getEquipmentStatus() {
		return this.equipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus) {
		this.equipmentStatus = equipmentStatus == null ? null : equipmentStatus.trim();
	}

	public String getSpecialStatus() {
		return this.specialStatus;
	}

	public void setSpecialStatus(String specialStatus) {
		this.specialStatus = specialStatus == null ? null : specialStatus.trim();
	}

	public String getLon() {
		return this.lon;
	}

	public void setLon(String lon) {
		this.lon = lon == null ? null : lon.trim();
	}

	public String getLat() {
		return this.lat;
	}

	public void setLat(String lat) {
		this.lat = lat == null ? null : lat.trim();
	}

	public String getAreaCode2() {
		return this.areaCode2;
	}

	public void setAreaCode2(String areaCode2) {
		this.areaCode2 = areaCode2 == null ? null : areaCode2.trim();
	}

	public String getAreaCode1() {
		return this.areaCode1;
	}

	public void setAreaCode1(String areaCode1) {
		this.areaCode1 = areaCode1 == null ? null : areaCode1.trim();
	}

	public String getAreaCode3() {
		return this.areaCode3;
	}

	public void setAreaCode3(String areaCode3) {
		this.areaCode3 = areaCode3 == null ? null : areaCode3.trim();
	}

	public boolean isCameraFlag() {
		return cameraFlag;
	}

	public void setCameraFlag(boolean cameraFlag) {
		this.cameraFlag = cameraFlag;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

}
