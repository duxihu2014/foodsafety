package com.otec.foodsafety.web.supervisory;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.purchase.Materials;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.supervisory.PersonnelThreeService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.MD5Encoder;
import com.otec.foodsafety.util.StringUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/supervisory/personnalThree")
public class PersonnelThreeController extends VueBaseController<PersonnelThreeService, PersonnelThree, Long> {


    @Autowired
    PersonnelThreeService personnelThreeService;
    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    DateLocalService dateLocalService;

    @Override
    public GridDataModel list(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){
            params.put("institutionId",sysUser.getInstitutionId().toString());
        }
        PageObject po = getPageObject(params);
        Integer total = personnelThreeService.getPageCount(params);
        List<PersonnelThree> list = personnelThreeService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;

    }

    @Override
    @ResponseBody
    public ObjectRestResponse<PersonnelThree> add(@RequestBody PersonnelThree entity) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);

            SysUser newUser=new SysUser();
            newUser.setUserName(entity.getUserName());
            newUser.setUserType("3");//监管人员
            newUser.setUserPwd(MD5Encoder.encode(SysInitConfig.getInstance().get(SysInitConfig.CfgProp.DEFAULTPASSWORD)));
            newUser.setCreateTime(dateLocalService.getDataBaseDate());
            newUser.setUserStatus("1");
            newUser.setRealName(entity.getUserName());
            newUser.setRoleId(Long.parseLong(entity.getPersonnelType()));

            entity.setInstitutionId(sysUser.getInstitutionId());
            entity.setAreaId(sysUser.getAreaId());
            entity.setCreateTime(dateLocalService.getDataBaseDate());
            entity.setPersonnelStatus("1");
            personnelThreeService.add(newUser,entity);

            return new ObjectRestResponse<PersonnelThree>().rel(true).data(entity);

        }catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<PersonnelThree>().rel(false);
            resp.setStatus(500);
            resp.setMessage("添加三员信息失败！");
            return resp;
        }
    }

    public ObjectRestResponse<PersonnelThree> update(@RequestBody PersonnelThree entity) {

        try {

              personnelThreeService.update(entity);

            return new ObjectRestResponse<PersonnelThree>().rel(true).data(entity);

        }catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<PersonnelThree>().rel(false);
            resp.setStatus(500);
            resp.setMessage("更新三员信息失败！");
            return resp;
        }
    }


    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ObjectRestResponse<PersonnelThree> changeStatus(@RequestParam Long[] ids, @RequestParam String status,@RequestParam Long[] userIds) {
        try {
            if (ids == null || !(status.equals("1") || status.equals("0"))) {
                return new ObjectRestResponse<PersonnelThree>().rel(false);
            }
            if (ids != null || ids.length != 0) {
                personnelThreeService.batchChangeStatus(ids, status,  userIds);
                return new ObjectRestResponse<PersonnelThree>().rel(true);
            }
            return new ObjectRestResponse<PersonnelThree>().rel(false);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<PersonnelThree>().rel(false);
            resp.setStatus(500);
            resp.setMessage("供货商删除异常");
            return resp;
        }
    }

}
