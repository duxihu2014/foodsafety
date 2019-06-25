package com.otec.foodsafety;

import org.apache.ibatis.annotation.myibatis.GenerateMapperSqlUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

//该测试用例仅支持DBCP连接池的数据源，请注意SPRING配置文件是否选用该数据源
public abstract class SpringTestCase extends AbstractJUnit4SpringContextTests {
	
	@Before
	public void before() {//这里可以预加载
		SqlSessionFactory factory = applicationContext.getBean(SqlSessionFactory.class); 
		GenerateMapperSqlUtil genMapperUtil = new GenerateMapperSqlUtil();
		genMapperUtil.setConfiguration(factory.getConfiguration());
		genMapperUtil.initMyIbatisMapper("com.otec.foodsafety");
	}
}