package com.otec.foodsafety.entity;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public class RegisterRequestParams {


    private Map<String, Object> account;
    private Map<String, Object> enterprise;
    private  Map<String, Object> license;

    public Map<String, Object> getAccount() {
        return account;
    }

    public void setAccount(Map<String, Object> account) {
        this.account = account;
    }

    public Map<String, Object> getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Map<String, Object> enterprise) {
        this.enterprise = enterprise;
    }

    public Map<String, Object> getLicense() {
        return license;
    }

    public void setLicense(Map<String, Object> license) {
        this.license = license;
    }
}
