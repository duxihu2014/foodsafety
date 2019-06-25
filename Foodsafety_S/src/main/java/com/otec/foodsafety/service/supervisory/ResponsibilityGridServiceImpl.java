package com.otec.foodsafety.service.supervisory;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGrid;
import com.otec.foodsafety.entity.supervisory.ResponsibilityGridRegion;
import com.otec.foodsafety.mapper.supervisory.PersonnelThreeMapper;
import com.otec.foodsafety.mapper.supervisory.ResponsibilityGridMapper;
import com.otec.foodsafety.mapper.supervisory.ResponsibilityGridRegionMapper;
import com.otec.foodsafety.util.AMapUtil;
import org.apache.ibatis.session.RowBounds;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ResponsibilityGridServiceImpl extends BaseServiceImpl<ResponsibilityGrid, Long> implements ResponsibilityGridService {
    @Autowired
    ResponsibilityGridMapper responsibilityGridMapper;
    @Autowired
    ResponsibilityGridRegionMapper  responsibilityGridRegionMapper;
    @Autowired
    PersonnelThreeMapper personnelThreeMapper;

    @Autowired
    public void setMapper(ResponsibilityGridMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public Integer getPageCount(Map<String, String> params) {
        return responsibilityGridMapper.getPageCount(params);
    }

    @Override
    public List<ResponsibilityGrid> getPage(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return responsibilityGridMapper.getPage(params,rowBounds);
    }

    @Override
    public void batchChangeStatus(Long[] ids, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("status", status);
        responsibilityGridMapper.batchChangeStatus(map);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(ResponsibilityGrid entity) {
        // 计算网格中心点坐标
        String center= AMapUtil.getCenter(entity.getMarkers());
        entity.setCenterLongitude(center.split(",")[0]);
        entity.setCenterLatitude(center.split(",")[1]);
        responsibilityGridMapper.persist(entity);
        if(entity.getMarkers()!=null && entity.getMarkers().size()>0){
            for(String[] position:entity.getMarkers()){
                ResponsibilityGridRegion region=new ResponsibilityGridRegion();
                region.setGridId(entity.getGridId());
                region.setRegionLongitude(position[0]);
                region.setRegionLatitude(position[1]);
                responsibilityGridRegionMapper.persist(region);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(ResponsibilityGrid entity) {
        // 计算网格中心点坐标
        String center= AMapUtil.getCenter(entity.getMarkers());
        entity.setCenterLongitude(center.split(",")[0]);
        entity.setCenterLatitude(center.split(",")[1]);
        responsibilityGridMapper.updateById(entity);
        if(entity.getMarkers()!=null && entity.getMarkers().size()>0){
            responsibilityGridRegionMapper.deleteByGridId (entity.getGridId());
            for(String[] position:entity.getMarkers()){
                ResponsibilityGridRegion region=new ResponsibilityGridRegion();
                region.setGridId(entity.getGridId());
                region.setRegionLongitude(position[0]);
                region.setRegionLatitude(position[1]);
                responsibilityGridRegionMapper.persist(region);
            }
        }
    }

    @Override
    public List<ResponsibilityGrid> getOtherGridsCoordinate(Long institutionId, Long id) {
        return responsibilityGridMapper.getOtherGridsCoordinate( institutionId,  id);
    }

    @Override
    public ResponsibilityGrid findById(Long id) {
        ResponsibilityGrid entity= responsibilityGridMapper.findById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gridId", entity.getGridId());
        List<ResponsibilityGridRegion> list=responsibilityGridRegionMapper.findEntitysByCondition(map);
        List<String[]> markers=new ArrayList<String[]>();
        for (ResponsibilityGridRegion region: list) {
            String[] postion={ region.getRegionLongitude(),region.getRegionLatitude()};
            markers.add(postion);
        }
        entity.setMarkers(markers);
        entity.setMonitorsName(personnelThreeMapper.findById(entity.getMonitors()).getPersonnelName());
        entity.setAssistantsName(personnelThreeMapper.findById(entity.getAssistants()).getPersonnelName());
        entity.setStandardsName(personnelThreeMapper.findById(entity.getStandards()).getPersonnelName());
        return entity;
    }

    @Override
    public List<ResponsibilityGrid> findEntitysByCondition(Map<String, Object> param){
        List<ResponsibilityGrid> responsibilityGridList = responsibilityGridMapper.findEntitysByCondition(param);
        List<ResponsibilityGrid> result = new ArrayList<>();
        for(ResponsibilityGrid entity :responsibilityGridList){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gridId", entity.getGridId());
            List<ResponsibilityGridRegion> list=responsibilityGridRegionMapper.findEntitysByCondition(map);
            List<String[]> markers=new ArrayList<String[]>();
            for (ResponsibilityGridRegion region: list) {
                String[] postion={ region.getRegionLongitude(),region.getRegionLatitude()};
                markers.add(postion);
            }
            entity.setMarkers(markers);
            entity.setMonitorsName(personnelThreeMapper.findById(entity.getMonitors()).getPersonnelName());
            entity.setAssistantsName(personnelThreeMapper.findById(entity.getAssistants()).getPersonnelName());
            entity.setStandardsName(personnelThreeMapper.findById(entity.getStandards()).getPersonnelName());
            result.add(entity);
        }
        return result;
    }
}
