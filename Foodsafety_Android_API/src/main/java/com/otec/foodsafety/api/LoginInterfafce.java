package com.otec.foodsafety.api;


import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.JwtAuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/login")
public class LoginInterfafce {
    private Logger logger = LoggerFactory.getLogger(LoginInterfafce.class);

    @Autowired
    private AuthService authService;


    /**
     * 监管用户登录
     * @param authenticationRequest
     * @return
     */
    @RequestMapping(value = "/supervise")
    public ResponseEntity<?> superviseCreateAuthenticationToken(
            @RequestBody  JwtAuthenticationRequest authenticationRequest) {
        final String token = authService.loginSupervise(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword());

        Map<Object, Object> item = new HashMap<Object, Object>();
        if(token.equals("")){
            item.put("state", 0);
            item.put("errorMsg", "用户名或密码错误");
        }else {
            item.put("state", 1);
            item.put("token", token);

        }
        return ResponseEntity.ok(item);
    }

    /**
     * 企业用户登录
     * @param authenticationRequest
     * @return
     */
    @RequestMapping(value = "/enterprise")
    public ResponseEntity<?> enterpriseCreateAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        final String token = authService.loginEnterprise(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword());

        Map<Object, Object> item = new HashMap<Object, Object>();
        if(token.equals("")){
            item.put("state", 0);
            item.put("errorMsg", "用户名或密码错误");
        }else {
            item.put("state", 1);
            item.put("token", token);

        }

        return ResponseEntity.ok(item);

      //  return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }


    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity<?> invalid(@RequestHeader("${gate.jwt.header}") String token) {
        authService.invalid(token);
        Map<Object, Object> item = new HashMap<Object, Object>();
        item.put("state", 1);
        item.put("token", token);
        return ResponseEntity.ok(item);
    }
}
