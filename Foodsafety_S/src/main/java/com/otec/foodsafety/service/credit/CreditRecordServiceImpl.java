package com.otec.foodsafety.service.credit;

import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.credit.CreditRecord;
import com.otec.foodsafety.mapper.credit.CreditRecordMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreditRecordServiceImpl extends BaseServiceImpl<
        CreditRecord, Long> implements CreditRecordService {
    @Autowired
    CreditRecordMapper creditRecordMapper;

    @Autowired
    public void setMapper(CreditRecordMapper mapper) {
        setGenericMapper(mapper);
    }


    @Override
    public CreditRecord getCreditRecordCurrentYear(Long enterpriseId) {
        return creditRecordMapper.getCreditRecordCurrentYear(enterpriseId);
    }

    @Override
    public CreditRecord initCreditRecordCurrentYear(CreditRecord creditRecord) {
        creditRecordMapper.initCreditRecordCurrentYear(creditRecord);
        return creditRecord;
    }

    @Override
    public void updateCreditRecordCurrentYear(CreditRecord creditRecord) {
        creditRecordMapper.updateCreditRecordCurrentYear(creditRecord);
    }

    @Override
    public List<CreditRecord> getCycleScoreRanking(PageObject po) {
        RowBounds rowBounds = new RowBounds(po.getCurrPage(), po.getPageSize());
        return creditRecordMapper.getCycleScoreRankingPage(po.getCondition(),rowBounds);
    }
}
