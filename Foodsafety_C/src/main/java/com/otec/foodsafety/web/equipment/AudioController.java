package com.otec.foodsafety.web.equipment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.equipment.Audio;
import com.otec.foodsafety.entity.equipment.EquipmentModel;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.enterprise.EnterpriseService;
import com.otec.foodsafety.service.equipment.AudioService;
import com.otec.foodsafety.service.equipment.EquipmentAudioService;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;

/**
 * 音频controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("api/admin/audio")
public class AudioController extends VueBaseController<AudioService, Audio, Long> {
	@Autowired
	EquipmentAudioService equAudioService;
	@Autowired
	private AuthService authService;
	@Autowired
	private EnterpriseService enterpriseService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);
		List<Audio> list = (List<Audio>) model.getRows();

		for (Audio item : list) {
			Integer num = countCameraByAudioId(item.getAudioId().toString());
			item.setCameraFlag(num > 0 ? true : false);
		}
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
	public ObjectRestResponse<Audio> add(@RequestBody Audio entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		entity.setCreateOpId(userInfo.getUserId());

		biz.persist(preHandler(entity));
		return new ObjectRestResponse<Audio>().rel(true).data(entity);
	}

	/**
	 * 公共方法，修改实体
	 *
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<Audio> update(@RequestBody Audio entity) {
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		entity.setModifyOpId(userInfo.getUserId());

		biz.updateById(preHandler(entity));
		return new ObjectRestResponse<Audio>().rel(true);
	}

	/**
	 * 逻辑删除单条记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<Audio> remove(@PathVariable Long id) {
		try {
			Integer num = countCameraByAudioId(id + "");
			if (num > 0) {
				ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);
				resp.setStatus(500);
				resp.setMessage("所选音频下面存在摄像头信息,无法删除");
				return resp;
			} else {
				String token = request.getHeader("access-token");
				FrontUser userInfo = authService.getUserInfo(token);

				Audio entity = biz.findById(id);
				entity.setStatus("0");// 删除标识
				entity.setModifyOpId(userInfo.getUserId());
				biz.updateById(preHandler(entity));
			}
			return new ObjectRestResponse<Audio>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
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
	public ObjectRestResponse<Audio> remove(@PathVariable("ids") String ids) {
		try {
			/*
			 * 删除逻辑，音频下有摄像头时不允许删除
			 */
			Integer num = countCameraByAudioId(ids);
			if (num > 0) {
				ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);
				resp.setStatus(500);
				resp.setMessage("所选音频下面存在摄像头信息,无法删除");
				return resp;
			} else {
				String token = request.getHeader("access-token");
				FrontUser userInfo = authService.getUserInfo(token);

				String[] aDeleteId = ids.split(",");
				for (String id : aDeleteId) {
					Audio entity = biz.findById(Long.valueOf(id));
					entity.setStatus("0");
					entity.setModifyOpId(userInfo.getUserId());
					biz.updateById(preHandler(entity));
				}
			}

			return new ObjectRestResponse<Audio>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Audio>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	/**
	 * 音频编码不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkAudioNo", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existAudioNo(@RequestParam Map<String, String> params) {
		String audioNO = params.get("audioNO");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("audioNo", audioNO);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	/**
	 * 音频名称不能重复
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkAudioName", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existAudioName(@RequestParam Map<String, String> params) {
		String audioName = params.get("audioName");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("audioName", audioName);
		cond.put("status", "1");
		Integer num = biz.countByCondition(cond);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	/**
	 * 音频下是否有摄像头
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "checkExistCamera", method = RequestMethod.GET)
	public ObjectRestResponse<Boolean> existCamera(@RequestParam Map<String, String> params) {
		String audioId = params.get("audioId");
		Integer num = countCameraByAudioId(audioId);
		return new ObjectRestResponse<Boolean>().rel(true).data(num > 0 ? true : false);
	}

	/**
	 * 统计音频下有多少有效的摄像头
	 * 
	 * @param audioId
	 * @return
	 */
	private Integer countCameraByAudioId(String audioId) {
		Map<String, Object> cond = new HashMap<String, Object>();
//		cond.put("audioId", audioId);
		cond.put("extendSql", "AUDIO_ID in (" + audioId + ")");
		cond.put("status", "1");
		Integer num = equAudioService.countByCondition(cond);
		return num;
	}

	/**
	 * 实体预处理
	 * 
	 * @param entity
	 * @return
	 */
	public Audio preHandler(Audio entity) {

		if (entity.getStatus() == null) {
			entity.setStatus("1");
		}
		if (entity.getCreateTime() == null) {
			entity.setCreateTime(new Date());
		} else {
			if (entity.getModifyTime() == null) {
				entity.setModifyTime(new Date());
			}
		}

		return entity;
	}

	/**
	 * 得到所有企业信息 map
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAllEnterpriseMap", method = RequestMethod.GET)
	public ObjectRestResponse<List<ComboData>> getEnterpriseModeMap() {

		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("enterpriseStatus", "3");// 企业状态为3的数据
		List<EnterpriseBase> datalist = enterpriseService.findEntitysByCondition(map2);

		List<ComboData> params = new ArrayList<ComboData>();
		for (EnterpriseBase item : datalist) {
			ComboData cData = new ComboData(item.getEnterpriseId()+"", item.getEnterpriseName());
			params.add(cData);
		}	
		
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(params);
	}

	@RequestMapping(value = "/getAudioMap", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String, Object>> getAudioMap(@RequestParam Map<String, String> params) {

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("enterpriseId", params.get("compId"));// 企业状态为3的数据
		List<Audio> datalist = biz.findEntitysByCondition(map2);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(datalist.size());
		for (Audio item : datalist) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", String.valueOf(item.getAudioId()));
			map.put("text", item.getAudioName());
			list.add(map);
		}
		return new ObjectRestResponse<List<Map<String, Object>>>().rel(true).data(list);
	}
}
