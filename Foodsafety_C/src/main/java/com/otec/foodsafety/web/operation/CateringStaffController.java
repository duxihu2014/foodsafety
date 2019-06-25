package com.otec.foodsafety.web.operation;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.operation.CateringStaffCertificate;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.catering.CateringStaffService;
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
@RequestMapping("api/catering/staff")
public class CateringStaffController extends VueBaseController<CateringStaffService, CateringStaff, Long> {
    @Autowired
    private CateringStaffService cateringStaffService;
    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    SysAreaService sysAreaService;


    @Override
     public GridDataModel list(@RequestParam Map<String, String> params) {

         SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());
         // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = cateringStaffService.getPageCount(params);
        List<CateringStaff> list = cateringStaffService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;

    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ObjectRestResponse<CateringStaff> save(
              @RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
              @RequestParam("staffName") String staffName,
              @RequestParam("idCardNo")String idCardNo,
              @RequestParam("sex")String sex,
              @RequestParam("enterpriseId")String enterpriseId,
              @RequestParam(value = "birthDate")String birthDate,
              @RequestParam(value = "homeAddress",required = false) String homeAddress,
              @RequestParam(value = "education",required = false)String education,
              @RequestParam(value = "major",required = false)String major,
              @RequestParam(value = "department",required = false)String department,
              @RequestParam(value = "employeeNumber")String employeeNumber,
              @RequestParam(value = "workType")String workType,
              @RequestParam(value = "employmentDate")String employmentDate,
              @RequestParam(value = "contactNumber",required = false)String contactNumber,
              @RequestParam(value = "staffStatus")String staffStatus,
              @RequestParam(value = "certificateNumber")String certificateNumber,
              @RequestParam(value = "examinationDate")String examinationDate,
              @RequestParam(value = "validDate")String validDate,
              @RequestParam(value = "issuingDate")String issuingDate,
              @RequestParam(value = "issuingUnit")String issuingUnit,
              @RequestParam(value = "certificateId",required = false)String certificateId,
              @RequestParam(value = "staffId",required = false)String staffId) {
        try {

            CateringStaff cs=new CateringStaff();

            cs.setEnterpriseId(Long.parseLong(enterpriseId));
            cs.setStaffName(staffName);
            cs.setIdCardNo(idCardNo);
            cs.setSex(sex);
            cs.setBirthDate(DateUtils.getDate(birthDate,"yyyy-MM-dd"));
            cs.setEducation(education);
            cs.setMajor(major);
            cs.setDepartment(department);
            cs.setEmployeeNumber(employeeNumber);
            cs.setEmploymentDate(DateUtils.getDate(employmentDate,"yyyy-MM-dd"));
            cs.setWorkType(workType);
            cs.setContactNumber(contactNumber);
            cs.setHomeAddress(homeAddress);
            cs.setStaffStatus(staffStatus);
            CateringStaffCertificate csc=new CateringStaffCertificate();
            if(!StringUtils.isBlankString(certificateId))
                csc.setCertificateId(Long.parseLong(certificateId));
            csc.setCertificateNumber(certificateNumber);
            csc.setExaminationDate(DateUtils.getDate(examinationDate,"yyyy-MM-dd"));
            csc.setValidDate(DateUtils.getDate(validDate,"yyyy-MM-dd"));
            csc.setIssuingDate(DateUtils.getDate(issuingDate,"yyyy-MM-dd"));
            csc.setIssuingUnit(issuingUnit);

            String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
            String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);

            if(StringUtils.isBlankString(staffId)){
                cateringStaffService.add(cs,csc,imageFile, uploadUrl, imageFolder);
            }
            else{
                cs.setStaffId(Long.parseLong(staffId));
                csc.setStaffId(cs.getStaffId());
                cateringStaffService.update(cs,csc,imageFile, uploadUrl, imageFolder);
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




    @Override
    public ObjectRestResponse<CateringStaff> get(@PathVariable Long id) {
        CateringStaff cs= cateringStaffService.findByStaffId(id);
        return new ObjectRestResponse<CateringStaff>().rel(true) .data(cs);
    }


}

