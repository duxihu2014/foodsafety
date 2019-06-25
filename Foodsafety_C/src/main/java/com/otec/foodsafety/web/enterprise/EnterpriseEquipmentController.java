package com.otec.foodsafety.web.enterprise;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipment;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseScale;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseEquipmentChangeService;
import com.otec.foodsafety.service.enterprise.EnterpriseEquipmentService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.util.CompareObjectUtil;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;


/**
 * 企业设备及企业设备变更
 * */
@RestController
@RequestMapping("api/enterpriseEquipment")
public class EnterpriseEquipmentController
		extends
		VueBaseController<EnterpriseEquipmentService, EnterpriseEquipment, Long> {

	@Autowired
	EnterpriseEquipmentService enterpriseEquipmentService;

	@Autowired
    EnterpriseVerifyService enterpriseVerifyService;

	@Autowired
	SessionFilter sessionFilter;
	@Autowired
	EnterpriseEquipmentChangeService eeChangeService;
	
	/**
	 * 通过变更编号获取变更记录，对比历史记录返回同列内容不同的信息
	 * @param id变更编号
	 * 
	 * */
	@RequestMapping(value = "/getChange/{id}", method = RequestMethod.GET)
	private ObjectRestResponse<EnterpriseEquipmentChange> getChange(
			@PathVariable Long id) {
		try {
			Map<String, Object> result = new HashMap();
			List<Map<String, Object>> compareList = null;
			EnterpriseEquipmentChange eec = enterpriseEquipmentService
					.findByChangeId(id);
			if ("2".equals(eec.getChangeType())) {// 修改操作
				EnterpriseEquipment enterpriseEquipment = enterpriseEquipmentService
						.findById(Long.valueOf(eec.getEquipmentId()));
				compareList = CompareObjectUtil.compareTwoClass(
						enterpriseEquipment, eec);
			}
			result.put("formData", eec);
			result.put("compareData", compareList);
			return new ObjectRestResponse<EnterpriseEquipmentChange>()
					.rel(true).data(result);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseEquipmentChange>()
					.rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 审批操作
	 * @param changeId 变更记录Id
	 * @param auditType 0：审核不通过 1:审核通过
	 * @param verifyConclusion 审核批语
	 * @param verifyId 企业信息审批表主键
	 * */
	@RequestMapping(value="/verify",method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseVerify> auditEnterpriseCertificate(@RequestParam("changeId") String changeId,
                                                                           @RequestParam("auditType") String auditType,
                                                                           @RequestParam("verifyConclusion") String verifyConclusion,
                                                                           @RequestParam("verifyId") String verifyId) {
        try{
        	EnterpriseEquipment enterpriseEquipment = new EnterpriseEquipment();
        	EnterpriseEquipmentChange eec=enterpriseEquipmentService.findByChangeId(Long.valueOf(changeId));
            if(eec.getEquipmentId()!=null){
            	enterpriseEquipment = enterpriseEquipmentService.findById(Long.valueOf(eec.getEquipmentId()));
            }
            EnterpriseVerify enterpriseVerify = enterpriseVerifyService.findById(Long.valueOf(verifyId));
            if(auditType.equals("0")){//审核不通过
                enterpriseVerify.setVerifyStatus("3");
            }else {//审核通过
                enterpriseVerify.setVerifyStatus("2");
            }
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
	
	@RequestMapping(value = "/getEnterpriseEqu/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EnterpriseEquipmentExt> getEnterpriseEqu(@PathVariable Long id) {
		EnterpriseEquipmentExt item = enterpriseEquipmentService.getEnterpriseEquipmentById(id);
		return new ObjectRestResponse<EnterpriseMaterialExt>().rel(true).data(item);
	}


	/**
	 * 添加企业产品信息
	 * 
	 * @param multipartFile
	 * @param enterpriseCertificateStr
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseEquipmentChange> addCertificate(			
			@RequestParam(value = "EnterpriseEquipment") String enterpriseEquipmentStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);

			EnterpriseEquipment enterpriseEqu = JSONUtils.fromJson(enterpriseEquipmentStr, EnterpriseEquipment.class);

			enterpriseEquipmentService.addEnterpriseEquipment(sysUser.getUserId(), reason, enterpriseEqu);
			return new ObjectRestResponse<EnterpriseEquipmentChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseEquipmentChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	/**
	 * 修改企业证照信息
	 * 
	 * @param multipartFile
	 * @param enterpriseCertificateStr
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ObjectRestResponse<EnterpriseEquipmentChange> changrCertificate(
			@RequestParam(value = "EnterpriseEquipment") String enterpriseProductStr,
			@RequestParam(value = "reason") String reason) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);

			EnterpriseEquipment enterpriseEqu = JSONUtils.fromJson(enterpriseProductStr, EnterpriseEquipment.class);

			enterpriseEquipmentService.modifyEnterpriseEquipment(sysUser.getUserId(), reason, enterpriseEqu, "2");// 修改
			return new ObjectRestResponse<EnterpriseEquipmentChange>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseEquipmentChange>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<EnterpriseProduct> delete(@PathVariable Long id) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			EnterpriseEquipment enterpriseMaterial = enterpriseEquipmentService.findById(id);
			enterpriseEquipmentService.modifyEnterpriseEquipment(sysUser.getUserId(), "将产品永久报废", enterpriseMaterial, "3");// 删除

			return new ObjectRestResponse<EnterpriseProduct>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EnterpriseProduct>().rel(false);
			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}
	
	  /**
     * 企业设备信息审核查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/verify/query" ,method = RequestMethod.GET)
    public GridDataModel getCertificateChangePage(@RequestParam Map<String, String> params) {
    	  // 查询列表数据
        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model = eeChangeService.getGridDataModelByCondition(po);
        return model;
    }
    
    @RequestMapping(value = "/getEquipmentDetail/{id}", method = RequestMethod.GET)
   	@ResponseBody
   	public ObjectRestResponse<List<EnterpriseEquipment>> getEquipmentDetail(@PathVariable("id") Long id) {
   		Map<String,Object> map=new HashMap<>();
   		map.put("enterpriseId", id);
   		List<EnterpriseEquipment> list=enterpriseEquipmentService.findEntitysByCondition(map);
   		return new ObjectRestResponse<List<EnterpriseEquipment>>().rel(true).data(list);
   	}
}
