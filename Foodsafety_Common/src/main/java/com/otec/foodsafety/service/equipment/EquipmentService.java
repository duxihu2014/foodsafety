package com.otec.foodsafety.service.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.util.vo.EquGroupTree;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.Equipment;

import java.util.List;

@RemoteService
public interface EquipmentService extends BaseService<Equipment,Long>{

    /**
     * 设备分组树
     * @param compId 公司id
     * @return
     */
    List<EquGroupTree> groupTree(String compId);

    GridDataModel getGridDataByCondition(PageObject po);

    Equipment add(Equipment entity);

    void update(Equipment entity);
    String getPlayUrl(long id);
}
