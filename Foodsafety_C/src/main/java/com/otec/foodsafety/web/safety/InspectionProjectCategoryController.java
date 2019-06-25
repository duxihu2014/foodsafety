package com.otec.foodsafety.web.safety;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionProjectCategory;
import com.otec.foodsafety.service.safety.InspectionProjectCategoryService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/inspection/template/category")
public class InspectionProjectCategoryController extends VueBaseController<
        InspectionProjectCategoryService, InspectionProjectCategory, Integer> {
    @Autowired
    InspectionProjectCategoryService inspectionProjectCategoryService;
    @Autowired
    DateLocalService dateLocalService;

    @Override
    public ObjectRestResponse<InspectionProjectCategory> add(@RequestBody InspectionProjectCategory entity) {
        try {
            entity.setCategoryStatus("1");
            entity.setCreateTime(dateLocalService.getDataBaseDate());
            return super.add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProjectCategory>().rel(false);
            resp.setStatus(500);
            resp.setMessage("新增失败!");
            return resp;
        }
    }

    @Override
    public ObjectRestResponse<InspectionProjectCategory> update(@RequestBody InspectionProjectCategory entity) {
        try{
            entity.setUpdateTime(dateLocalService.getDataBaseDate());
            return super.update(entity);
        }catch (Exception e){
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProjectCategory>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新失败!");
            return resp;
        }
    }

    /**
     * @TODO 检查类型名称是否重复
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "/checkCategoryName", method = RequestMethod.GET)
    public ObjectRestResponse<InspectionProjectCategory> checkCategoryName(@RequestParam String categoryName) {
        Map<String ,Object> param = new HashMap<>();
        param.put("categoryName", categoryName);
        List<InspectionProjectCategory> inspectionProjectCategory = inspectionProjectCategoryService.findEntitysByCondition(param);
        if(inspectionProjectCategory.size() > 0){
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProjectCategory>().rel(false);
            return resp;
        } else {
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProjectCategory>().rel(true);
            return resp;
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<InspectionProjectCategory> changeStatus(@RequestParam Integer[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<InspectionProjectCategory>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                inspectionProjectCategoryService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<InspectionProjectCategory>().rel(true);
            }
            return new ObjectRestResponse<InspectionProjectCategory>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<InspectionProjectCategory>().rel(false);
            resp.setStatus(500);
            resp.setMessage("类型删除异常");
            return resp;
        }
    }
}
