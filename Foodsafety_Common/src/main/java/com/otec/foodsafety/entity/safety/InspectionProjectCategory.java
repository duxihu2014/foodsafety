package com.otec.foodsafety.entity.safety;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_PROJECT_CATEGORY", pkId = "categoryId", seqName = "",notColumn = {""})
public class InspectionProjectCategory implements Serializable {

private static final long serialVersionUID = 1L;
	
	private int categoryId;
	
	private String categoryName;
	
	private String categoryDesc;

    private Date createTime;

	private Date updateTime;
	
	private String categoryStatus;
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	 
	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	 
	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	 
	public String getCategoryDesc() {
		return this.categoryDesc;
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

	public void setCategoryStatus(String categoryStatus) {
		this.categoryStatus = categoryStatus;
	}
	 
	public String getCategoryStatus() {
		return this.categoryStatus;
	}


}