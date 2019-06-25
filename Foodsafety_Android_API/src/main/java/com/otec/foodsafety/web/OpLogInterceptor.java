package com.otec.foodsafety.web;

import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.system.SysLog;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.system.SysLogService;
import com.otec.foodsafety.util.Globals;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作员操作日志拦截器
 * 
 * @author db
 * 
 */

public class OpLogInterceptor extends HandlerInterceptorAdapter {
	protected static final Logger logger = LoggerFactory
			.getLogger(OpLogInterceptor.class);
	private final ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<StopWatch>();
	@Autowired
	SysLogService sysLogService;

	@Value("${gate.jwt.header}")
	private String headerToken;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthService authService;

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行
	 * ，而且所有的Interceptor中的preHandle方法都会在
	 * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的
	 * ，这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		StopWatch stopWatch = new StopWatch(handler.toString());
		stopWatchLocal.set(stopWatch);
		stopWatch.start(handler.toString());

		return true;
	}

	/**
	 * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，
	 * 它的执行时间是在处理器进行处理之
	 * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行
	 * ，也就是说在这个方法中你可以对ModelAndView进行操
	 * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用
	 * ，这跟Struts2里面的拦截器的执行过程有点像，
	 * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法
	 * ，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
	 * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前
	 * ，要在Interceptor之后调用的内容都写在调用invoke方法之后。
	 */
	/*
	 * @Override public void postHandle(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, ModelAndView modelAndView)
	 * throws Exception { }
	 */

	/**
	 * 生成日志
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		StopWatch stopWatch = stopWatchLocal.get();
		stopWatchLocal.set(null);
		long processTime = 0;
		if (stopWatch != null) {
			stopWatch.stop();
			processTime = stopWatch.getTotalTimeMillis();
		}
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();

			Object requestBody=request.getAttribute("request-body-data");
			Object responseBody=request.getAttribute("response-body-data");
			request.removeAttribute("request-body-data");
			request.removeAttribute("response-body-data");
			String reqMethod = request.getMethod();
			//if (!RequestMethod.GET.toString().equals(reqMethod)) { // 查询不记录日志
				logger.info("\n-------------------------------------------------请求信息-------------------------------------------------\n"
								+" URI : {} \n Controller : {} \n Method : {}\n RequestMethod : {} \n Token : {} \n Params : {} \n Data : {} \n Result: {}"
								+"\n----------------------------------------------------------------------------------------------------------",
						request.getRequestURI(),
						handlerMethod.getBean().getClass().getName(),
						method.getName(),
						reqMethod,
						request.getHeader(this.headerToken),
						JSONUtils.toJson(request.getParameterMap()),
						JSONUtils.toJson(requestBody),
						JSONUtils.toJson(responseBody)
				);
		//	}
		}

	}

}
