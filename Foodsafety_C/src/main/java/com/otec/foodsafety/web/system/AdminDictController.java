package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysDict;
import com.otec.foodsafety.service.system.SysDictService;
import com.otec.foodsafety.web.VueBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典管理
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 */
@RestController
@RequestMapping("api/admin/dict")
public class AdminDictController extends VueBaseController<SysDictService, SysDict, Long> {
	Logger log = LoggerFactory.getLogger(AdminDictController.class);
	
	
	@RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<SysDict> remove(@PathVariable("ids") String ids) {
		try {			
			String[] aDeleteId = ids.split(",");
			for(String id:aDeleteId){
				biz.removeById(Long.valueOf(id));				
			}
			return new ObjectRestResponse<SysDict>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysDict>()
					.rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

}
