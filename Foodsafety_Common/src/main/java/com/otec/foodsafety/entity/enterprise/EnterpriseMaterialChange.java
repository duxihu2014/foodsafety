package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
/**
 * 企业原材料信息_变更
 * @author Administrator
 *
 */
@Table(pkId = "changeId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ENTERPRISE_MATERIAL_CHANGE")
public class EnterpriseMaterialChange implements Serializable{

	//变更编号 
	private Integer changeId; 

	//材料编号 
	private Integer materialId; 

	//材料名称
	private String materialName; 

	//材料类型
	private String materialType; 

	//企业编号 
	private Integer enterpriseId; 

	//原材料的规格型号
	private String specificationModel; 

	//原材料的年需求量
	private String annualDemand; 

	//原材料的执行标准代码
	private String productStandard; 

	//原材料的生产厂家
	private String manufacturer; 

	//原材料的生产国
	private String country; 

	//原材料的生产许可证编号
	private String licenseNo; 

	//审批状态 1.待审核；2.审核通过；3.审核不通过
	private String verifyStatus; 

	//变更的操作类型  1.新增；2.修改；3.删除
	private String changeType; 

	public Integer  getChangeId(){
		return this.changeId;
	}

	public void setChangeId(Integer changeId){
		this.changeId=changeId;
	}

	public Integer  getMaterialId(){
		return this.materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId=materialId;
	}

	public String  getMaterialName(){
		return this.materialName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName == null ? null : materialName.trim();
	}

	public String  getMaterialType(){
		return this.materialType;
	}

	public void setMaterialType(String materialType){
		this.materialType = materialType == null ? null : materialType.trim();
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

	public String  getAnnualDemand(){
		return this.annualDemand;
	}

	public void setAnnualDemand(String annualDemand){
		this.annualDemand = annualDemand == null ? null : annualDemand.trim();
	}

	public String  getProductStandard(){
		return this.productStandard;
	}

	public void setProductStandard(String productStandard){
		this.productStandard = productStandard == null ? null : productStandard.trim();
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

	public String  getLicenseNo(){
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo){
		this.licenseNo = licenseNo == null ? null : licenseNo.trim();
	}

	public String  getVerifyStatus(){
		return this.verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus){
		this.verifyStatus = verifyStatus == null ? null : verifyStatus.trim();
	}

	public String  getChangeType(){
		return this.changeType;
	}

	public void setChangeType(String changeType){
		this.changeType = changeType == null ? null : changeType.trim();
	}
}
