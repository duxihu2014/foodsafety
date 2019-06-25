package com.otec.foodsafety.entity.jwt;

import com.otec.foodsafety.constant.RestCodeConstants;

public class TokenErrorResponse extends BaseResponse{
	
	  public TokenErrorResponse(String message) {
	  	super(RestCodeConstants.TOKEN_ERROR_CODE, message);
	  }

}
