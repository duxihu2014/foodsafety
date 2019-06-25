package com.otec.foodsafety.service.supervisory;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.system.SysRoleUser;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.entity.system.SysUserInstitution;
import com.otec.foodsafety.mapper.supervisory.PersonnelThreeMapper;
import com.otec.foodsafety.mapper.supervisory.ResponsibilityGridMapper;
import com.otec.foodsafety.mapper.system.SysRoleUserMapper;
import com.otec.foodsafety.mapper.system.SysUserInstitutionMapper;
import com.otec.foodsafety.mapper.system.SysUserMapper;
import com.otec.foodsafety.service.system.SysUserService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PersonnelThreeServiceImpl extends BaseServiceImpl<PersonnelThree, Long> implements PersonnelThreeService {
    @Autowired
    PersonnelThreeMapper personnelThreeMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserInstitutionMapper sysUserInstitutionMapper;
    @Autowired
    private SysRoleUserMapper roleUsermapper;

    @Autowired
    public void setMapper(PersonnelThreeMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchChangeStatus(Long[] ids, String status,Long[] userIds) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("status", status);
        personnelThreeMapper.batchChangeStatus(map);

        map.put("ids",userIds);
        userMapper.batchChangeStatus(map);
     }

    @Override
    public Integer getPageCount(Map<String, String> params) {
        return personnelThreeMapper.getPageCount(params);
    }

    @Override
    public List<PersonnelThree> getPage(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return personnelThreeMapper.getPage(params,rowBounds);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(SysUser newUser, PersonnelThree entity) {
        userMapper.persist(newUser);

        SysUserInstitution userInstitution = new SysUserInstitution();
        userInstitution.setInstitutionId(entity.getInstitutionId());
        userInstitution.setUserId(newUser.getUserId());
        userInstitution.setAreaId(entity.getAreaId());
        sysUserInstitutionMapper.persist(userInstitution);

        SysRoleUser roleUser = new SysRoleUser();
        roleUser.setRoleId(newUser.getRoleId());
        roleUser.setUserId(newUser.getUserId());
        roleUsermapper.persist(roleUser);

        entity.setUserId(newUser.getUserId());
        personnelThreeMapper.persist(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(PersonnelThree entity) {
        SysUser user=userMapper.findByUserId(entity.getUserId());
        if(!entity.getPersonnelType().equals(user.getRoleId())){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", entity.getUserId());
            List<SysRoleUser> sysRoleUserList=roleUsermapper.findEntitysByCondition(param);
            if(sysRoleUserList.size()>0){
                SysRoleUser roleUser=sysRoleUserList.get(0);
                if(!String.valueOf(roleUser.getRoleId()).equals(entity.getPersonnelType())){
                    roleUser.setRoleId(Long.parseLong(entity.getPersonnelType()));
                    roleUsermapper.updateById(roleUser);
                }
            }
        }
        personnelThreeMapper.updateById(entity);
    }


}
