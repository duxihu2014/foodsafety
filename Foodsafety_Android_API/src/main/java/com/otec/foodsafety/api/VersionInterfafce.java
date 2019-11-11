package com.otec.foodsafety.api;


import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysFeedback;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.entity.system.SysVersion;
import com.otec.foodsafety.service.system.SysFeedbackService;
import com.otec.foodsafety.service.system.SysVersionService;
import com.otec.foodsafety.web.context.SessionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/version")
public class VersionInterfafce {

    private Logger logger = LoggerFactory.getLogger(VersionInterfafce.class);


    @Autowired
    private SysVersionService sysVersionService;

    @Autowired
    private SysFeedbackService sysFeedbackService;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private SessionFilter sessionFilter;

    /**
     * @param sourceCode  (sourceCode=1 android用户版本，sourceCode=2 ios用户版本，
     */
    @GetMapping(value = "/updateApp")
    public ResponseEntity<?>  checkUpdate(String sourceCode, String versionNum){
        Map<Object, Object> item = new HashMap<Object, Object>();
        try {
//            // 检查更新来源是否为空。
//            if (StringUtils.isEmpty(sourceCode)) {
//                return Jsonp.paramError("来源为空");
//            }
//            // 检查版本号是否为空。
//            if (StringUtils.isEmpty(versionNum)) {
//                return Jsonp.paramError("版本号为空");
//            }
            List<SysVersion> sysmgrVersionConfigList = sysVersionService.getLatestVersion(sourceCode, versionNum);
            if(sysmgrVersionConfigList!=null&&sysmgrVersionConfigList.size()>0){
                SysVersion sysmgrVersionConfig =  (SysVersion)sysmgrVersionConfigList.get(0);
                item.put("isForce",2);
                item.put("name",sysmgrVersionConfig.getName());
                item.put("versionNum",sysmgrVersionConfig.getVersionNum());
                item.put("content",sysmgrVersionConfig.getFeature());
                item.put("updateUrl",sysmgrVersionConfig.getUrl());
                // 返回最新版本内容
                item.put("state",1);
                return ResponseEntity.ok(item);
            }
            else{
                item.put("state",0);
                item.put("errorMsg","暂最新版本!");
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



    @GetMapping(value = "/updateLog")
    public ResponseEntity<?>  updateLog(){
        Map<Object, Object> item = new HashMap<Object, Object>();
        try {
                item.put("state",1);
                item.put("updateLogUrl","http://60.165.161.141:9000/img/userfiles/html/worker/versionLog.html");
                return ResponseEntity.ok(item);
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




    @GetMapping(value = "/feedbacksave")
    public ResponseEntity<?> getUserInfo(String content,String sourceCode) {

        SysUser userInfo = sessionFilter.getJWTUser(request);

        SysFeedback sysFeedback = new SysFeedback();
        sysFeedback.setContent(content);
        if(userInfo!=null){
            sysFeedback.setUserId(String.valueOf(userInfo.getUserId()));
        }
        sysFeedback.setType(1);
        sysFeedback.setCreateTime(new Date());
        sysFeedback.setSourceCode(sourceCode);
        sysFeedbackService.persist(sysFeedback);
        Map<Object, Object> item = new HashMap<Object, Object>();
        item.put("state",1);
        return ResponseEntity.ok(item);
    }




}
