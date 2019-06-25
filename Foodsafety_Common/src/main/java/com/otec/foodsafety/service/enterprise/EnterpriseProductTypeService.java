package com.otec.foodsafety.service.enterprise;

import java.util.List;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductType;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductTypeTree;

@RemoteService
public interface EnterpriseProductTypeService extends BaseService<EnterpriseProductType,Long>{
	public List<EnterpriseProductTypeTree> listTree(boolean expanded, boolean hideDisableMenus);
}
