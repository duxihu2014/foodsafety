package com.otec.foodsafety.service.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.equipment.EquipmentAudio;
import com.otec.foodsafety.entity.equipment.EquipmentPushflowInfo;
import com.otec.foodsafety.mapper.equipment.EquipmentAudioMapper;
import com.otec.foodsafety.mapper.equipment.EquipmentMapper;
import com.otec.foodsafety.mapper.equipment.EquipmentPushflowInfoMapper;
import com.otec.foodsafety.util.vo.EquGroupTree;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EquipmentPushflowInfoServiceImpl extends BaseServiceImpl<EquipmentPushflowInfo,Long> implements EquipmentPushflowInfoService{

	@Autowired
	private EquipmentPushflowInfoMapper mapper;


	@Autowired
	public  void  setMapper(EquipmentPushflowInfoMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public EquipmentPushflowInfo findById(Long id){
		return mapper.findById(id);
	}


//    @Override
//    public Equipment add(Equipment entity) {
//        persist(entity);
//        if(entity.getAudioId() != null){
//            saveEquipmentAndAudio(entity.getEquipmentId(), entity.getAudioId());
//        }
//        return entity;
//    }
//
//    @Override
//    public void update(Equipment entity) {
//        updateById(entity);
//        if(entity.getAudioId() != null){
//            saveEquipmentAndAudio(entity.getEquipmentId(), entity.getAudioId());
//        }
//    }


//    private void saveEquipmentAndAudio(long equipmentId, long audioId){
//	    Map<String, Object> map = Maps.newHashMap();
//	    map.put("equipmentId", equipmentId);
//        List<EquipmentAudio> list = equipmentAudioMapper.findEntitysByCondition(map);
//        for (EquipmentAudio item : list){
//            equipmentAudioMapper.removeById(item.getEquipmentAudioId());
//        }
//
//        EquipmentAudio entity = new EquipmentAudio();
//        entity.setAudioId(audioId);
//        entity.setEquipmentId(equipmentId);
//        entity.setStatus("1");
//        equipmentAudioMapper.persist(entity);
//    }

//	@Override
//	public void add(EquipmentPushflowInfo entity) {
//		mapper.persist(entity);
//	}
//
//	@Override
//	public void update(EquipmentPushflowInfo entity) {
//		mapper.updateById(entity);
//	}
//
//	@Override
//	public EquipmentPushflowInfo EquipmentPushflowInfoById(long id) {
//		return mapper.findById(id);
//	}
//
//	@Override
//	public void persist(EquipmentPushflowInfo equipmentPushflowInfo) {
//
//	}
//
//	@Override
//	public int updateById(EquipmentPushflowInfo equipmentPushflowInfo) {
//		return 0;
//	}
//
//	@Override
//	public int mergeById(EquipmentPushflowInfo equipmentPushflowInfo) {
//		return 0;
//	}


}
