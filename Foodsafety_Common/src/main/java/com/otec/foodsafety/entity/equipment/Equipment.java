package com.otec.foodsafety.entity.equipment;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(pkId = "equipmentId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_EQUIPMENT", notColumn = { "enterpriseName",
		"enterpriseGroupName", "contacts", "contactNumber", "audioId", "audioName", "equModel" })
public class Equipment implements Serializable{

	//
	private Long equipmentId; 

	//
	private String equipmentNo; 

	//
	private String equipmentName; 

	//
	private String equipmentDescribe; 

	//1摄像头、2NVR、3视频存储
	private String equipmentType; 

	//
	private String videoUrlHigh; 

	//
	private String videoUrlCentre; 

	//
	private String videoUrlLow; 

	//
	private String cloudControlUrl; 

	//
	private String videotapeUrl; 

	//1公共、2地区受控
	private String controltId; 

	//
	private String namagerUrl; 

	//
	private Long equModelId; 

	//
	private String status; 

	//
	private Date installTime; 

	//
	private Date createTime; 

	//
	private Long createOpId; 

	//
	private Date modifyTime; 

	//
	private Long modifyOpId; 

	//
	private String cameraType; 

	//0未知 1在线 2离线
	private String equipmentStatus; 

	//
	private String imgUrl; 

	//
	private String specialStatus; 

	//
	private String lon; 

	//
	private String lat; 

	//
	private String areaCode2; 

	//
	private String areaCode1; 


	private String areaCode3;

	private Long enterpriseId;

	private Long enterpriseGroupId;

	private String enterpriseName;

	private String enterpriseGroupName;

	private String contacts;

	private String contactNumber;

	private Long audioId;

	private String audioName;

	private String equModel;

	private Long equipmentPushId;



	public Long  getEquipmentId(){
		return this.equipmentId;
	}

	public void setEquipmentId(Long equipmentId){
		this.equipmentId=equipmentId;
	}

	public String  getEquipmentNo(){
		return this.equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo){
		this.equipmentNo = equipmentNo == null ? null : equipmentNo.trim();
	}

	public String  getEquipmentName(){
		return this.equipmentName;
	}

	public void setEquipmentName(String equipmentName){
		this.equipmentName = equipmentName == null ? null : equipmentName.trim();
	}

	public String  getEquipmentDescribe(){
		return this.equipmentDescribe;
	}

	public void setEquipmentDescribe(String equipmentDescribe){
		this.equipmentDescribe = equipmentDescribe == null ? null : equipmentDescribe.trim();
	}

	public String  getEquipmentType(){
		return this.equipmentType;
	}

	public void setEquipmentType(String equipmentType){
		this.equipmentType = equipmentType == null ? null : equipmentType.trim();
	}

	public String  getVideoUrlHigh(){
		return this.videoUrlHigh;
	}

	public void setVideoUrlHigh(String videoUrlHigh){
		this.videoUrlHigh = videoUrlHigh == null ? null : videoUrlHigh.trim();
	}

	public String  getVideoUrlCentre(){
		return this.videoUrlCentre;
	}

	public void setVideoUrlCentre(String videoUrlCentre){
		this.videoUrlCentre = videoUrlCentre == null ? null : videoUrlCentre.trim();
	}

	public String  getVideoUrlLow(){
		return this.videoUrlLow;
	}

	public void setVideoUrlLow(String videoUrlLow){
		this.videoUrlLow = videoUrlLow == null ? null : videoUrlLow.trim();
	}

	public String  getCloudControlUrl(){
		return this.cloudControlUrl;
	}

	public void setCloudControlUrl(String cloudControlUrl){
		this.cloudControlUrl = cloudControlUrl == null ? null : cloudControlUrl.trim();
	}

	public String  getVideotapeUrl(){
		return this.videotapeUrl;
	}

	public void setVideotapeUrl(String videotapeUrl){
		this.videotapeUrl = videotapeUrl == null ? null : videotapeUrl.trim();
	}

	public String  getControltId(){
		return this.controltId;
	}

	public void setControltId(String controltId){
		this.controltId = controltId == null ? null : controltId.trim();
	}

	public String  getNamagerUrl(){
		return this.namagerUrl;
	}

	public void setNamagerUrl(String namagerUrl){
		this.namagerUrl = namagerUrl == null ? null : namagerUrl.trim();
	}

	public Long  getEquModelId(){
		return this.equModelId;
	}

	public void setEquModelId(Long equModelId){
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

	public Date  getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Long  getCreateOpId(){
		return this.createOpId;
	}

	public void setCreateOpId(Long createOpId){
		this.createOpId=createOpId;
	}

	public Date  getModifyTime(){
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}

	public Long  getModifyOpId(){
		return this.modifyOpId;
	}

	public void setModifyOpId(Long modifyOpId){
		this.modifyOpId=modifyOpId;
	}

	public String  getCameraType(){
		return this.cameraType;
	}

	public void setCameraType(String cameraType){
		this.cameraType = cameraType == null ? null : cameraType.trim();
	}

	public String  getEquipmentStatus(){
		return this.equipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus){
		this.equipmentStatus = equipmentStatus == null ? null : equipmentStatus.trim();
	}

	public String  getImgUrl(){
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl == null ? null : imgUrl.trim();
	}

	public String  getSpecialStatus(){
		return this.specialStatus;
	}

	public void setSpecialStatus(String specialStatus){
		this.specialStatus = specialStatus == null ? null : specialStatus.trim();
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

	public String  getAreaCode2(){
		return this.areaCode2;
	}

	public void setAreaCode2(String areaCode2){
		this.areaCode2 = areaCode2 == null ? null : areaCode2.trim();
	}

	public String  getAreaCode1(){
		return this.areaCode1;
	}

	public void setAreaCode1(String areaCode1){
		this.areaCode1 = areaCode1 == null ? null : areaCode1.trim();
	}

	public String  getAreaCode3(){
		return this.areaCode3;
	}

	public void setAreaCode3(String areaCode3){
		this.areaCode3 = areaCode3 == null ? null : areaCode3.trim();
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

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getAudioId() {
		return audioId;
	}

	public void setAudioId(Long audioId) {
		this.audioId = audioId;
	}

	public String getAudioName() {
		return audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}

	public String getEquModel() {
		return equModel;
	}

	public void setEquModel(String equModel) {
		this.equModel = equModel;
	}

	public Long getEquipmentPushId() {
		return equipmentPushId;
	}

	public void setEquipmentPushId(Long equipmentPushId) {
		this.equipmentPushId = equipmentPushId;
	}


}
