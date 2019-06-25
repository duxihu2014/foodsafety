package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;
import java.util.Date;

@Table(dataBase = DataBase.MYSQL, tabName = "T_S_INSTITUTION", pkId = "institutionId", seqName = "",notColumn = {"pInstitutionName","areaName"})
public class SysInstitution implements Serializable {
    private long institutionId;

    private String institutionName;

    private String institutionDesc;

    private String institutionType;

    private long pInstitutionId;

    private String pInstitutionName;

    private long areaId;

    private String areaName;

    private String contactPhone;

    private String contactAddress;

    private String institutionStatus;

    private Date createTime;

    private Date updateTime;

    public long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(long institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName == null ? null : institutionName.trim();
    }

    public String getInstitutionDesc() {
        return institutionDesc;
    }

    public void setInstitutionDesc(String institutionDesc) {
        this.institutionDesc = institutionDesc == null ? null : institutionDesc.trim();
    }

    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType == null ? null : institutionType.trim();
    }

    public long getpInstitutionId() {
        return pInstitutionId;
    }

    public void setpInstitutionId(long pInstitutionId) {
        this.pInstitutionId = pInstitutionId;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
    }

    public String getInstitutionStatus() {
        return institutionStatus;
    }

    public void setInstitutionStatus(String institutionStatus) {
        this.institutionStatus = institutionStatus == null ? null : institutionStatus.trim();
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

    public String getpInstitutionName() {
        return pInstitutionName;
    }

    public void setpInstitutionName(String pInstitutionName) {
        this.pInstitutionName = pInstitutionName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}