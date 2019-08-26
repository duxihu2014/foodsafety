package com.otec.foodsafety.api.enterprise;

import com.alibaba.fastjson.JSONArray;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.CateringStaffeExt;
import com.otec.foodsafety.entity.EnterpriseEquipmentChangeExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipment;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseEquipmentService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


/**
 * 设备信息
 */
@RestController
@RequestMapping("/api/enterprise/equipment")
public class EnterpriseEquipmentInterface  extends BaseInterface {


    @Autowired
    EnterpriseEquipmentService enterpriseEquipmentService;
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    EnterpriseVerifyService enterpriseVerifyService;

    /**
     *  获取企业设备设施列表
     * @param
     * @return
     */
    @RequestMapping(value="/list/{enterpriseId}" ,method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        params.put("enterpriseId",enterpriseId.toString());
        params.put("equipmentStatus","1");
        po.getCondition().putAll(params);
        GridDataModel model = enterpriseEquipmentService.getGridDataModelByCondition(po);
        return model;

    }


    @RequestMapping(value = "/{equipmentId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EnterpriseEquipment> get(@PathVariable Long equipmentId) {
        EnterpriseEquipment equipment=enterpriseEquipmentService.findById(equipmentId);
        return ResponseEntity.ok(equipment);
    }

    /**
     * 	企业设备信息保存（添加、更新）
     * @param ext
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<EnterpriseEquipment> save(@RequestBody EnterpriseEquipmentChangeExt ext){
        try {

            SysUser sysUser = sessionFilter.getJWTUser(request);
            if(ext.getEquipment().getEquipmentId()!=null && ext.getEquipment().getEquipmentId()>0){
                EnterpriseEquipment equipment=enterpriseEquipmentService.findById(ext.getEquipment().getEquipmentId().longValue());
                ext.getEquipment().setEquipmentStatus(equipment.getEquipmentStatus());
                 enterpriseEquipmentService.modifyEnterpriseEquipment(sysUser.getUserId(),ext.getReason(),ext.getEquipment(),"2");
            }else{
              enterpriseEquipmentService.addEnterpriseEquipment(sysUser.getUserId() ,ext.getReason(),ext.getEquipment());
            }
            return new ObjectRestResponse<EnterpriseEquipment>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseEquipment>().rel(false);
            resp.setStatus(500);
            resp.setMessage("设备信息保存失败!");
            return resp;
        }
    }

    /**
     * 企业设备删除
     * @param equipmentId
     * @return
     */
    @RequestMapping(value = "/delete/{equipmentId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<EnterpriseEquipment> delete(@PathVariable Long equipmentId) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseEquipment enterpriseMaterial = enterpriseEquipmentService.findById(equipmentId);
            enterpriseEquipmentService.modifyEnterpriseEquipment(sysUser.getUserId(), "将产品永久报废", enterpriseMaterial, "3");// 删除
            return new ObjectRestResponse<EnterpriseProduct>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseEquipment>().rel(false);
            resp.setStatus(500);
            resp.setMessage("设备信息删除失败!");
            return resp;
        }
    }

    /**
     * 审批操作
     * @param changeId 变更记录Id
     * @param verifyConclusion 审核批语
     * @param verifyId 企业信息审批表主键
     * */
    @RequestMapping(value="/revoke/{changeId}/{verifyId}",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<EnterpriseVerify> revoke(@PathVariable String changeId,@PathVariable String verifyId,
                                                                         @RequestParam("verifyConclusion") String verifyConclusion) {
        try{
            EnterpriseEquipment enterpriseEquipment = new EnterpriseEquipment();
            EnterpriseEquipmentChange eec=enterpriseEquipmentService.findByChangeId(Long.valueOf(changeId));
//            if(eec.getEquipmentId()!=null){
//                enterpriseEquipment = enterpriseEquipmentService.findById(Long.valueOf(eec.getEquipmentId()));
//            }
            EnterpriseVerify enterpriseVerify = enterpriseVerifyService.findById(Long.valueOf(verifyId));
            enterpriseVerify.setVerifyStatus("4");  //撤回
            SysUser sysUser = sessionFilter.getJWTUser(request);
            enterpriseVerify.setVerifyUserId(sysUser.getUserId());
            enterpriseVerify.setVerifyTime(new Date());
            enterpriseVerify.setVerifyConclusion(verifyConclusion);
            enterpriseEquipmentService.verifyEnterpriseCertificate(enterpriseEquipment,eec,enterpriseVerify);
            return new ObjectRestResponse<EnterpriseEquipmentChange>().rel(true);
        }catch (Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseEquipmentChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }

    }
}
