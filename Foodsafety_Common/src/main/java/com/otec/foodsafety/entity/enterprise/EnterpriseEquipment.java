package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import java.util.Date;
/**
 * 企业设备表
 * @author Administrator
 *
 */
@Table(pkId = "equipmentId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ENTERPRISE_EQUIPMENT")
public class EnterpriseEquipment implements Serializable{

	//设备编号
	private Integer equipmentId; 

	//设备名称
	private String equipmentName; 

	//设备类型
	private String equipmentType; 

	//企业编号
	private Integer enterpriseId; 

	//设备的规格型号
	private String specificationModel; 

	//设备的精度等级
	private String precisionGrade; 

	//设备的数量
	private String quantity; 

	//设备的完好状态
	private String perfectState; 

	//设备的检定有效期
	private Date verificationPeriod; 

	//设备的使用场所
	private String usePlace; 

	//设备的生产厂家
	private String manufacturer; 

	//设备的生产国
	private String country; 

	//设备的生产日期
	private Date manufactureDate; 

	//设备的购置日期
	private Date purchaseDate; 

	//设备状态暂定为 1 正常、0报废
	private String equipmentStatus; 

	public Integer  getEquipmentId(){
		return this.equipmentId;
	}

	public void setEquipmentId(Integer equipmentId){
		this.equipmentId=equipmentId;
	}

	public String  getEquipmentName(){
		return this.equipmentName;
	}

	public void setEquipmentName(String equipmentName){
		this.equipmentName = equipmentName == null ? null : equipmentName.trim();
	}

	public String  getEquipmentType(){
		return this.equipmentType;
	}

	public void setEquipmentType(String equipmentType){
		this.equipmentType = equipmentType == null ? null : equipmentType.trim();
	}

	public Integer  getEnterpriseId(){
		return this.enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId){
		this.enterpriseId=enterpriseId;
	}

	public String  getSpecificationModel(){
		return this.specificationModel;
	}

	public void setSpecificationModel(String specificationModel){
		this.specificationModel = specificationModel == null ? null : specificationModel.trim();
	}

	public String  getPrecisionGrade(){
		return this.precisionGrade;
	}

	public void setPrecisionGrade(String precisionGrade){
		this.precisionGrade = precisionGrade == null ? null : precisionGrade.trim();
	}

	public String  getQuantity(){
		return this.quantity;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity == null ? null : quantity.trim();
	}

	public String  getPerfectState(){
		return this.perfectState;
	}

	public void setPerfectState(String perfectState){
		this.perfectState = perfectState == null ? null : perfectState.trim();
	}

	public Date  getVerificationPeriod(){
		return this.verificationPeriod;
	}

	public void setVerificationPeriod(Date verificationPeriod){
		this.verificationPeriod=verificationPeriod;
	}

	public String  getUsePlace(){
		return this.usePlace;
	}

	public void setUsePlace(String usePlace){
		this.usePlace = usePlace == null ? null : usePlace.trim();
	}

	public String  getManufacturer(){
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer){
		this.manufacturer = manufacturer == null ? null : manufacturer.trim();
	}

	public String  getCountry(){
		return this.country;
	}

	public void setCountry(String country){
		this.country = country == null ? null : country.trim();
	}

	public Date  getManufactureDate(){
		return this.manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate){
		this.manufactureDate=manufactureDate;
	}

	public Date  getPurchaseDate(){
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate){
		this.purchaseDate=purchaseDate;
	}

	public String  getEquipmentStatus(){
		return this.equipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus){
		this.equipmentStatus = equipmentStatus == null ? null : equipmentStatus.trim();
	}
}
