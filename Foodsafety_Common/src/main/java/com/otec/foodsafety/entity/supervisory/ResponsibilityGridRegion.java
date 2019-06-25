package com.otec.foodsafety.entity.supervisory;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_RESPONSIBILITY_GRID_REGION", pkId = "regionId", seqName = "",notColumn = {""})
public class ResponsibilityGridRegion implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long regionId;
	
	private String regionLatitude;
	
	private String regionLongitude;
	
	private long gridId;
	
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	 
	public long getRegionId() {
		return this.regionId;
	}

	public void setRegionLatitude(String regionLatitude) {
		this.regionLatitude = regionLatitude;
	}
	 
	public String getRegionLatitude() {
		return this.regionLatitude;
	}

	public void setRegionLongitude(String regionLongitude) {
		this.regionLongitude = regionLongitude;
	}
	 
	public String getRegionLongitude() {
		return this.regionLongitude;
	}

	public void setGridId(long gridId) {
		this.gridId = gridId;
	}
	 
	public long getGridId() {
		return this.gridId;
	}


}