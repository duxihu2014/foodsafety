package com.otec.foodsafety.service.equipment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.equipment.Audio;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.equipment.EquipmentAudio;
import com.otec.foodsafety.entity.equipment.EquipmentAudioExt;
import com.otec.foodsafety.entity.producesafety.RecordAdditiveUseageExt;
import com.otec.foodsafety.mapper.equipment.AudioMapper;
import com.otec.foodsafety.mapper.equipment.EquipmentAudioMapper;
import com.otec.foodsafety.mapper.equipment.EquipmentMapper;

@Service
@Transactional
public class EquipmentAudioServiceImpl  extends BaseServiceImpl<EquipmentAudio,Long> implements EquipmentAudioService{
	@Autowired
	EquipmentAudioMapper mapper;
	@Autowired
	EquipmentMapper equMapper;
	@Autowired
	AudioMapper audioMapper;
	
	@Autowired
	public  void  setMapper(EquipmentAudioMapper mapper){
		 setGenericMapper(mapper);
	}
	@Override
	public GridDataModel getNoBindCameraByCondition(PageObject po) {
		//得到所有有效的设备信息
		Integer totalCount = equMapper.countEquipmentByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<Equipment> results = equMapper.findEquipmentByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}
	@Override
	public GridDataModel getBindCameraByCondition(PageObject po) {
		Integer totalCount = mapper.countBindCameraByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<EquipmentAudioExt> results = mapper.findBindCameraByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}
	
}
