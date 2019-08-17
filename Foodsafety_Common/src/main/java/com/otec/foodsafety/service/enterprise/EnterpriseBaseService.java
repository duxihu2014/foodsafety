package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.*;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface EnterpriseBaseService extends BaseService<EnterpriseBase,Long> {
  /*企业基本信息修改*/
    public void modifyEnterpriseBase(EnterpriseBase enterpriseBase,Long userId,String reason);

    List<EnterpriseBase> getEnterpriseByGrid(Long gridId);

    /**
     * 获取企业基本信息修改数据
     * @param map
     * @return
     */
    public List<EnterpriseBaseChange> getBaseChange(Map map);

  /**
   * 根据变更id获取基本信息变更数据
   * @param changeId
   * @return
   */
  public EnterpriseBaseChange getChangeById(Long changeId);

  /**
   * 企业基本信息审核
   * @param enterpriseBase
   * @param enterpriseBaseChange
   * @param enterpriseVerify
   */
  public void verifyEnterpriseBase(EnterpriseBase enterpriseBase,
                                    EnterpriseBaseChange enterpriseBaseChange,
                                    EnterpriseVerify enterpriseVerify);

  /**
   * 获取基本信息修改数据分页总条数
   * @param map
   * @return
   */
  Integer getBaseChangePageCount(Map map);

  /**
   * 获取基本信息修改数据分页数据
   * @param map
   * @param start
   * @param pageSize
   * @return
   */
  List<Map<String, Object>> getBaseChangePage(Map map, int start, int pageSize);

  GridDataModel getPage(PageObject po);

  /**档案查询管理*/
  GridDataModel getEnterpriseAllInfo(PageObject po);


  /**
   * 查询机构范围内的企业列表
   * @param userId
   * @return
   */
  List<Map> getJurisdictionEnterprise(Long userId, int start, int pageSize);

  Integer getJurisdictionEnterpriseCount(long userId);

  /**
   * 根据条件查询网格下所有的企业信息(分页查询)
   * @param po
   * @return
   */
  GridDataModel getEnterpriseByGridPage (PageObject po);

  List<Map<String,Object>> getSubjectClassificationByGrid(Long gridId);
  List<Map<String,Object>> getEnterpriseCountBySubject(Long areaId);
  List<Map<String,Object>> getEnterpriseCountBySupervise(Long areaId);

  List<String> getMyEnterpriseName(Map<String,Object> map);
}



