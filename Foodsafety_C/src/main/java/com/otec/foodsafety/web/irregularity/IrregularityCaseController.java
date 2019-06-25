package com.otec.foodsafety.web.irregularity;

import com.otec.foodsafety.entity.irregularity.IrregularityCase;
import com.otec.foodsafety.entity.irregularity.IrregularityCaseClose;
import com.otec.foodsafety.entity.irregularity.IrregularityCaseExt;
import com.otec.foodsafety.entity.irregularity.IrregularityPunish;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.irregularity.IrregularityCaseService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 违规立案
 *
 * @author Administrator
 *
 */

@RestController
@RequestMapping("api/admin/irregularity")
public class IrregularityCaseController extends VueBaseController<IrregularityCaseService, IrregularityCase, Long> {

    @Autowired
    private SessionFilter sessionFilter;
    @Autowired
    SysAreaService sysAreaService;

    @Autowired
    IrregularityCaseService irregularityCaseService;

      /**
     * 添加立案
     */
    @RequestMapping(value = "/saveIrregularity", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<IrregularityCase> addIrregularity(@RequestBody IrregularityCase irregularityCase  ) {
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            irregularityCase.setUserId(sysUser.getUserId());
            irregularityCase.setCaseStatus("1");
            if(irregularityCase.getIrregularityId()>0)
                irregularityCaseService.update(irregularityCase);
            else
                irregularityCaseService.add(irregularityCase);
            return new ObjectRestResponse<IrregularityCase>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<IrregularityCase>().rel(false);
            resp.setStatus(500);
            if(irregularityCase.getIrregularityId()>0)
                resp.setMessage("更新立案失败");
            else
                resp.setMessage("创建立案失败");
            return resp;
        }
    }

    /**
     * 保存案件惩罚信息
     * @param punish
     * @return
     */
    @RequestMapping(value = "/saveCasePunish", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<IrregularityCase> saveCasePunish(@RequestBody IrregularityPunish punish) {
        try {
            irregularityCaseService.saveCasePunish(punish);
            return new ObjectRestResponse<IrregularityCase>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<IrregularityCase>().rel(false);
            resp.setStatus(500);
            if(punish.getPunishId()>0)
                resp.setMessage("更新案件惩罚信息失败");
            else
                resp.setMessage("创建案件惩罚信息失败");
            return resp;
        }

    }

    /**
     * 保存结案信息
     * @param caseClose
     * @return
     */
    @RequestMapping(value = "/saveCaseClose", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<IrregularityCase> saveIrregularityClose(@RequestBody IrregularityCaseClose  caseClose) {
        try {
            irregularityCaseService.saveCaseClose(caseClose);
            return new ObjectRestResponse<IrregularityCase>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<IrregularityCase>().rel(false);
            resp.setStatus(500);
            if(caseClose.getClosecaseId()>0)
                resp.setMessage("更新结案信息失败");
            else
                resp.setMessage("创建结案信息失败");
            return resp;
        }

    }


    @RequestMapping(value = "/getIrregularity/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<IrregularityCaseExt> findByIrregularityId(@PathVariable Long id) {
        IrregularityCaseExt  ext=irregularityCaseService.findByIrregularityId(id);
        return new ObjectRestResponse<IrregularityCaseExt>().rel(true).data(ext);
    }





}
