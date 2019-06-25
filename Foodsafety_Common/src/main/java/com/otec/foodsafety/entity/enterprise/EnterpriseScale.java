package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_SCALE", pkId = "scaleId", seqName = "",notColumn = {""})
public class EnterpriseScale implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long scaleId;
	
	private long enterpriseId;
	
	private String enterpriseScale;
	
	private long enterpriseEmployees;
	
	private long enterpriseTechnicians;
	
	private Float areaFloor;
	
	private Float areaBuilding;
	
	private Float fixedAssets;
	
	private Float floatingCapital;
	
	private Float grossAnnualOutputValue;
	
	private Float annualSales;
	
	private Float annualTaxPayment;
	
	private Float annualProfit;

	public Long getScaleId() {
		return scaleId;
	}

	public void setScaleId(Long scaleId) {
		this.scaleId = scaleId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}
	 
	public String getEnterpriseScale() {
		return this.enterpriseScale;
	}

	public void setEnterpriseEmployees(long enterpriseEmployees) {
		this.enterpriseEmployees = enterpriseEmployees;
	}
	 
	public long getEnterpriseEmployees() {
		return this.enterpriseEmployees;
	}

	public void setEnterpriseTechnicians(long enterpriseTechnicians) {
		this.enterpriseTechnicians = enterpriseTechnicians;
	}
	 
	public long getEnterpriseTechnicians() {
		return this.enterpriseTechnicians;
	}

	public void setAreaFloor(Float areaFloor) {
		this.areaFloor = areaFloor;
	}
	 
	public Float getAreaFloor() {
		return this.areaFloor;
	}

	public void setAreaBuilding(Float areaBuilding) {
		this.areaBuilding = areaBuilding;
	}
	 
	public Float getAreaBuilding() {
		return this.areaBuilding;
	}

	public void setFixedAssets(Float fixedAssets) {
		this.fixedAssets = fixedAssets;
	}
	 
	public Float getFixedAssets() {
		return this.fixedAssets;
	}

	public void setFloatingCapital(Float floatingCapital) {
		this.floatingCapital = floatingCapital;
	}
	 
	public Float getFloatingCapital() {
		return this.floatingCapital;
	}

	public void setGrossAnnualOutputValue(Float grossAnnualOutputValue) {
		this.grossAnnualOutputValue = grossAnnualOutputValue;
	}
	 
	public Float getGrossAnnualOutputValue() {
		return this.grossAnnualOutputValue;
	}

	public void setAnnualSales(Float annualSales) {
		this.annualSales = annualSales;
	}
	 
	public Float getAnnualSales() {
		return this.annualSales;
	}

	public void setAnnualTaxPayment(Float annualTaxPayment) {
		this.annualTaxPayment = annualTaxPayment;
	}
	 
	public Float getAnnualTaxPayment() {
		return this.annualTaxPayment;
	}

	public void setAnnualProfit(Float annualProfit) {
		this.annualProfit = annualProfit;
	}
	 
	public Float getAnnualProfit() {
		return this.annualProfit;
	}


}