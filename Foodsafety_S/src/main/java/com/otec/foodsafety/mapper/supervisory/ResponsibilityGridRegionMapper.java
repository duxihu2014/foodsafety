package com.otec.foodsafety.mapper.supervisory;

import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGridRegion;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResponsibilityGridRegionMapper extends GenericMapper<ResponsibilityGridRegion, Long> {

    void deleteByGridId(long gridId);

}
