package com.otec.foodsafety.entity.producesafety;

import java.io.Serializable;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(pkId = "situationId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_RECORD_WASTE_SITUATION",notColumn = {"supplierName","wastePhotoPath"})
public class RecordWasteSituation implements Serializable{

	//
	private Long situationId; 

	//
	private String enterpriseId; 

	//废弃物的种类
	private String wasteCategory; 

	//废弃物的数量(单位:千克)
	private Long wasteAmount; 

	//废弃物的存放容器
	private String storageContainer; 

	//废弃物的发放人
	private String disposalPerson; 

	//废弃物的处理方式
	private String disposalMethod; 

	//废弃物的接收单位
	private String receivingUnit;

	private String supplierName;

	//废弃物的收运人
	private String deliveryPerson; 

	//废弃物的收运时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date deliveryTime; 

	//废弃物的回收用途
	private String purpose; 

	//
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime; 

	//
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	//现场处理图片
	private Long  wastePhoto;

	//现场处理图片访问地址
	private String wastePhotoPath;


	public String getWastePhotoPath() {
		return wastePhotoPath;
	}

	public void setWastePhotoPath(String wastePhotoPath) {
		this.wastePhotoPath = wastePhotoPath;
	}

	public Long getWastePhoto() {
		return wastePhoto;
	}

	public void setWastePhoto(Long wastePhoto) {
		this.wastePhoto = wastePhoto;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Long  getSituationId(){
		return this.situationId;
	}

	public void setSituationId(Long situationId){
		this.situationId=situationId;
	}

	public String  getEnterpriseId(){
		return this.enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId){
		this.enterpriseId=enterpriseId;
	}

	public String  getWasteCategory(){
		return this.wasteCategory;
	}

	public void setWasteCategory(String wasteCategory){
		this.wasteCategory = wasteCategory == null ? null : wasteCategory.trim();
	}

	public Long  getWasteAmount(){
		return this.wasteAmount;
	}

	public void setWasteAmount(Long wasteAmount){
		this.wasteAmount=wasteAmount;
	}

	public String  getStorageContainer(){
		return this.storageContainer;
	}

	public void setStorageContainer(String storageContainer){
		this.storageContainer = storageContainer == null ? null : storageContainer.trim();
	}

	public String  getDisposalPerson(){
		return this.disposalPerson;
	}

	public void setDisposalPerson(String disposalPerson){
		this.disposalPerson = disposalPerson == null ? null : disposalPerson.trim();
	}

	public String  getDisposalMethod(){
		return this.disposalMethod;
	}

	public void setDisposalMethod(String disposalMethod){
		this.disposalMethod = disposalMethod == null ? null : disposalMethod.trim();
	}

	public String  getReceivingUnit(){
		return this.receivingUnit;
	}

	public void setReceivingUnit(String receivingUnit){
		this.receivingUnit=receivingUnit;
	}

	public String  getDeliveryPerson(){
		return this.deliveryPerson;
	}

	public void setDeliveryPerson(String deliveryPerson){
		this.deliveryPerson = deliveryPerson == null ? null : deliveryPerson.trim();
	}

	public Date  getDeliveryTime(){
		return this.deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime=deliveryTime;
	}

	public String  getPurpose(){
		return this.purpose;
	}

	public void setPurpose(String purpose){
		this.purpose = purpose == null ? null : purpose.trim();
	}

	public Date  getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date  getUpdateTime(){
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
