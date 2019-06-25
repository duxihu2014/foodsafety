package com.otec.foodsafety.entity.jwt;

import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.util.vo.FrontUser;

public interface AuthService {

	String login(String clientId, String secret);

	FrontUser getUserInfo(String token);

	public SysUser getUser(String userName);

	public void invalid(String token);

	/**
	 *
	 * @param username
	 * @param password
	 * @return
	 */
    String loginSupervise(String username, String password);

	/**
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	String loginEnterprise(String username, String password);
}
