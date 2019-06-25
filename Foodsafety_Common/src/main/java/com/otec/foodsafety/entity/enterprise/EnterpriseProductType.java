package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(pkId = "productTypeId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ENTERPRISE_PRODUCT_TYPE")
public class EnterpriseProductType implements Serializable{

	//产品分类Id
	private Long productTypeId; 

	//产品分类编号
	private String productTypeNo; 

	//产品分类名称
	private String productTypeName; 

	//父id
	private Long productTypePId; 

	public Long  getProductTypeId(){
		return this.productTypeId;
	}

	public void setProductTypeId(Long productTypeId){
		this.productTypeId=productTypeId;
	}

	public String  getProductTypeNo(){
		return this.productTypeNo;
	}

	public void setProductTypeNo(String productTypeNo){
		this.productTypeNo = productTypeNo == null ? null : productTypeNo.trim();
	}

	public String  getProductTypeName(){
		return this.productTypeName;
	}

	public void setProductTypeName(String productTypeName){
		this.productTypeName = productTypeName == null ? null : productTypeName.trim();
	}

	public Long  getProductTypePId(){
		return this.productTypePId;
	}

	public void setProductTypePId(Long productTypePId){
		this.productTypePId=productTypePId;
	}
}
