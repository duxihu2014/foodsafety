package com.otec.foodsafety.service.enterprise;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseMaterialExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.enterprise.EnterpriseMaterialChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseMaterialMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;

@Service
@Transactional
public class EnterpriseMaterialServiceImpl extends BaseServiceImpl<EnterpriseMaterial, Long>
		implements EnterpriseMaterialService {

	@Autowired
	EnterpriseMaterialChangeMapper enterpriseMaterialChangeMapper;
	@Autowired
	EnterpriseMaterialMapper enterpriseMaterialMapper;

	@Autowired
	private EnterpriseVerifyMapper enterpriseVerifyMapper;
	@Autowired
	private SysResourceMapper sysResourceMapper;

	@Autowired
	public void setMapper(EnterpriseMaterialMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public EnterpriseMaterialChange findByChangeId(Long id) {
		// TODO Auto-generated method stub
		return enterpriseMaterialChangeMapper.findById(id);
	}

	@Override
	public void verifyEnterpriseCertificate(EnterpriseMaterial enterpriseMaterial, EnterpriseMaterialChange emc,
			EnterpriseVerify enterpriseVerify) {
		// 1.更新审核表
		enterpriseVerifyMapper.updateById(enterpriseVerify);
		if (enterpriseVerify.getVerifyStatus().equals("2")) {// 审核通过
			if ("3".equals(emc.getChangeType())) {// 删除操作
				enterpriseMaterial.setMaterialStatus("0");
				enterpriseMaterialMapper.persist(enterpriseMaterial);
			} else if ("2".equals(emc.getChangeType())) {// 修改操作
				BeanUtils.copyProperties(emc, enterpriseMaterial);
				enterpriseMaterialMapper.updateById(enterpriseMaterial);
			} else if ("1".equals(emc.getChangeType())) {// 新增操作
				BeanUtils.copyProperties(emc, enterpriseMaterial);
				enterpriseMaterial.setMaterialStatus("1");
				enterpriseMaterialMapper.persist(enterpriseMaterial);
				emc.setMaterialId(enterpriseMaterial.getMaterialId());
			}
			emc.setVerifyStatus("2");

		} else {// 审核不通过
			emc.setVerifyStatus("3");
		}
		enterpriseMaterialChangeMapper.updateById(emc);

	}

	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {
		// 得到所有有效的设备信息
		Integer totalCount = enterpriseMaterialMapper.countEnterpriseMaterialByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<EnterpriseMaterialExt> results = enterpriseMaterialMapper
				.findEnterpriseMaterialByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public EnterpriseMaterialExt getEnterpriseMaterialById(Long productId) {
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("productId", productId);
		List<EnterpriseMaterialExt> item = enterpriseMaterialMapper.findEnterpriseMaterialByCondition(cond);

		return item == null ? null : item.get(0);
	}

	@Override
	public void addEnterpriseMaterial(Long userId, String reason, EnterpriseMaterial enterpriseMaterial)
			throws Exception {
		EnterpriseMaterialChange enterpriseMaterialChange = new EnterpriseMaterialChange();
		BeanUtils.copyProperties(enterpriseMaterial, enterpriseMaterialChange);
		// 1.新增 企业产品变更表
		enterpriseMaterialChange.setVerifyStatus("1");// 设置状态为待审批
		enterpriseMaterialChange.setChangeType("1");// 新增操作
		enterpriseMaterialChangeMapper.persist(enterpriseMaterialChange);
		// 3.新增审核记录表
		EnterpriseVerify enterpriseVerify = new EnterpriseVerify();

		enterpriseVerify.setChangeId(enterpriseMaterialChange.getChangeId());
		enterpriseVerify.setChangeType("1");// 新增操作
		enterpriseVerify.setDataType("5");//// 变更数据类型 1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
		enterpriseVerify.setChangeUserId(userId);
		enterpriseVerify.setChangeTime(new Date());
		enterpriseVerify.setEnterpriseId(enterpriseMaterialChange.getEnterpriseId());
		enterpriseVerify.setVerifyStatus("1");// 设置状态为待审批
		enterpriseVerify.setChangeReason(reason);// 变更原因
		enterpriseVerifyMapper.persist(enterpriseVerify);

	}

	@Override
	public void modifyEnterpriseMaterial(Long userId, String reason, EnterpriseMaterial enterpriseMaterial,
			String operType) throws Exception {
		EnterpriseMaterialChange enterpriseMaterialChange = new EnterpriseMaterialChange();
		BeanUtils.copyProperties(enterpriseMaterial, enterpriseMaterialChange);
		// 1.新增 企业产品变更表
		enterpriseMaterialChange.setVerifyStatus("1");// 设置状态为待审批
		enterpriseMaterialChange.setChangeType(operType + "");// 新增操作
		enterpriseMaterialChangeMapper.persist(enterpriseMaterialChange);
		// 3.新增审核记录表
		EnterpriseVerify enterpriseVerify = new EnterpriseVerify();

		enterpriseVerify.setChangeId(enterpriseMaterialChange.getChangeId());
		enterpriseVerify.setChangeType(operType + "");// 新增操作
		enterpriseVerify.setDataType("5");//// 变更数据类型 1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
		enterpriseVerify.setChangeUserId(userId);
		enterpriseVerify.setChangeTime(new Date());
		enterpriseVerify.setEnterpriseId(enterpriseMaterialChange.getEnterpriseId());
		enterpriseVerify.setVerifyStatus("1");// 设置状态为待审批
		enterpriseVerify.setChangeReason(reason);// 变更原因
		enterpriseVerifyMapper.persist(enterpriseVerify);

	}
}
