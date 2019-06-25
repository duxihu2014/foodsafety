package com.otec.foodsafety.service.supervisory;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGridRegion;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface ResponsibilityGridService extends BaseService<ResponsibilityGrid, Long> {

    Integer getPageCount(Map<String, String> params);

    List<ResponsibilityGrid> getPage(Map<String, String> params, int offset, int pageSize);

    void batchChangeStatus(Long[] ids, String status);

    void add(ResponsibilityGrid entity);

    void update(ResponsibilityGrid entity);

    /**
     * 获取其它责任区域的坐标
     * @param institutionId
     * @param id
     */
    List<ResponsibilityGrid> getOtherGridsCoordinate(Long institutionId, Long id);

}
