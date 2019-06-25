package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.service.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/equipment")
@RestController
public class EquipmentInterface extends BaseInterface {

    @Autowired
    EquipmentService equipmentService;
    /**
     *  获取企业监控设备列表
     * @param
     * @return
     */
    @RequestMapping(value="/list/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public List<Equipment> list(@RequestParam Map<String, String> params, @PathVariable Long enterpriseId) {
         params.put("enterpriseId",enterpriseId.toString());
        params.put("status","1");

        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model= equipmentService.getGridDataByCondition(po);

        return (List<Equipment>)model.getRows();

    }

}
