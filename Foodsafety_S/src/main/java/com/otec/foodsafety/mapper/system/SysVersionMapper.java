package com.otec.foodsafety.mapper.system;

import com.otec.foodsafety.entity.system.SysVersion;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.List;
import java.util.Map;

public interface SysVersionMapper extends GenericMapper<SysVersion,Long>{

    public List<SysVersion> getLatestVersion(Map<String, Object> map);
}
