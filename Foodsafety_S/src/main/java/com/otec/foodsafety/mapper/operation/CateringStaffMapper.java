package com.otec.foodsafety.mapper.operation;

import com.otec.foodsafety.entity.operation.CateringStaff;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CateringStaffMapper extends GenericMapper<CateringStaff, Long> {


    void batchChangeStatus(Map<String, Object> map);

    List<CateringStaff> getPage(Map<String, String> params, RowBounds rowBounds);

    Integer getPageCount(Map<String, String> params);
}
