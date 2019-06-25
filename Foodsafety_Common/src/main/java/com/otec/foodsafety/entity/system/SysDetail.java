package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(pkId = "detailId", usingSeq = true, dataBase = DataBase.MYSQL, tabName = "T_S_DETAIL", seqName = "")
public class SysDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2939687590595282954L;

	//
	private Long detailId;

	//
	private Integer dictId;

	//
	private String detailName;

	//
	private String detailValue;

	//
	private String detailDesc;

	//
	private Integer detailStatus;

	//
	private Date createTime;

	//
	private Date updateTime;

	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Integer getDictId() {
		return this.dictId;
	}

	public void setDictId(Integer dictId) {
		this.dictId = dictId;
	}

	public String getDetailName() {
		return this.detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName == null ? null : detailName.trim();
	}

	public String getDetailValue() {
		return this.detailValue;
	}

	public void setDetailValue(String detailValue) {
		this.detailValue = detailValue;
	}

	public String getDetailDesc() {
		return this.detailDesc;
	}

	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc == null ? null : detailDesc.trim();
	}

	public Integer getDetailStatus() {
		return this.detailStatus;
	}

	public void setDetailStatus(Integer detailStatus) {
		this.detailStatus = detailStatus;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
