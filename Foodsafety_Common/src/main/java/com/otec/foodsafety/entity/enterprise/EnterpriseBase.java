package com.otec.foodsafety.entity.enterprise;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Table(dataBase = DataBase.MYSQL, tabName = "T_ENTERPRISE_BASE", pkId = "enterpriseId", seqName = "",notColumn = {""})
public class EnterpriseBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private long enterpriseId;

    private String enterpriseName;

    private long areaId;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date registerDate;

    private String registerAddress;

    private String productionAddress;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operatingTerm;

    private String organizingInstitutionBarCode;

    private String subjectClassification;

    private String superviseClassification;

    private String corporateRepresentative;

    private String idCardNo;

    private String registeredCapital;

    private String economicNature;

    private String operationScope;

    private String postalCode;

    private String email;

    private String fax;

    private String contacts;

    private String contactNumber;

    private String contactMobile;

    private String inspectorMobile;

    private String enterpriseStatus;

    private String latitude;

    private String longitude;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date archivedDate;

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress == null ? null : registerAddress.trim();
    }

    public String getProductionAddress() {
        return productionAddress;
    }

    public void setProductionAddress(String productionAddress) {
        this.productionAddress = productionAddress == null ? null : productionAddress.trim();
    }

    public Date getOperatingTerm() {
        return operatingTerm;
    }

    public void setOperatingTerm(Date operatingTerm) {
        this.operatingTerm = operatingTerm;
    }

    public String getOrganizingInstitutionBarCode() {
        return organizingInstitutionBarCode;
    }

    public void setOrganizingInstitutionBarCode(String organizingInstitutionBarCode) {
        this.organizingInstitutionBarCode = organizingInstitutionBarCode == null ? null : organizingInstitutionBarCode.trim();
    }

    public String getSubjectClassification() {
        return subjectClassification;
    }

    public void setSubjectClassification(String subjectClassification) {
        this.subjectClassification = subjectClassification == null ? null : subjectClassification.trim();
    }

    public String getSuperviseClassification() {
        return superviseClassification;
    }

    public void setSuperviseClassification(String superviseClassification) {
        this.superviseClassification = superviseClassification == null ? null : superviseClassification.trim();
    }

    public String getCorporateRepresentative() {
        return corporateRepresentative;
    }

    public void setCorporateRepresentative(String corporateRepresentative) {
        this.corporateRepresentative = corporateRepresentative == null ? null : corporateRepresentative.trim();
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital == null ? null : registeredCapital.trim();
    }

    public String getEconomicNature() {
        return economicNature;
    }

    public void setEconomicNature(String economicNature) {
        this.economicNature = economicNature == null ? null : economicNature.trim();
    }

    public String getOperationScope() {
        return operationScope;
    }

    public void setOperationScope(String operationScope) {
        this.operationScope = operationScope == null ? null : operationScope.trim();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber == null ? null : contactNumber.trim();
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile == null ? null : contactMobile.trim();
    }

    public String getInspectorMobile() {
        return inspectorMobile;
    }

    public void setInspectorMobile(String inspectorMobile) {
        this.inspectorMobile = inspectorMobile == null ? null : inspectorMobile.trim();
    }

    public String getEnterpriseStatus() {
        return enterpriseStatus;
    }

    public void setEnterpriseStatus(String enterpriseStatus) {
        this.enterpriseStatus = enterpriseStatus == null ? null : enterpriseStatus.trim();
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }
}