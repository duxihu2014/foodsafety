package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysVersion;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface SysVersionService extends BaseService<SysVersion,Long>{

	List<SysVersion> getLatestVersion(String sourceCode,String versionNum);


}
