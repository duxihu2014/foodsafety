package com.otec.foodsafety.api;


import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.system.SysUserService;
import com.otec.foodsafety.web.context.SessionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserInterfafce {

    private Logger logger = LoggerFactory.getLogger(UserInterfafce.class);

    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    protected HttpServletRequest request;

    @GetMapping(value = "/getUserInfo")
    public ResponseEntity<?> getUserInfo() {

        SysUser userInfo = sessionFilter.getJWTUser(request);

        Map<Object, Object> item = new HashMap<Object, Object>();

        if(userInfo==null){
            item.put("state",0);
        }else{
            item.put("state",1);
            item.put("userInfo",userInfo);
        }
        return ResponseEntity.ok(item);
    }


    @RequestMapping(value = "/updateImg", method = RequestMethod.POST)
    public ResponseEntity<?> updateImg(Long resourceId) {
        Map<Object, Object> item = new HashMap<Object, Object>();
        SysUser userInfo = sessionFilter.getJWTUser(request);

        if (resourceId!=null&&resourceId>0) {
            item.put("state",0);
            item.put("errorMsg","resourceId不能为空");
            return ResponseEntity.ok(item);
        }

        try {
            if (userInfo.getUserId() > 0) {
                userInfo.setUserId(userInfo.getUserId());
                userInfo.setResourceId(resourceId);
                sysUserService.updateById(userInfo);
                item.put("state",1);
                return ResponseEntity.ok(item);
            } else {
                item.put("state",0);
                item.put("errorMsg","操作员不存在，无法修改密码");
                return ResponseEntity.ok(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
            resp.setStatus(500);
            resp.setMessage(e.getMessage());
            item.put("state",0);
            item.put("errorMsg",e.getMessage());
            return ResponseEntity.ok(item);
        }
    }


    @RequestMapping(value = "/updateUserPwd", method = RequestMethod.POST)
    //public ResponseEntity<?> updateUserPwd(@RequestBody SysUser sysUser) {
    public ResponseEntity<?> updateUserPwd(String userPwd, String userPwdOld) {
        Map<Object, Object> item = new HashMap<Object, Object>();
        SysUser userInfo = sessionFilter.getJWTUser(request);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userInfo.getUserId());
        sysUser.setUserPwd(userPwd);
        sysUser.setUserPwd_old(userPwdOld);
        try {
            if (sysUser.getUserId() > 0) {
                if (sysUserService.checkUserPwd(sysUser)) {
                    sysUserService.updateUserPwd(sysUser);
                    item.put("state",1);
                    return ResponseEntity.ok(item);
                } else {
                    item.put("state",0);
                    item.put("errorMsg","原密码不匹配，无法修改密码");
                    return ResponseEntity.ok(item);
                 }
            } else {
                item.put("state",0);
                item.put("errorMsg","操作员不存在，无法修改密码");
                return ResponseEntity.ok(item);
             }
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
            resp.setStatus(500);
            resp.setMessage(e.getMessage());
            item.put("state",0);
            item.put("errorMsg",e.getMessage());
            return ResponseEntity.ok(item);
        }
    }



}
