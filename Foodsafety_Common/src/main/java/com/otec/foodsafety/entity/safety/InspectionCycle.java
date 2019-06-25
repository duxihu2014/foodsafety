package com.otec.foodsafety.entity.safety;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_CYCLE", pkId = "cycleId", seqName = "",notColumn = {""})
public class InspectionCycle implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int cycleId;
	
	private Long areaId;
	
	private long settingInstitution;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date settingTime;

	/*@DateTimeFormat(pattern="yyyy-MM-dd")*/
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date initialInspection;
	
	private int aheadDays;
	
	private int inspectionCycleA;
	
	private int inspectionCycleB;
	
	private int inspectionCycleC;
	
	private int inspectionCycleD;
	
	public void setCycleId(int cycleId) {
		this.cycleId = cycleId;
	}
	 
	public int getCycleId() {
		return this.cycleId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	 
	public Long getAreaId() {
		return this.areaId;
	}

	public void setSettingInstitution(long settingInstitution) {
		this.settingInstitution = settingInstitution;
	}
	 
	public long getSettingInstitution() {
		return this.settingInstitution;
	}

	public void setSettingTime(Date settingTime) {
		this.settingTime = settingTime;
	}
	 
	public Date getSettingTime() {
		return this.settingTime;
	}

	public void setInitialInspection(Date initialInspection) {
		this.initialInspection = initialInspection;
	}
	 
	public Date getInitialInspection() {
		return this.initialInspection;
	}

	public void setAheadDays(int aheadDays) {
		this.aheadDays = aheadDays;
	}
	 
	public int getAheadDays() {
		return this.aheadDays;
	}

	public void setInspectionCycleA(int inspectionCycleA) {
		this.inspectionCycleA = inspectionCycleA;
	}
	 
	public int getInspectionCycleA() {
		return this.inspectionCycleA;
	}

	public void setInspectionCycleB(int inspectionCycleB) {
		this.inspectionCycleB = inspectionCycleB;
	}
	 
	public int getInspectionCycleB() {
		return this.inspectionCycleB;
	}

	public void setInspectionCycleC(int inspectionCycleC) {
		this.inspectionCycleC = inspectionCycleC;
	}
	 
	public int getInspectionCycleC() {
		return this.inspectionCycleC;
	}

	public void setInspectionCycleD(int inspectionCycleD) {
		this.inspectionCycleD = inspectionCycleD;
	}
	 
	public int getInspectionCycleD() {
		return this.inspectionCycleD;
	}


}