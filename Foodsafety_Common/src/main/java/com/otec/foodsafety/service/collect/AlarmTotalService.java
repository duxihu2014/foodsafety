package com.otec.foodsafety.service.collect;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.collect.AlarmTotal;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface AlarmTotalService extends BaseService<AlarmTotal, Long> {

    List<AlarmTotal> getAlarmTask();

    boolean existAlarmTotal(Map<String, Object> map);

    AlarmTotal getAlarmTotal(Map<String, Object> map);

    void accumulateAlarmTotal(AlarmTotal alarmTotal);

    List<Integer> sortAlarmTotal(Map<String, Object> map);

    void rankingAlarmTotal(List<Integer> list);

    AlarmTotal getAlarmTotalLastMonth(Map<String, Object> map);

    void aggregateAlarmTotal(AlarmTotal alarmTotal);

    List<AlarmTotal> queryAlarmRankingCurrentMonth(Map<String, Object> map);
}
