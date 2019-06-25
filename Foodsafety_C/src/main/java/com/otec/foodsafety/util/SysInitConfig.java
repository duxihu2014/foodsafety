package com.otec.foodsafety.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;


/**
 * @author db
 * <b>功能：</b><br>
 * 加载foodsafety_config.xml
 * 
 */
public class SysInitConfig {
	protected static final Logger logger = LoggerFactory.getLogger(SysInitConfig.class);

	private static SysInitConfig instance = new SysInitConfig();

	private SysInitConfig() {
	}

	public static SysInitConfig getInstance() {
		return instance;
	}

	public void loadConfigXML() {
		// 获取配置文件
		Resource resource = new ClassPathResource("foodsafety_config.xml");
		try {
			//
			Config config = new Config(resource.getInputStream(),"//foodsafety-config/");
			fillPropMap(config);
			logger.info("display the Globals Parameter {}",propMap);
			logger.info("*************Getting  {}  is ok!*************",
					resource.getFilename());
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("*************Getting {} is err!*************",
					resource.getFilename());
		}
	}
	
	private void fillPropMap(Config config) {
		propMap.put(CfgProp.REMOTESERVERURL, config.get("path/remoteServerUrl", ""));
		propMap.put(CfgProp.IMAGESERVERURL, config.get("path/imageServerUrl", ""));
		propMap.put(CfgProp.DEFAULTPASSWORD, config.get("passwrod/defaultPassword", ""));
		propMap.put(CfgProp.VERSION, config.get("version", ""));
		propMap.put(CfgProp.HTTPCLIENTPOLICY_CONNECTIONTIMEOUT, config.get("webservice/httpClientPolicy/ConnectionTimeout", ""));
		propMap.put(CfgProp.HTTPCLIENTPOLICY_RECEIVETIMEOUT, config.get("webservice/httpClientPolicy/ReceiveTimeout", ""));
		propMap.put(CfgProp.HTTPCLIENTPOLICY_ALLOWCHUNKING, config.get("webservice/httpClientPolicy/AllowChunking", ""));
		propMap.put(CfgProp.UPLOADURL, config.get("uploadUrl", ""));
		propMap.put(CfgProp.IMAGEFOLDER, config.get("imageFolder", ""));
		propMap.put(CfgProp.BATCH_IMP_EQUIPMENT,config.get("batchImpEquipment",""));
	}
	 

	public String get(CfgProp prop) {
	   return propMap.get(prop);
	}
	
	public static enum CfgProp {
		REMOTESERVERURL,
		IMAGESERVERURL,
		DEFAULTPASSWORD,
		VERSION,
		HTTPCLIENTPOLICY_CONNECTIONTIMEOUT,
		HTTPCLIENTPOLICY_RECEIVETIMEOUT,
		HTTPCLIENTPOLICY_ALLOWCHUNKING,
		UPLOADURL,
		IMAGEFOLDER,
		BATCH_IMP_EQUIPMENT
	  }

	 
	  private Map<CfgProp, String> propMap = new HashMap<CfgProp, String>();
}