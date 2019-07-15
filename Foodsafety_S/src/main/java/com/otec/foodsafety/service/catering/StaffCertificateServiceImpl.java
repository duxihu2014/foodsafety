package com.otec.foodsafety.service.catering;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.operation.CateringStaffCertificateMapper;
import com.otec.foodsafety.mapper.operation.CateringStaffMapper;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public  class StaffCertificateServiceImpl extends BaseServiceImpl<CateringStaffCertificate, Long> implements  StaffCertificateService {

    @Autowired
    private CateringStaffMapper cateringStaffMapper;

    @Autowired
    private CateringStaffCertificateMapper cateringStaffCertificateMapper;

    @Autowired
    SysResourceService sysResourceMapper;


    @Autowired
    public void setMapper(CateringStaffCertificateMapper mapper) {
        setGenericMapper(mapper);
    }


    @Override
    public Integer getPageCount(Map<String, String> params) {

        return cateringStaffCertificateMapper.getCertificatePageCount(params);
    }

    @Override
    public List<CateringStaff> getPage(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return cateringStaffCertificateMapper.getCertificatePage(params,rowBounds);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void add(CateringStaffCertificate csc, SysResource resource,String uploadUrl,String imageFolder) throws Exception {
        cateringStaffCertificateMapper.changeDisable(csc.getStaffId());
        String fileName = resource.getResourceName();
        byte[] fileByte = resource.getResourceContent();
         //1.先将图片上传服务器
        String str = HttpURLConnectionUtils.sendMessage(
                uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder, fileByte);
        JSONObject jSONObject = JSON.parseObject(str);
        String path = jSONObject.getString("imgUrl");
        String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
        String rename = path.split("/")[path.split("/").length-1];
        //2.将图片信息写入数据库
        resource.setResourceName(fileName);
        resource.setResourceRename(rename);
        resource.setResourceStorage(ResourceStorage.LOCAL.toString());
        resource.setResourceExtension(subfix);
        resource.setResourceStatus("1");
        resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
        resource.setResourceContent(null);
        resource.setResourcePath( path );
        sysResourceMapper.persist(resource);
        csc.setCertificatePhoto(resource.getResourceId());
        csc.setCertificateStatus("1");
        cateringStaffCertificateMapper.persist(csc);
    }





    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(CateringStaffCertificate csc, SysResource resource,String uploadUrl,String imageFolder) throws Exception {

        CateringStaffCertificate cscold=cateringStaffCertificateMapper.findById(csc.getCertificateId());

        if(resource!=null){
            String fileName = resource.getResourceName();
            byte[] fileByte = resource.getResourceContent();
            //1.先将图片上传服务器
            String str = HttpURLConnectionUtils.sendMessage(
                    uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder, fileByte);
            JSONObject jSONObject = JSON.parseObject(str);
            String path = jSONObject.getString("imgUrl");
            String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
            String rename = path.split("/")[path.split("/").length-1];
            //2.将图片信息写入数据库
            resource.setResourceName(fileName);
            resource.setResourceRename(rename);
            resource.setResourceStorage(ResourceStorage.LOCAL.toString());
            resource.setResourceExtension(subfix);
            resource.setResourceStatus("1");
            resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
            resource.setResourceContent(null);
            resource.setResourcePath( path );
            if(cscold.getCertificateNumber().equals(csc.getCertificateNumber())){ //替换
                sysResourceMapper.updateResource(resource);
                csc.setCertificatePhoto(resource.getResourceId());

            }else//更新
            {
                sysResourceMapper.addResource(resource);
                csc.setCertificatePhoto(resource.getResourceId());
            }
        }


        if(cscold.getCertificateNumber().equals(csc.getCertificateNumber())){ //替换
            csc.setCertificateStatus(cscold.getCertificateStatus());
            csc.setCertificatePhoto(cscold.getCertificatePhoto());
            CommonUtil.copyBean(csc,cscold);
            cateringStaffCertificateMapper.updateById(cscold);

        }else//更新
        {
            cscold.setCertificateStatus("0");//设置老数据过期
            cateringStaffCertificateMapper.updateById(cscold);
            csc.setCertificateStatus("1");
            cateringStaffCertificateMapper.persist(csc);
        }
    }



    /**
     * 获取企业健康证各状态数量
     * @param enterpriseId
     * @return
     */
    @Override
    public Map getStateCertificate(Long enterpriseId) {
        return cateringStaffCertificateMapper.getStateCertificate(enterpriseId);
    }


}
