package com.otec.foodsafety.service.classification;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.classification.ClassificationDetail;
import com.otec.foodsafety.entity.commission.CommissionContractFiles;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:34
 */
@RemoteService
public interface ClassificationDetailService extends BaseService<ClassificationDetail, Long> {

    void add(ClassificationDetail detail, SysResource photo);

    List<Map> getChangeRecords(Long enterpriseId);
}
