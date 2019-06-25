package com.otec.foodsafety.mapper.collect;

import com.otec.foodsafety.entity.collect.AlarmTotal;
import org.apache.ibatis.annotation.myibatis.GenericMapper;

import java.util.List;
import java.util.Map;

public interface AlarmTotalMapper extends GenericMapper<AlarmTotal, Long> {

    List<AlarmTotal> queryAlarmTask();

    void accumulateAlarmTotal(AlarmTotal alarmTotal);

    List<Integer> sortAlarmTotal(Map<String, Object> map);

    void rankingAlarmTotal(Map<String, Object> map);

    AlarmTotal getAlarmTotal(Map<String, Object> map);

    AlarmTotal queryAlarmTotalLastMonth(Map<String, Object> map);

    void insertAlarmTotal(AlarmTotal alarmTotal);

    List<AlarmTotal> queryAlarmRankingCurrentMonth(Map<String, Object> map);
}
