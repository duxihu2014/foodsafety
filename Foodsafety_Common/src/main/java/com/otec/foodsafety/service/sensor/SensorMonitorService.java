package com.otec.foodsafety.service.sensor;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.sensor.SensorMonitor;

import java.util.List;
import java.util.Map;

@RemoteService
public interface SensorMonitorService extends BaseService<SensorMonitor,Long>{

    List<SensorMonitor> getReport(String sensorNo);

    List<SensorMonitor> getCurrentMonitor(String sensorNo);

    Map<String, Object> getReportByAlarm(Long alarmId);

    List<SensorMonitor> getSupplySensorMonitor (Map<String, Object> map);

    Map<String, Object> getAppReportByAlarm(Long alarmId);
}
