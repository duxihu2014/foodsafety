package com.otec.foodsafety.entity.supervisory;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_RESPONSIBILITY_GRID", pkId = "gridId", seqName = "",notColumn = {"lat","lng","markers","standardsName","assistantsName","monitorsName"})
public class ResponsibilityGrid implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long gridId;
	
	private String gridName;
	
	private long areaId;
	
	private String townStreet;
	
	private String committeeVillage;
	
	private long monitors; //监管员编号
	
	private long assistants; //协管员编号
	
	private long standards; //检验员编号
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	private String centerLatitude;
	
	private String centerLongitude;
	
	private long institutionId;

	private String gridStatus;

	private String monitorsName;
	private String assistantsName;
	private String standardsName;
	private String lng;
	private String lat;

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	private List<String[]>  markers;

	public List<String[]> getMarkers() {
		return markers;
	}

	public void setMarkers(List<String[]> markers) {
		this.markers = markers;
	}

	public String getMonitorsName() {
		return monitorsName;
	}

	public void setMonitorsName(String monitorsName) {
		this.monitorsName = monitorsName;
	}

	public String getAssistantsName() {
		return assistantsName;
	}

	public void setAssistantsName(String assistantsName) {
		this.assistantsName = assistantsName;
	}

	public String getStandardsName() {
		return standardsName;
	}

	public void setStandardsName(String standardsName) {
		this.standardsName = standardsName;
	}

	public void setGridId(long gridId) {
		this.gridId = gridId;
	}
	 
	public long getGridId() {
		return this.gridId;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	 
	public String getGridName() {
		return this.gridName;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	 
	public long getAreaId() {
		return this.areaId;
	}

	public void setTownStreet(String townStreet) {
		this.townStreet = townStreet;
	}
	 
	public String getTownStreet() {
		return this.townStreet;
	}

	public void setCommitteeVillage(String committeeVillage) {
		this.committeeVillage = committeeVillage;
	}
	 
	public String getCommitteeVillage() {
		return this.committeeVillage;
	}

	public void setMonitors(long monitors) {
		this.monitors = monitors;
	}
	 
	public long getMonitors() {
		return this.monitors;
	}

	public void setAssistants(long assistants) {
		this.assistants = assistants;
	}
	 
	public long getAssistants() {
		return this.assistants;
	}

	public void setStandards(long standards) {
		this.standards = standards;
	}
	 
	public long getStandards() {
		return this.standards;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	 
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCenterLatitude(String centerLatitude) {
		this.centerLatitude = centerLatitude;
	}
	 
	public String getCenterLatitude() {
		return this.centerLatitude;
	}

	public void setCenterLongitude(String centerLongitude) {
		this.centerLongitude = centerLongitude;
	}
	 
	public String getCenterLongitude() {
		return this.centerLongitude;
	}

	public void setInstitutionId(long institutionId) {
		this.institutionId = institutionId;
	}
	 
	public long getInstitutionId() {
		return this.institutionId;
	}

    public String getGridStatus() {
        return gridStatus;
    }

    public void setGridStatus(String gridStatus) {
        this.gridStatus = gridStatus;
    }
}