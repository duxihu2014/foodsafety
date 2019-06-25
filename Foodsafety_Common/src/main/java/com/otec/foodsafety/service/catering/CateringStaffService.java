package com.otec.foodsafety.service.catering;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import org.springframework.remoting.service.annotation.RemoteService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RemoteService
public interface CateringStaffService extends BaseService<CateringStaff,Long> {


    CateringStaff findByStaffId(long id);


    void update(CateringStaff cs, CateringStaffCertificate csc, MultipartFile imageFile,String uploadUrl,String imageFolder) throws Exception;

    void add(CateringStaff cs, CateringStaffCertificate csc, MultipartFile imageFile,String uploadUrl,String imageFolder) throws Exception;

    void update(CateringStaff cs, CateringStaffCertificate csc)throws Exception;

    void add(CateringStaff cs, CateringStaffCertificate csc) throws Exception;

             Integer getPageCount(Map<String, String> params);

    List<CateringStaff> getPage(Map<String, String> params, int offset, int pageSize);

}
