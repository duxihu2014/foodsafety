package com.otec.foodsafety.service.equipment;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.otec.foodsafety.util.PlayUtil;
import com.otec.foodsafety.util.vo.EquGroupTree;
import com.otec.foodsafety.entity.equipment.EquipmentAudio;
import com.otec.foodsafety.mapper.equipment.EquipmentAudioMapper;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.mapper.equipment.EquipmentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EquipmentServiceImpl  extends BaseServiceImpl<Equipment,Long> implements EquipmentService{

	@Autowired
	private EquipmentMapper mapper;

    @Autowired
	private EquipmentAudioMapper equipmentAudioMapper;

	@Autowired
	public  void  setMapper(EquipmentMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public List<EquGroupTree> groupTree(String compId) {
		List<EquGroupTree> result = Lists.newArrayList();

		Map<String, Object> map = Maps.newHashMap();
		map.put("compId", compId);
		List<Map<String, Object>> list = mapper.getGroupTree(map);
		Map<String, EquGroupTree> parents = Maps.newHashMap();
		for(Map<String, Object> item : list){
			String pid = item.get("pid").toString();
			EquGroupTree parentNode = parents.get(pid);
			if(parentNode == null){
				parentNode = new EquGroupTree(pid, item.get("pname").toString(), null, "1");
				parentNode.setChildren(new ArrayList<EquGroupTree>());//Lists.newArrayList()

				if(item.get("id") != null){
					EquGroupTree childNode = new EquGroupTree(item.get("id").toString(), item.get("name").toString(), pid, "2");
					parentNode.getChildren().add(childNode);
				}

				parents.put(pid, parentNode);
			}else {
				if(item.get("id") != null){
					EquGroupTree childNode = new EquGroupTree(item.get("id").toString(), item.get("name").toString(), pid, "2");
					parentNode.getChildren().add(childNode);
				}
			}
		}

		result.addAll(parents.values());
		return result;
	}

	@Override
	public GridDataModel getGridDataByCondition(PageObject po) {
		Integer totalCount = mapper.countEquipmentByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<Equipment> results = mapper.findEquipmentByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount);
	}

    @Override
    public Equipment add(Equipment entity) {
        persist(entity);
        if(entity.getAudioId() != null){
            saveEquipmentAndAudio(entity.getEquipmentId(), entity.getAudioId());
        }
        return entity;
    }

    @Override
    public void update(Equipment entity) {
        updateById(entity);
        if(entity.getAudioId() != null){
            saveEquipmentAndAudio(entity.getEquipmentId(), entity.getAudioId());
        }
    }

	@Override
	public String getPlayUrl(long id) {
		Equipment equipment = mapper.findById((long) id);
		if (equipment != null) {
			return equipment.getVideotapeUrl();
//			return PlayUtil.getPlayUrl(equipment.getEquipmentNo());
		}
		return null;
	}
    private void saveEquipmentAndAudio(long equipmentId, long audioId){
	    Map<String, Object> map = Maps.newHashMap();
	    map.put("equipmentId", equipmentId);
        List<EquipmentAudio> list = equipmentAudioMapper.findEntitysByCondition(map);
        for (EquipmentAudio item : list){
            equipmentAudioMapper.removeById(item.getEquipmentAudioId());
        }

        EquipmentAudio entity = new EquipmentAudio();
        entity.setAudioId(audioId);
        entity.setEquipmentId(equipmentId);
        entity.setStatus("1");
        equipmentAudioMapper.persist(entity);
    }
}
