package com.otec.foodsafety.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.annotation.myibatis.GenerateMapperSqlUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;


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
		super.contextInitialized(event);
		
		ServletContext sc = event.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
		GenerateMapperSqlUtil genMapperUtil = new GenerateMapperSqlUtil();
		genMapperUtil.setConfiguration(factory.getConfiguration());
		genMapperUtil.initMyIbatisMapper(sc.getInitParameter("contextScanPackage"));
		
		ServletContext servletContext = event.getServletContext();
		if (servletContext != null){
			ObjectFactory.getInstance(servletContext);
		}
		SystemCache.cacheAllDict(); 
	}
	
	
	
	
}
