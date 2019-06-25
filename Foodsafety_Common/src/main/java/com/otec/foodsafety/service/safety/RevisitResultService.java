package com.otec.foodsafety.service.safety;

import com.otec.foodsafety.entity.safety.InspectionResultItem;
import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.safety.RevisitResult;

import java.util.List;

@RemoteService
public interface RevisitResultService extends BaseService<RevisitResult,Long> {

    void create(RevisitResult revisitResult,
                List<InspectionResultItem> resultItemList,
                List<SysResource> sysResourceList);

}
