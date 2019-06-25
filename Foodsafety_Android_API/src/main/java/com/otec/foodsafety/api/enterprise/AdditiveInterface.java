package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.CateringStaffeExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseage;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.producesafety.RecordAdditiveUseageService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.SysInitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 添加剂
 */
@RestController
@RequestMapping("/api/enterprise/additive")
public class AdditiveInterface  extends BaseInterface {

    @Autowired
    RecordAdditiveUseageService recordAdditiveUseageService;
    @Autowired
    DateLocalService dateLocalService;

    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    public GridDataModel getCertificate(@RequestParam Map<String, String> params, @PathVariable Long enterpriseId) {
        // 查询列表数据
        params.put("enterpriseId", enterpriseId.toString());
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = recordAdditiveUseageService.getGridDataAdditiveUseageByCondition(po);
        return model;


    }

    /**
     * 获取添加剂使用详细信息
     * @param useageId
     * @return
     */
    @RequestMapping(value = "/detail/{useageId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RecordAdditiveUseage> get(@PathVariable Long useageId) {
        RecordAdditiveUseage cs= recordAdditiveUseageService.findById(useageId);
         return ResponseEntity.ok(cs);
    }

    /**
     * 添加剂使用记录保存（添加、更新）
     * @param additiveUseage
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<RecordAdditiveUseage> save(@RequestBody RecordAdditiveUseage  additiveUseage){
        try {
            if(additiveUseage.getUseageId()==null ||  additiveUseage.getUseageId().intValue()<=0){
                additiveUseage.setCreateTime(dateLocalService.getDataBaseDate());
                recordAdditiveUseageService.persist(additiveUseage);
            }
            else{
                RecordAdditiveUseage old=recordAdditiveUseageService.findById(additiveUseage.getUseageId().longValue());
                additiveUseage.setCreateTime(old.getCreateTime());
                additiveUseage.setUpdateTime(dateLocalService.getDataBaseDate());
                recordAdditiveUseageService.updateById(additiveUseage);
            }
            return new ObjectRestResponse<RecordAdditiveUseage>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RecordAdditiveUseage>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }
    }

    /**
     * 添加剂使用记录删除
     * @param useageId
     * @return
     */
    @RequestMapping(value = "/delete/{useageId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<RecordAdditiveUseage> remove(@PathVariable("useageId") Long useageId) {
        try {
            recordAdditiveUseageService.removeById(useageId);
            return new ObjectRestResponse<RecordAdditiveUseage>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RecordAdditiveUseage>().rel(false);
            resp.setStatus(500);
            resp.setMessage("删除失败!");
            return resp;
        }
    }

}
