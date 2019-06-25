package com.otec.foodsafety.api.enterprise;


import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.CateringStaffeExt;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.catering.CateringStaffService;
import com.otec.foodsafety.service.catering.StaffCertificateService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.SysInitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/staff")
@RestController
public class StaffInterface extends BaseInterface{

    @Autowired
    CateringStaffService cateringStaffService;

    @Autowired
    private StaffCertificateService staffCertificateService;

    @Autowired
    SysResourceService sysResourceService;

    /**
     * 获取企业人员列表
     * @param params
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/list/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel list(@RequestParam Map<String, String> params,@PathVariable Long enterpriseId) {
        params.put("enterpriseId",enterpriseId.toString() );
        params.put("staffStatus","1");
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        Integer total = staffCertificateService.getPageCount(params);
        List<CateringStaff> list = staffCertificateService.getPage(params,po.getOffset(),po.getPageSize());

        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (CateringStaff cs:list){
            SysResource resource=sysResourceService.findById(cs.getCertificatePhoto());
            if(resource!=null){
                cs.setResourcePath(imageServerUrl+"/"+resource.getResourcePath());
            }
        }
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }


    /**
     * 获取人员详细信息
     * @param staffId
     * @return
     */
    @RequestMapping(value = "/{staffId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CateringStaff> get(@PathVariable Long staffId) {
        CateringStaff cs= cateringStaffService.findByStaffId(staffId);
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        cs.setResourcePath(imageServerUrl+"/"+cs.getResourcePath());
        return ResponseEntity.ok(cs);
    }


    /**
     *  获取企业人员健康证状态分类数量
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/stateCertificate/{enterpriseId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> stateCertificate(@PathVariable Long enterpriseId) {
        Map result= staffCertificateService.getStateCertificate(enterpriseId);
        if(result==null){
            result=new HashMap();
            result.put("imminentCount",0);
            result.put("expiredCount",0);
            result.put("normalCount",0);
        }
        return ResponseEntity.ok(result);
    }


    /**
     * 	企业人员信息保存 包括健康证（添加、更新）
     * @param ext
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<CateringStaff> save(@RequestBody CateringStaffeExt ext){
        try {

            CateringStaff cs=ext.getStaff();
            CateringStaffCertificate csc=ext.getCertificate();
            if(cs.getStaffId()<=0){
                cateringStaffService.add(cs,csc);
            }
            else{
                csc.setStaffId(cs.getStaffId());
                cateringStaffService.update(cs,csc);
            }
            return new ObjectRestResponse<CateringStaff>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<CateringStaff>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }
    }

    /**
     *	从业人员删除（离职）
     * @param staffId
     * @return
     */
    @RequestMapping(value = "/delete/{staffId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<CateringStaff> delete(@PathVariable Long staffId) {
        try {
            CateringStaff cs = cateringStaffService.findById(staffId);
            cs.setStaffStatus("2");
            cateringStaffService.updateById(cs);
            return new ObjectRestResponse<CateringStaff>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<CateringStaff>().rel(false);
            resp.setStatus(500);
            resp.setMessage("从业人员离职删除失败!");
            return resp;
        }
    }

}
