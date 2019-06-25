package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseScale;
import com.otec.foodsafety.entity.enterprise.EnterpriseScaleChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.mapper.enterprise.EnterpriseScaleChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseScaleMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/10/22 0022 上午 10:41
 */
@Service
@Transactional
public class EnterpriseScaleServiceImpl extends BaseServiceImpl<EnterpriseScale, Long> implements EnterpriseScaleService{
    @Autowired
    public void setMapper(EnterpriseScaleMapper mapper) {
        setGenericMapper(mapper);
    }
    @Autowired
    private EnterpriseScaleChangeMapper enterpriseScaleChangeMapper;

    @Autowired
    private EnterpriseVerifyMapper enterpriseVerifyMapper;

    @Autowired
    private EnterpriseScaleMapper enterpriseScaleMapper;

    /**
     * 获取企业规模修改数据
     * @param map
     * @return
     */
    @Override
    public List<EnterpriseScaleChange> getScaleChange(Map map) {
        map.put("orderByClause","CHANGE_ID desc");
        return enterpriseScaleChangeMapper.findEntitysByCondition(map);
    }

    /**
     * 修改企业规模信息
     *
     * @param enterpriseScale
     * @param userId
     * @param reason
     */
    @Override
    public void modifyEnterpriseScale(EnterpriseScale enterpriseScale, Long userId, String reason) {
        //1.EnterpriseScaleChange企业规模信息变更表新增一条数据
        EnterpriseScaleChange enterpriseScaleChange = new EnterpriseScaleChange();
        BeanUtils.copyProperties(enterpriseScale,enterpriseScaleChange);
        enterpriseScaleChange.setVerifyStatus("1");
        enterpriseScaleChange.setChangeType("2");
        enterpriseScaleChangeMapper.persist(enterpriseScaleChange);
        //2.EnterpriseVerify企业信息审核表新增一条数据
        EnterpriseVerify enterpriseVerify = new EnterpriseVerify();
        Date data = new Date();
        enterpriseVerify.setChangeId(enterpriseScaleChange.getChangeId());
        enterpriseVerify.setChangeType("2");//变更操作类型1.新增、2.修改、3.删除
        enterpriseVerify.setDataType("3");
        enterpriseVerify.setChangeUserId(userId);
        enterpriseVerify.setChangeTime(data);
        enterpriseVerify.setEnterpriseId(enterpriseScale.getEnterpriseId());
        enterpriseVerify.setVerifyStatus("1");
        enterpriseVerify.setChangeReason(reason);
        enterpriseVerifyMapper.persist(enterpriseVerify);
    }

    /**
     * 新增企业规模信息
     * @param enterpriseScale
     * @param userId
     * @param reason
     */
    @Override
    public void addEnterpriseScale(EnterpriseScale enterpriseScale, Long userId, String reason) {
        //1.EnterpriseScaleChange企业规模信息变更表新增一条数据
        EnterpriseScaleChange enterpriseScaleChange = new EnterpriseScaleChange();
        BeanUtils.copyProperties(enterpriseScale,enterpriseScaleChange);
        enterpriseScaleChange.setVerifyStatus("1");
        enterpriseScaleChange.setChangeType("1");
        enterpriseScaleChangeMapper.persist(enterpriseScaleChange);
        //2.EnterpriseVerify企业信息审核表新增一条数据
        EnterpriseVerify enterpriseVerify = new EnterpriseVerify();
        Date data = new Date();
        enterpriseVerify.setChangeId(enterpriseScaleChange.getChangeId());
        enterpriseVerify.setChangeType("1");//变更操作类型1.新增、2.修改、3.删除
        enterpriseVerify.setDataType("3");
        enterpriseVerify.setChangeUserId(userId);
        enterpriseVerify.setChangeTime(data);
        enterpriseVerify.setEnterpriseId(enterpriseScale.getEnterpriseId());
        enterpriseVerify.setVerifyStatus("1");
        enterpriseVerify.setChangeReason(reason);
        enterpriseVerifyMapper.persist(enterpriseVerify);
    }

    /**
     * 根据变更id获取规模变更数据
     *
     * @param changeId
     * @return
     */
    @Override
    public EnterpriseScaleChange getChangeById(Long changeId) {
        return enterpriseScaleChangeMapper.findById(changeId);
    }

    /**
     * 企业规模审核
     *
     * @param enterpriseScale
     * @param enterpriseScaleChange
     * @param enterpriseVerify
     */
    @Override
    public void verifyEnterpriseScale(EnterpriseScale enterpriseScale, EnterpriseScaleChange enterpriseScaleChange, EnterpriseVerify enterpriseVerify) {
        //1.更新审核表
        enterpriseVerifyMapper.updateById(enterpriseVerify);
        if(enterpriseVerify.getVerifyStatus().equals("2")){//审核通过,将change表数据更新到原始表
            if(enterpriseScale.getScaleId()!=null){//修改
                BeanUtils.copyProperties(enterpriseScaleChange,enterpriseScale);
                enterpriseScaleMapper.updateById(enterpriseScale);
            }else{//新增
                BeanUtils.copyProperties(enterpriseScaleChange,enterpriseScale);
                enterpriseScaleMapper.persist(enterpriseScale);
                enterpriseScaleChange.setScaleId(enterpriseScale.getScaleId());
            }
            enterpriseScaleChange.setVerifyStatus("2");

        }else{//审核不通过
            enterpriseScaleChange.setVerifyStatus("3");
        }
        enterpriseScaleChangeMapper.updateById(enterpriseScaleChange);
    }

    /**
     * 获取企业规模修改数据分页总条数
     *
     * @param map
     * @return
     */
    @Override
    public Integer getScaleChangePageCount(Map map) {
        return enterpriseScaleChangeMapper.getScaleChangePageCount(map);
    }

    /**
     * 获取企业规模修改数据分页数据
     *
     * @param map
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> getScaleChangePage(Map map, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return enterpriseScaleChangeMapper.getScaleChangePage(map,rowBounds);
    }
}
