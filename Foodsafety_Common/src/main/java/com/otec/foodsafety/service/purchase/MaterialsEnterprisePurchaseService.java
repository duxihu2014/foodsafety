package com.otec.foodsafety.service.purchase;

import java.util.List;
import java.util.Map;

import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.purchase.MaterialsEnterprisePurchase;
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.util.vo.ComboData;

@RemoteService
public interface MaterialsEnterprisePurchaseService extends BaseService<MaterialsEnterprisePurchase, Long> {

	GridDataModel getGridDataModelByCondition(PageObject po);

	MaterialsEnterprisePurchase getMaterialsEnterprisePurchaseById(Map<String, Object> condition);

	/**
	 * 根据条件 得到combo、selectd所需要数据
	 * 
	 * @param condition
	 * @return
	 */
	List<ComboData> findPurchaseComboByCondition(Map<String, Object> condition);

	/**
	 * 添加采购记录
	 * 
	 * @param entity
	 * @param inspectionReportSource 图片资源
	 * @param inspectionReport
	 */
	void add(MaterialsEnterprisePurchase materialEnterprisePurchase, SysResource inspectionReportSource);

	/**
	 * 修改采购记录
	 * 
	 * @param materialEnterprisePurchase
	 * @param inspectionReportSource
	 * @throws Exception
	 */
	void update(MaterialsEnterprisePurchase materialEnterprisePurchase, SysResource inspectionReportSource)
			throws Exception;

	void deleteById(Long purchaseId);


	/**
	 * 获取企业原料最新进货信息
	 * @param enterpriseId
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	List<MaterialsEnterprisePurchase> getMaterialNewRecordPage(Map<String, String> enterpriseId, int offset, int pageSize);

	Integer getMaterialNewRecordCount(Map<String, String> enterpriseId);

	/**
	 *添加采购记录   图片编号先存入库中
	 * @param purchase
	 */
	void add(MaterialsEnterprisePurchase purchase);

	/**
	 *修改采购记录  图片编号先存入库中
	 * @param purchase
	 */
	void update(MaterialsEnterprisePurchase purchase) throws Exception;
}
