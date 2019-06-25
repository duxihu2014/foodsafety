package com.otec.foodsafety.web.context;

import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.TokenErrorResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.JwtTokenUtil;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SessionFilter extends AuthorizationFilter {

	protected static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

	@Value("${gate.jwt.header}")
	private String headerToken;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AuthService authService;

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
									  ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		SysUser user = getJWTUser(req);
		if (user == null) {
			HttpServletResponse httpResponse = WebUtils.toHttp(response);
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("application/json; charset=utf-8");
			PrintWriter out = null;
			try {
				out = httpResponse.getWriter();
				out.append(JSONUtils.toJson(new TokenErrorResponse(
						"Token Error or Token Expired!")));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean onPreHandle(ServletRequest request,
							   ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
//		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//			httpResponse.setHeader("Access-control-Allow-Origin", "*");
//			httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//			httpResponse.setHeader("Access-Control-Max-Age", "3600");
//			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//			httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, access-token");
//			httpResponse.setStatus(HttpStatus.OK.value());
			return true;
		}
		return isAccessAllowed(request, response, mappedValue)
				|| onAccessDenied(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
									 ServletResponse response) throws IOException {
		return false;
	}

	public SysUser getJWTUser(HttpServletRequest req) {

		String authToken = req.getHeader(this.headerToken);
		if (authToken != null) {
			String username = jwtTokenUtil.getUsernameFromToken(authToken);
			logger.info("checking authentication " + username);
			if (username != null) {
				SysUser userDetails = authService.getUser(username);
				if (jwtTokenUtil.validateToken(authToken, userDetails)) { // 验证通过
					return userDetails;
				} else { // 验证不通过
					return null;
				}
			}
		}
		return null;
	}

}
