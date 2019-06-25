package com.otec.foodsafety.entity.equipment;

import java.io.Serializable;
import java.util.Date;

public class EquipmentAudioExt extends EquipmentAudio implements Serializable {

	//
	private String equipmentNo;

	//
	private String equipmentName;

	//
	private String equipmentDescribe;

	// 1摄像头、2NVR、3视频存储
	private String equipmentType;

	//
	private String namagerUrl;

	// 0未知 1在线 2离线
	private String equipmentStatus;

	// 音频编码
	private String audioNo;

	// 音频名称
	private String audioName;

	// 设备描述
	private String audioDescribe;

	// 是否志用 1是 0否
	private String specialStatus;
	
	private Long enterpriseId;

	private Long enterpriseGroupId;

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentDescribe() {
		return equipmentDescribe;
	}

	public void setEquipmentDescribe(String equipmentDescribe) {
		this.equipmentDescribe = equipmentDescribe;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getNamagerUrl() {
		return namagerUrl;
	}

	public void setNamagerUrl(String namagerUrl) {
		this.namagerUrl = namagerUrl;
	}

	public String getEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}

	public String getAudioNo() {
		return audioNo;
	}

	public void setAudioNo(String audioNo) {
		this.audioNo = audioNo;
	}

	public String getAudioName() {
		return audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}

	public String getAudioDescribe() {
		return audioDescribe;
	}

	public void setAudioDescribe(String audioDescribe) {
		this.audioDescribe = audioDescribe;
	}

	public String getSpecialStatus() {
		return specialStatus;
	}

	public void setSpecialStatus(String specialStatus) {
		this.specialStatus = specialStatus;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getEnterpriseGroupId() {
		return enterpriseGroupId;
	}

	public void setEnterpriseGroupId(Long enterpriseGroupId) {
		this.enterpriseGroupId = enterpriseGroupId;
	}

}
