package com.otec.foodsafety.util.vo;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String,Object> {
	public Result() {

		put("code", 0);
	}

	public static Result ok(String msg) {
		Result r = new Result();
		r.put("msg", msg);
		return r;

	}

	public static Result ok() {
		Result r = new Result();
		return r;

	}

	public static Result error(int code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Result ok(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}

	public static Result error(String message) {
		return error(500, message);

	}

	public static Result error() {
		return error(500, "未知异常，请联系管理员");
	}

	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}

}
