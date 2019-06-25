package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificate;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/10/16 0016 下午 16:11
 */
@RemoteService
public interface EnterpriseVerifyService  extends BaseService<EnterpriseVerify,Long> {

    /**
     * 根据区域id和企业数据变更类型查询审批记录总条数
     * @param map
     * @return
     */
    Integer getEnterpriseVerifyCount(Map map);
    /**
     * 根据区域id和企业数据变更类型查询审批分页记录
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    public List<EnterpriseVerify> getEnterpriseVerify(Map map, int start, int pageSize);
}
