package com.otec.foodsafety.entity;

import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;

import java.io.Serializable;

public class EnterpriseCertificateChangeExt implements Serializable {
    private static final long serialVersionUID = 1L;

    private EnterpriseCertificate certificate;

    private String reason; //变更原因

    public EnterpriseCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(EnterpriseCertificate certificate) {
        this.certificate = certificate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}