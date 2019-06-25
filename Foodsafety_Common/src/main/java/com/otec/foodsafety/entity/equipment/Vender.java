package com.otec.foodsafety.entity.equipment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

@Table(pkId = "venderId", usingSeq = true, 
dataBase = DataBase.MYSQL, tabName = "T_VENDER", seqName = "",notColumn = {})
public class Vender implements Serializable {

	/**
	 * 厂商信息
	 */
	private static final long serialVersionUID = -8446871706107428945L;
	
	/*
	 * VENDER_ID  厂商编号
	 * VENDER_NO  厂商编码
	 * VENDER_NAME 厂商名称
	 * VENDER_LINKMAN  联系人
	 * CREATE_TIME 创建时间
	 * OP_ID  创建人
	 * STATUS 状态
	 * */
	
	private Integer venderId;
	private Integer venderNo;
	private String  venderName;
	private String  venderLinkman;
	private Date createTime;
	private Integer opId;
	private String status;
	public Integer getVenderId() {
		return venderId;
	}
	public void setVenderId(Integer venderId) {
		this.venderId = venderId;
	}
	public Integer getVenderNo() {
		return venderNo;
	}
	public void setVenderNo(Integer venderNo) {
		this.venderNo = venderNo;
	}
	public String getVenderName() {
		return venderName;
	}
	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}
	public String getVenderLinkman() {
		return venderLinkman;
	}
	public void setVenderLinkman(String venderLinkman) {
		this.venderLinkman = venderLinkman;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getOpId() {
		return opId;
	}
	public void setOpId(Integer opId) {
		this.opId = opId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}