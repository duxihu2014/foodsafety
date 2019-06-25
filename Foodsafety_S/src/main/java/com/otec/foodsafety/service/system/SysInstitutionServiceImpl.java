package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.safety.InspectionCycle;
import com.otec.foodsafety.entity.system.*;
import com.otec.foodsafety.mapper.system.SysInstitutionMapper;
import com.otec.foodsafety.mapper.system.SysUserInstitutionMapper;
import com.otec.foodsafety.service.safety.InspectionCycleService;
import com.otec.foodsafety.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public  class SysInstitutionServiceImpl extends BaseServiceImpl<SysInstitution, Long> implements SysInstitutionService {
    @Autowired
    private SysInstitutionMapper sysInstitutionMapper;
    @Autowired
    private SysUserInstitutionMapper sysUserInstitutionMapper;
    @Autowired
    SysDictService sysDictService;
    @Autowired
    InspectionCycleService inspectionCycleService;

    @Autowired
    public void setMapper(SysInstitutionMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public void addSysInstitution(SysInstitution sysInstitution) {
        sysInstitutionMapper.persist(sysInstitution);
        // 新增机构相关的默认日常检查周期设置
        InspectionCycle cycle = null;
        if (sysInstitution.getpInstitutionId() > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("settingInstitution", sysInstitution.getpInstitutionId());
            List<InspectionCycle> inspectionCycle = inspectionCycleService.findEntitysByCondition(param);
            if(inspectionCycle.size() == 1) {
                cycle = inspectionCycle.get(0);
                cycle.setAreaId(sysInstitution.getAreaId());
                cycle.setSettingInstitution(sysInstitution.getInstitutionId());
                cycle.setSettingTime(sysInstitution.getCreateTime());
            } else {
                Map<String, String> map = sysDictService.getDetailNameMap("日常检查周期");

                cycle = new InspectionCycle();
                cycle.setAreaId(sysInstitution.getAreaId());
                cycle.setSettingInstitution(sysInstitution.getInstitutionId());
                cycle.setSettingTime(sysInstitution.getCreateTime());
                cycle.setAheadDays(Integer.parseInt(map.get("提前生成天数")));
                cycle.setInspectionCycleA(Integer.parseInt(map.get("A")));
                cycle.setInspectionCycleB(Integer.parseInt(map.get("B")));
                cycle.setInspectionCycleC(Integer.parseInt(map.get("C")));
                cycle.setInspectionCycleD(Integer.parseInt(map.get("D")));

                String monthDay = map.get("初次检查日期");
                String month = org.apache.commons.lang.StringUtils.substring(monthDay, 0, 2);
                String day = org.apache.commons.lang.StringUtils.substring(monthDay, 2,4);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                cycle.setInitialInspection(calendar.getTime());
            }
        } else {
            Map<String, String> map = sysDictService.getDetailNameMap("日常检查周期");

            cycle = new InspectionCycle();
            cycle.setAreaId(sysInstitution.getAreaId());
            cycle.setSettingInstitution(sysInstitution.getInstitutionId());
            cycle.setSettingTime(sysInstitution.getCreateTime());
            cycle.setAheadDays(Integer.parseInt(map.get("提前生成天数")));
            cycle.setInspectionCycleA(Integer.parseInt(map.get("A")));
            cycle.setInspectionCycleB(Integer.parseInt(map.get("B")));
            cycle.setInspectionCycleC(Integer.parseInt(map.get("C")));
            cycle.setInspectionCycleD(Integer.parseInt(map.get("D")));

            String monthDay = map.get("初次检查日期");
            String month = org.apache.commons.lang.StringUtils.substring(monthDay, 0, 2);
            String day = org.apache.commons.lang.StringUtils.substring(monthDay, 2,4);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            cycle.setInitialInspection(calendar.getTime());
        }
        inspectionCycleService.persist(cycle);
    }

    @Override
    public SysInstitution findByInstitutionName(String deptName) {
        return sysInstitutionMapper.findByInstitutionName(deptName);
    }

    @Override
    public void modifyInstitutions(Long userId, String ids) {
        if (userId != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            sysUserInstitutionMapper.deleteByCondition(param);
            if (StringUtils.isNotBlank(ids)) {
                String[] instIdArr = ids.split(",");
                for (String instId : instIdArr) {
                    SysUserInstitution userInstitution = new SysUserInstitution();
                    userInstitution.setInstitutionId(Long.valueOf(instId));
                    userInstitution.setUserId(userId);
                    sysUserInstitutionMapper.persist(userInstitution);
                }
            }
        }
    }

    @Override
    public List<SysInstitution> selectInstitutionsByUserId(Long userId){
        return  sysInstitutionMapper.selectInstitutionsByUserId(userId);
    }

    @Override
    public void batchChangeStatus(Long[] ids, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("status", status);
        sysInstitutionMapper.batchChangeStatus( map);
    }

    @Override
    public List<InstitutionTree> listTree(Long institutionId, boolean expanded) {
        Map<String, Object> condition = new HashMap<String, Object>();
        // 查询显示所有的区域
        condition.put("orderByClause", "P_INSTITUTION_ID asc, INSTITUTION_ID asc");
        condition.put("institutionStatus", 1);
        List<SysInstitution> allInstitutionList = findEntitysByCondition(condition);
        List<InstitutionTree> nodeList = new ArrayList<InstitutionTree>();
        List<SysInstitution> list = new ArrayList<SysInstitution>();
        SysInstitution s = findById(institutionId);
        // 判断操作员所在机构是否存在且启用
        if (s != null && "1".equals(s.getInstitutionStatus())) {
            // 添加根节点
            InstitutionTree root = new InstitutionTree(s);
            root.setRoot(true);
            nodeList.add(root);
            // 递归添加子节点
            getChildrenNode(institutionId, allInstitutionList, list);
            for (int i = 0; i < list.size(); i++) {
                SysInstitution institution = list.get(i);
                InstitutionTree node = new InstitutionTree(institution);
                if (institution.getpInstitutionId() == 0)
                    node.setRoot(true);
                else
                    node.setExpanded(expanded);
                nodeList.add(node);
            }
        }
        List<InstitutionTree> tree = new ArrayList<InstitutionTree>();
        if (nodeList.size() > 0)
            tree = TreeUtil.getResult(nodeList);
        return tree;
    }

    private void getChildrenNode(Long institutionId, List<SysInstitution> institutionList, List<SysInstitution> list) {
        for (SysInstitution institution : institutionList) {
            if (institution.getpInstitutionId() == institutionId) {
                list.add(institution);
                getChildrenNode(institution.getInstitutionId(), institutionList, list);
            }
        }
    }
}
