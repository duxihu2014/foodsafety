package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.io.InputStream;
import java.util.List;

@RemoteService
public interface SysResourceService extends BaseService<SysResource,Long> {
    // 新增文件资源
    public SysResource  addResource(SysResource resource, String uploadPath) throws Exception;

    // 获取文件资源
    public SysResource getResourceById(Long resourceId);

    // 读取文件资源输出流
    public InputStream getResourceInputStreamById(Long resourceId)  throws Exception;

    // 更新文件资源
    void updateResource(SysResource image) throws Exception;
    // 更新文件资源
    public void updateResource(SysResource resource, String uploadPath) throws Exception;

    SysResource addResource(SysResource resource);

    SysResource getResourceByMd5(SysResource resource);

    List<SysResource> getResourcePage(SysResource resource, Integer start, Integer limit);

    Integer getResourcePageCount(SysResource resource);

    void delResource(Long resourceId);
}
