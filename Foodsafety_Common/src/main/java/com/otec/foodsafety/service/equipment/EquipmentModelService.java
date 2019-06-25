package com.otec.foodsafety.service.equipment;

import java.util.List;
import java.util.Map;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.EquipmentModel;
import com.otec.foodsafety.util.vo.ComboData;

@RemoteService
public interface EquipmentModelService extends BaseService<EquipmentModel,Long>{

	/**
	 * 得到设备型号combo数据
	 * @return
	 */
	public Map<String, String> findEquModelCombo();
	public List<ComboData> findEuqModel();
}
