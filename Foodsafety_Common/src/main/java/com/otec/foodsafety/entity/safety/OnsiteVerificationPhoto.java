package com.otec.foodsafety.entity.safety;

import java.io.Serializable;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(pkId = "photoId", usingSeq = true, seqName = "", dataBase=DataBase.MYSQL, tabName = "T_ONSITE_VERIFICATION_PHOTO")
public class OnsiteVerificationPhoto implements Serializable{

	//
	private Long photoId;

	//
	private Long resultId;

	//
	private String photoDesc; 

	//
	private Long resourceId;

	public String  getPhotoDesc(){
		return this.photoDesc;
	}

	public void setPhotoDesc(String photoDesc){
		this.photoDesc = photoDesc == null ? null : photoDesc.trim();
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
}
