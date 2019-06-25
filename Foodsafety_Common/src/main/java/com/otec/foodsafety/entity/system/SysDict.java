package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(pkId = "dictId", usingSeq = true, dataBase = DataBase.MYSQL, tabName = "T_S_DICT", seqName = "")
public class SysDict implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5036950269644360016L;

	//
	private Long dictId;

	//
	private String dictName;

	//
	private String dictDesc;

	//
	private Integer dictStatus;

	//
	private Date createTime;

	//
	private Date updateTime;

	public Long getDictId() {
		return this.dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return this.dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName == null ? null : dictName.trim();
	}

	public String getDictDesc() {
		return this.dictDesc;
	}

	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc == null ? null : dictDesc.trim();
	}

	public Integer getDictStatus() {
		return this.dictStatus;
	}

	public void setDictStatus(Integer dictStatus) {
		this.dictStatus = dictStatus;
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
