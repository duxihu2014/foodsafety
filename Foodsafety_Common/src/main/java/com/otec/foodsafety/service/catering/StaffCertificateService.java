package com.otec.foodsafety.service.catering;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface StaffCertificateService extends BaseService<CateringStaffCertificate,Long> {


    Integer getPageCount(Map<String, String> params);

    List<CateringStaff> getPage(Map<String, String> params, int offset, int pageSize);

    void add(CateringStaffCertificate csc, SysResource resource, String uploadUrl, String imageFolder) throws Exception;

    void update(CateringStaffCertificate csc, SysResource resource, String uploadUrl, String imageFolder) throws Exception;

    /**
     * 获取企业健康证各状态数量
     * @param enterpriseId
     * @return
     */
    Map getStateCertificate(Long enterpriseId);


}
