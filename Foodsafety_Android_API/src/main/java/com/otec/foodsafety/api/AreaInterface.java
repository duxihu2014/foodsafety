package com.otec.foodsafety.api;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.AreaTree;
import com.otec.foodsafety.entity.system.SysArea;
import com.otec.foodsafety.service.system.SysAreaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 区域controller
 * 
 * @author yy.w
 * 
 */
@RestController
@RequestMapping("api/area")
public class AreaInterface {

	@Autowired
	private SysAreaService areaService;

	private static final Logger log = LoggerFactory.getLogger(AreaInterface.class);

	/**
	 * 得到区域的树型结果数据
	 *
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<AreaTree> getTree() {
		return areaService.listTree(true, false);
	}


}
