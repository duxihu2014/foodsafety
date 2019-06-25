package com.otec.foodsafety.service.equipment;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.EquipmentAudio;

/**
 * 音频与摄像头关系 类
 * 
 * @author Administrator
 *
 */
@RemoteService
public interface EquipmentAudioService extends BaseService<EquipmentAudio, Long> {
	/**
	 * 统计条件查询音频下已绑或未绑的摄像头
	 * 
	 * @param po
	 * @return
	 */
	GridDataModel getBindCameraByCondition(PageObject po);

	/**
	 * 统计条件查询音频下已绑或未绑的摄像头
	 * 
	 * @param po
	 * @return
	 */
	GridDataModel getNoBindCameraByCondition(PageObject po);

}
