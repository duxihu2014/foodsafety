package com.otec.foodsafety.entity;

import com.otec.foodsafety.entity.enterprise.EnterpriseEquipment;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业设备表
 * @author Administrator
 *
 */
public class EnterpriseEquipmentChangeExt implements Serializable{

	private EnterpriseEquipment equipment;

	private String reason; //变更原因

	public EnterpriseEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(EnterpriseEquipment equipment) {
		this.equipment = equipment;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
