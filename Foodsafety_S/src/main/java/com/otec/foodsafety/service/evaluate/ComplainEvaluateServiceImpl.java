package com.otec.foodsafety.service.evaluate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.evaluate.ComplainEvaluate;
import com.otec.foodsafety.mapper.evaluate.ComplainEvaluateMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.ImageUtil;


@Service
@Transactional
public class ComplainEvaluateServiceImpl extends BaseServiceImpl<ComplainEvaluate, Long>
		implements ComplainEvaluateService {
	@Autowired
	ComplainEvaluateMapper mapper;
	@Autowired
	SysResourceMapper sysResourceMapper;

	@Autowired
	public void setMapper(ComplainEvaluateMapper mapper) {
		setGenericMapper(mapper);
	}

//	@Override
	public GridDataModel getGridDataModelByCondition(PageObject po) {
		// 得到所有有效的设备信息
		Integer totalCount = mapper.countComplainEvaluateBycondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<ComplainEvaluate> results = mapper.findComplainEvaluateBycondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount.intValue());
	}

	@Override
	public void chargeComplainEvaluate(String uploadUrl, String imageFolder, MultipartFile[] multipartFileArr,
			Long userId, ComplainEvaluate complainEvaluate, String operType) throws Exception {

		// 1.保存或更新图片资源。
		List<String> resIdList = new ArrayList<String>();
		if (multipartFileArr != null && multipartFileArr.length > 0) {
			for (int i = 0; i < multipartFileArr.length; i++) {
				MultipartFile file = multipartFileArr[i];
				// 保存文件
				Long resId = ImageUtil.ImageUpload(uploadUrl, imageFolder, file, sysResourceMapper);
				System.out.println("---[testd]---" + resId);
				resIdList.add(resId + "");
			}
		}
		if (resIdList.size() > 0) {
			System.out.println(StringUtils.join(resIdList, ","));
			complainEvaluate.setImgPath(StringUtils.join(resIdList, ","));
		}
		// 2.新增审核记录表
		if ("1".equals(operType)) {// 新增
			complainEvaluate.setCreateTime(new Date());
			complainEvaluate.setStatus("1");// 已提交
			super.persist(complainEvaluate);
		} else {// 修改
			super.mergeById(complainEvaluate);
		}

	}
	
	@Override
	public ComplainEvaluate getComplainEvaluateById(Long pId) {
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("complainEvaluateId", pId);
		List<ComplainEvaluate> item = mapper.findComplainEvaluateBycondition(cond);

		return item == null ? null : item.get(0);
	}
	@Override
	public ComplainEvaluate findById(Long pId) {
		return getComplainEvaluateById(pId);
	}
}
