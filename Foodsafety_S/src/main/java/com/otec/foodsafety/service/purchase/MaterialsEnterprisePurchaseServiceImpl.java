package com.otec.foodsafety.service.purchase;

import com.otec.foodsafety.entity.enterprise.EnterpriseMaterial;
import com.otec.foodsafety.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.purchase.MaterialsEnterprisePurchase;
import com.otec.foodsafety.entity.purchase.MaterialsInspectionReport;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.purchase.MaterialsEnterprisePurchaseMapper;
import com.otec.foodsafety.mapper.purchase.MaterialsInspectionReportMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.CommonUtil;
import com.otec.foodsafety.util.vo.ComboData;

@Service
@Transactional
public class MaterialsEnterprisePurchaseServiceImpl extends BaseServiceImpl<MaterialsEnterprisePurchase, Long>
		implements MaterialsEnterprisePurchaseService {
	@Autowired
	MaterialsEnterprisePurchaseMapper mapper;
	@Autowired
	private SysResourceMapper sysResourceMapper;
	@Autowired
	MaterialsInspectionReportMapper inspectionReportMapper;

	@Autowired
	public void setMapper(MaterialsEnterprisePurchaseMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {
		// 得到所有有效的设备信息
		Integer totalCount = mapper.countMaterialsEnterprisePurchaseByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<MaterialsEnterprisePurchase> results = mapper.findMaterialsEnterprisePurchaseByCondition(po.getCondition(),
				rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public MaterialsEnterprisePurchase getMaterialsEnterprisePurchaseById(Map<String, Object> condition) {
		return mapper.getMaterialsEnterprisePurchaseById(condition);
	}

	@Override
	public List<ComboData> findPurchaseComboByCondition(Map<String, Object> condition) {
		List<MaterialsEnterprisePurchase> datalist = mapper.findEntitysByCondition(condition);

		List<ComboData> params = new ArrayList<ComboData>();
		for (MaterialsEnterprisePurchase item : datalist) {
			ComboData cData = new ComboData(item.getPurchaseId() + "", item.getPurchaseId() + "");
			params.add(cData);
		}
		return params;
	}

	@Override
	public void add(MaterialsEnterprisePurchase materialEnterprisePurchase, SysResource inspectionReportSource) {
		// 创建采购记录
		mapper.persist(materialEnterprisePurchase);
		// 创建质检信息记录
		if (inspectionReportSource != null) {
			MaterialsInspectionReport materialsInspectionReport = new MaterialsInspectionReport();
			sysResourceMapper.persist(inspectionReportSource);
			setInspectionReport(materialsInspectionReport, materialEnterprisePurchase,
					inspectionReportSource.getResourceId());
			inspectionReportMapper.persist(materialsInspectionReport);
		}
	}

	@Override
	public void update(MaterialsEnterprisePurchase materialEnterprisePurchase, SysResource inspectionReportSource)
			throws Exception {
		MaterialsEnterprisePurchase entity = mapper.findById(materialEnterprisePurchase.getPurchaseId().longValue());
		MaterialsInspectionReport reportEntity = inspectionReportMapper
				.findByPurcharseId(materialEnterprisePurchase.getPurchaseId().longValue());
		// 更新图片数据到数据库
		if (inspectionReportSource != null && inspectionReportSource.getResourcePath() != null) {
			sysResourceMapper.persist(inspectionReportSource);

			if (reportEntity == null) {
				reportEntity = new MaterialsInspectionReport();
				sysResourceMapper.persist(inspectionReportSource);

				setInspectionReport(reportEntity, materialEnterprisePurchase, inspectionReportSource.getResourceId());
				inspectionReportMapper.persist(reportEntity);
			} else {
				setInspectionReport(reportEntity, materialEnterprisePurchase, inspectionReportSource.getResourceId());
				inspectionReportMapper.updateById(reportEntity);
			}
		} else if (inspectionReportSource != null && inspectionReportSource.getResourcePath() == null) {
			if (reportEntity != null) {
				// 删除图片
				if (reportEntity.getInspectionReportPhoto() != null) {
					inspectionReportSource = sysResourceMapper
							.findById(reportEntity.getInspectionReportPhoto().longValue());
					inspectionReportSource.setResourceStatus("0");
					sysResourceMapper.updateById(inspectionReportSource);

					setInspectionReport(reportEntity, materialEnterprisePurchase, null);
//					inspectionReportMapper.updateById(reportEntity);
					inspectionReportMapper.removeById(reportEntity.getInspectionReportId().longValue());
				}
			}

		} else {
		}

		// 更新供货商信息
		CommonUtil.copyBean(materialEnterprisePurchase, entity, false);
		mapper.updateById(entity);
	}

	@Override
	public void deleteById(Long purchaseId) {
		// 删除对应的质检信息
		inspectionReportMapper.removeByPurchaseId(purchaseId);
		// 删除采购记录
		mapper.removeById(purchaseId);
		

	}

	/**
	 * 设置质检信息
	 * 
	 * @param report                     质检报告信息
	 * @param materialEnterprisePurchase 采购记录信息
	 * @param resourceId                 图片资源id
	 * @param OpFlag                     1 添加，2修改
	 */
	private void setInspectionReport(MaterialsInspectionReport report,
			MaterialsEnterprisePurchase materialEnterprisePurchase, Long resourceId) {
		report.setInspectionReportPhoto(resourceId == null ? null : resourceId.intValue());
		report.setMaterialId(materialEnterprisePurchase.getMaterialId());
		report.setMaterialName(materialEnterprisePurchase.getMaterialName());
		report.setMaterialCategory(materialEnterprisePurchase.getMaterialCategory());
		report.setBarcodeCoding(materialEnterprisePurchase.getBarcodeCoding());
		report.setProductionDate(materialEnterprisePurchase.getProductionDate());
		report.setValidDate(materialEnterprisePurchase.getValidDate());

		report.setPurchaseId(materialEnterprisePurchase.getPurchaseId());
//		return report;
	}

	@Override
	public List<MaterialsEnterprisePurchase> getMaterialNewRecordPage(Map<String, String> params, int start, int pageSize) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return mapper.getMaterialNewRecordPage(params,rowBounds);
	}

	@Override
	public Integer getMaterialNewRecordCount(Map<String, String> params) {
		return mapper.getMaterialNewRecordCount(params);
	}

	@Override
	public void add(MaterialsEnterprisePurchase purchase) {
		mapper.persist(purchase);
		if (!StringUtils.isEmpty(purchase.getInspectionReportPhoto()) && Integer.parseInt(purchase.getInspectionReportPhoto())>0){
			MaterialsInspectionReport materialsInspectionReport = new MaterialsInspectionReport();
			setInspectionReport(materialsInspectionReport, purchase, Long.parseLong(purchase.getInspectionReportPhoto()));
			inspectionReportMapper.persist(materialsInspectionReport);
		}

	}

	@Override
	public void update(MaterialsEnterprisePurchase materialEnterprisePurchase) throws Exception {
		MaterialsEnterprisePurchase entity = mapper.findById(materialEnterprisePurchase.getPurchaseId().longValue());
		MaterialsInspectionReport reportEntity = inspectionReportMapper
				.findByPurcharseId(materialEnterprisePurchase.getPurchaseId().longValue());
		// 更新图片数据到数据库
		if (reportEntity != null) {
			if (!reportEntity.getInspectionReportPhoto().toString().equals(materialEnterprisePurchase.getInspectionReportPhoto())) {
				// 如果传了新的图片  删除之前的图片
				SysResource inspectionReportSource = sysResourceMapper.findById(reportEntity.getInspectionReportPhoto().longValue());
				inspectionReportSource.setResourceStatus("0");
				sysResourceMapper.updateById(inspectionReportSource);
  			//	inspectionReportMapper.removeById(reportEntity.getInspectionReportId().longValue());
			}
			//更新图片数据
			setInspectionReport(reportEntity, materialEnterprisePurchase, null);
			inspectionReportMapper.updateById(reportEntity);
		}else{
			if(!StringUtils.isEmpty(materialEnterprisePurchase.getInspectionReportPhoto())&& Integer.parseInt(materialEnterprisePurchase.getInspectionReportPhoto())>0){
				MaterialsInspectionReport materialsInspectionReport = new MaterialsInspectionReport();
				setInspectionReport(materialsInspectionReport, materialEnterprisePurchase, Long.parseLong(materialEnterprisePurchase.getInspectionReportPhoto()));
				inspectionReportMapper.persist(materialsInspectionReport);
			}
		}
		// 更新供货商信息
		CommonUtil.copyBean(materialEnterprisePurchase, entity, false);
		mapper.updateById(entity);
	}
}
