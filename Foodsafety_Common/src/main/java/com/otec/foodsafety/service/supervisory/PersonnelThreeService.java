package com.otec.foodsafety.service.supervisory;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.supervisory.PersonnelThree;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.system.SysUser;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

@RemoteService
public interface PersonnelThreeService extends BaseService<PersonnelThree, Long> {

    void batchChangeStatus(Long[] ids, String status,Long[] userIds);

    Integer getPageCount(Map<String, String> params);

    List<PersonnelThree> getPage(Map<String, String> params, int offset, int pageSize);

    void add(SysUser newUser, PersonnelThree entity);

    void update(PersonnelThree entity);
}
