package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;

@Table(dataBase = DataBase.MYSQL, tabName = "T_S_USER_ENTERPRISE", pkId = "userEnterpriseId", seqName = "",notColumn = {""})
public class SysUserEnterprise implements Serializable {
    private long userEnterpriseId;

    private long userId;

    private long areaId;

    private long enterpriseId;

    public long getUserEnterpriseId() {
        return userEnterpriseId;
    }

    public void setUserEnterpriseId(long userEnterpriseId) {
        this.userEnterpriseId = userEnterpriseId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}