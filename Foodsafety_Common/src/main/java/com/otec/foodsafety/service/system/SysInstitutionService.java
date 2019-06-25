package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.InstitutionTree;
import com.otec.foodsafety.entity.system.SysInstitution;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface SysInstitutionService extends BaseService<SysInstitution,Long> {

    void addSysInstitution(SysInstitution sysInstitution);
    /**
     * 根据部门名称查询机构
     * @param deptName
     * @return
     */
    SysInstitution findByInstitutionName(String deptName);


    /**
     * 修改绑定机构
     */
    public void modifyInstitutions(Long userId, String deptIds);

    public List<SysInstitution> selectInstitutionsByUserId(Long userId);


    /**
     * 批量修改机构状态
     * @param ids
     * @param status
     */
    void batchChangeStatus(Long[] ids, String status);

    /**
     * 加载所有的机构
     *
     * @param expanded
     *            是否展开树菜单
     * @return
     */
    List<InstitutionTree> listTree(Long institutionId, boolean expanded);

 }
