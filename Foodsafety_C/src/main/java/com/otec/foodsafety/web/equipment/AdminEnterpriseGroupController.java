package com.otec.foodsafety.web.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.util.vo.EquGroupTree;
import com.otec.foodsafety.entity.equipment.EnterpriseGroup;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.equipment.EnterpriseGroupService;
import com.otec.foodsafety.service.equipment.EquipmentService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备分组controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("api/admin/enterpriseGroup")
public class AdminEnterpriseGroupController extends VueBaseController<EnterpriseGroupService, EnterpriseGroup, Long> {
	@Autowired
	private EnterpriseGroupService enterpriseGroupService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private AuthService authService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);
		List<EnterpriseGroup> list = (List<EnterpriseGroup> )model.getRows();

		return model;

	}

	/**
	 * 公共方法，添加实体
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<EnterpriseGroup> add(@RequestBody EnterpriseGroup entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);

		biz.persist(preHandler(entity));
		return new ObjectRestResponse<EnterpriseGroup>().rel(true).data(entity);
	}

	/**
	 * 公共方法，修改实体
	 *
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<EnterpriseGroup> update(@RequestBody EnterpriseGroup entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);

		biz.updateById(preHandler(entity));
		return new ObjectRestResponse<EnterpriseGroup>().rel(true);
	}

	/**
	 * 设备编码不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkEnterpriseGroupNo", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existEnterpriseGroupNo(@RequestParam Map<String, String> params) {
		String audioNO = params.get("audioNO");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("audioNo", audioNO);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	/**
	 * 设备名称不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkEnterpriseGroupName", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existEnterpriseGroupName(@RequestParam Map<String, String> params) {
		String name = params.get("enterpriseGroupName");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("enterpriseId", params.get("compId"));
		cond.put("enterpriseGroupName", name);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

    @RequestMapping(value = "checkGroupVideo", method = RequestMethod.GET)
    public ObjectRestResponse<Boolean> checkGroupVideo(@RequestParam Map<String, String> params) {
        String groupId = params.get("groupId");
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("enterpriseGroupId", groupId);
        cond.put("status", "1");
        Integer num = equipmentService.countByCondition(cond);
        return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
    }

	@RequestMapping(value = "/groupTree", method = RequestMethod.GET)
	@ResponseBody
	public List<EquGroupTree> groupTree(@RequestParam Map<String, String> params) {

		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		String compId = null;
		if(StringUtils.equalsIgnoreCase("4", userInfo.getUserType())){
			compId = userInfo.getEnterpriseId().toString();
		}
		return equipmentService.groupTree(compId);

	}

	/**
	 * 实体预处理
	 * 
	 * @param entity
	 * @return
	 */
	public EnterpriseGroup preHandler(EnterpriseGroup entity) {

		if (entity.getStatus() == null) {
			entity.setStatus("1");
		}
		return entity;
	}
}
