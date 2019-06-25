package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.producesafety.RecordDisinfection;
import com.otec.foodsafety.entity.producesafety.RecordMorningCheck;
import com.otec.foodsafety.service.producesafety.RecordMorningCheckService;
import com.otec.foodsafety.service.util.DateLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 晨检
 */
@RequestMapping("/api/enterprise/morningCheck")
@RestController
public class MorningCheckInterface extends BaseInterface {

    @Autowired
    RecordMorningCheckService recordMorningCheckService;
    @Autowired
    DateLocalService dateLocalService;

    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params, @PathVariable Long enterpriseId) {
        // 查询列表数据
        params.put("enterpriseId",enterpriseId.toString());
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = recordMorningCheckService.findRecordMorningCheck(po);
        return model;
    }

    /**
     * 	获取晨检记录详细信息
     * @param recordId
     * @return
     */
    @RequestMapping(value="/getDetail/{recordId}" ,method = RequestMethod.GET)
    public ResponseEntity<RecordMorningCheck> getDetail(@PathVariable Long recordId ) {
        RecordMorningCheck morningCheck= recordMorningCheckService.findById(recordId);
        return   ResponseEntity.ok(morningCheck);
    }

    /**
     *  晨检记录保存（添加、更新）
     * @param entity
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<RecordMorningCheck> save(@RequestBody RecordMorningCheck entity) {
        try {
            if(entity.getRecordId()==null || entity.getRecordId().intValue()<=0){
                entity.setCreateTime(dateLocalService.getDataBaseDate());
                recordMorningCheckService.persist(entity);
            }
            else{
                RecordMorningCheck old=recordMorningCheckService.findById(entity.getRecordId().longValue());
                entity.setCreateTime(old.getCreateTime());
                entity.setUpdateTime(dateLocalService.getDataBaseDate());
                recordMorningCheckService.updateById(entity);
            }
            return new ObjectRestResponse<RecordMorningCheck>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RecordMorningCheck>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }

    }

}
