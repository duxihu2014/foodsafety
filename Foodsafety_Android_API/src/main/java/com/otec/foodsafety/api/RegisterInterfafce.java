package com.otec.foodsafety.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.RegisterRequestParams;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.register.RegisterService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/register")
@RestController
public class RegisterInterfafce {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private DateLocalService dateLocalService;

    /**
     * 企业注册
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<?> addRegister(HttpServletRequest request, @RequestBody RegisterRequestParams params) {
         try {
            Map<String, Object> accountMap=params.getAccount();
            accountMap.put("userPwd", MD5Encoder.encode((String) accountMap.get("userPwd")));
            accountMap.put("ipAddress", request.getRemoteAddr());
            accountMap.put("registerTime", dateLocalService.getDataBaseDate());
            accountMap.put("registerStatus", 0);

            registerService.addRegister(accountMap, params.getEnterprise(), params.getLicense());
            return new ObjectRestResponse<>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<>().rel(false);
            resp.setStatus(500);
            resp.setMessage("企业注册失败");
            return resp;
        }

    }


}
