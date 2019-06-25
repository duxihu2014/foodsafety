package com.otec.foodsafety.entity.safety;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Table(dataBase = DataBase.MYSQL, tabName = "T_INSPECTION_RESULT_PHOTO", pkId = "photoId", seqName = "",notColumn = {""})
public class InspectionResultPhoto implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long photoId;
	
	private long resultId;
	
	private String photoDesc;
	
	private long resourceId;
	
	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}
	 
	public long getPhotoId() {
		return this.photoId;
	}

	public void setResultId(long resultId) {
		this.resultId = resultId;
	}
	 
	public long getResultId() {
		return this.resultId;
	}

	public void setPhotoDesc(String photoDesc) {
		this.photoDesc = photoDesc;
	}
	 
	public String getPhotoDesc() {
		return this.photoDesc;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	 
	public long getResourceId() {
		return this.resourceId;
	}


}