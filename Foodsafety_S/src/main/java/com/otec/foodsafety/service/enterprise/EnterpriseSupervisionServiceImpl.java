package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.enterprise.EnterpriseSupervision;
import com.otec.foodsafety.entity.system.SysRoleUser;
import com.otec.foodsafety.entity.system.SysUserEnterprise;
import com.otec.foodsafety.mapper.enterprise.EnterpriseBaseMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseSupervisionMapper;
import com.otec.foodsafety.mapper.system.SysRoleUserMapper;
import com.otec.foodsafety.mapper.system.SysUserEnterpriseMapper;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnterpriseSupervisionServiceImpl extends BaseServiceImpl<
        EnterpriseSupervision, Long> implements EnterpriseSupervisionService {
    @Autowired
    EnterpriseSupervisionMapper enterpriseSupervisionMapper;
    @Autowired
    EnterpriseBaseMapper eBaseMapper;
    @Autowired
    SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    SysUserEnterpriseMapper sysUserEnterpriseMapper;
    @Autowired
    InspectionPlanService inspectionPlanService;
    
    @Autowired
    public void setMapper(EnterpriseSupervisionMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void addEnterpriseSupervision(EnterpriseSupervision entity) {
        enterpriseSupervisionMapper.addEnterpriseSupervision(entity);
    }

	@Override
	public void updateEnterpriseSupervision(EnterpriseSupervision entity,String lnglat) {
        // 更新监管级别
		EnterpriseSupervision item = enterpriseSupervisionMapper.findById(entity.getEnterpriseId());
		item.setRegulatoryLevel(entity.getRegulatoryLevel());
		enterpriseSupervisionMapper.updateById(item);
		//更新企业状态
		EnterpriseBase baseitem = eBaseMapper.findById(entity.getEnterpriseId());
		baseitem.setSuperviseClassification(item.getRegulatoryLevel());
		String[] lnglatArr =lnglat.split(",");
        baseitem.setLongitude(lnglatArr[0]);
		baseitem.setLatitude(lnglatArr[1]);
		baseitem.setEnterpriseStatus("3");//正常
        baseitem.setArchivedDate(new Date());
		eBaseMapper.mergeById(baseitem);
		//更新用户角色表
        Map param1 = new HashMap();
        param1.put("enterpriseId",baseitem.getEnterpriseId());
        List<SysUserEnterprise> sysUserEnterpriseList = sysUserEnterpriseMapper.findEntitysByCondition(param1);
        Map param2 = new HashMap();
        param2.put("userId", sysUserEnterpriseList.get(0).getUserId());
        List<SysRoleUser> sysRoleUserList = sysRoleUserMapper.findEntitysByCondition(param2);
        SysRoleUser sysRoleUser = sysRoleUserList.get(0);
        sysRoleUser.setRoleId(10L);
        sysRoleUserMapper.updateById(sysRoleUser);
        // 创建日常检查计划
        inspectionPlanService.createInspectionPlan(entity.getEnterpriseId());
	}

    @Override
    public List<Map<String, Object>> getSupervisionByGrid(Long gridId) {
        return enterpriseSupervisionMapper.getSupervisionByGrid(gridId);
    }
}
