package com.otec.foodsafety.web.safety;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionProject;
import com.otec.foodsafety.service.safety.InspectionProjectService;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/inspection/template/project")
public class InspectionProjectController extends VueBaseController<
        InspectionProjectService, InspectionProject, Integer> {
    @Autowired
    InspectionProjectService inspectionProjectService;

    @Override
    public ObjectRestResponse<InspectionProject> add(@RequestBody InspectionProject entity) {
        try {
            String[] resultsQualified = entity.getResultsQualified().split("\\s+");
            String[] resultsUnqualified = entity.getResultsUnqualified().split("\\s+");
            entity.setResultsQualified(StringUtils.join(resultsQualified, ' '));
            entity.setResultsUnqualified(StringUtils.join(resultsUnqualified, ' '));
            entity.setProjectStatus("1");
            return super.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProject>().rel(false);
            resp.setStatus(500);
            resp.setMessage("新增失败!");
            return resp;
        }
    }

    @Override
    public ObjectRestResponse<InspectionProject> update(@RequestBody InspectionProject entity) {
        try{
            String[] resultsQualified = entity.getResultsQualified().split("\\s+");
            String[] resultsUnqualified = entity.getResultsUnqualified().split("\\s+");
            entity.setResultsQualified(StringUtils.join(resultsQualified, ' '));
            entity.setResultsUnqualified(StringUtils.join(resultsUnqualified, ' '));
            return super.update(entity);
        } catch (Exception e) {
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProject>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新失败!");
            return resp;
        }
    }

    /**
     * @TODO 检查项目名称是否重复
     * @param projectName
     * @return
     */
    @RequestMapping(value = "/checkProjectName", method = RequestMethod.GET)
    public ObjectRestResponse<InspectionProject> checkProjectName(@RequestParam String projectName) {
        Map<String ,Object> param = new HashMap<>();
        param.put("projectName", projectName);
        List<InspectionProject> inspectionProject = inspectionProjectService.findEntitysByCondition(param);
        if(inspectionProject.size() > 0){
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProject>().rel(false);
            return resp;
        } else {
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProject>().rel(true);
            return resp;
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<InspectionProject> changeStatus(@RequestParam Integer[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<InspectionProject>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                inspectionProjectService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<InspectionProject>().rel(true);
            }
            return new ObjectRestResponse<InspectionProject>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProject>().rel(false);
            resp.setStatus(500);
            resp.setMessage("项目删除异常");
            return resp;
        }
    }

    /**
     * TODO 查询模板绑定的项目
     *
     * @return
     */
    @RequestMapping(value = "/getTemplateProject", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getTemplateProject(@RequestParam Map<String, String> params) {
        PageObject po = getPageObject(params);
        Integer total = inspectionProjectService.getTemplateProjectCount(params);
        List<InspectionProject> projectList = inspectionProjectService.getTemplateProject(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(projectList, total);
        return model;
    }

    @RequestMapping(value = "/getTemplateAllProject", method = RequestMethod.GET)
    @ResponseBody
    public List<InspectionProject> getTemplateAllProject(@RequestParam Map<String, String> params) {
        return inspectionProjectService.getTemplateProject(params);
    }

    /**
     * TODO 查询模板未绑定的项目
     * @param params
     * @return
     */
    @RequestMapping(value = "/getUnbandingProject", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getUnbandingProject(@RequestParam Map<String, String> params) {
        PageObject po = getPageObject(params);
        Integer total = inspectionProjectService.getUnbandingProjectCount(params);
        List<InspectionProject> projectList = inspectionProjectService.getUnbandingProject(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(projectList, total);
        return model;
    }

}
