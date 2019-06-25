package com.otec.foodsafety.entity.system;

import org.apache.ibatis.annotation.myibatis.DataBase;
import org.apache.ibatis.annotation.myibatis.Table;

import java.io.Serializable;

@Table(dataBase = DataBase.MYSQL, tabName = "T_S_RESOURCE", pkId = "resourceId", seqName = "")
public class SysResource  implements Serializable {

    private static final long serialVersionUID = 1L;

    private long resourceId;

    private String resourceName;

    private String resourceRename;

    private String resourceExtension;

    private String resourceDesc;

    private Long resourceType;

    private String resourcePath;

    private byte[] resourceContent;

    private String resourceStatus;

    private String resourceStorage;

    private Long resourceLength;

    private String resourceMd5; //  md5码


    public String getResourceRename() {
        return resourceRename;
    }

    public void setResourceRename(String resourceRename) {
        this.resourceRename = resourceRename;
    }

    public String getResourceMd5() {
        return resourceMd5;
    }

    public void setResourceMd5(String resourceMd5) {
        this.resourceMd5 = resourceMd5;
    }

    public Long getResourceLength() {
        return resourceLength;
    }

    public void setResourceLength(Long resourceLength) {
        this.resourceLength = resourceLength;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceExtension() {
        return resourceExtension;
    }

    public void setResourceExtension(String resourceExtension) {
        this.resourceExtension = resourceExtension;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public Long getResourceType() {
        return resourceType;
    }

    public void setResourceType(Long resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public byte[] getResourceContent() {
        return resourceContent;
    }

    public void setResourceContent(byte[] resourceContent) {
        this.resourceContent = resourceContent;
    }

    public String getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(String resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public String getResourceStorage() {
        return resourceStorage;
    }

    public void setResourceStorage(String resourceStorage) {
        this.resourceStorage = resourceStorage;
    }


}
