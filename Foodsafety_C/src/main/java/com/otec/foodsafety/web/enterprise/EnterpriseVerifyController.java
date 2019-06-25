package com.otec.foodsafety.web.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.service.enterprise.EnterpriseVerifyService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业信息审核
 * @Author: weichang
 * @Date: 2018/10/16 0016 下午 16:09
 */
@RestController
@RequestMapping("api/enterpriseVerify")
public class EnterpriseVerifyController extends VueBaseController<EnterpriseVerifyService, EnterpriseVerify, Long> {
    @Autowired
    private EnterpriseVerifyService enterpriseVerifyService;

    /**
     * 根据区域id和数据类型获取审批数据
     * @param params
     * @return
     */
    @RequestMapping(value="/getPage")
    public GridDataModel getEnterpriseVerifyPage(@RequestParam Map<String, String> params) {
        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = enterpriseVerifyService.getEnterpriseVerifyCount(params);
        List<EnterpriseVerify> enterpriseVerifyList = enterpriseVerifyService.getEnterpriseVerify(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(enterpriseVerifyList,total);
        return model;
    }
}
