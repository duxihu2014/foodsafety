package com.otec.foodsafety.service.safety;

import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResult;

import java.util.List;
import java.util.Map;

@RemoteService
public interface OnsiteVerificationResultService extends BaseService<OnsiteVerificationResult,Long>{
    /**
     * 查询现场检查计划的检查记录
     * @param params
     * @return
     */
    List<Map<String, Object>> getOnsiteResult(Map<String, String> params);

    /**
     * 创建现场检查记录
     *
     */
    void create(OnsiteVerificationResult onsiteVerificationResult,
                List<OnsiteVerificationResultItem> resultItemList, List<SysResource> SysResourceList);

    /**
     * 创建现场检查记录
     *
     */
    void createByResourceId(OnsiteVerificationResult onsiteVerificationResult,
                List<OnsiteVerificationResultItem> resultItemList, List<Long> resourceIds);

    /**
     * 跟进现场检查记录
     *
     */
    void update(OnsiteVerificationResult onsiteVerificationResult,
                List<OnsiteVerificationResultItem> resultItemList, List<SysResource> sysResourceList);
}
