package com.otec.foodsafety.mapper.sensor;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import com.otec.foodsafety.entity.sensor.SensorInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SensorInfoMapper extends GenericMapper<SensorInfo,Long>{

    Integer countSensorInfoByCondition(Map<String, Object> condition);

    /**
     * [分页] 查询传感器
     *
     * @param condition
     * @param rowBounds
     * @return
     */
    List<SensorInfo> findSensorInfoByCondition(Map<String, Object> condition, RowBounds rowBounds);


    List<Map<String, Object>> getListCurrentMonitor(Map<String, Object> condition, RowBounds rowBounds);
}
