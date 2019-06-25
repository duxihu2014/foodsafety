package com.otec.foodsafety.mapper.credit;

import com.otec.foodsafety.entity.credit.CreditRecord;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CreditRecordMapper extends GenericMapper<CreditRecord, Long> {

    CreditRecord getCreditRecordCurrentYear(@Param("enterpriseId") Long enterpriseId);

    Long initCreditRecordCurrentYear(CreditRecord creditRecord);

    void updateCreditRecordCurrentYear(CreditRecord creditRecord);

    List<CreditRecord> getCycleScoreRankingPage(Map<String, Object> condition, RowBounds rowBounds);
}
