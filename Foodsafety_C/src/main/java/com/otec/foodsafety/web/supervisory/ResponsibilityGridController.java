package com.otec.foodsafety.web.supervisory;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGridRegion;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseSupervisionService;
import com.otec.foodsafety.service.supervisory.ResponsibilityGridService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/supervisory/grid")
public class ResponsibilityGridController extends VueBaseController<
        ResponsibilityGridService, ResponsibilityGrid, Long> {

    @Autowired
    ResponsibilityGridService responsibilityGridService;
    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    DateLocalService dateLocalService;
    @Autowired
    EnterpriseSupervisionService enterpriseSupervisionService;

    @Override
    @ResponseBody
    public ObjectRestResponse<ResponsibilityGrid> add(@RequestBody ResponsibilityGrid entity) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);

            entity.setCreateTime(dateLocalService.getDataBaseDate());
            entity.setGridStatus("1");
            if(entity.getInstitutionId()<=0){
                entity.setInstitutionId(sysUser.getInstitutionId());
                entity.setAreaId(sysUser.getAreaId());
            }
            responsibilityGridService.add(entity);

            return  new ObjectRestResponse<ResponsibilityGrid>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ResponsibilityGrid>().rel(false);
            resp.setStatus(500);
            resp.setMessage("添加责任区域信息失败！");
            return resp;
        }
    }

    @ResponseBody
    public ObjectRestResponse<ResponsibilityGrid> update(@RequestBody ResponsibilityGrid entity) {
        try {
            responsibilityGridService.update(entity);
            return  new ObjectRestResponse<ResponsibilityGrid>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ResponsibilityGrid>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新责任区域信息失败！");
            return resp;
        }
    }


    @Override
    public GridDataModel list(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){
            params.put("institutionId",sysUser.getInstitutionId().toString());
        }
        PageObject po = getPageObject(params);
        Integer total = responsibilityGridService.getPageCount(params);
        List<ResponsibilityGrid> list = responsibilityGridService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }


    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<ResponsibilityGrid> changeStatus(@RequestParam Long[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<ResponsibilityGrid>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                responsibilityGridService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<ResponsibilityGrid>().rel(true);
            }
            return new ObjectRestResponse<ResponsibilityGrid>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ResponsibilityGrid>().rel(false);
            resp.setStatus(500);
            resp.setMessage("供货商删除异常");
            return resp;
        }
    }


    /**
     * 查询本机构下的 除gridId ={id}的其它网格坐标
     * @param gridId
     * @param institutionId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/otherGrids", method = RequestMethod.GET)
    public ObjectRestResponse<ResponsibilityGrid> getOtherGrids(@RequestParam(required = false) Long gridId,@RequestParam(required = false) Long institutionId) {

        List<ResponsibilityGrid>  list=responsibilityGridService.getOtherGridsCoordinate(institutionId,gridId);

        return new ObjectRestResponse<ResponsibilityGrid>().rel(true).data(list);
    }

    @ResponseBody
    @RequestMapping(value = "/getSupervisionByGrid", method = RequestMethod.GET)
    public ObjectRestResponse< List<Map<String,Object>>> getSupervisionByGrid(@RequestParam(required = false) Long gridId) {

        List<Map<String,Object>>  list=enterpriseSupervisionService.getSupervisionByGrid(gridId);

        return new ObjectRestResponse<ResponsibilityGrid>().rel(true).data(list);
    }

}
