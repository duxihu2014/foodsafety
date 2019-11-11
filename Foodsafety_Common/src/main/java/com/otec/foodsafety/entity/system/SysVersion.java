package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Table(dataBase = DataBase.MYSQL, tabName = "t_s_version_config", pkId = "id", seqName = "")
public class SysVersion implements Serializable {

private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;

    private Integer versionNum;

	private String sourceCode;

	private Integer isFource;
	
	private String feature;
	
	private String url;
	
	private String isDeleted;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Integer versionNum) {
		this.versionNum = versionNum;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Integer getIsFource() {
		return isFource;
	}

	public void setIsFource(Integer isFource) {
		this.isFource = isFource;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}