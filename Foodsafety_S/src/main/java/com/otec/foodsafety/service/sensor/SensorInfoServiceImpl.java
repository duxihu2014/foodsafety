package com.otec.foodsafety.service.sensor;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.sensor.SensorInfo;
import com.otec.foodsafety.mapper.sensor.SensorInfoMapper;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SensorInfoServiceImpl  extends BaseServiceImpl<SensorInfo,Long> implements SensorInfoService{
	@Autowired
	private SensorInfoMapper mapper;

	@Autowired
	public  void  setMapper(SensorInfoMapper mapper){
		 setGenericMapper(mapper);
	}

	@Override
	public GridDataModel getGridDataByCondition(PageObject po) {
		Integer totalCount = mapper.countSensorInfoByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<SensorInfo> results = mapper.findSensorInfoByCondition(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount);
	}

	@Override
	public GridDataModel getListCurrentMonitor(PageObject po) {
		Integer totalCount = mapper.countSensorInfoByCondition(po.getCondition());
		RowBounds rowBounds = new RowBounds(po.getOffset(), po.getPageSize());
		List<Map<String, Object>> results = mapper.getListCurrentMonitor(po.getCondition(), rowBounds);
		return new GridDataModel(results, totalCount);
	}
}
