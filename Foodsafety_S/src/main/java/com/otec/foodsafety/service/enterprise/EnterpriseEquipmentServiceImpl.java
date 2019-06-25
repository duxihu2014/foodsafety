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
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipment;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseEquipmentExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseProduct;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductChange;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductExt;
import com.otec.foodsafety.entity.enterprise.EnterpriseVerify;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.enterprise.EnterpriseEquipmentChangeMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseEquipmentMapper;
import com.otec.foodsafety.mapper.enterprise.EnterpriseVerifyMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;

@Service
@Transactional
public class EnterpriseEquipmentServiceImpl extends BaseServiceImpl<EnterpriseEquipment, Long>
		implements EnterpriseEquipmentService {

	@Autowired
	EnterpriseEquipmentChangeMapper enterpriseEquipmentChangeMapper;

	@Autowired
	EnterpriseEquipmentMapper enterpriseEquipmentMapper;

	@Autowired
	private EnterpriseVerifyMapper enterpriseVerifyMapper;
	@Autowired
	private SysResourceMapper sysResourceMapper;

	@Autowired
	public void setMapper(EnterpriseEquipmentMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public EnterpriseEquipmentChange findByChangeId(Long id) {
		// TODO Auto-generated method stub
		return enterpriseEquipmentChangeMapper.findById(id);
	}

	@Override
	public void verifyEnterpriseCertificate(EnterpriseEquipment enterpriseEquipment, EnterpriseEquipmentChange eec,
			EnterpriseVerify enterpriseVerify) {
		// 1.更新审核表
		enterpriseVerifyMapper.updateById(enterpriseVerify);
		if (enterpriseVerify.getVerifyStatus().equals("2")) {// 审核通过
			if ("3".equals(eec.getChangeType())) {// 删除操作
				enterpriseEquipment.setEquipmentStatus("0");
				enterpriseEquipmentMapper.persist(enterpriseEquipment);
			} else if ("2".equals(eec.getChangeType())) {// 修改操作
				BeanUtils.copyProperties(eec, enterpriseEquipment);
				enterpriseEquipmentMapper.updateById(enterpriseEquipment);
			} else if ("1".equals(eec.getChangeType())) {// 新增操作
				BeanUtils.copyProperties(eec, enterpriseEquipment);
				enterpriseEquipment.setEquipmentStatus("1");
				enterpriseEquipmentMapper.persist(enterpriseEquipment);
				eec.setEquipmentId(enterpriseEquipment.getEquipmentId());
			}
			eec.setVerifyStatus("2");

		} else {// 审核不通过
			eec.setVerifyStatus("3");
		}
		enterpriseEquipmentChangeMapper.updateById(eec);

	}

	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {
		// 得到所有有效的设备信息
		Integer totalCount = enterpriseEquipmentMapper.countEnterpriseEquipmentByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<EnterpriseEquipmentExt> results = enterpriseEquipmentMapper
				.findEnterpriseEquipmentByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public EnterpriseEquipmentExt getEnterpriseEquipmentById(Long productId) {
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("productId", productId);
		List<EnterpriseEquipmentExt> item = enterpriseEquipmentMapper.findEnterpriseEquipmentByCondition(cond);

		return item == null ? null : item.get(0);
	}

	@Override
	public void addEnterpriseEquipment(Long userId, String reason, EnterpriseEquipment enterpriseEquipment)
			throws Exception {

		EnterpriseEquipmentChange enterpriseEquChange = new EnterpriseEquipmentChange();
		BeanUtils.copyProperties(enterpriseEquipment, enterpriseEquChange);
		// 1.新增 企业产品变更表
		enterpriseEquChange.setVerifyStatus("1");// 设置状态为待审批
		enterpriseEquChange.setChangeType("1");// 新增操作
		enterpriseEquipmentChangeMapper.persist(enterpriseEquChange);
		// 2.新增审核记录表
		EnterpriseVerify enterpriseVerify = new EnterpriseVerify();

		enterpriseVerify.setChangeId(enterpriseEquChange.getChangeId());
		enterpriseVerify.setChangeType("1");// 新增操作
		enterpriseVerify.setDataType("6");//// 变更数据类型 1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
		enterpriseVerify.setChangeUserId(userId);
		enterpriseVerify.setChangeTime(new Date());
		enterpriseVerify.setEnterpriseId(enterpriseEquChange.getEnterpriseId());
		enterpriseVerify.setVerifyStatus("1");// 设置状态为待审批
		enterpriseVerify.setChangeReason(reason);// 变更原因
		enterpriseVerifyMapper.persist(enterpriseVerify);
	}

	@Override
	public void modifyEnterpriseEquipment(Long userId, String reason, EnterpriseEquipment enterpriseEquipment,
			String operType) throws Exception {
		EnterpriseEquipmentChange enterpriseEquChange = new EnterpriseEquipmentChange();
		BeanUtils.copyProperties(enterpriseEquipment, enterpriseEquChange);
		// 1.新增 企业产品变更表
		enterpriseEquChange.setVerifyStatus("1");// 设置状态为待审批
		enterpriseEquChange.setChangeType(operType + "");// 操作
		enterpriseEquipmentChangeMapper.persist(enterpriseEquChange);
		// 2.新增审核记录表
		EnterpriseVerify enterpriseVerify = new EnterpriseVerify();

		enterpriseVerify.setChangeId(enterpriseEquChange.getChangeId());
		enterpriseVerify.setChangeType(operType + "");
		enterpriseVerify.setDataType("6");//// 变更数据类型 1.基本；2.证照；3.规模；4.产品；5.原材料；6.设备
		enterpriseVerify.setChangeUserId(userId);
		enterpriseVerify.setChangeTime(new Date());
		enterpriseVerify.setEnterpriseId(enterpriseEquChange.getEnterpriseId());
		enterpriseVerify.setVerifyStatus("1");// 设置状态为待审批
		enterpriseVerify.setChangeReason(reason);// 变更原因
		enterpriseVerifyMapper.persist(enterpriseVerify);

	}
}
