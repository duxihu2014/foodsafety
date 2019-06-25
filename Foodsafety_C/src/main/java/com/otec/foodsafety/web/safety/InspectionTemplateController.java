package com.otec.foodsafety.web.safety;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionTemplate;
import com.otec.foodsafety.entity.system.SysArea;
import com.otec.foodsafety.entity.system.SysInstitution;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.safety.InspectionTemplateService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.service.system.SysInstitutionService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/inspection/template")
public class InspectionTemplateController extends VueBaseController<
        InspectionTemplateService, InspectionTemplate, Integer> {
    @Autowired
    InspectionTemplateService inspectionTemplateService;
    @Autowired
    SysInstitutionService sysInstitutionService;
    @Autowired
    SysAreaService sysAreaService;
    @Autowired
    DateLocalService dateLocalService;
    @Autowired
    SessionFilter sessionFilter;

    @Override
    public ObjectRestResponse<InspectionTemplate> add(@RequestBody InspectionTemplate entity) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);

            entity.setTemplateStatus("1");
            entity.setWorkoutTime(dateLocalService.getDataBaseDate());
            entity.setWorkoutInstitution(sysUser.getInstitutionId());
            entity.setWorkoutUser(sysUser.getUserId());

            SysInstitution sysInstitution = sysInstitutionService.findById(sysUser.getInstitutionId());
            SysArea sysArea = sysAreaService.findById(sysInstitution.getAreaId());
            entity.setAdministrativeLevel(sysArea.getAdministrativeLevel());

            return super.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionTemplate>().rel(false);
            resp.setStatus(500);
            resp.setMessage("新增失败!");
            return resp;
        }
    }

    /**
     * @TODO 检查模板名称是否重复
     * @param templateName
     * @return
     */
    @RequestMapping(value = "/checkTemplateName", method = RequestMethod.GET)
    public ObjectRestResponse<InspectionTemplate> checkTemplateName(@RequestParam String templateName) {
        Map<String ,Object> param = new HashMap<>();
        param.put("templateName", templateName);
        List<InspectionTemplate> inspectionTemplate = inspectionTemplateService.findEntitysByCondition(param);
        if(inspectionTemplate.size() > 0){
            ObjectRestResponse resp = new ObjectRestResponse<InspectionTemplate>().rel(false);
            return resp;
        } else {
            ObjectRestResponse resp = new ObjectRestResponse<InspectionTemplate>().rel(true);
            return resp;
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<InspectionTemplate> changeStatus(@RequestParam Integer[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<InspectionTemplate>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                inspectionTemplateService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<InspectionTemplate>().rel(true);
            }
            return new ObjectRestResponse<InspectionTemplate>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionTemplate>().rel(false);
            resp.setStatus(500);
            resp.setMessage("模板删除异常");
            return resp;
        }
    }
}
