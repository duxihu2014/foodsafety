package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;

@Table(dataBase = DataBase.MYSQL, tabName = "T_S_USER_INSTITUTION", pkId = "userInstitutionId", seqName = "",notColumn = {""})
public class SysUserInstitution implements Serializable {

    private long userInstitutionId;

    private long userId;

    private long institutionId;

    private long areaId;

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getUserInstitutionId() {
        return userInstitutionId;
    }

    public void setUserInstitutionId(long userInstitutionId) {
        this.userInstitutionId = userInstitutionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(long institutionId) {
        this.institutionId = institutionId;
    }
}