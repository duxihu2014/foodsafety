package com.otec.foodsafety.service.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.equipment.EquipmentPushflowInfo;
import com.otec.foodsafety.util.vo.EquGroupTree;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface EquipmentPushflowInfoService extends BaseService<EquipmentPushflowInfo,Long>{

//    /**
//     * 设备分组树
//     * @param compId 公司id
//     * @return
//     */
//    List<EquGroupTree> groupTree(String compId);
//
//    GridDataModel getGridDataByCondition(PageObject po);

//    EquipmentPushflowInfo add(EquipmentPushflowInfo entity);
//
//    void update(EquipmentPushflowInfo entity);
//    EquipmentPushflowInfo EquipmentPushflowInfoById(long id);
}
