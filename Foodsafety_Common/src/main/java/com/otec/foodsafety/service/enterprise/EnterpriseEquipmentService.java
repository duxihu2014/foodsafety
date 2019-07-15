package com.otec.foodsafety.service.enterprise;

import org.springframework.remoting.service.annotation.RemoteService;
import org.springframework.web.multipart.MultipartFile;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipment;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;

@RemoteService
public interface EnterpriseEquipmentService extends BaseService<EnterpriseEquipment, Long> {

	EnterpriseEquipmentChange findByChangeId(Long id);

	void verifyEnterpriseCertificate(EnterpriseEquipment enterpriseEquipment, EnterpriseEquipmentChange eec,
                                     EnterpriseVerify enterpriseVerify);

	/**
	 * 得到页面列表分页数据
	 */
	GridDataModel getGridDataModelByCondition(PageObject po);

	EnterpriseEquipmentExt getEnterpriseEquipmentById(Long equipmentId);

	/**
	 * 添加
	 */
//	void addEnterpriseEquipment(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
//			String reason, EnterpriseEquipment enterpriseEquipment) throws Exception;
	void addEnterpriseEquipment(Long userId, String reason, EnterpriseEquipment enterpriseEquipment) throws Exception;

	/**
	 * 修改
	 */
//	void modifyEnterpriseEquipment(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
//			String reason, EnterpriseEquipment enterpriseEquipment, String operType) throws Exception;
	void modifyEnterpriseEquipment(Long userId, String reason, EnterpriseEquipment enterpriseEquipment, String operType)
			throws Exception;
}
