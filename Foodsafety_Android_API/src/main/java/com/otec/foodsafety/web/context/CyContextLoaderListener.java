package com.otec.foodsafety.web.context;

import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.util.SysInitConfig.CfgProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.client.RemoteServiceScannerConfigurer;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * 系统启动时加载
 * @version 1.0
 */
public class CyContextLoaderListener extends ContextLoaderListener implements ServletContextListener {


	protected static final Logger logger = LoggerFactory.getLogger(CyContextLoaderListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) { 
		super.contextDestroyed(event);
	}
	 
	 
	public void contextInitialized(ServletContextEvent event) {
		logger.info("begin init foodsafety_config.xml");
		SysInitConfig.getInstance().loadConfigXML();
		logger.info("end init foodsafety_config.xml");
		ServletContext servletContext = event.getServletContext();
		
		RemoteServiceScannerConfigurer.setBasePackage(servletContext.getInitParameter("ScanServicePackage"));
		RemoteServiceScannerConfigurer.setBaseUrl(SysInitConfig.getInstance().get(CfgProp.REMOTESERVERURL));
		super.contextInitialized(event);
		
		servletContext.setAttribute("jsVersion", SysInitConfig.getInstance().get(CfgProp.VERSION));
		servletContext.setAttribute("imageServerUrl", SysInitConfig.getInstance().get(CfgProp.IMAGESERVERURL));
	}
	
}
