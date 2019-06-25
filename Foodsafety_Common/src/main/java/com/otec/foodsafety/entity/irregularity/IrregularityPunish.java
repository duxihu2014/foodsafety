package com.otec.foodsafety.entity.irregularity;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_IRREGULARITY_PUNISH", pkId = "punishId", seqName = "",notColumn = {""})
public class IrregularityPunish implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long punishId;
	
	private long irregularityId;
	
	private String content;

	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date punishmentDate;
	
	private String fineSituation;
	
	public void setPunishId(long punishId) {
		this.punishId = punishId;
	}
	 
	public long getPunishId() {
		return this.punishId;
	}

	public void setIrregularityId(long irregularityId) {
		this.irregularityId = irregularityId;
	}
	 
	public long getIrregularityId() {
		return this.irregularityId;
	}

	public void setContent(String content) {
		this.content = content;
	}
	 
	public String getContent() {
		return this.content;
	}

	public void setPunishmentDate(Date punishmentDate) {
		this.punishmentDate = punishmentDate;
	}
	 
	public Date getPunishmentDate() {
		return this.punishmentDate;
	}

	public void setFineSituation(String fineSituation) {
		this.fineSituation = fineSituation;
	}
	 
	public String getFineSituation() {
		return this.fineSituation;
	}


}