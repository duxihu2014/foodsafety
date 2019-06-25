package com.otec.foodsafety.web.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.*;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseScaleService;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.util.CompareObjectUtil;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/10/22 0022 上午 10:38
 */
@RestController
@RequestMapping("api/enterpriseScale")
public class EnterpriseScaleController extends VueBaseController<EnterpriseScaleService, EnterpriseScale, Long> {

    @Autowired
    private EnterpriseScaleService enterpriseScaleService;

    @Autowired
    private EnterpriseVerifyService enterpriseVerifyService;

    @Autowired
    private SessionFilter sessionFilter;

    /**
     * 根据条件获取规模信息改变数据
     * @param map
     * @return
     */
    @RequestMapping(value="/getChange",method = RequestMethod.GET)
    public ObjectRestResponse<EnterpriseScaleChange> changeScale(@RequestParam Map<String, String> map) {
        try{
            List<EnterpriseScaleChange> enterpriseScaleChanges = enterpriseScaleService.getScaleChange(map);
            return new ObjectRestResponse<EnterpriseScaleChange>().rel(true).data(enterpriseScaleChanges);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseScaleChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     * 新增企业规模信息
     * @param enterpriseScaleStr
     * @param reason
     * @return
     */
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseScale> addScale(@RequestParam(value = "enterpriseScale") String enterpriseScaleStr, @RequestParam(value = "reason") String reason) {
        try{
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseScale enterpriseScale = JSONUtils.fromJson(enterpriseScaleStr,EnterpriseScale.class);
            enterpriseScaleService.addEnterpriseScale(enterpriseScale,sysUser.getUserId(),reason);
            return new ObjectRestResponse<EnterpriseScale>().rel(true);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseScale>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     * 修改企业规模信息
     * @param enterpriseScaleStr
     * @param reason
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseScale> changeScale(@RequestParam(value = "enterpriseScale") String enterpriseScaleStr, @RequestParam(value = "reason") String reason) {
        try{
            SysUser sysUser = sessionFilter.getJWTUser(request);
            EnterpriseScale enterpriseScale = JSONUtils.fromJson(enterpriseScaleStr,EnterpriseScale.class);
            enterpriseScaleService.modifyEnterpriseScale(enterpriseScale,sysUser.getUserId(),reason);
            return new ObjectRestResponse<EnterpriseScale>().rel(true);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseScale>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }
    /**
     * 根据changeId获取企业规模修改数据表
     * @param id
     * @return
     */
    @RequestMapping(value="/getChange/{id}",method = RequestMethod.GET)
    public ObjectRestResponse<EnterpriseScaleChange> getChange(@PathVariable Long id) {
        try{
            Map<String ,Object> result = new HashMap();
            List<Map<String ,Object>>  compareList=null;
            EnterpriseScaleChange enterpriseScaleChange = enterpriseScaleService.getChangeById(id);
            //判断是新增还是修改操作
            if(enterpriseScaleChange.getScaleId()!=null){//修改
                EnterpriseScale enterpriseScale = enterpriseScaleService.findById(enterpriseScaleChange.getScaleId());
                compareList = CompareObjectUtil.compareTwoClass(enterpriseScale,enterpriseScaleChange);
            }
            result.put("formData",enterpriseScaleChange);
            result.put("compareData",compareList);
            return new ObjectRestResponse<EnterpriseScaleChange>().rel(true).data(result);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseScaleChange>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     * 企业规模审批
     * @param changeId
     * @param auditType
     * @param verifyConclusion
     * @param verifyId
     * @return
     */
    @RequestMapping(value="/verify",method = RequestMethod.POST)
    public ObjectRestResponse<EnterpriseVerify> auditEnterpriseScale(@RequestParam("changeId") String changeId,
                                                                           @RequestParam("auditType") String auditType,
                                                                           @RequestParam("verifyConclusion") String verifyConclusion,
                                                                           @RequestParam("verifyId") String verifyId) {
        try{
            EnterpriseScale enterpriseScale = new EnterpriseScale();
            EnterpriseScaleChange enterpriseScaleChange = enterpriseScaleService.getChangeById(Long.valueOf(changeId));
            if(enterpriseScaleChange.getScaleId()!=null){
                enterpriseScale = enterpriseScaleService.findById(enterpriseScaleChange.getScaleId());
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
            enterpriseScaleService.verifyEnterpriseScale(enterpriseScale,enterpriseScaleChange,enterpriseVerify);
            return new ObjectRestResponse<EnterpriseVerify>().rel(true);
        }catch (Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<EnterpriseVerify>().rel(false);
            resp.setStatus(500);
            resp.setMessage("error");
            return resp;
        }
    }

    /**
     * 企业规模信息审核查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/verify/query" ,method = RequestMethod.GET)
    public GridDataModel getCertificateChangePage(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = enterpriseScaleService.getScaleChangePageCount(params);
        List<Map<String,Object>> list = enterpriseScaleService.getScaleChangePage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }
    
    @RequestMapping(value = "/getEnterpriseScale/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EnterpriseScale> getEnterpriseScale(@PathVariable("id") Long id) {
		Map<String,Object> map=new HashMap<>();
		map.put("enterpriseId", id);
		EnterpriseScale es=new EnterpriseScale();
		List<EnterpriseScale> list=enterpriseScaleService.findEntitysByCondition(map);
		if(list.size()>0){
			es=list.get(0);
		}
		return new ObjectRestResponse<EnterpriseScale>().rel(true).data(es);
	}

}
