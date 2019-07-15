package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface EnterpriseCertificateService extends BaseService<EnterpriseCertificate,Long> {
    /**
     * 获取证照数据分页总条数
     * @param map
     * @return
     */
    Integer getEnterpriseCertificatePageCount(Map map);

    /**
     * 获取证照分页数据
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    List<EnterpriseCertificate> getEnterpriseCertificatePage(Map map, int start, int pageSize);
    /**
     *修改企业证照信息
     * @param uploadUrl
     * @param imageFolder
     * @param resource
     * @param userId
     * @param reason
     * @param enterpriseCertificate
     * @param operType 操作类型 1添加 2 修改 3删除
     */
    public void modifyEnterpriseCertificate(String uploadUrl, String imageFolder, SysResource resource,
                                            Long userId, String reason, EnterpriseCertificate enterpriseCertificate, String operType) throws Exception;

    /**
     * 新增企业证照信息
     * @param uploadUrl
     * @param imageFolder
     * @param resource
     * @param userId
     * @param reason
     * @param enterpriseCertificate
     * @throws Exception
     */
    public void addEnterpriseCertificate(String uploadUrl, String imageFolder, SysResource resource,
                                         Long userId, String reason, EnterpriseCertificate enterpriseCertificate) throws Exception;

    /**
     * 根据变更id获取证照变更数据
     * @param changeId
     * @return
     */
    public EnterpriseCertificateChange getChangeById(Long changeId);

    /**
     * 根据条件查询变更表数据
     * @param map
     * @return
     */
    public List<EnterpriseCertificateChange> getChangeByCondition(Map map);

    /**
     * 企业证照审核
     * @param enterpriseCertificate
     * @param enterpriseCertificateChange
     * @param enterpriseVerify
     */
    public void verifyEnterpriseCertificate(EnterpriseCertificate enterpriseCertificate,
                                            EnterpriseCertificateChange enterpriseCertificateChange,
                                            EnterpriseVerify enterpriseVerify);

    /**
     * 获取企业证照修改数据分页总条数
     * @param map
     * @return
     */
    Integer getCertificateChangePageCount(Map map);

    /**
     * 获取企业证照修改数据分页数据
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> getCertificateChangePage(Map map, int start, int pageSize);
}
