package com.otec.foodsafety.web.operation;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.catering.StaffCertificateService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.util.DateUtils;
import com.otec.foodsafety.util.StringUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/staff/certificate")
public class StaffCertificateController extends VueBaseController<StaffCertificateService, CateringStaffCertificate, Long> {
    @Autowired
    private  StaffCertificateService staffCertificateService;
    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    SysAreaService sysAreaService;

    @RequestMapping(value = "/getCertificateList", method = RequestMethod.GET)
    public GridDataModel getUserList(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){
            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());

        PageObject po = getPageObject(params);
        Integer total = staffCertificateService.getPageCount(params);
        List<CateringStaff> list = staffCertificateService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ObjectRestResponse<CateringStaff> update(
            @RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
            @RequestParam(value = "certificateNumber")String certificateNumber,
            @RequestParam(value = "examinationDate")String examinationDate,
            @RequestParam(value = "validDate")String validDate,
            @RequestParam(value = "issuingDate")String issuingDate,
            @RequestParam(value = "issuingUnit")String issuingUnit,
            @RequestParam(value = "certificateId",required = false)String certificateId,
            @RequestParam(value = "staffId")String staffId) {
        try {

            CateringStaffCertificate csc=new CateringStaffCertificate();
            csc.setCertificateId(Long.parseLong(certificateId));
            csc.setCertificateNumber(certificateNumber);
            csc.setExaminationDate(DateUtils.getDate(examinationDate,"yyyy-MM-dd"));
            csc.setValidDate(DateUtils.getDate(validDate,"yyyy-MM-dd"));
            csc.setIssuingDate(DateUtils.getDate(issuingDate,"yyyy-MM-dd"));
            csc.setIssuingUnit(issuingUnit);

            csc.setStaffId(Long.parseLong(staffId));


            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);

            staffCertificateService.update(csc,imageFile, uploadUrl, imageFolder);

            return new ObjectRestResponse<CateringStaff>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<CateringStaff>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ObjectRestResponse<CateringStaff> add(
            @RequestParam(value = "imageFile") MultipartFile imageFile,
            @RequestParam(value = "certificateNumber")String certificateNumber,
            @RequestParam(value = "examinationDate")String examinationDate,
            @RequestParam(value = "validDate")String validDate,
            @RequestParam(value = "issuingDate")String issuingDate,
            @RequestParam(value = "issuingUnit")String issuingUnit,
            @RequestParam(value = "staffId")String staffId) {
        try {

            CateringStaffCertificate csc=new CateringStaffCertificate();

            csc.setCertificateNumber(certificateNumber);
            csc.setExaminationDate(DateUtils.getDate(examinationDate,"yyyy-MM-dd"));
            csc.setValidDate(DateUtils.getDate(validDate,"yyyy-MM-dd"));
            csc.setIssuingDate(DateUtils.getDate(issuingDate,"yyyy-MM-dd"));
            csc.setIssuingUnit(issuingUnit);
            csc.setStaffId(Long.parseLong(staffId));

            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);


            staffCertificateService.add(csc,imageFile, uploadUrl, imageFolder);

            return new ObjectRestResponse<CateringStaff>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<CateringStaff>().rel(false);
            resp.setStatus(500);
            resp.setMessage("保存失败!");
            return resp;
        }
    }

}

