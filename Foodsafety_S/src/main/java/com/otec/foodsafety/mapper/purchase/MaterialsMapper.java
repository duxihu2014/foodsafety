package com.otec.foodsafety.mapper.purchase;

import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.purchase.Materials;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MaterialsMapper extends GenericMapper<Materials, Long> {

	void batchChangeStatus(Map<String, Object> map);

	List<Materials> findMaterialsByCondition(Map<String, Object> map);
	 
    List<Materials> getPage(Map<String, String> params, RowBounds rowBounds);

    Integer getPageCount(Map<String, String> params);

    Materials findMaterialsById(Long materialId);
}
