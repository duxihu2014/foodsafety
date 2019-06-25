package com.otec.foodsafety.mapper.system;

import com.otec.foodsafety.entity.system.SysResource;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SysResourceMapper extends GenericMapper<SysResource, Long> {
    SysResource getResourceByMd5(SysResource sysResource);
    List<SysResource> getPage(SysResource sysResource, RowBounds rowBounds);
    Integer getPageCount(SysResource sysResource);
}