package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.equipment.EquipmentPushflowInfo;
import com.otec.foodsafety.service.equipment.EquipmentPushflowInfoService;
import com.otec.foodsafety.service.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/equipmentPushflowInfo")
@RestController
public class EquipmentPushflowInfoInterface extends BaseInterface {

    @Autowired
    EquipmentPushflowInfoService equipmentPushflowInfoService;


    /**
     *  通过视频推流设备ID，拿详情信息
     * @param
     * @return
     */
    @RequestMapping(value="/equipmentPushflowInfo/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    public EquipmentPushflowInfo equipmentPushflowInfo(@PathVariable Long id) {
        return equipmentPushflowInfoService.findById(id);
    }

}
