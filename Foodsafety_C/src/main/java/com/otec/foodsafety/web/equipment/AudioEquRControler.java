package com.otec.foodsafety.web.equipment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.equipment.Audio;
import com.otec.foodsafety.entity.equipment.EquipmentAudio;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.service.equipment.AudioService;
import com.otec.foodsafety.service.equipment.EquipmentAudioService;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;

/**
 * 音频设备关系controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("api/admin/audioequR")
public class AudioEquRControler extends VueBaseController<EquipmentAudioService, EquipmentAudio, Long> {
	@Autowired
	private AuthService authService;

	/**
	 * 选择已绑摄像头信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/bindCameraData", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel bindCameraData(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getBindCameraByCondition(po);
		return model;
	}

	/**
	 * 选择未绑摄像头信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/noBindCameraData", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel noBindCameraData(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getNoBindCameraByCondition(po);
		return model;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<EquipmentAudio> remove(@PathVariable Long id) {
		try {

			String token = request.getHeader("access-token");
			FrontUser userInfo = authService.getUserInfo(token);

			EquipmentAudio entity = biz.findById(id);
			entity.setStatus("0");// 删除标识
			biz.updateById(preHandler(entity));

			return new ObjectRestResponse<EquipmentAudio>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	/**
	 * 绑定摄像头
	 * 
	 * @param autoId
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "bindCamera/{autoId}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EquipmentAudio> bindCamera(@PathVariable Long autoId,
			@RequestParam Map<String, String> params) {
		try {
			String ids = params.get("ids");
			String[] cameraIds = ids.split(",");
			for (String id : cameraIds) {
				Map<String, Object> cond = new HashMap<String, Object>();
//				cond.put("audioId", autoId);
				cond.put("equipmentId", id);
				cond.put("status", "1");
				List<EquipmentAudio> exitsData = biz.findEntitysByCondition(cond);
				/*
				 * 如果数据已存在，则先删除后增加
				 */
				if (exitsData != null && exitsData.size() > 0) {
					for (EquipmentAudio item : exitsData) {
						item.setStatus("0");
						biz.updateById(preHandler(item));
					}
				}
				EquipmentAudio item = new EquipmentAudio();
				item.setAudioId(autoId);
				item.setEquipmentId(Long.valueOf(id));
				item.setStatus("1");
				biz.persist(item);

			}
			return new ObjectRestResponse<EquipmentAudio>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentAudio>().rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;
		}
	}

	/**
	 * 绑定音频
	 * 
	 * @param autoId
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "bindAudio/{equmentId}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<EquipmentAudio> bindAudio(@PathVariable Long equmentId,
			@RequestParam Map<String, String> params) {
		try {
			String ids = params.get("ids");
			String[] cameraIds = ids.split(",");
			for (String id : cameraIds) {
				EquipmentAudio item = new EquipmentAudio();
				item.setAudioId(Long.valueOf(id));
				item.setEquipmentId(equmentId);
				item.setStatus("1");
				biz.persist(item);
			}
			return new ObjectRestResponse<EquipmentAudio>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<EquipmentAudio>().rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;
		}
	}

	/**
	 * 逻辑删除多条记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<EquipmentAudio> remove(@PathVariable("ids") String ids) {
		try {

			String[] aDeleteId = ids.split(",");
			for (String id : aDeleteId) {
				EquipmentAudio entity = biz.findById(Long.valueOf(id));
				entity.setStatus("0");
				biz.updateById(preHandler(entity));
			}

			return new ObjectRestResponse<EquipmentAudio>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	public Audio preHandler(Audio entity) {

		if (entity.getStatus() == null) {
			entity.setStatus("1");
		}

		return entity;
	}
}
