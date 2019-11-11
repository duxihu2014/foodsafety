package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.SysVersion;
import com.otec.foodsafety.mapper.system.SysVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysVersionServiceImpl extends BaseServiceImpl<SysVersion, Long>
		implements SysVersionService {


	@Autowired
	private SysVersionMapper sysVersionMapper;

	@Autowired
	public void setMapper(SysVersionMapper mapper) {
		setGenericMapper(mapper);
	}


	@Override
	public List<SysVersion> getLatestVersion(String sourceCode, String versionNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceCode", sourceCode);
		map.put("versionNum", versionNum);
		return sysVersionMapper.getLatestVersion(map);
	}
}
