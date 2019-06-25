package com.otec.foodsafety.service.enterprise;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;

import org.springframework.remoting.service.annotation.RemoteService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Administrator
 *
 */
@RemoteService
public interface EnterpriseProductService extends BaseService<EnterpriseProduct, Long> {
	/**
	 * 得到页面列表分页数据
	 */
	GridDataModel getGridDataModelByCondition(PageObject po);

	/**
	 * 根据Id得到企业产品信息
	 * 
	 * @param productId
	 * @return
	 */
	EnterpriseProductExt getEnterpriseProductById(Long productId);

	EnterpriseProductChange findByChangeId(Long id);

	void verifyEnterpriseCertificate(EnterpriseProduct enterpriseProduct, EnterpriseProductChange epc,
			EnterpriseVerify enterpriseVerify);

	/**
	 * 添加企业产品
	 * 
	 * @param uploadUrl         上传URL
	 * @param imageFolder       图片保存文件夹
	 * @param multipartFile     图片数据
	 * @param userId            当前用户ID
	 * @param reason            变更原因
	 * @param enterpriseProduct 企业产品
	 * @throws Exception
	 */
	void addEnterpriseProduct(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
			String reason, EnterpriseProduct enterpriseProduct) throws Exception;

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
	void modifyEnterpriseProduct(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
			String reason, EnterpriseProduct enterpriseProduct, String operType) throws Exception;
}
