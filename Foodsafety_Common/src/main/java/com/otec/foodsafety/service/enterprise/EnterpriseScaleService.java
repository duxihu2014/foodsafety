package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseScale;
import com.otec.foodsafety.entity.enterprise.EnterpriseScaleChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/10/22 0022 上午 10:40
 */
@RemoteService
public interface EnterpriseScaleService extends BaseService<EnterpriseScale,Long> {
    /**
     * 获取企业规模修改数据
     * @param map
     * @return
     */
    public List<EnterpriseScaleChange> getScaleChange(Map map);

    /**
     * 修改企业规模信息
     * @param enterpriseScale
     * @param userId
     * @param reason
     */
    public void modifyEnterpriseScale(EnterpriseScale enterpriseScale,Long userId,String reason);

    /**
     * 新增企业规模信息
     * @param enterpriseScale
     * @param userId
     * @param reason
     */
    public void addEnterpriseScale(EnterpriseScale enterpriseScale,Long userId,String reason);

    /**
     * 根据变更id获取规模变更数据
     * @param changeId
     * @return
     */
    public EnterpriseScaleChange getChangeById(Long changeId);

    /**
     * 企业规模审核
     * @param enterpriseScale
     * @param enterpriseScaleChange
     * @param enterpriseVerify
     */
    public void verifyEnterpriseScale(EnterpriseScale enterpriseScale,
                                            EnterpriseScaleChange enterpriseScaleChange,
                                            EnterpriseVerify enterpriseVerify);

    /**
     * 获取企业规模修改数据分页总条数
     * @param map
     * @return
     */
    Integer getScaleChangePageCount(Map map);

    /**
     * 获取企业规模修改数据分页数据
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> getScaleChangePage(Map map, int start, int pageSize);
}
