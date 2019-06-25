package com.otec.foodsafety.service.irregularity;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.irregularity.IrregularityCase;
import com.otec.foodsafety.entity.irregularity.IrregularityCaseClose;
import com.otec.foodsafety.entity.irregularity.IrregularityCaseExt;
import com.otec.foodsafety.entity.irregularity.IrregularityPunish;
import org.springframework.remoting.service.annotation.RemoteService;

/**
 *  食品案件立案
 *
 * @author Administrator
 */
@RemoteService
public interface IrregularityCaseService extends BaseService<IrregularityCase, Long> {


    void add(IrregularityCase irregularityCase);

    IrregularityCaseExt findByIrregularityId(Long id);

    void saveCasePunish(IrregularityPunish punish);

    void saveCaseClose(IrregularityCaseClose caseClose);

    void update(IrregularityCase irregularityCase);
}
