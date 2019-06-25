package com.otec.foodsafety.mapper.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EnterpriseCertificateChangeMapper extends GenericMapper<EnterpriseCertificateChange, Long> {
    Integer getCertificateChangePageCount(Map map);

    List<Map<String,Object>> getCertificateChangePage(Map map, RowBounds rowBounds);
}
