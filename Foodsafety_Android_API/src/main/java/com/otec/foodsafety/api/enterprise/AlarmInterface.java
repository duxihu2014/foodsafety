package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.alarm.AlarmExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  视频报警
 */
@RestController
@RequestMapping("/api/alarm")
public class AlarmInterface extends BaseInterface {


    @Autowired
    AlarmService alarmService;
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    EnterpriseBaseService enterpriseBaseService;

    /**
     * 报警列表查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel staffcert(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("areaId",sysUser.getAreaId().toString());
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model= alarmService.getGridDataModelByCondition(po);
        List<AlarmExt> list= (List<AlarmExt>) model.getRows();
        for (AlarmExt alarm:list){
            EnterpriseBase enterpriseBase=enterpriseBaseService.findById(alarm.getEnterpriseId());
            if(enterpriseBase!=null){
                alarm.setOrganizingInstitutionBarCode(enterpriseBase.getOrganizingInstitutionBarCode());
                alarm.setRegisterAddress(enterpriseBase.getRegisterAddress());
            }
        }
        return model;
    }




}
