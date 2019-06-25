package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.producesafety.RecordWasteSituation;
import com.otec.foodsafety.entity.purchase.MaterialsEnterprisePurchase;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.producesafety.RecordWasteSituationService;
import com.otec.foodsafety.service.purchase.MaterialsEnterprisePurchaseService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.SysInitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 废弃物处理
 */
@RequestMapping("/api/enterprise/waste")
@RestController
public class WasteInterfafce extends BaseInterface {



    @Autowired
    RecordWasteSituationService recordWasteSituationService;
    @Autowired
    DateLocalService dateLocalService;
    @Autowired
    SysResourceService sysResourceService;


    /**
     * 获取企业废弃物处理信息列表
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value="/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {

        // 查询列表数据
        params.put("enterpriseId",enterpriseId.toString());
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = recordWasteSituationService.findRecordWasteSituation(po);

        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (RecordWasteSituation situation:(List<RecordWasteSituation>)model.getRows()){
            SysResource sysResource=sysResourceService.getResourceById(situation.getWastePhoto());
            if(sysResource!=null){
                situation.setWastePhotoPath(imageServerUrl+"/"+sysResource.getResourcePath());
            }
         }

        return model;
    }

    /**
     *  获取废弃物处理详细信息
     * @param situationId
     * @return
     */
    @RequestMapping(value = "/detail/{situationId}", method = RequestMethod.GET)
    public ResponseEntity  get(@PathVariable Long situationId) {
        RecordWasteSituation situation= recordWasteSituationService.findById(situationId);
        SysResource sysResource=sysResourceService.getResourceById(situation.getWastePhoto());
        if(sysResource!=null){
            String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
            situation.setWastePhotoPath(imageServerUrl+"/"+sysResource.getResourcePath());
        }
         return ResponseEntity.ok(situation);
    }


    /**
     * 废弃物处理信息保存 （添加、更新）
     * @param situation
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<RecordWasteSituation> save(@RequestBody RecordWasteSituation situation){
        try {
            if(situation.getSituationId()!=null && situation.getSituationId().longValue()>0){
                RecordWasteSituation old=recordWasteSituationService.findById(situation.getSituationId());
                situation.setCreateTime(old.getCreateTime());
                situation.setUpdateTime(dateLocalService.getDataBaseDate());
                recordWasteSituationService.updateById(situation);
            }else{
                situation.setCreateTime(dateLocalService.getDataBaseDate());
                recordWasteSituationService.persist(situation);
            }
            return new ObjectRestResponse<RecordWasteSituation>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<RecordWasteSituation>().rel(false);
            resp.setStatus(500);
            resp.setMessage("废弃物处理保存失败!");
            return resp;
        }
    }

}
