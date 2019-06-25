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

	EnterpriseEquipmentExt getEnterpriseEquipmentById(Long productId);

	/**
	 * 添加
	 * 
	 * @param uploadUrl         上传URL
	 * @param imageFolder       图片保存文件夹
	 * @param multipartFile     图片数据
	 * @param userId            当前用户ID
	 * @param reason            变更原因
	 * @param enterpriseProduct 企业产品
	 * @throws Exception
	 */
//	void addEnterpriseEquipment(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
//			String reason, EnterpriseEquipment enterpriseEquipment) throws Exception;
	void addEnterpriseEquipment(Long userId, String reason, EnterpriseEquipment enterpriseEquipment) throws Exception;

	/**
	 * 修改
	 * 
	 * @param uploadUrl         上传URL
	 * @param imageFolder       图片保存文件夹
	 * @param multipartFile     图片数据
	 * @param userId            当前用户ID
	 * @param reason            变更原因
	 * @param enterpriseProduct 企业产品
	 * @param operType          操作类型 1添加 2 修改 3删除
	 * @throws Exception
	 */
//	void modifyEnterpriseEquipment(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
//			String reason, EnterpriseEquipment enterpriseEquipment, String operType) throws Exception;
	void modifyEnterpriseEquipment(Long userId, String reason, EnterpriseEquipment enterpriseEquipment, String operType)
			throws Exception;
}
