package com.otec.foodsafety.mapper.operation;

import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CateringStaffCertificateMapper extends GenericMapper<CateringStaffCertificate, Long> {


    List<CateringStaffCertificate> getStaffCert(@Param("staffId") long staffId);


    List<CateringStaff> getCertificatePage(Map<String, String> params, RowBounds rowBounds);

    Integer getCertificatePageCount(Map<String, String> params);

    void changeDisable(@Param("staffId") long staffId);

    Map getStateCertificate(@Param("enterpriseId") Long enterpriseId);

}
