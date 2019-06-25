package com.otec.foodsafety.service.collect;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.collect.AlarmTotal;
import com.otec.foodsafety.mapper.collect.AlarmTotalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AlarmTotalServiceImpl extends BaseServiceImpl<AlarmTotal, Long> implements AlarmTotalService {

    @Autowired
    AlarmTotalMapper alarmTotalMapper;

    @Autowired
    public void setMapper(AlarmTotalMapper mapper) {
        setGenericMapper(mapper);
    }

    /**
     * TODO 查询昨日的报警数据
     * @return
     */
    @Override
    public List<AlarmTotal> getAlarmTask() {
        return alarmTotalMapper.queryAlarmTask();
    }

    /**
     * TODO 判断企业在本月是否已经累计过一次汇总数据
     * @param map [alarmMonth, enterpriseId]
     * @return
     */
    @Override
    public boolean existAlarmTotal(Map<String, Object> map) {
        int count = alarmTotalMapper.countByCondition(map);
        return count > 0;
    }

    /**
     * TODO 查询企业在本月的累计汇总数据
     * @param map [alarmMonth, enterpriseId]
     * @return
     */
    @Override
    public AlarmTotal getAlarmTotal(Map<String, Object> map) {
        return alarmTotalMapper.getAlarmTotal(map);
    }

    /**
     * TODO 将昨日的报警数据，累加到本月报警数据。
     * @param alarmTotal
     */
    @Override
    public void accumulateAlarmTotal(AlarmTotal alarmTotal) {
        alarmTotalMapper.accumulateAlarmTotal(alarmTotal);
    }

    /**
     * TODO 查询本月的报警数据，并按总数量进行降序排序。
     * @param map
     * @return
     */
    @Override
    public List<Integer> sortAlarmTotal(Map<String, Object> map) {
        return alarmTotalMapper.sortAlarmTotal(map);
    }

    /**
     * TODO 根据总数量的降序排名，更新企业本月报警总数量排名。
     * @param list
     */
    @Override
    public void rankingAlarmTotal(List<Integer> list) {
        Map<String, Object> param = new HashMap<>(2);
        for (int i = 0; i < list.size(); i++) {
            param.put("totalId", list.get(i));
            param.put("ranking", i + 1);
            alarmTotalMapper.rankingAlarmTotal(param);
        }
    }

    /**
     * TODO 查询企业上月报警汇总数据
     * @param map
     * @return
     */
    @Override
    public AlarmTotal getAlarmTotalLastMonth(Map<String, Object> map) {
        return alarmTotalMapper.queryAlarmTotalLastMonth(map);
    }

    /**
     * TODO 创建企业本月报警汇总数据
     * @param alarmTotal
     */
    @Override
    public void aggregateAlarmTotal(AlarmTotal alarmTotal) {
        alarmTotalMapper.insertAlarmTotal(alarmTotal);
    }

    @Override
    public List<AlarmTotal> queryAlarmRankingCurrentMonth(Map<String, Object> map) {
        return alarmTotalMapper.queryAlarmRankingCurrentMonth(map);
    }
}
