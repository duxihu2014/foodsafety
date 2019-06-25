package com.otec.foodsafety.util;

/**
 * 状态返回码枚举类
 *
 */
public enum ResultCodeConsts {

	SUCCESS("0000", "success"), 
	NOT_LOGIN("0001", "用户未登录"), 
	UNKOWN_ACCOUNT("0002", "未知账户"), 
	INCORRECT_PASSWORD("0003", "密码不正确"), 
	ALREADY_LOGIN("0004", "用户已登录"), 
	AUTHENTICATION_FAIL("0005", "认证失败"), 
	USERNAME_EXIST("0006", "用户名或设备号或手机号已被注册"), 
	ALREADY_LOCKED("0007", "账户已锁定"),
	PARAM_ERROR("0010", "参数缺失"), 
	ATTEMPT_EXCESSIVE("0008", "用户名或密码错误超过5次"),
	UNKOWN_ERROR("0009", "未知帐号错误或用户状态异常"),
	AREA_NOT_EXISTS("0014", "地区不存在"),
	PARENT_AREA_NOT_EXISTS("0011", "上级地区不存在"),
	EXTERN_URL_ERROR("0012", "获取实时视频URL失败"),
	EQUIPMENT_NOT_EXISTS("0013", "设备不存在"),
	EXIST_FAVORITE_ENTERPRISE("0015", "该企业已收藏"), 
	REPEAT_BIND("0020", "收藏夹中已经存在该设备"), 
	PAY_ACCOUNT_LACK("0021", "缺失支付平台账号"), 
	CVS_NOT_OPEN("0022", "增值业务未开通"), 
	PRODUCT_NOT_EXIST("0023", "购买的产品不存在"), 
	PRODUCT_NULL("0024", "购买的产品不能为空"),
	CVS_ID_NULL("0025", "增值业务编号不能为空"), 
	CVS_NOT_EXIST("0026", "增值业务不存在"),
	SYS_ERR("9999", "系统错误"), 
	ORDER_NOT_FOUND("0050", "订单不存在"),
	INTERFACE_CALL_ERR("0051", "接口错误"),
	INTERFACE_SIGN_ERR("0052", "接口签名错误"),
	CUSTOMERID_IS_NULL("0053","用户编码为空"),
	STATUS_IS_NULL("0054","状态为空");

	private String code;
	private String msg;

	private ResultCodeConsts(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {

		this.msg = msg;
	}

}
