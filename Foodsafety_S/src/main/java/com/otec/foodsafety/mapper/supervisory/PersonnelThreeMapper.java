package com.otec.foodsafety.mapper.supervisory;

import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.purchase.Materials;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PersonnelThreeMapper extends GenericMapper<PersonnelThree, Long> {

    void batchChangeStatus(Map<String, Object> map);


    List<PersonnelThree> getPage(Map<String, String> params, RowBounds rowBounds);

    Integer getPageCount(Map<String, String> params);

 }
