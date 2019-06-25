package com.otec.foodsafety.web.equipment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.otec.foodsafety.entity.equipment.EquipmentModel;
import com.otec.foodsafety.entity.equipment.Vender;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.equipment.EquipmentModelService;
import com.otec.foodsafety.service.equipment.VenderService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;


/**
 * 厂商controller
 * 
 * @author minwei
 * @description
 * @date 2018年10月09日
 */
@RestController
@RequestMapping("api/admin/vender")
public class AdminVenderController extends
		VueBaseController<VenderService, Vender, Long> {

	@Autowired
	private VenderService venderService;
		
	@Autowired
	private EquipmentModelService equipmentModelService;
	
	@Autowired
	private AuthService authService;
	
	private static final Logger log = LoggerFactory
			.getLogger(AdminVenderController.class);

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<Vender> get(@PathVariable("id") Long id) {
		Vender vender = venderService.findById(id);
		return new ObjectRestResponse<Vender>().rel(true).data(vender);

	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<Vender> add(@RequestBody Vender vender) {
		String token=request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);	
		vender.setOpId(Integer.valueOf(userInfo.userId.toString()));
		vender.setCreateTime(new Date());
		venderService.persist(vender);
		return new ObjectRestResponse<Vender>().rel(true);
	}
	
	@RequestMapping(value = "isExistVenderNo/{no}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<Vender> checkVenderNo(@PathVariable String no) {
		try {
			String newVenderNo= no.split(",")[0];
			String noVenderId= no.split(",")[1].equals("anObject")?"":no.split(",")[1];
			Map<String, Object> condititon = new HashMap<String, Object>();
			condititon.put("venderNo", newVenderNo);
			condititon.put("noVenderId",noVenderId);
			int flag = venderService.countByCondition(condititon);
			if(flag>0){
				return new ObjectRestResponse<Vender>().rel(true).data(true);
			}else{
				return new ObjectRestResponse<Vender>().rel(true).data(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Vender>()
					.rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;
		}
	
	}

	
	@RequestMapping(value = "all/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<Vender> removeByIds(@PathVariable String ids) {
		try {
			String status="0";
			if(StringUtils.isNotBlank(ids)){
				Map<String, Object> condititon = new HashMap<String, Object>();
				condititon.put("venderIds", ids);
				condititon.put("status", "1");
				int flag =equipmentModelService.countByCondition(condititon);
				if(flag>0){
					ObjectRestResponse resp = new ObjectRestResponse<Vender>()
							.rel(false);
					resp.setStatus(500);
					resp.setMessage("存在设备型号使用该厂商，无法删除");
					return resp;
				}else{
					String[] equModelId = ids.split(",");
					for(String str:equModelId){
						Vender vender=	venderService.findById(Long.valueOf(str));
						vender.setStatus(status);
						venderService.updateById(vender);
					}	
				}
			}
			return new ObjectRestResponse<EquipmentModel>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Vender>()
					.rel(false);

			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}
	
	@RequestMapping(value = "revocer/{ids}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<Vender> recover(@PathVariable String  ids) {
		try {
			String status="1";
			if(StringUtils.isNotBlank(ids)){
				String[] venderId = ids.split(",");
				for(String str:venderId){
					Vender vender=	venderService.findById(Long.valueOf(str));
					vender.setStatus(status);
					venderService.updateById(vender);
				}	
			}
			return new ObjectRestResponse<Vender>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Vender>()
					.rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}
}
