package com.otec.foodsafety.web.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.equipment.EquipmentModel;
import com.otec.foodsafety.entity.equipment.Vender;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.equipment.EquipmentModelService;
import com.otec.foodsafety.service.equipment.EquipmentService;
import com.otec.foodsafety.service.equipment.VenderService;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 设备型号controller
 * 
 * @author minwei
 * @description
 * @date 2018年10月09日
 */
@RestController
@RequestMapping("api/admin/equipmentModel")
public class AdminEquModelController extends
		VueBaseController<EquipmentModelService, EquipmentModel, Long> {

	@Autowired
	private EquipmentModelService equipmentModelService;
	
	@Autowired
	private VenderService venderService;
	
	@Autowired
	private  EquipmentService equipmentService;
	
	@Autowired
	private AuthService authService;
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EquipmentModel> get(@PathVariable("id") Long id) {
		EquipmentModel equipmentModel = equipmentModelService.findById(id);
		return new ObjectRestResponse<EquipmentModel>().rel(true).data(equipmentModel);
	}
	

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params ) {
		// 查询列表数据

		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = equipmentModelService.getGridDataModelByCondition(po);
		return model;
	}
	
	@RequestMapping(value = "/getVenderMap", method = RequestMethod.GET)
	public Hashtable<Object, List<ComboData>> getVenderMap(){
		Map<String, Object> condititon = new HashMap<String, Object>();
		List<Vender> venderList= venderService.findEntitysByCondition(condititon);

		Hashtable<Object, List<ComboData>> verderDict =new Hashtable<Object, List<ComboData>>();
		List<ComboData> lst = new ArrayList<ComboData>();
		for(Vender v:venderList){
			ComboData data=new ComboData(v.getVenderId().toString(), v.getVenderName());
			lst.add(data);
		}
		verderDict.put("厂商", lst);
		return verderDict;
	}

	//禁用
	@RequestMapping(value = "all/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<EquipmentModel> removeByIds(@PathVariable String ids) {
		try {
			String status="0";
			if(StringUtils.isNotBlank(ids)){
				Map<String, Object> condititon = new HashMap<String, Object>();
//				condititon.put("equModelIds", ids);
				condititon.put("extendSql"," and EQU_MODEL_ID in ("+ids+")");
				condititon.put("status", "1");
				int flag=equipmentService.countByCondition(condititon);
				if(flag>0){
					ObjectRestResponse resp = new ObjectRestResponse<EquipmentModel>()
							.rel(false);
					resp.setStatus(500);
					resp.setMessage("存在设备使用该型号，无法删除");
					return resp;
				}else{
					String[] equModelId = ids.split(",");
					for(String str:equModelId){
						EquipmentModel equipmentModel=	equipmentModelService.findById(Long.valueOf(str));
						equipmentModel.setStatus(status);
						equipmentModelService.updateById(equipmentModel);
					}	
				}
			}
			return new ObjectRestResponse<EquipmentModel>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentModel>()
					.rel(false);

			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}
	
	
	//恢复接口
	@RequestMapping(value = "revocer/{ids}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<EquipmentModel> recover(@PathVariable String  ids) {
		try {
			String status="1";
			if(StringUtils.isNotBlank(ids)){
				String[] equModelId = ids.split(",");
				for(String str:equModelId){
					EquipmentModel equipmentModel=	equipmentModelService.findById(Long.valueOf(str));
					equipmentModel.setStatus(status);
					equipmentModelService.updateById(equipmentModel);
				}	
			}
			return new ObjectRestResponse<EquipmentModel>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentModel>()
					.rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<EquipmentModel> add(@RequestBody EquipmentModel equipmentModel) {
		String token=request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);	
		equipmentModel.setCreateOpId(Long.valueOf(userInfo.userId.toString()));
		equipmentModel.setCreateTime(new Date());
		equipmentModelService.persist(equipmentModel);
		return new ObjectRestResponse<EquipmentModel>().rel(true);
	}

	@RequestMapping(value = "isExistEquModel/{no}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<EquipmentModel> checkEquModel(@PathVariable String no) {
		try {
			String newEquModel= no.split(",")[0];
			String noEquModelId= no.split(",")[1].equals("anObject")?"":no.split(",")[1];
			Map<String, Object> condititon = new HashMap<String, Object>();
			condititon.put("newEquModel", newEquModel);
			condititon.put("noEquModelId",noEquModelId);
			int flag = equipmentModelService.countByCondition(condititon);
			if(flag>0){
				return new ObjectRestResponse<EquipmentModel>().rel(true).data(true);
			}else{
				return new ObjectRestResponse<EquipmentModel>().rel(true).data(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentModel>()
					.rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;
		}
	
	}

	@RequestMapping(value = "/getModelMap", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String, Object>> getModelMap() {

		Map<String, Object> map2 = new HashMap<String, Object>();
		String extendSql = "and user_status = 1 ";
		map2.put("status", "1");
		List<EquipmentModel> datas = equipmentModelService.findEntitysByCondition(map2);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(datas.size());
		for (EquipmentModel item : datas) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", String.valueOf(item.getEquModelId()));
			map.put("text", item.getEquModel());
			list.add(map);
		}
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(list);
	}

}
