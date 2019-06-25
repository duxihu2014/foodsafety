package com.otec.foodsafety.web.safety;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionTemplateProject;
import com.otec.foodsafety.service.safety.InspectionTemplateProjectService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/safety/inspection/templateProject")
public class InspectionTemplateProjectController extends VueBaseController<
        InspectionTemplateProjectService, InspectionTemplateProject, Integer> {
    @Autowired
    InspectionTemplateProjectService inspectionTemplateProjectService;

    @RequestMapping(value = "/unbanding", method = RequestMethod.PUT)
    public ObjectRestResponse<InspectionTemplateProject> unbanding(@RequestParam Integer templateId, @RequestParam Integer[] ids) {
        try {
            // 判断模板编号、项目编号参数是否合法
            if (templateId != null && templateId > 0 && ids != null && ids.length > 0) {
                inspectionTemplateProjectService.unbanding(ids, templateId);
                return new ObjectRestResponse<InspectionTemplateProject>().rel(true);
            }
            return new ObjectRestResponse<InspectionTemplateProject>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionTemplateProject>().rel(false);
            resp.setStatus(500);
            resp.setMessage("解除绑定异常");
            return resp;
        }
    }

    @RequestMapping(value = "/setProjectScore", method = RequestMethod.PUT)
    public ObjectRestResponse<InspectionTemplateProject> setProjectScore(@RequestParam Integer id, @RequestParam Integer score) {
        try {
            // 判断编号、分值参数是否合法
            if (id != null && id > 0 && score != null && score >= 0) {
                inspectionTemplateProjectService.setProjectScore(id, score);
                return new ObjectRestResponse<InspectionTemplateProject>().rel(true);
            }
            return new ObjectRestResponse<InspectionTemplateProject>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionTemplateProject>().rel(false);
            resp.setStatus(500);
            resp.setMessage("分值设置异常");
            return resp;
        }
    }
}
