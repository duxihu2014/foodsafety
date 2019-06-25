package com.otec.foodsafety.web.safety;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionCycle;
import com.otec.foodsafety.entity.system.SysArea;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.service.safety.InspectionCycleService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.service.system.SysInstitutionService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/inspection/cycle")
public class InspectionCycleController extends VueBaseController<
        InspectionCycleService, InspectionCycle, Integer> {
    @Autowired
    DateLocalService dateLocalService;
    @Autowired
    InspectionCycleService inspectionCycleService;
    @Autowired
    SysInstitutionService sysInstitutionService;
    @Autowired
    SysAreaService sysAreaService;

    @Override
    public ObjectRestResponse<InspectionCycle> add(@RequestBody InspectionCycle entity) {
        try {
            SysInstitution sysInstitution = sysInstitutionService.findById(entity.getSettingInstitution());
            SysArea sysArea = sysAreaService.findById(sysInstitution.getAreaId());
            entity.setAreaId(sysArea.getAreaId());
            entity.setSettingTime(dateLocalService.getDataBaseDate());
            return super.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionCycle>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新失败!");
            return resp;
        }
    }

    @Override
    public ObjectRestResponse<InspectionCycle> update(@RequestBody InspectionCycle entity) {
        try {
            entity.setSettingTime(dateLocalService.getDataBaseDate());
            return super.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionCycle>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新失败!");
            return resp;
        }
    }

    /**
     * @TODO 根据设置机构查询日常检查周期
     * @param settingInstitution
     * @return
     */
    @RequestMapping(value = "/getInspectionCycle", method = RequestMethod.GET)
    public ObjectRestResponse<InspectionCycle> getInspectionCycle(@RequestParam Integer settingInstitution) {
        Map<String ,Object> param = new HashMap<>();
        param.put("settingInstitution", settingInstitution);
        List<InspectionCycle> inspectionCycle = inspectionCycleService.findEntitysByCondition(param);
        if(inspectionCycle.size() == 1){
            return new ObjectRestResponse<InspectionCycle>().rel(true)
                    .data(inspectionCycle.get(0));
        } else {
            ObjectRestResponse resp = new ObjectRestResponse<InspectionCycle>().rel(false);
            return resp;
        }
    }
}
