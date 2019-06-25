package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysDetail;
import com.otec.foodsafety.service.system.SysDetailService;
import com.otec.foodsafety.web.VueBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典详情
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 *
 */
@RestController
@RequestMapping("api/admin/dictDetail")
public class AdminDetailController extends VueBaseController<SysDetailService, SysDetail, Long> {
	Logger log = LoggerFactory.getLogger(AdminDetailController.class);

	@RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<SysDetail> remove(@PathVariable("ids") String ids) {
		try {
			String[] aDeleteId = ids.split(",");
			for (String id : aDeleteId) {
				biz.removeById(Long.valueOf(id));
			}
			return new ObjectRestResponse<SysDetail>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysDetail>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}
}
