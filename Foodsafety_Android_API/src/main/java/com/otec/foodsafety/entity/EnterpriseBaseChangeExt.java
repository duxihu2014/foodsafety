package com.otec.foodsafety.entity;

import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class EnterpriseBaseChangeExt implements Serializable {
    private static final long serialVersionUID = 1L;

    private EnterpriseBase base;

    private String reason;  //变更原因


    public EnterpriseBase getBase() {
        return base;
    }

    public void setBase(EnterpriseBase base) {
        this.base = base;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}