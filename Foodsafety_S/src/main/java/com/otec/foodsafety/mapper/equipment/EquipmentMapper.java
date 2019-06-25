package com.otec.foodsafety.mapper.equipment;

import com.otec.foodsafety.entity.equipment.Equipment;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EquipmentMapper extends GenericMapper<Equipment,Long>{
	/**
	 * 统计条件查询音频下已绑或未绑的摄像头信息
	 * 
	 * @param condition
	 */
	Integer countNOBindCameraByCondition(Map<String, Object> condition);

	/**
	 * 根据条件查询音频下已绑或未绑的摄像头信息
	 * 
	 * @param condition
	 */
	List<Equipment> findNOBindCameraByCondition(Map<String, Object> condition, RowBounds rowBounds);

	List<Equipment> findNOBindCameraByCondition(Map<String, Object> condition);

    List<Map<String, Object>> getGroupTree(Map<String, Object> condition);

	Integer countEquipmentByCondition(Map<String, Object> condition);

	/**
	 * [分页] 查询项目
	 *
	 * @param condition
	 * @param rowBounds
	 * @return
	 */
	List<Equipment> findEquipmentByCondition(Map<String, Object> condition, RowBounds rowBounds);
}
