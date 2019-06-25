package com.otec.foodsafety.service.credit;

import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.credit.CreditRecord;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface CreditRecordService extends BaseService<CreditRecord, Long> {

    CreditRecord getCreditRecordCurrentYear(Long enterpriseId);

    CreditRecord initCreditRecordCurrentYear(CreditRecord creditRecord);

    void updateCreditRecordCurrentYear(CreditRecord creditRecord);

    List<CreditRecord> getCycleScoreRanking(PageObject po);
}
