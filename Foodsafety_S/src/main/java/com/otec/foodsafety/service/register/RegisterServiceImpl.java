package com.otec.foodsafety.service.register;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.register.RegisterBase;
import com.otec.foodsafety.entity.register.RegisterCertificate;
import com.otec.foodsafety.entity.register.RegisterUser;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.register.RegisterBaseMapper;
import com.otec.foodsafety.mapper.register.RegisterCertificateMapper;
import com.otec.foodsafety.mapper.register.RegisterUserMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RegisterServiceImpl extends BaseServiceImpl<RegisterUser, Long> implements RegisterService{
    @Autowired
    RegisterUserMapper registerUserMapper;
    @Autowired
    RegisterBaseMapper registerBaseMapper;
    @Autowired
    RegisterCertificateMapper registerCertificateMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    public void setMapper(RegisterUserMapper mapper) { setGenericMapper(mapper);
    }

    @Override

    public void addRegister(Map<String, Object> registerUserMap,
                            Map<String, Object> registerBaseMap,
                            Map<String, Object> registerCertificateMap ,
                            MultipartFile multipartFile,String uploadUrl,String imageFolder) throws Exception  {

        String fileName = multipartFile.getOriginalFilename();
        //1.先将图片上传服务器
        String str = HttpURLConnectionUtils.sendMessage(
                uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,
                multipartFile.getBytes());
        JSONObject jSONObject = JSON.parseObject(str);
        String path = jSONObject.getString("imgUrl");
        String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
        String rename = path.split("/")[path.split("/").length-1];
        //2.将图片信息写入数据库
        SysResource resource = new SysResource();
        resource.setResourceName(fileName);
        resource.setResourceRename(rename);
        resource.setResourceStorage(ResourceStorage.LOCAL.toString());
        resource.setResourceExtension(subfix);
        resource.setResourceStatus("1");
        resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
        resource.setResourceLength(multipartFile.getSize());
        resource.setResourcePath( path );
        sysResourceMapper.persist(resource);
        Map param = new HashMap();
        param.put("resourceName",fileName);
        param.put("resourcePath",path);
        List<SysResource> sysResourceList=sysResourceMapper.findEntitysByCondition(param);
        SysResource resource1 = sysResourceList.get(0);
        //3.将注册信息写入数据库
        registerUserMapper.addRegisterUser(registerUserMap);
        RegisterUser registerUser=registerUserMapper.getByUserName((String)registerUserMap.get("userName"));
        Object registerId = registerUser.getRegisterId();
        registerBaseMap.put("registerId",registerId);
        registerCertificateMap.put("registerId",registerId);
        registerCertificateMap.put("certificateType","1");
        registerCertificateMap.put("certificateName","营业执照");
        registerCertificateMap.put("certificatePhoto",resource1.getResourceId());
        registerBaseMapper.addRegisterBase(registerBaseMap);
        registerCertificateMapper.addRegisterCertificate(registerCertificateMap);

    }

    @Override
    public void addRegister(Map<String, Object> registerUserMap,
                            Map<String, Object> registerBaseMap,
                            Map<String, Object> registerCertificateMap)   {
        //将注册信息写入数据库
        registerUserMapper.addRegisterUser(registerUserMap);
        RegisterUser registerUser=registerUserMapper.getByUserName((String)registerUserMap.get("userName"));
        Object registerId = registerUser.getRegisterId();
        registerBaseMap.put("registerId",registerId);
        registerCertificateMap.put("registerId",registerId);
        registerCertificateMap.put("certificateType","1");
        registerCertificateMap.put("certificateName","营业执照");
     //   registerCertificateMap.put("certificatePhoto",resource1.getResourceId());
        registerBaseMapper.addRegisterBase(registerBaseMap);
        registerCertificateMapper.addRegisterCertificate(registerCertificateMap);

    }

    @Override
    public Boolean getRegisterUserByUserName(String userName) {
        RegisterUser registerUser=registerUserMapper.getByUserName(userName);
        return registerUser==null?true:false;
    }

    @Override
    public Boolean getRegisterBaseByOrganizingInstitutionBarCode(String organizingInstitutionBarCode) {
        RegisterBase registerBase = registerBaseMapper.getByOrganizingInstitutionBarCode(organizingInstitutionBarCode);
        return registerBase==null?true:false;
    }

    @Override
    public RegisterBase findRegisterBaseById(Long registerId) {
        return registerBaseMapper.findById(registerId);
    }

    @Override
    public RegisterCertificate findRegisterCertificateById(Long registerId) {
        return registerCertificateMapper.findById(registerId);
    }

    @Override
    public void updateRegister(RegisterUser registerUser,MultipartFile multipartFile,String uploadUrl,String imageFolder)  throws Exception{
        RegisterBase registerBase = registerUser.getRegisterBase();
        RegisterCertificate registerCertificate = registerUser.getRegisterCertificate();
        //1.判断multipartFile是否未空,不为空则要更新资源表
        if(multipartFile!=null){
            String fileName = multipartFile.getOriginalFilename();
            //先将图片上传服务器
            String str = HttpURLConnectionUtils.sendMessage(
                    uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,
                    multipartFile.getBytes());
            JSONObject jSONObject = JSON.parseObject(str);
            String path = jSONObject.getString("imgUrl");
            String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
            String rename = path.split("/")[path.split("/").length-1];
            //2.将图片信息写入数据库
            SysResource resource = new SysResource();
            resource.setResourceName(fileName);
            resource.setResourceRename(rename);
            resource.setResourceStorage(ResourceStorage.LOCAL.toString());
            resource.setResourceExtension(subfix);
            resource.setResourceStatus("1");
            resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
            resource.setResourceLength(multipartFile.getSize());
            resource.setResourcePath( path );
            sysResourceMapper.persist(resource);
            registerCertificate.setCertificatePhoto(resource.getResourceId());
        }
        //2.每次修改都需要将注册状态改为待审核
        registerUser.setRegisterStatus("0");
        registerUserMapper.updateById(registerUser);
        registerBaseMapper.updateById(registerBase);
        registerCertificateMapper.updateById(registerCertificate);
    }

}
