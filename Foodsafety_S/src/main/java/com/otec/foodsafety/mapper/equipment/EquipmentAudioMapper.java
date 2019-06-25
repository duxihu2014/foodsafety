package com.otec.foodsafety.mapper.equipment;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import com.otec.foodsafety.entity.equipment.EquipmentAudio;
import com.otec.foodsafety.entity.equipment.EquipmentAudioExt;

public interface EquipmentAudioMapper extends GenericMapper<EquipmentAudio, Long> {
	/**
	 * 统计条件查询音频下已绑或未绑的摄像头信息
	 * 
	 * @param condition
	 */
	Integer countBindCameraByCondition(Map<String, Object> condition);

	/**
	 * 根据条件查询音频下已绑或未绑的摄像头信息
	 * 
	 * @param condition
	 */
	List<EquipmentAudioExt> findBindCameraByCondition(Map<String, Object> condition, RowBounds rowBounds);

	List<EquipmentAudioExt> findBindCameraByCondition(Map<String, Object> condition);

	/**
	 * 统计条件查询摄像头下已绑或未绑的音频
	 * 
	 * @param condition
	 */
	Integer countBindAudioByCondition(Map<String, Object> condition);

	/**
	 * 根据条件查询摄像头下已绑或未绑的音频
	 * 
	 * @param condition
	 */
	List<EquipmentAudioExt> findBindAudioByCondition(Map<String, Object> condition, RowBounds rowBounds);

	List<EquipmentAudioExt> findBindAudioByCondition(Map<String, Object> condition);
}
