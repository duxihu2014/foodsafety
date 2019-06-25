package com.otec.foodsafety.entity.equipment;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(pkId = "id", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_EQUIPMENT_PUSHFLOW_INFO")
public class EquipmentPushflowInfo implements Serializable{


	private Long id;


	private String videoInfoId;


	private String deviceId;


	private String name;



	private Integer status;


	private String deliveryAddress;


	private String operator;


	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVideoInfoId() {
		return videoInfoId;
	}

	public void setVideoInfoId(String videoInfoId) {
		this.videoInfoId = videoInfoId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
