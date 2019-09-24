package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.producesafety.RecordMorningCheck;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.catering.CateringStaffService;
import com.otec.foodsafety.service.producesafety.RecordMorningCheckService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.context.SessionFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private SessionFilter sessionFilter;


    @Autowired
    private CateringStaffService cateringStaffService;

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


    @RequestMapping(value="/deteteMorningCheck" ,method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> deteteMorningCheck(Long recordId) {
        recordMorningCheckService.removeById(recordId);
        Map<Object, Object> item = new HashMap<Object, Object>();
        item.put("state",1);
        return ResponseEntity.ok(item);
    }



    //晨检人列表接口
    @RequestMapping(value="/checkPeopleList" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel checkPeopleList(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());

        params.put("staffStatus","1");//员工状态 ，1-在职

        String staffName = params.get("staffName");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(staffName)) {
            params.put("staffName",staffName);
        }

        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = cateringStaffService.getPageCount(params);
        List<CateringStaff> list = cateringStaffService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;

    }



}
