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
import com.otec.foodsafety.util.CommonUtil;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public  class CateringStaffServiceImpl extends BaseServiceImpl<CateringStaff, Long> implements  CateringStaffService {

    @Autowired
    private CateringStaffMapper cateringStaffMapper;

    @Autowired
    private CateringStaffCertificateMapper cateringStaffCertificateMapper;

    @Autowired
    SysResourceService sysResourceMapper;


    @Autowired
    public void setMapper(CateringStaffMapper mapper) {
        setGenericMapper(mapper);
    }



    @Override
    public Integer getPageCount(Map<String, String> params) {
        return cateringStaffMapper.getPageCount(params);
    }

    @Override
    public List<CateringStaff> getPage(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return cateringStaffMapper.getPage(params,rowBounds);
    }

    @Override
    public CateringStaff findByStaffId(long id) {
        CateringStaff cs=cateringStaffMapper.findById(id);
        List<CateringStaffCertificate> csc=cateringStaffCertificateMapper.getStaffCert(id);
        if(csc.size()>0){
            cs.setCertificateNumber(csc.get(0).getCertificateNumber());
            cs.setCertificatePhoto(csc.get(0).getCertificatePhoto());
            cs.setIssuingDate(csc.get(0).getIssuingDate());
            cs.setIssuingUnit(csc.get(0).getIssuingUnit());
            cs.setValidDate(csc.get(0).getValidDate());
            cs.setExaminationDate(csc.get(0).getExaminationDate());
            cs.setCertificateId(csc.get(0).getCertificateId());
            SysResource img= sysResourceMapper.findById(cs.getCertificatePhoto());
            if(img!=null) cs.setResourcePath(img.getResourcePath());
        }
        return cs;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(CateringStaff cs, CateringStaffCertificate csc, SysResource resource,String uploadUrl,String imageFolder) throws Exception {
        cateringStaffMapper.persist(cs);
        String fileName = resource.getResourceName();
        byte[] fileByte = resource.getResourceContent();
        String str = HttpURLConnectionUtils.sendMessage(
                uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,fileByte);
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
        resource.setResourcePath( path );
        resource.setResourceContent(null);
        sysResourceMapper.persist(resource);
        csc.setCertificatePhoto(resource.getResourceId());
        csc.setStaffId(cs.getStaffId());
        csc.setCertificateStatus("1");
        cateringStaffCertificateMapper.persist(csc);

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(CateringStaff cs, CateringStaffCertificate csc, SysResource resource,String uploadUrl,String imageFolder)throws Exception {
        CateringStaff old=cateringStaffMapper.findById(cs.getStaffId());
        CommonUtil.copyBean(cs,old,false);
        cateringStaffMapper.updateById(old);

        CateringStaffCertificate cscold=cateringStaffCertificateMapper.findById(csc.getCertificateId());
        if(resource!=null){
            String fileName = resource.getResourceName();
            byte[] fileByte = resource.getResourceContent();
            //1.先将图片上传服务器
            String str = HttpURLConnectionUtils.sendMessage(
                    uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,fileByte);
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
            resource.setResourcePath( path );
            resource.setResourceContent(null);
            if(cscold.getCertificateNumber().equals(csc.getCertificateNumber())){ //替换
                sysResourceMapper.updateResource(resource);
                csc.setCertificatePhoto(resource.getResourceId());
            }else{//更新
                sysResourceMapper.addResource(resource);
                csc.setCertificatePhoto(resource.getResourceId());
            }
        }
        if(cscold.getCertificateNumber().equals(csc.getCertificateNumber())){ //替换
            csc.setCertificateStatus(cscold.getCertificateStatus());
            csc.setCertificatePhoto(cscold.getCertificatePhoto());
            CommonUtil.copyBean(csc,cscold,false);
            cateringStaffCertificateMapper.updateById(cscold);

        }else {//更新
            csc.setStaffId(cs.getStaffId());
            cscold.setCertificateStatus("2");//设置老数据过期
            cateringStaffCertificateMapper.updateById(cscold);
            csc.setCertificateStatus("1");
            cateringStaffCertificateMapper.persist(csc);
        }
     }

    @Override
    public void update(CateringStaff cs, CateringStaffCertificate csc) throws Exception {
        CateringStaff old=cateringStaffMapper.findById(cs.getStaffId());
        CommonUtil.copyBean(cs,old,false);
        cateringStaffMapper.updateById(old);

        CateringStaffCertificate cscold=cateringStaffCertificateMapper.findById(csc.getCertificateId());

        if(cscold.getCertificateNumber().equals(csc.getCertificateNumber())){ //替换
            csc.setCertificateStatus(cscold.getCertificateStatus());
            CommonUtil.copyBean(csc,cscold,false);
            cateringStaffCertificateMapper.updateById(cscold);
        }else//更新
        {
            csc.setStaffId(cs.getStaffId());
            cscold.setCertificateStatus("2");//设置老数据过期
            cateringStaffCertificateMapper.updateById(cscold);
            csc.setCertificateStatus("1");
            cateringStaffCertificateMapper.persist(csc);
        }
    }

    @Override
    public void add(CateringStaff cs, CateringStaffCertificate csc) {
        cateringStaffMapper.persist(cs);
        csc.setStaffId(cs.getStaffId());
        csc.setCertificateStatus("1");
        cateringStaffCertificateMapper.persist(csc);
    }
}
