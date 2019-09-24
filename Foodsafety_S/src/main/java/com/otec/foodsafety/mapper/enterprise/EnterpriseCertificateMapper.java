package com.otec.foodsafety.mapper.enterprise;


import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateExt;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EnterpriseCertificateMapper extends GenericMapper<EnterpriseCertificate, Long> {

    Integer getCertificatePageCount(Map map);

    List<EnterpriseCertificate> getCertificatePage(Map map, RowBounds rowBounds);

    List<EnterpriseCertificateExt> getCertificatePageExt(Map map, RowBounds rowBounds);
}
