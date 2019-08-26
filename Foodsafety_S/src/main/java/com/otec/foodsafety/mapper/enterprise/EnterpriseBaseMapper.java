package com.otec.foodsafety.mapper.enterprise;

import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EnterpriseBaseMapper extends GenericMapper<EnterpriseBase, Long> {

    List<EnterpriseBase> getEnterpriseByGrid(@Param("gridId") Long gridId);

    List<Map<String, Object>> getPage(Map<String, Object> params, RowBounds rowBounds);

    int getPageCount(Map<String, Object> params);
    /**
     * 查询监管人员所在网格范围内的企业列表
     * @param userId
     * @param rowBounds
     * @return
     */
    List<Map> getJurisdictionEnterprise(Long userId, RowBounds rowBounds);

    Integer getJurisdictionEnterpriseCount(long userId);

    /**
     * 企业档案查询*/
    List<EnterpriseBase> getEnterpriseAllInfo(Map<String, Object> params, RowBounds rowBounds);

    Integer countEnterpriseAllInfo(Map<String, Object> params);

    /**
     * 根据网格查询网格下所有的企业信息（分页查询）
     * @param params
     * @param rowBounds
     * @return
     */
    List<EnterpriseBase> getEnterpriseByGridPage(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 根据网格查询网格下所有的企业数量
     * @param params
     * @return
     */
    Integer getEnterpriseByGridCount(Map<String, Object> params);

    List<Map<String,Object>> getSubjectClassificationByGrid(@Param("gridId") Long gridId);
    List<Map<String,Object>> getEnterpriseCountBySubject(@Param("areaId") Long areaId);
    List<Map<String,Object>> getEnterpriseCountBySupervise(@Param("areaId") Long areaId);
    List<String> getMyEnterpriseName(Map<String, Object> params);



}