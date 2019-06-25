package com.otec.foodsafety.task;

import com.otec.foodsafety.entity.collect.AlarmTotal;
import com.otec.foodsafety.service.collect.AlarmTotalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmTask {

    @Autowired
    AlarmTotalService alarmTotalService;

    public void aggregateQuantity() {
        System.out.println("AlarmTask：aggregateQuantity begin");

        // 查询昨日的报警数据
        List<AlarmTotal> list =  alarmTotalService.getAlarmTask();

        if (!list.isEmpty()) {
            AlarmTotal alarmTotal = null;
            AlarmTotal alarmTotalLastMonth = null;
            List<Integer> rankingList = null;
            Map<String, Object> param = new HashMap<>(2);
            for (AlarmTotal vAlarmTotal : list) {
                param.put("alarmMonth", vAlarmTotal.getAlarmMonth());
                param.put("enterpriseId", vAlarmTotal.getEnterpriseId());
                // 判断企业在本月是否已经累计过一次汇总数据
                if (alarmTotalService.existAlarmTotal(param)) {
                    // 查询企业在本月的累计汇总数据
                    alarmTotal = alarmTotalService.getAlarmTotal(param);
                    // 将昨日的报警数据，累加到本月报警数据。
                    alarmTotalService.accumulateAlarmTotal(alarmTotal);
                } else {
                    // 新增企业本月汇总数据
                    alarmTotal = vAlarmTotal;
                    // 查询企业上月排名
                    alarmTotalLastMonth = alarmTotalService.getAlarmTotalLastMonth(param);
                    if (alarmTotalLastMonth != null)
                        alarmTotal.setRankingLastMonth(alarmTotalLastMonth.getRanking());
                    // 创建企业本月报警汇总数据
                    alarmTotalService.aggregateAlarmTotal(alarmTotal);
                }
                // 查询本月的报警数据，并按总数量进行降序排序。
                rankingList = alarmTotalService.sortAlarmTotal(param);
                // 根据总数量的降序排名，更新企业本月报警总数量排名。
                alarmTotalService.rankingAlarmTotal(rankingList);
            }
        }

        System.out.println("AlarmTask：aggregateQuantity end");
    }
}
