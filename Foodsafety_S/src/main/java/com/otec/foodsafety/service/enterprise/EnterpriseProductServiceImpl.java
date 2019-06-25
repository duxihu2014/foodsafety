package com.otec.foodsafety.service.enterprise;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseCertificateChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.enterprise.EnterpriseProductChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseProductMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EnterpriseProductServiceImpl extends BaseServiceImpl<EnterpriseProduct, Long>
		implements EnterpriseProductService {

	@Autowired
	EnterpriseProductMapper enterpriseProductMapper;

	@Autowired
	EnterpriseProductChangeMapper enterpriseProductChangeMapper;

	@Autowired
	private EnterpriseVerifyMapper enterpriseVerifyMapper;
	@Autowired
	private SysResourceMapper sysResourceMapper;

	@Autowired
	public void setMapper(EnterpriseProductMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public EnterpriseProductChange findByChangeId(Long id) {

		return enterpriseProductChangeMapper.findById(id);
	}

	@Override
	public void verifyEnterpriseCertificate(EnterpriseProduct enterpriseProduct, EnterpriseProductChange epc,
			EnterpriseVerify enterpriseVerify) {
		// 1.更新审核表
		enterpriseVerifyMapper.updateById(enterpriseVerify);
		if (enterpriseVerify.getVerifyStatus().equals("2")) {// 审核通过
			  if("3".equals(epc.getChangeType())){//删除操作
				 enterpriseProduct.setProductStatus("0");
				 enterpriseProductMapper.persist(enterpriseProduct);
			  }else if("2".equals(epc.getChangeType())){//修改操作
				BeanUtils.copyProperties(epc, enterpriseProduct);
				enterpriseProductMapper.updateById(enterpriseProduct);
			} else if("1".equals(epc.getChangeType())){//新增操作
				BeanUtils.copyProperties(epc, enterpriseProduct);
				enterpriseProduct.setProductStatus("1");
				enterpriseProductMapper.persist(enterpriseProduct);
				epc.setProductId(enterpriseProduct.getProductId());
			}
			epc.setVerifyStatus("2");

		} else {// 审核不通过
			epc.setVerifyStatus("3");
		}
		enterpriseProductChangeMapper.updateById(epc);
	}

	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {
		// 得到所有有效的设备信息
		Integer totalCount = enterpriseProductMapper.countEnterpriseProductByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<EnterpriseProductExt> results = enterpriseProductMapper.findEnterpriseProductByCondition(po.getCondition(),
				rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public EnterpriseProductExt getEnterpriseProductById(Long productId) {
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("productId", productId);
		List<EnterpriseProductExt> item = enterpriseProductMapper.findEnterpriseProductByCondition(cond);		
		
		return item==null?null:item.get(0);
	}

	@Override
	public void addEnterpriseProduct(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
			String reason, EnterpriseProduct enterpriseProduct) throws Exception {
		EnterpriseProductChange enterpriseProductChange = new EnterpriseProductChange();
		BeanUtils.copyProperties(enterpriseProduct, enterpriseProductChange);
		// 1.更新图片资源。
		if (multipartFile != null) {
			String fileName = multipartFile.getOriginalFilename();
			// 1.1先将图片上传服务器
			String str = HttpURLConnectionUtils.sendMessage(
					uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder, multipartFile.getBytes());
			JSONObject jSONObject = JSON.parseObject(str);
			String path = jSONObject.getString("imgUrl");
			String subfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());// 文件后缀
			String rename = path.split("/")[path.split("/").length - 1];
			// 1.2.将图片信息写入数据库
			SysResource resource = new SysResource();
			resource.setResourceName(fileName);
			resource.setResourceRename(rename);
			resource.setResourceStorage(ResourceStorage.LOCAL.toString());
			resource.setResourceExtension(subfix);
			resource.setResourceStatus("1");
			resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
			resource.setResourceLength(multipartFile.getSize());
			resource.setResourcePath(path);
			sysResourceMapper.persist(resource);
			enterpriseProductChange.setProductPicture(resource.getResourceId());
		}
		// 2.新增 企业产品变更表
		enterpriseProductChange.setVerifyStatus("1");// 设置状态为待审批
		enterpriseProductChange.setChangeType("1");// 新增操作
		enterpriseProductChangeMapper.persist(enterpriseProductChange);
		// 3.新增审核记录表
		EnterpriseVerify enterpriseVerify = new EnterpriseVerify();

		enterpriseVerify.setChangeId(enterpriseProductChange.getChangeId());
		enterpriseVerify.setChangeType("1");// 新增操作
		enterpriseVerify.setDataType("4");//// 变更数据类型 1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
		enterpriseVerify.setChangeUserId(userId);
		enterpriseVerify.setChangeTime(new Date());
		enterpriseVerify.setEnterpriseId(enterpriseProductChange.getEnterpriseId());
		enterpriseVerify.setVerifyStatus("1");// 设置状态为待审批
		enterpriseVerify.setChangeReason(reason);// 变更原因
		enterpriseVerifyMapper.persist(enterpriseVerify);

	}

	/**
	 * 修改产品变更信息
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
	@Override
	public void modifyEnterpriseProduct(String uploadUrl, String imageFolder, MultipartFile multipartFile, Long userId,
			String reason, EnterpriseProduct enterpriseProduct, String operType) throws Exception {
		EnterpriseProductChange enterpriseProductChange = new EnterpriseProductChange();
		BeanUtils.copyProperties(enterpriseProduct, enterpriseProductChange);
		// 1.更新图片资源。
		if (multipartFile != null) {
			String fileName = multipartFile.getOriginalFilename();
			// 1.1先将图片上传服务器
			String str = HttpURLConnectionUtils.sendMessage(
					uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder, multipartFile.getBytes());
			JSONObject jSONObject = JSON.parseObject(str);
			String path = jSONObject.getString("imgUrl");
			String subfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());// 文件后缀
			String rename = path.split("/")[path.split("/").length - 1];
			// 1.2.将图片信息写入数据库
			SysResource resource = new SysResource();
			resource.setResourceName(fileName);
			resource.setResourceRename(rename);
			resource.setResourceStorage(ResourceStorage.LOCAL.toString());
			resource.setResourceExtension(subfix);
			resource.setResourceStatus("1");
			resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
			resource.setResourceLength(multipartFile.getSize());
			resource.setResourcePath(path);
			sysResourceMapper.persist(resource);
			enterpriseProductChange.setProductPicture(resource.getResourceId());
		}
		// 2.新增 企业产品变更表
		enterpriseProductChange.setVerifyStatus("1");// 设置状态为待审批
		enterpriseProductChange.setChangeType(operType + "");// 操作
		enterpriseProductChangeMapper.persist(enterpriseProductChange);
		// 3.新增审核记录表
		EnterpriseVerify enterpriseVerify = new EnterpriseVerify();

		enterpriseVerify.setChangeId(enterpriseProductChange.getChangeId());
		enterpriseVerify.setChangeType(operType + "");
		enterpriseVerify.setDataType("4");//// 变更数据类型 1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
		enterpriseVerify.setChangeUserId(userId);
		enterpriseVerify.setChangeTime(new Date());
		enterpriseVerify.setEnterpriseId(enterpriseProductChange.getEnterpriseId());
		enterpriseVerify.setVerifyStatus("1");// 设置状态为待审批
		enterpriseVerify.setChangeReason(reason);// 变更原因
		enterpriseVerifyMapper.persist(enterpriseVerify);

	}

}
