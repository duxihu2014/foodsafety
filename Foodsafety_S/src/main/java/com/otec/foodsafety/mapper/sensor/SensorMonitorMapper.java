package com.otec.foodsafety.mapper.sensor;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import com.otec.foodsafety.entity.sensor.SensorMonitor;

import java.util.List;
import java.util.Map;

public interface SensorMonitorMapper extends GenericMapper<SensorMonitor,Long>{

    List<SensorMonitor> getReport(Map<String, Object> map);

    List<SensorMonitor> getCurrentMonitor(Map<String, Object> map);

    List<SensorMonitor> getSupplySensorMonitor(Map<String, Object> map);
}
