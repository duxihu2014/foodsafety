package com.otec.foodsafety.mapper.supervisory;

import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGridRegion;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ResponsibilityGridMapper extends GenericMapper<ResponsibilityGrid, Long> {

    void batchChangeStatus(Map<String, Object> map);

    Integer getPageCount(Map<String, String> params);

    List<ResponsibilityGrid> getPage(Map<String, String> params, RowBounds rowBounds);

    List<ResponsibilityGrid> getOtherGridsCoordinate(@Param("institutionId") Long institutionId, @Param("gridId") Long gridId);

}
