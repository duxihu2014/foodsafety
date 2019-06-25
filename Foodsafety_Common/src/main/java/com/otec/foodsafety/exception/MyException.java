package com.otec.foodsafety.exception;

import com.otec.foodsafety.util.ResultCodeConsts;

public class MyException extends Exception {

	private ResultCodeConsts result;

	public MyException() {
		super();
	}

	public MyException(ResultCodeConsts result) {
		super(result.getMsg());
		this.result=  result;
		
	}

	public MyException(Throwable cause) {
		super(cause);
	}

	public MyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResultCodeConsts getResult() {
		return result;
	}

	public void setErrMsg(String message) {
		result.setMsg(message);
	}

}
