package com.otec.foodsafety.entity.purchase;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_SUPPLIER_SERVICE_ENTERPRISE", pkId = "enterpriseSupplierId", seqName = "",notColumn = {""})
public class SupplierServiceEnterprise implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long enterpriseSupplierId;
	
	private long enterpriseId;
	
	private long supplierId;
	
	public void setEnterpriseSupplierId(long enterpriseSupplierId) {
		this.enterpriseSupplierId = enterpriseSupplierId;
	}
	 
	public long getEnterpriseSupplierId() {
		return this.enterpriseSupplierId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	 
	public long getSupplierId() {
		return this.supplierId;
	}


}