package com.otec.foodsafety.entity.sensor;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;

@Table(pkId = "sensorId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_SENSOR_INFO", notColumn = { "enterpriseName",
		"enterpriseGroupName", "equModel", "modelNo" })
public class SensorInfo implements Serializable{

	//
	private Integer sensorId; 

	//
	private Integer enterpriseGroupId; 

	//
	private String sensorNo; 

	//
	private String sensorName; 

	//
	private String sensorDescribe; 

	//1温湿度
	private String sensorType; 

	//
	private String sensorUrl; 

	//
	private Integer equModelId; 

	//
	private String status; 

	//
	private Date installTime; 

	//
	private Integer humidityHigh; 

	//
	private Integer humidityLow; 

	//
	private Integer dampnessHigh; 

	//
	private Integer dampnessLow; 

	//
	private Date createTime; 

	//
	private Long createOpId;

	//
	private Date modifyTime; 

	//
	private Long modifyOpId;

	//
	private String lon; 

	//
	private String lat;
 //用途类型：1、室内  2、配送  3、消毒   4、冷冻  5、 冷藏   9、其他
	private String usesType;
	//ext field
	private Long enterpriseId;

	private String enterpriseName;

	private String enterpriseGroupName;

	private String equModel;

	private String modelNo;

	public String getUsesType() {
		return usesType;
	}

	public void setUsesType(String usesType) {
		this.usesType = usesType;
	}

	public Integer  getSensorId(){
		return this.sensorId;
	}

	public void setSensorId(Integer sensorId){
		this.sensorId=sensorId;
	}

	public Integer  getEnterpriseGroupId(){
		return this.enterpriseGroupId;
	}

	public void setEnterpriseGroupId(Integer enterpriseGroupId){
		this.enterpriseGroupId=enterpriseGroupId;
	}

	public String  getSensorNo(){
		return this.sensorNo;
	}

	public void setSensorNo(String sensorNo){
		this.sensorNo = sensorNo == null ? null : sensorNo.trim();
	}

	public String  getSensorName(){
		return this.sensorName;
	}

	public void setSensorName(String sensorName){
		this.sensorName = sensorName == null ? null : sensorName.trim();
	}

	public String  getSensorDescribe(){
		return this.sensorDescribe;
	}

	public void setSensorDescribe(String sensorDescribe){
		this.sensorDescribe = sensorDescribe == null ? null : sensorDescribe.trim();
	}

	public String  getSensorType(){
		return this.sensorType;
	}

	public void setSensorType(String sensorType){
		this.sensorType = sensorType == null ? null : sensorType.trim();
	}

	public String  getSensorUrl(){
		return this.sensorUrl;
	}

	public void setSensorUrl(String sensorUrl){
		this.sensorUrl = sensorUrl == null ? null : sensorUrl.trim();
	}

	public Integer  getEquModelId(){
		return this.equModelId;
	}

	public void setEquModelId(Integer equModelId){
		this.equModelId=equModelId;
	}

	public String  getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status == null ? null : status.trim();
	}

	public Date  getInstallTime(){
		return this.installTime;
	}

	public void setInstallTime(Date installTime){
		this.installTime=installTime;
	}

	public Integer  getHumidityHigh(){
		return this.humidityHigh;
	}

	public void setHumidityHigh(Integer humidityHigh){
		this.humidityHigh=humidityHigh;
	}

	public Integer  getHumidityLow(){
		return this.humidityLow;
	}

	public void setHumidityLow(Integer humidityLow){
		this.humidityLow=humidityLow;
	}

	public Integer  getDampnessHigh(){
		return this.dampnessHigh;
	}

	public void setDampnessHigh(Integer dampnessHigh){
		this.dampnessHigh=dampnessHigh;
	}

	public Integer  getDampnessLow(){
		return this.dampnessLow;
	}

	public void setDampnessLow(Integer dampnessLow){
		this.dampnessLow=dampnessLow;
	}

	public Date  getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Long getCreateOpId() {
		return createOpId;
	}

	public void setCreateOpId(Long createOpId) {
		this.createOpId = createOpId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseGroupName() {
		return enterpriseGroupName;
	}

	public void setEnterpriseGroupName(String enterpriseGroupName) {
		this.enterpriseGroupName = enterpriseGroupName;
	}

	public Date  getModifyTime(){
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}

	public Long getModifyOpId() {
		return modifyOpId;
	}

	public void setModifyOpId(Long modifyOpId) {
		this.modifyOpId = modifyOpId;
	}

	public String  getLon(){
		return this.lon;
	}

	public void setLon(String lon){
		this.lon = lon == null ? null : lon.trim();
	}

	public String  getLat(){
		return this.lat;
	}

	public void setLat(String lat){
		this.lat = lat == null ? null : lat.trim();
	}

	public String getEquModel() {
		return equModel;
	}

	public void setEquModel(String equModel) {
		this.equModel = equModel;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}
}
