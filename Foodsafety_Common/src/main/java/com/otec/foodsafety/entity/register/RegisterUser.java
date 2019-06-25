package com.otec.foodsafety.entity.register;

import java.beans.Transient;
import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(dataBase = DataBase.MYSQL, tabName = "T_REGISTER_USER", pkId = "registerId", seqName = "",notColumn = {"registerBase","registerCertificate"})
public class RegisterUser implements Serializable {

private static final long serialVersionUID = 1L;
	
	private long registerId;
	
	private String userName;
	
	private String userPwd;
	
	private String realName;
	
	private String mobile;
	
	private String email;
	
	private String ipAddress;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date registerTime;
	
	private String registerStatus;

	private RegisterBase registerBase;

	private RegisterCertificate registerCertificate;
	
	public void setRegisterId(long registerId) {
		this.registerId = registerId;
	}
	 
	public long getRegisterId() {
		return this.registerId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	 
	public String getUserName() {
		return this.userName;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	 
	public String getUserPwd() {
		return this.userPwd;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	 
	public String getRealName() {
		return this.realName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	 
	public String getMobile() {
		return this.mobile;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	 
	public String getEmail() {
		return this.email;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	 
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	 
	public Date getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}
	 
	public String getRegisterStatus() {
		return this.registerStatus;
	}

	public RegisterBase getRegisterBase() {
		return registerBase;
	}

	public void setRegisterBase(RegisterBase registerBase) {
		this.registerBase = registerBase;
	}

	public RegisterCertificate getRegisterCertificate() {
		return registerCertificate;
	}

	public void setRegisterCertificate(RegisterCertificate registerCertificate) {
		this.registerCertificate = registerCertificate;
	}

}