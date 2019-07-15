package com.otec.foodsafety.service.purchase;

import java.util.List;
import java.util.Map;

import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.purchase.Materials;

@RemoteService
public interface MaterialsService extends BaseService<Materials, Long> {

	List<Materials> findMaterialsByCondition(Map<String, Object> condition);
	/**
	 * 根据原料id得到原料的信息
	 * 
	 * @param condition
	 * @return
	 */
	Materials findMaterialByCondition(Map<String, Object> condition);


	void add(Materials entity, SysResource resource, String uploadUrl, String imageFolder) throws  Exception;

	Materials findByMaterialId(Long id);

	/**
	 * 批量修改原料使用状态
	 * @param ids
	 * @param status
	 */
	void batchChangeStatus(Long[] ids, String status);


	void updateMaterial(Materials entity, SysResource resource)throws  Exception;

	Integer getPageCount(Map<String, String> params);

	List<Materials> getPage(Map<String, String> params, int offset, int pageSize);
}
