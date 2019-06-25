package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(pkId = "logId", usingSeq = true, dataBase = DataBase.MYSQL, tabName = "T_S_LOG", seqName = "")
public class SysLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2216372364220040522L;

	//
	private Long logId;

	//
	private String actionUrl;

	//
	private Date logTime;

	//
	private String userIp;

	//
	private Long userId;

	//
	private String logDesc;

	//
	private String userName;
	
	private String controllerName; // 鎺у埗鍣ㄥ悕锟?
	private String controllerMethod; // 鎺у埗鍣ㄦ柟娉曞悕锟?
	private Long processTime; // 鍝嶅簲鏃堕棿

	public Long getLogId() {
		return this.logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getActionUrl() {
		return this.actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl == null ? null : actionUrl.trim();
	}

	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp == null ? null : userIp.trim();
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLogDesc() {
		return this.logDesc;
	}

	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc == null ? null : logDesc.trim();
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getControllerMethod() {
		return controllerMethod;
	}

	public void setControllerMethod(String controllerMethod) {
		this.controllerMethod = controllerMethod;
	}

	public Long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}
	
	
}
