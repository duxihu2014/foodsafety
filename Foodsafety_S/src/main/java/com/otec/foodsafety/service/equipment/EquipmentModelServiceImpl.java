package com.otec.foodsafety.service.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.equipment.EquipmentModel;
import com.otec.foodsafety.mapper.equipment.EquipmentModelMapper;
import com.otec.foodsafety.util.vo.ComboData;

@Service
@Transactional
public class EquipmentModelServiceImpl  extends BaseServiceImpl<EquipmentModel,Long> implements EquipmentModelService{

	@Autowired
	private EquipmentModelMapper mapper;

	@Autowired
	public void setMapper(EquipmentModelMapper mapper) {
		setGenericMapper(mapper);
	}
	
	
	@Override
	public Map<String, String> findEquModelCombo() {
		List<EquipmentModel> equModList = mapper.findEntitysByCondition(null);
		if (equModList == null || equModList.size() == 0) {
			return null;
		}
		Map<String, String> params = new TreeMap<String, String>();

		for (EquipmentModel item : equModList) {
			params.put(item.getEquModelId() + "", item.getEquModel());
		}
		return params;
	}
    /**
     * 获得设备页面显示企业型号下拉数据
     * @return
     */
	@Override
	public List<ComboData> findEuqModel() {
		List<EquipmentModel> equModList = mapper.findEntitysByCondition(null);
		if (equModList == null || equModList.size() == 0) {
			return null;
		}
		List<ComboData> params = new ArrayList<ComboData>();
		for (EquipmentModel item : equModList) {
			ComboData cData = new ComboData(item.getEquModelId()+"", item.getEquModel());
			params.add(cData);
		}
		return params;

	}
}
