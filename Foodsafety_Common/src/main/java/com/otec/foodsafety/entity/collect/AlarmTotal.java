package com.otec.foodsafety.entity.collect;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ALARM_TOTAL", pkId = "totalId", seqName = "", notColumn = {""})
public class AlarmTotal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long totalId;
	
	private String alarmYear;
	
	private String alarmMonth;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private long totalAll;
	
	private int totalVideo;
	
	private int totalSensor;
	
	private int eventId1;
	
	private int eventId2;
	
	private int eventId3;
	
	private int eventId4;
	
	private int eventId10001;
	
	private int eventId10002;
	
	private int eventId10003;
	
	private int eventId10004;
	
	private int status1;
	
	private int status2;

	private int ranking;
	
	private int rankingLastMonth;
	
	public void setTotalId(Long totalId) {
		this.totalId = totalId;
	}
	 
	public Long getTotalId() {
		return this.totalId;
	}

	public void setAlarmYear(String alarmYear) {
		this.alarmYear = alarmYear;
	}
	 
	public String getAlarmYear() {
		return this.alarmYear;
	}

	public void setAlarmMonth(String alarmMonth) {
		this.alarmMonth = alarmMonth;
	}
	 
	public String getAlarmMonth() {
		return this.alarmMonth;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	 
	public Long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	 
	public String getEnterpriseName() {
		return this.enterpriseName;
	}

//	public void setTotalAll(int totalAll) {
//		this.totalAll = totalAll;
//	}
//
//	public int getTotalAll() {
//		return this.totalAll;
//	}
	public long getTotalAll() {
		return totalAll;
	}

	public void setTotalAll(long totalAll) {
		this.totalAll = totalAll;
	}

	public void setTotalVideo(int totalVideo) {
		this.totalVideo = totalVideo;
	}
	 
	public int getTotalVideo() {
		return this.totalVideo;
	}

	public void setTotalSensor(int totalSensor) {
		this.totalSensor = totalSensor;
	}
	 
	public int getTotalSensor() {
		return this.totalSensor;
	}

	public void setEventId1(int eventId1) {
		this.eventId1 = eventId1;
	}
	 
	public int getEventId1() {
		return this.eventId1;
	}

	public void setEventId2(int eventId2) {
		this.eventId2 = eventId2;
	}
	 
	public int getEventId2() {
		return this.eventId2;
	}

	public void setEventId3(int eventId3) {
		this.eventId3 = eventId3;
	}
	 
	public int getEventId3() {
		return this.eventId3;
	}

	public void setEventId4(int eventId4) {
		this.eventId4 = eventId4;
	}
	 
	public int getEventId4() {
		return this.eventId4;
	}

	public void setEventId10001(int eventId10001) {
		this.eventId10001 = eventId10001;
	}
	 
	public int getEventId10001() {
		return this.eventId10001;
	}

	public void setEventId10002(int eventId10002) {
		this.eventId10002 = eventId10002;
	}
	 
	public int getEventId10002() {
		return this.eventId10002;
	}

	public void setEventId10003(int eventId10003) {
		this.eventId10003 = eventId10003;
	}
	 
	public int getEventId10003() {
		return this.eventId10003;
	}

	public void setEventId10004(int eventId10004) {
		this.eventId10004 = eventId10004;
	}
	 
	public int getEventId10004() {
		return this.eventId10004;
	}

	public void setStatus1(int status1) {
		this.status1 = status1;
	}
	 
	public int getStatus1() {
		return this.status1;
	}

	public void setStatus2(int status2) {
		this.status2 = status2;
	}
	 
	public int getStatus2() {
		return this.status2;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	 
	public int getRanking() {
		return this.ranking;
	}

	public void setRankingLastMonth(int rankingLastMonth) {
		this.rankingLastMonth = rankingLastMonth;
	}
	 
	public int getRankingLastMonth() {
		return this.rankingLastMonth;
	}


}