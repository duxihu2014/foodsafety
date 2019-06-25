package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource, Long> implements SysResourceService{

    @Autowired
    SysResourceMapper sysResourceMapper;

    @Autowired
    public void setMapper(SysResourceMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SysResource addResource(SysResource resource, String uploadPath) throws Exception {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(uploadPath).append(File.separator)
                    .append(UUID.randomUUID()).append(".").append(resource.getResourceExtension());
            FileUtils.copyInputStreamToFile(new ByteArrayInputStream(
                    resource.getResourceContent()), new File(stringBuilder.toString()));
            resource.setResourcePath(stringBuilder.toString());
            resource.setResourceContent(null);
            sysResourceMapper.persist(resource);
        } catch (Exception e) {
            throw e;
        }
        return resource;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SysResource addResource(SysResource resource) {
        sysResourceMapper.persist(resource);
        return resource;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delResource(Long resourceId) {
        sysResourceMapper.removeById(resourceId);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true)
    public SysResource getResourceById(Long resourceId) {
        return sysResourceMapper.findById(resourceId);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true)
    public List<SysResource> getResourcePage(SysResource resource, Integer start, Integer limit) {
        RowBounds rowBounds = new RowBounds(start, limit);
        return sysResourceMapper.getPage(resource,rowBounds);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true)
    public Integer getResourcePageCount(SysResource resource) {
        return sysResourceMapper.getPageCount(resource);
    }

    @Override
    public SysResource getResourceByMd5(SysResource resource) {
        return sysResourceMapper.getResourceByMd5(resource);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true)
    public InputStream getResourceInputStreamById(Long resourceId) throws Exception {
        InputStream inputStream = null;
        SysResource resource = getResourceById(resourceId);
        inputStream = new FileInputStream(new File(resource.getResourcePath()));

        return inputStream;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateResource(SysResource resource) throws Exception {
        try {
            sysResourceMapper.updateById(resource);
        }  catch (Exception e) {
            throw e;
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateResource(SysResource resource, String uploadPath) throws Exception {
        try {
            String delelePath=resource.getResourcePath();
            byte[] rescouceConetent=resource.getResourceContent();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(uploadPath).append(File.separator)
                    .append(UUID.randomUUID()).append(".").append(resource.getResourceExtension());
            resource.setResourcePath(stringBuilder.toString());
            resource.setResourceContent(null);
            sysResourceMapper.updateById(resource);
            // 如果原资源文件路径不为空，则删除文件系统中的原文件。
            if (StringUtils.isNotBlank(delelePath))
                FileUtils.deleteQuietly(new File(delelePath));
            // 新增文件系统文件
            FileUtils.copyInputStreamToFile(new ByteArrayInputStream(
                    rescouceConetent), new File(stringBuilder.toString()));

        }  catch (Exception e) {

            throw e;
        }
    }


}
