package com.otec.foodsafety.web.register;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.register.RegisterBase;
import com.otec.foodsafety.entity.register.RegisterCertificate;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.entity.register.RegisterVerify;
import com.otec.foodsafety.entity.system.AreaTree;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.register.RegisterService;
import com.otec.foodsafety.service.register.RegisterVerifyService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.service.system.SysDictService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.service.system.SysUserService;
import com.otec.foodsafety.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/register/api")
@RestController
public class RegisterController {
    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysAreaService areaService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private RegisterVerifyService registerVerifyService;

    /*获取字典表数据*/
    @GetMapping(value = "/loadStaticData")
    @ResponseBody
    public ResponseEntity<?> loadStaticData() {
        return ResponseEntity.ok(sysDictService.loadStaticData());
    }

    /*获取地区数据*/
    @GetMapping(value = "/area/tree")
    @ResponseBody
    public List<AreaTree> getTree(@RequestParam(required = false) String menuName) {
        return areaService.listTree(true, false);
    }

    /*企业注册*/
    @RequestMapping(value = "/commit", method = RequestMethod.POST, consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<?> addRegister(HttpServletRequest request,
                                         @RequestPart(value = "file") MultipartFile multipartFile,
                                         @RequestParam(value = "accountForm") String registerUserStr,
                                         @RequestParam(value = "enterpriseForm") String registerBaseStr,
                                         @RequestParam(value = "licenseForm") String registerCertificateStr) {
        try {
            Date date = new Date();
            Gson gson = new Gson();
            Map<String, Object> registerUserMap = gson.fromJson(registerUserStr, new TypeToken<Map<String, Object>>() {
            }.getType());
            registerUserMap.put("userPwd", MD5Encoder.encode((String) registerUserMap.get("userPwd")));
            registerUserMap.put("ipAddress", request.getRemoteAddr());
            registerUserMap.put("registerTime", date);
            registerUserMap.put("registerStatus", 0);
            Map<String, Object> registerBaseMap = gson.fromJson(registerBaseStr, new TypeToken<Map<String, Object>>() {
            }.getType());
            Map<String, Object> registerCertificateMap = gson.fromJson(registerCertificateStr, new TypeToken<Map<String, Object>>() {
            }.getType());
            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
            registerService.addRegister(registerUserMap, registerBaseMap, registerCertificateMap, multipartFile, uploadUrl, imageFolder);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("error");
        }

    }

    /*企业注册时，企业账号信息用户名重复校验*/
    @RequestMapping(value = "/validateUserName", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> validateUserName(@RequestParam String userName) {
        Boolean flag = registerService.getRegisterUserByUserName(userName);
        Map param = new HashMap();
        param.put("userName", userName);
        List<SysUser> userList = sysUserService.findEntitysByCondition(param);
        if (flag && userList.size() < 1) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }

    }

    /*企业注册时，企业统一社会信用代码唯一性校验*/
    @RequestMapping(value = "/validateOrganizingInstitutionBarCode", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> validateOrganizingInstitutionBarCode(@RequestParam String organizingInstitutionBarCode) {
        Boolean flag = registerService.getRegisterBaseByOrganizingInstitutionBarCode(organizingInstitutionBarCode);
        return ResponseEntity.ok(flag);
    }

    /*企业注册状态查询的登陆入口*/
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<RegisterUser> enterpriseRegisterLogin(@RequestParam String userName, @RequestParam String userPwd) {
        try {
            String encodePwd = MD5Encoder.encode(userPwd);
            Map param = new HashMap();
            param.put("userName", userName);
            param.put("userPwd", encodePwd);
            List<RegisterUser> registerUserList = registerService.findEntitysByCondition(param);
            if (registerUserList.size() > 0) {
                RegisterUser registerUser = registerUserList.get(0);
                RegisterBase registerBase = registerService.findRegisterBaseById(registerUser.getRegisterId());
                RegisterCertificate registerCertificate = registerService.findRegisterCertificateById(registerUser.getRegisterId());
                registerUser.setRegisterBase(registerBase);
                registerUser.setRegisterCertificate(registerCertificate);
                return new ObjectRestResponse<RegisterUser>().rel(true).data(registerUser);
            } else {
                throw new Exception("用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysUser>().rel(false);
            resp.setStatus(500);
            resp.setMessage(e.getMessage());
            return resp;
        }
    }

    /*获取图片资源*/
    @RequestMapping(value = "/getResource/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<SysResource> getResource(@PathVariable Long id) {
        return new ObjectRestResponse<SysResource>().rel(true)
                .data(sysResourceService.findById(id));
    }

    /*修改注册信息*/
    @RequestMapping(value = "/updateRegister", method = RequestMethod.POST, consumes = "multipart/form-data")
    @ResponseBody
    public ObjectRestResponse<RegisterUser> update(@RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                                   @RequestParam(value = "formData") String registerUserStr) {
        try {
            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
            RegisterUser registerUser = JSONUtils.fromJson(registerUserStr,RegisterUser.class);
            registerService.updateRegister(registerUser,multipartFile,uploadUrl,imageFolder);
            return new ObjectRestResponse<RegisterUser>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RegisterUser>().rel(false);
            resp.setStatus(500);
            resp.setMessage(e.getMessage());
            return resp;
        }
    }

    /*注册审批查询*/
    @RequestMapping(value = "/getVerify", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<RegisterVerify> getVerify(@RequestParam String registerId) {
        Map param = new HashMap();
        param.put("registerId",registerId);
        param.put("orderByClause", "VERIFY_TIME desc");
        List<RegisterVerify> registerVerifyList = registerVerifyService.findEntitysByCondition(param);
        if(registerVerifyList.size()>0){
            return new ObjectRestResponse<RegisterVerify>().rel(true)
                    .data(registerVerifyList.get(0));
        }else{
            return new ObjectRestResponse<RegisterVerify>().rel(true)
                    .data(null);
        }

    }
    /*获取多个图片资源*/
    @RequestMapping(value = "/getMulResource/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<SysResource> getMulResource(@PathVariable String ids) {
    	  Map param = new HashMap();
          param.put("extendSql"," and RESOURCE_ID in ("+ids+")");
          List<SysResource> data = sysResourceService.findEntitysByCondition(param);
        return new ObjectRestResponse<SysResource>().rel(true)
                .data(data);
    }
}
