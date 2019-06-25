package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordDisinfection;
import com.otec.foodsafety.service.producesafety.RecordDisinfectionService;
import com.otec.foodsafety.service.util.DateLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 餐具消毒监督
 */
@RequestMapping("/api/enterprise/disinfection")
@RestController
public class DisinfectionInterface extends BaseInterface {

    @Autowired
    RecordDisinfectionService recordDisinfectionService;
    @Autowired
    DateLocalService dateLocalService;

    /**
     * 餐具消毒查询
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params, @PathVariable Long enterpriseId) {

        // 查询列表数据
        params.put("enterpriseId",enterpriseId.toString());
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = recordDisinfectionService.findRecordDisinfection(po);
        return model;
    }

    /**
     * 	获取餐具消毒记录详细信息
     * @param recordId
     * @return
     */
    @RequestMapping(value="/getDetail/{recordId}" ,method = RequestMethod.GET)
    public ResponseEntity<RecordDisinfection> getDetail(@PathVariable Long recordId ) {
        RecordDisinfection disinfection= recordDisinfectionService.findById(recordId);
        return   ResponseEntity.ok(disinfection);
    }

    /**
     * 	餐具消毒记录保存（添加、更新）
     * @param entity
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<RecordDisinfection> save(@RequestBody RecordDisinfection entity) {
        try {
            if(entity.getRecordId() == null || entity.getRecordId().intValue()<=0){
                entity.setCreateTime(dateLocalService.getDataBaseDate());
                recordDisinfectionService.persist(entity);
            }
            else{
                RecordDisinfection old=recordDisinfectionService.findById(entity.getRecordId().longValue());
                entity.setCreateTime(old.getCreateTime());
                entity.setUpdateTime(dateLocalService.getDataBaseDate());
                recordDisinfectionService.updateById(entity);
            }
            return new ObjectRestResponse<RecordDisinfection>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RecordDisinfection>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }

     }
}
