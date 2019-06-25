package com.otec.foodsafety.entity.equipment;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(pkId = "equipmentAudioId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_EQUIPMENT_AUDIO")
public class EquipmentAudio implements Serializable{

	//
	private Long equipmentAudioId; 

	//
	private Long equipmentId; 

	//
	private Long audioId; 

	//
	private String status; 

	public Long  getEquipmentAudioId(){
		return this.equipmentAudioId;
	}

	public void setEquipmentAudioId(Long equipmentAudioId){
		this.equipmentAudioId=equipmentAudioId;
	}

	public Long  getEquipmentId(){
		return this.equipmentId;
	}

	public void setEquipmentId(Long equipmentId){
		this.equipmentId=equipmentId;
	}

	public Long  getAudioId(){
		return this.audioId;
	}

	public void setAudioId(Long audioId){
		this.audioId=audioId;
	}

	public String  getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status == null ? null : status.trim();
	}
}
