package com.otec.foodsafety.service.evaluate;

import com.otec.foodsafety.entity.system.SysResource;
import org.springframework.remoting.service.annotation.RemoteService;
import org.springframework.web.multipart.MultipartFile;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.alarm.AlarmExt;
import com.otec.foodsafety.entity.evaluate.ComplainEvaluate;

import java.util.List;

/**
 *  投诉评价服务类
 * 
 * @author Administrator
 *
 */
@RemoteService
public interface ComplainEvaluateService extends BaseService<ComplainEvaluate, Long> {
	/**
	 * 
	 * @param uploadUrl
	 * @param imageFolder
	 * @param resourceList
	 * @param userId
	 * @param operType         操作类型 1：添加 2修改
	 * @param complainEvaluate
	 * @throws Exception
	 */
	public void chargeComplainEvaluate(String uploadUrl, String imageFolder, List<SysResource> resourceList,
                                       Long userId, ComplainEvaluate complainEvaluate, String operType) throws Exception;
	/**
	 * 得到页面列表分页数据
	 */
	GridDataModel getGridDataModelByCondition(PageObject po);

	/**
	 * 根据Id得到企业产品信息
	 * 
	 * @param pId
	 * @return
	 */
	ComplainEvaluate getComplainEvaluateById(Long pId);
}
