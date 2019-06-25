package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionProject;
import com.otec.foodsafety.entity.safety.InspectionResultPhoto;
import com.otec.foodsafety.entity.safety.InspectionTemplate;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.safety.*;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.StringUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RequestMapping("/api/enterprise/inspection")
@RestController
public class InspectionPlanInterface extends BaseInterface {


    @Autowired
    InspectionPlanService inspectionPlanService;
    @Autowired
    InspectionResultService inspectionResultService;
    @Autowired
    InspectionResultItemService inspectionResultItemService;
    @Autowired
    InspectionTemplateService inspectionTemplateService;
    @Autowired
    InspectionProjectService inspectionProjectService;
    @Autowired
    InspectionResultPhotoService inspectionResultPhotoService;
    @Autowired
    SysResourceService sysResourceService;
    /**
     * 获取企业已完成的日常检查
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/{enterpriseId}", method = RequestMethod.GET)
    public GridDataModel getEnterpriseInspectionPlan(@RequestParam Map<String, String> params, @PathVariable Long enterpriseId) {

        params.put("enterpriseId", enterpriseId.toString());
        params.put("planStatus", "1");
        PageObject po = getPageObject(params);
        List<Map<String, Object>> planList = new ArrayList<>();
        Integer total = inspectionPlanService.getEnterpriseInspectionPlanCount(params);
        if (total > 0)
            planList = inspectionPlanService.getEnterpriseInspectionPlan(params, po.getOffset(), po.getPageSize());

        GridDataModel model = new GridDataModel(planList, total);
        return model;
    }

    /**
     * 获取日常核查结果
     * @param planId
     * @return
     */
    @RequestMapping(value = "/result/{planId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOnsiteVerificationResult(@RequestParam Map<String, String> params,@PathVariable Long planId) {

        params.put("planId",planId.toString());
        List<Map<String, Object>> resultList = inspectionResultService.getInspectionResult(params);

        if (resultList != null && !resultList.isEmpty()){
            Map<String, Object> result=resultList.get(0);
            Map<String, Object> params2= new HashMap<>();
            params2.put("resultId",result.get("resultId"));

            InspectionTemplate  template = inspectionTemplateService.findById(((Long)result.get("templateId")).intValue());

            if(template!=null){
                Map templatemap=new HashMap();
                templatemap.put("templateId",template.getTemplateId());
                templatemap.put("projectStatus","1");
                List<HashMap> pros=(List<HashMap>)inspectionProjectService.getTemplateProject(templatemap);
                 String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
                for (HashMap pro:pros){
                    HashMap<String,Object> pr=new HashMap<>();
                    params2.put("projectId",pro.get("projectId"));
                    List<Map<String, Object>> list = inspectionResultItemService.getInspectionResultItem(params2);
                    if(list.size()>0){
                        Map<String, Object> itemMap=list.get(0);
                        SysResource resource= sysResourceService.findById((Long)itemMap.get("resourceId"));
                        pro.put("photoPath",imageServerUrl+"/"+resource.getResourcePath());
                        pro.put("resultsQualifiedSelectIndex", itemMap.get("resultsQualified"));
                        pro.put("resultsUnqualifiedSelectIndex", itemMap.get("resultsUnqualified"));
                    }

                }

                result.put("projects",pros);
            }
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(new HashMap<>());
        }
    }


}
