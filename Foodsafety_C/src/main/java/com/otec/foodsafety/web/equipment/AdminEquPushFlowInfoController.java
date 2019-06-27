package com.otec.foodsafety.web.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.equipment.EquipmentPushflowInfo;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.equipment.EquipmentPushflowInfoService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 推流设备信息
 * 
 * @author duxihu
 */
@RestController
@RequestMapping("api/admin/equipmentPushFlowInfo")
public class AdminEquPushFlowInfoController extends
		VueBaseController<EquipmentPushflowInfoService, EquipmentPushflowInfo, Long> {

	@Autowired
	private EquipmentPushflowInfoService equipmentPushflowInfoService;
	
	@Autowired
	private AuthService authService;
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EquipmentPushflowInfo> get(@PathVariable("id") Long id) {
		EquipmentPushflowInfo equipmentPushflowInfo = equipmentPushflowInfoService.findById(id);
		return new ObjectRestResponse<EquipmentPushflowInfo>().rel(true).data(equipmentPushflowInfo);
	}
	

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params ) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = equipmentPushflowInfoService.getGridDataModelByCondition(po);
		return model;
	}
	


	//禁用
	@RequestMapping(value = "all/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<EquipmentPushflowInfo> removeByIds(@PathVariable String ids) {
		try {
			Integer status=0;

			String[] equModelId = ids.split(",");
			for(String str:equModelId){
				EquipmentPushflowInfo equipmentPushflowInfo=	equipmentPushflowInfoService.findById(Long.valueOf(str));
				equipmentPushflowInfo.setStatus(status);
				equipmentPushflowInfoService.updateById(equipmentPushflowInfo);
			}
			return new ObjectRestResponse<EquipmentPushflowInfo>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentPushflowInfo>()
					.rel(false);

			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}
	
	
	//恢复接口
	@RequestMapping(value = "revocer/{ids}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<EquipmentPushflowInfo> recover(@PathVariable String  ids) {
		try {
			Integer status=1;
			if(StringUtils.isNotBlank(ids)){
				String[] equModelId = ids.split(",");
				for(String str:equModelId){
					EquipmentPushflowInfo equipmentPushflowInfo=	equipmentPushflowInfoService.findById(Long.valueOf(str));
					equipmentPushflowInfo.setStatus(status);
					equipmentPushflowInfoService.updateById(equipmentPushflowInfo);
				}	
			}
			return new ObjectRestResponse<EquipmentPushflowInfo>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentPushflowInfo>()
					.rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}

	//新增
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<EquipmentPushflowInfo> add(@RequestBody EquipmentPushflowInfo equipmentPushflowInfo) {
		String token=request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);	
		//equipmentPushflowInfo.setCreateOpId(Long.valueOf(userInfo.userId.toString()));
		equipmentPushflowInfo.setCreateTime(new Date());
		equipmentPushflowInfoService.persist(equipmentPushflowInfo);
		return new ObjectRestResponse<EquipmentPushflowInfo>().rel(true);
	}

}
