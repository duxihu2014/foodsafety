package com.otec.foodsafety.entity.sensor;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(pkId = "id", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_SENSOR_MONITOR", notColumn = { "mini","sensorName" })
public class SensorMonitor implements Serializable{

	//
	private Long id; 

	//
	private String sensorNo;

	//
	private Float humidity;

	//
	private Float dampness;

	//
	private Date createTime; 

	//
	private String msg;

	private String mini;//分钟数

	private Integer wdAlarmSec;
	private Integer sdAlarmSec;

	private String lat;
	private String lon;
	private String sensorName;//传感器名称
	public Long  getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public String getSensorNo() {
		return sensorNo;
	}

	public void setSensorNo(String sensorNo) {
		this.sensorNo = sensorNo;
	}

	public Float getHumidity() {
		return humidity;
	}

	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}

	public Float getDampness() {
		return dampness;
	}

	public void setDampness(Float dampness) {
		this.dampness = dampness;
	}

	public Date  getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public String  getMsg(){
		return this.msg;
	}

	public void setMsg(String msg){
		this.msg = msg == null ? null : msg.trim();
	}

	public String getMini() {
		return mini;
	}

	public void setMini(String mini) {
		this.mini = mini;
	}

	public Integer getWdAlarmSec() {
		return wdAlarmSec;
	}

	public void setWdAlarmSec(Integer wdAlarmSec) {
		this.wdAlarmSec = wdAlarmSec;
	}

	public Integer getSdAlarmSec() {
		return sdAlarmSec;
	}

	public void setSdAlarmSec(Integer sdAlarmSec) {
		this.sdAlarmSec = sdAlarmSec;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
}
