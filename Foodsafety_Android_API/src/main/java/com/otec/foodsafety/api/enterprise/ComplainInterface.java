package com.otec.foodsafety.api.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.alarm.AlarmExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.evaluate.ComplainEvaluate;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseBaseService;
import com.otec.foodsafety.service.evaluate.ComplainEvaluateService;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 投诉
 */
@RequestMapping("/api/complain")
@RestController
public class ComplainInterface extends BaseInterface {


    @Autowired
    ComplainEvaluateService complainEvaluateService;
    @Autowired
    SessionFilter sessionFilter;
    @Autowired
    EnterpriseBaseService enterpriseBaseService;
    @Autowired
    SysResourceService sysResourceService;
    /**
     *  获取企业评论或投诉
     * @param params
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public GridDataModel getComplains(@RequestParam Map<String, String> params) {
        SysUser sysUser = sessionFilter.getJWTUser(request);
        params.put("areaIds",sysUser.getAreaId().toString());
        if(params.get("typeId").equals("1")){
            params.put("someStatus","2,3");
        }
        params.put("extendSql", " and MIR.STATUS !=0");

        PageObject po = getPageObject(params);
        po.getCondition().putAll(params);
        GridDataModel model= complainEvaluateService.getGridDataModelByCondition(po);
        List<ComplainEvaluate> results=(List<ComplainEvaluate>)model.getRows();

        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        for (ComplainEvaluate res:results){
           String[] ids= res.getImgPath().split(",");
           List<String>  paths=new ArrayList<String>();
           for (String id:ids){
               SysResource resource=sysResourceService.findById(Long.parseLong(id));
               if(resource!=null){
                   paths.add(imageServerUrl+"/"+resource.getResourcePath());
               }
           }
            res.setImgPaths(paths);
        }
        return model;
    }


    /**
     *  食客点评回复
     * @param
     * @return
     */
    @RequestMapping(value = "/remark/reply/{complainEvaluateId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<ComplainEvaluate> remark(@PathVariable Long complainEvaluateId,
                                                       @RequestParam Map<String, String> params) {
        try {
            String answer = params.get("answer");
            SysUser sysUser = sessionFilter.getJWTUser(request);
            ComplainEvaluate entity = complainEvaluateService.findById(complainEvaluateId);

            entity.setStatus("2");// 受理完成
            entity.setWriting1(answer);
            entity.setCreateOp1(Integer.valueOf(sysUser.getUserId() + ""));
            entity.setCreateTime1(new Date());

            complainEvaluateService.updateById(entity);
            return new ObjectRestResponse<ComplainEvaluate>().rel(true);

        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
            resp.setStatus(500);
            resp.setMessage("回复保存出错，联系管理员!");
            return resp;
        }

    }

    /**
     * 	投诉处理
     * @param id
     * @param params
     * @return
     */
    @RequestMapping(value = "/complaint/handling/{complainEvaluateId}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<ComplainEvaluate> handleEdit(@PathVariable Long id,
                                                           @RequestParam Map<String, String> params) {

        try {
            String handleResult = params.get("handleResult");//处理结果
            String personLiable = params.get("personLiable"); //责任人
            String personLiableTel = params.get("personLiableTel");//责任人电话


            SysUser sysUser = sessionFilter.getJWTUser(request);
            ComplainEvaluate entity = complainEvaluateService.findById(id);

            entity.setStatus("3");// 已处理
            entity.setHandleResult(handleResult);
            entity.setPersonLiable(personLiable);
            entity.setPersonLiableTel(personLiableTel);
            entity.setHandleManId(Integer.valueOf(sysUser.getUserId() + ""));
            entity.setHandleTime(new Date());

            complainEvaluateService.updateById(entity);

            return new ObjectRestResponse<ComplainEvaluate>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
            resp.setStatus(500);
            resp.setMessage("投诉处理保存出错，联系管理员");
            return resp;

        }
    }



}
