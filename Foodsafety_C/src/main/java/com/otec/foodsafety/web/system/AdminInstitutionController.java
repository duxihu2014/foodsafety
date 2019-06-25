package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.*;
import com.otec.foodsafety.service.system.SysInstitutionService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/institution")
public class AdminInstitutionController extends VueBaseController<SysInstitutionService, SysInstitution, Long> {
    @Autowired
    private SysInstitutionService sysInstitutionService;
    @Autowired
    private DateLocalService dateLocalService;
    @Autowired
    SessionFilter sessionFilter;

    @Override
    public ObjectRestResponse<SysInstitution> add(@RequestBody SysInstitution entity) {
        try {
            entity.setInstitutionStatus("1");
            entity.setCreateTime(dateLocalService.getDataBaseDate());
            sysInstitutionService.addSysInstitution(entity);
            return new ObjectRestResponse<SysInstitution>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysInstitution>().rel(false);
            resp.setStatus(500);
            resp.setMessage("新增失败!");
            return resp;
        }
    }

    @Override
    public ObjectRestResponse<SysInstitution> update(@RequestBody SysInstitution entity) {
        try{
            entity.setUpdateTime(dateLocalService.getDataBaseDate());
            return super.update(entity);
        }catch (Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysInstitution>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新失败!");
            return resp;
        }
    }
    /**
     * 检查部门名称是否重复
     * @param deptName
     * @return
     */
    @RequestMapping(value = "/checkInstitutionName", method = RequestMethod.GET)
    public ObjectRestResponse<SysInstitution> checkCompName(@RequestParam String deptName) {
        Map<String ,Object> param = new HashMap<>();
        param.put("institutionName",deptName);
        List<SysInstitution> sysInstitutions = sysInstitutionService.findEntitysByCondition(param);
        if(sysInstitutions.size()>0){
            ObjectRestResponse resp = new ObjectRestResponse<SysInstitution>().rel(false);
            return resp;
        }else{
            ObjectRestResponse resp = new ObjectRestResponse<SysInstitution>().rel(true);
            return resp;
        }

    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<SysInstitution> changeStatus(@RequestParam Long[] ids, @RequestParam String status) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<SysInstitution>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                sysInstitutionService.batchChangeStatus(ids, status);
                return new ObjectRestResponse<SysInstitution>().rel(true);
            }
            return new ObjectRestResponse<SysInstitution>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SysInstitution>().rel(false);
            resp.setStatus(500);
            resp.setMessage("机构删除异常");
            return resp;
        }
    }

    /**
     * 查询操作员相关机构的树型结果数据
     *
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<InstitutionTree> getTree() {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        return sysInstitutionService.listTree(sysUser.getInstitutionId(), true);
    }
}

