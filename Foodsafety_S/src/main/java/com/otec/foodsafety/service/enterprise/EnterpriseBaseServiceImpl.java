package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseBaseChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public  class EnterpriseBaseServiceImpl extends BaseServiceImpl<EnterpriseBase, Long> implements EnterpriseBaseService {
    @Autowired
    private EnterpriseBaseMapper enterpriseBaseMapper;
    @Autowired
    private EnterpriseBaseChangeMapper enterpriseBaseChangeMapper;
    @Autowired
    private EnterpriseVerifyMapper enterpriseVerifyMapper;

    @Autowired
    public void setMapper(EnterpriseBaseMapper mapper) {
        setGenericMapper(mapper);
    }

    /*企业基本信息修改*/
    @Override
    public void modifyEnterpriseBase(EnterpriseBase enterpriseBase,Long userId,String reason) {
        //1.EnterpriseBaseChange企业基本信息变更表新增一条数据
        EnterpriseBaseChange enterpriseBaseChange = new EnterpriseBaseChange();
        BeanUtils.copyProperties(enterpriseBase,enterpriseBaseChange);
        enterpriseBaseChange.setVerifyStatus("1");
        enterpriseBaseChange.setChangeType("2");
        enterpriseBaseChangeMapper.persist(enterpriseBaseChange);
        //2.EnterpriseVerify企业信息审核表新增一条数据
        EnterpriseVerify enterpriseVerify = new EnterpriseVerify();
        Date data = new Date();
        enterpriseVerify.setChangeId(enterpriseBaseChange.getChangeId());
        enterpriseVerify.setChangeType("2");//变更操作类型1.新增、2.修改、3.删除
        enterpriseVerify.setDataType("1");
        enterpriseVerify.setChangeUserId(userId);
        enterpriseVerify.setChangeTime(data);
        enterpriseVerify.setEnterpriseId(enterpriseBase.getEnterpriseId());
        enterpriseVerify.setVerifyStatus("1");
        enterpriseVerify.setChangeReason(reason);
        enterpriseVerifyMapper.persist(enterpriseVerify);
    }

    @Override
    public List<EnterpriseBase> getEnterpriseByGrid(Long gridId) {
        return enterpriseBaseMapper.getEnterpriseByGrid(gridId);
    }

    /**
     * 获取企业基本信息修改数据
     *
     * @param map
     * @return
     */
    @Override
    public List<EnterpriseBaseChange> getBaseChange(Map map) {
        map.put("orderByClause","CHANGE_ID desc");
        return enterpriseBaseChangeMapper.findEntitysByCondition(map);
    }

    /**
     * 根据变更id获取基本信息变更数据
     *
     * @param changeId
     * @return
     */
    @Override
    public EnterpriseBaseChange getChangeById(Long changeId) {
        return enterpriseBaseChangeMapper.findById(changeId);
    }

    /**
     * 企业基本信息审核
     *
     * @param enterpriseBase
     * @param enterpriseBaseChange
     * @param enterpriseVerify
     */
    @Override
    public void verifyEnterpriseBase(EnterpriseBase enterpriseBase, EnterpriseBaseChange enterpriseBaseChange, EnterpriseVerify enterpriseVerify) {
        //1.更新审核表
        enterpriseVerifyMapper.updateById(enterpriseVerify);
        if(enterpriseVerify.getVerifyStatus().equals("2")){//审核通过,将change表数据更新到原始表
            BeanUtils.copyProperties(enterpriseBaseChange,enterpriseBase);
            EnterpriseBase enterpriseBase1 = enterpriseBaseMapper.findById(enterpriseBaseChange.getEnterpriseId());
            enterpriseBase.setEnterpriseStatus(enterpriseBase1.getEnterpriseStatus());
            enterpriseBaseMapper.updateById(enterpriseBase);
            enterpriseBaseChange.setVerifyStatus("2");
        }else{//审核不通过
            enterpriseBaseChange.setVerifyStatus("3");
        }
        enterpriseBaseChangeMapper.updateById(enterpriseBaseChange);
    }

    /**
     * 获取基本信息修改数据分页总条数
     *
     * @param map
     * @return
     */
    @Override
    public Integer getBaseChangePageCount(Map map) {
        return enterpriseBaseChangeMapper.getBaseChangePageCount(map);
    }

    /**
     * 获取基本信息修改数据分页数据
     *
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> getBaseChangePage(Map map, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return enterpriseBaseChangeMapper.getBaseChangePage(map,rowBounds);
    }

    @Override
    public GridDataModel getPage(PageObject po) {
        RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
        int total = enterpriseBaseMapper.getPageCount(po.getCondition());
        List<Map<String, Object>> list = enterpriseBaseMapper.getPage(po.getCondition(), rowBounds);
        List<Map<String, Object>> result = new ArrayList<>();
        for(Map map : list){
            Map param = new HashMap();
            param.put("verifyStatus","1");
            param.put("enterpriseId",map.get("enterpriseId"));
            if(!(enterpriseVerifyMapper.findEntitysByCondition(param).isEmpty())){//正在审批
                map.put("verifyStatus","1");
            }else{//未审批或审批完成
                map.put("verifyStatus","0");
            }
            result.add(map);
        }
        return new GridDataModel(list, total);
    }

    /**
     * 企业档案查询*/
	@Override
	public GridDataModel getEnterpriseAllInfo(PageObject po) {
		 RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
        int total = enterpriseBaseMapper.countEnterpriseAllInfo(po.getCondition());
        List<EnterpriseBase> list = enterpriseBaseMapper.getEnterpriseAllInfo(po.getCondition(), rowBounds);
        return new GridDataModel(list, total);
	}

    @Override
    public List<Map> getJurisdictionEnterprise(Long userId, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return enterpriseBaseMapper.getJurisdictionEnterprise(userId,rowBounds);
    }

    @Override
    public Integer getJurisdictionEnterpriseCount(long userId) {
        return enterpriseBaseMapper.getJurisdictionEnterpriseCount(userId);
    }

    /**
     * 根据条件查询网格下所有的企业信息(分页查询)
     *
     * @param po
     * @return
     */
    @Override
    public GridDataModel getEnterpriseByGridPage(PageObject po) {
        RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
        int total = enterpriseBaseMapper.getEnterpriseByGridCount(po.getCondition());
        List<EnterpriseBase> list = enterpriseBaseMapper.getEnterpriseByGridPage(po.getCondition(), rowBounds);
        return new GridDataModel(list, total);
    }

    @Override
    public List<Map<String, Object>> getSubjectClassificationByGrid(Long gridId) {
        return enterpriseBaseMapper.getSubjectClassificationByGrid(gridId);
    }

    @Override
    public List<Map<String, Object>> getEnterpriseCountBySubject(Long areaId) {
        return enterpriseBaseMapper.getEnterpriseCountBySubject(areaId);
    }

    @Override
    public List<Map<String, Object>> getEnterpriseCountBySupervise(Long areaId) {
        return enterpriseBaseMapper.getEnterpriseCountBySupervise(areaId);
    }

    @Override
    public List<String> getMyEnterpriseName(Map<String, Object> params) {
        return enterpriseBaseMapper.getMyEnterpriseName(params);
    }


}
