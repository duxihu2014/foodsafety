package com.otec.foodsafety.web.system;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.system.SysLog;
import com.otec.foodsafety.service.system.SysLogService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 日志管理controller
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 */
@RequestMapping("/api/admin/gateLog")
@RestController
public class AdminLogController extends
        VueBaseController<SysLogService, SysLog, Long> {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.findSysLog(po);		
		return model;

	}

}
