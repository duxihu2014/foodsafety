package com.otec.foodsafety.service.enterprise;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.enterprise.EnterpriseCertificateChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseCertificateMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnterpriseCertificateServiceImpl extends BaseServiceImpl<EnterpriseCertificate, Long> implements EnterpriseCertificateService{
    @Autowired
    public void setMapper(EnterpriseCertificateMapper mapper) {
        setGenericMapper(mapper);
    }

    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private EnterpriseCertificateMapper enterpriseCertificateMapper;
    @Autowired
    private EnterpriseCertificateChangeMapper enterpriseCertificateChangeMapper;
    @Autowired
    private EnterpriseVerifyMapper enterpriseVerifyMapper;

    /**
     * 获取证照数据分页总条数
     *
     * @param map
     * @return
     */
    @Override
    public Integer getEnterpriseCertificatePageCount(Map map) {
        return enterpriseCertificateMapper.getCertificatePageCount(map);
    }

    /**
     * 获取证照分页数据
     *
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<EnterpriseCertificate> getEnterpriseCertificatePage(Map map, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return enterpriseCertificateMapper.getCertificatePage(map,rowBounds);
    }

    /**
     * @param uploadUrl
     * @param imageFolder
     * @param userId
     * @param reason
     * @param enterpriseCertificate
     * @param operType 操作类型 1添加 2 修改 3删除
     */
    @Override
    public void modifyEnterpriseCertificate(String uploadUrl, String imageFolder,MultipartFile multipartFile,
                                            Long userId, String reason, EnterpriseCertificate enterpriseCertificate,String operType) throws Exception{
        EnterpriseCertificateChange enterpriseCertificateChange = new EnterpriseCertificateChange();
        BeanUtils.copyProperties(enterpriseCertificate,enterpriseCertificateChange);
        //1.更新图片资源。（如果multipartFile为空，说明图片文件没有被修改，则不不要更新资源表）
        if(multipartFile!=null){
            String fileName = multipartFile.getOriginalFilename();
            //1.1先将图片上传服务器
            String str = HttpURLConnectionUtils.sendMessage(
                    uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,
                    multipartFile.getBytes());
            JSONObject jSONObject = JSON.parseObject(str);
            String path = jSONObject.getString("imgUrl");
            String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
            String rename = path.split("/")[path.split("/").length-1];
            //1.2.将图片信息写入数据库
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
            enterpriseCertificateChange.setCertificatePhoto(resource.getResourceId());
        }
        //2.新增证照改变表
        enterpriseCertificateChange.setVerifyStatus("1");//设置状态为待审批
        enterpriseCertificateChange.setChangeType(operType);
        enterpriseCertificateChangeMapper.persist(enterpriseCertificateChange);
        //3.新增审核记录表
        EnterpriseVerify enterpriseVerify = new EnterpriseVerify();
        Date data = new Date();
        enterpriseVerify.setChangeId(enterpriseCertificateChange.getChangeId());
        enterpriseVerify.setChangeType(operType);//变更操作类型1.新增、2.修改、3.删除
        enterpriseVerify.setDataType("2");
        enterpriseVerify.setChangeUserId(userId);
        enterpriseVerify.setChangeTime(data);
        enterpriseVerify.setEnterpriseId(enterpriseCertificate.getEnterpriseId());
        enterpriseVerify.setVerifyStatus("1");
        enterpriseVerify.setChangeReason(reason);
        enterpriseVerifyMapper.persist(enterpriseVerify);
    }

    /**
     * 新增企业证照信息
     *
     * @param uploadUrl
     * @param imageFolder
     * @param multipartFile
     * @param userId
     * @param reason
     * @param enterpriseCertificate
     * @throws Exception
     */
    @Override
    public void addEnterpriseCertificate(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId, String reason, EnterpriseCertificate enterpriseCertificate) throws Exception {
        EnterpriseCertificateChange enterpriseCertificateChange = new EnterpriseCertificateChange();
        BeanUtils.copyProperties(enterpriseCertificate,enterpriseCertificateChange);
        //1.更新图片资源。
        if(multipartFile!=null){
            String fileName = multipartFile.getOriginalFilename();
            //1.1先将图片上传服务器
            String str = HttpURLConnectionUtils.sendMessage(
                    uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,
                    multipartFile.getBytes());
            JSONObject jSONObject = JSON.parseObject(str);
            String path = jSONObject.getString("imgUrl");
            String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
            String rename = path.split("/")[path.split("/").length-1];
            //1.2.将图片信息写入数据库
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
            enterpriseCertificateChange.setCertificatePhoto(resource.getResourceId());
        }
        //2.新增证照改变表
        enterpriseCertificateChange.setVerifyStatus("1");//设置状态为待审批
        enterpriseCertificateChange.setChangeType("1");
        enterpriseCertificateChangeMapper.persist(enterpriseCertificateChange);
        //3.新增审核记录表
        EnterpriseVerify enterpriseVerify = new EnterpriseVerify();
        Date data = new Date();
        enterpriseVerify.setChangeId(enterpriseCertificateChange.getChangeId());
        enterpriseVerify.setChangeType("1");//变更操作类型1.新增、2.修改、3.删除
        enterpriseVerify.setDataType("2");
        enterpriseVerify.setChangeUserId(userId);
        enterpriseVerify.setChangeTime(data);
        enterpriseVerify.setEnterpriseId(enterpriseCertificate.getEnterpriseId());
        enterpriseVerify.setVerifyStatus("1");
        enterpriseVerify.setChangeReason(reason);
        enterpriseVerifyMapper.persist(enterpriseVerify);
    }

    /**
     * 根据变更id获取证照变更数据
     *
     * @param changeId
     * @return
     */
    @Override
    public EnterpriseCertificateChange getChangeById(Long changeId) {
        return enterpriseCertificateChangeMapper.findById(changeId);
    }

    /**
     * 根据条件查询变更表数据
     *
     * @param map
     * @return
     */
    @Override
    public List<EnterpriseCertificateChange> getChangeByCondition(Map map) {
        return enterpriseCertificateChangeMapper.findEntitysByCondition(map);
    }

    /**
     * 企业证照审核
     *
     * @param enterpriseCertificate
     * @param enterpriseCertificateChange
     * @param enterpriseVerify
     */
    @Override
    public void verifyEnterpriseCertificate(EnterpriseCertificate enterpriseCertificate, EnterpriseCertificateChange enterpriseCertificateChange, EnterpriseVerify enterpriseVerify) {
        //1.更新审核表
        enterpriseVerifyMapper.updateById(enterpriseVerify);
        if(enterpriseVerify.getVerifyStatus().equals("2")){//审核通过,将change表数据更新到原始表
            if("3".equals(enterpriseCertificateChange.getChangeType())){//删除
                enterpriseCertificate.setCertificateStatus("0");
                enterpriseCertificateMapper.updateById(enterpriseCertificate);
            }else if("2".equals(enterpriseCertificateChange.getChangeType())){//修改
                BeanUtils.copyProperties(enterpriseCertificateChange,enterpriseCertificate);
                enterpriseCertificateMapper.updateById(enterpriseCertificate);
            }else if("1".equals(enterpriseCertificateChange.getChangeType())){//新增
                BeanUtils.copyProperties(enterpriseCertificateChange,enterpriseCertificate);
                enterpriseCertificate.setCertificateStatus("1");
                enterpriseCertificateMapper.persist(enterpriseCertificate);
                enterpriseCertificateChange.setCertificateId(enterpriseCertificate.getCertificateId());
            }
            enterpriseCertificateChange.setVerifyStatus("2");

        }else{//审核不通过
            enterpriseCertificateChange.setVerifyStatus("3");
        }
        enterpriseCertificateChangeMapper.updateById(enterpriseCertificateChange);
    }

    /**
     * 获取企业证照修改数据分页总条数
     *
     * @param map
     * @return
     */
    @Override
    public Integer getCertificateChangePageCount(Map map) {
        return enterpriseCertificateChangeMapper.getCertificateChangePageCount(map);
    }

    /**
     * 获取企业证照修改数据分页数据
     *
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> getCertificateChangePage(Map map, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return enterpriseCertificateChangeMapper.getCertificateChangePage(map,rowBounds);
    }
}
