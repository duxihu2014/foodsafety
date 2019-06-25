package com.otec.foodsafety.entity.enterprise;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_SUPERVISION", pkId = "enterpriseId", seqName = "",notColumn = {""})
public class EnterpriseSupervision implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long enterpriseId;
	
	private String regulatoryLevel;
	
	private long gridId;
	
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setRegulatoryLevel(String regulatoryLevel) {
		this.regulatoryLevel = regulatoryLevel;
	}
	 
	public String getRegulatoryLevel() {
		return this.regulatoryLevel;
	}

	public void setGridId(long gridId) {
		this.gridId = gridId;
	}
	 
	public long getGridId() {
		return this.gridId;
	}


}