package com.otec.foodsafety.service.enterprise;

import com.otec.foodsafety.entity.operation.CateringStaff;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;

import java.util.List;
import java.util.Map;

@RemoteService
public interface EnterpriseMaterialService extends BaseService<EnterpriseMaterial, Long> {

	EnterpriseMaterialChange findByChangeId(Long id);

	void verifyEnterpriseCertificate(EnterpriseMaterial enterpriseMaterial, EnterpriseMaterialChange emc,
			EnterpriseVerify enterpriseVerify);

	/**
	 * 得到页面列表分页数据
	 */
	GridDataModel getGridDataModelByCondition(PageObject po);

	EnterpriseMaterialExt getEnterpriseMaterialById(Long productId);

	/**
	 * 添加
	 * 
	 * 
	 * @param userId            当前用户ID
	 * @param reason            变更原因
	 * @param enterpriseProduct 企业产品
	 * @throws Exception
	 */
	void addEnterpriseMaterial(Long userId, String reason, EnterpriseMaterial enterpriseMaterial) throws Exception;

	/**
	 * 修改
	 * 
	 * 
	 * @param userId            当前用户ID
	 * @param reason            变更原因
	 * @param enterpriseProduct 企业产品
	 * @param operType          操作类型 1添加 2 修改 3删除
	 * @throws Exception
	 */
	void modifyEnterpriseMaterial(Long userId, String reason, EnterpriseMaterial enterpriseMaterial, String operType)
			throws Exception;






}
