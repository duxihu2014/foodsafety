package com.otec.foodsafety.web;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class VueBaseController<Biz extends BaseService<Entity, PK>, Entity, PK> {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected Biz biz;

	/**
	 * 公共方法，添加实体
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<Entity> add(@RequestBody Entity entity) {
//		biz.persist(entity);
		biz.persist(preHandler(entity));
		return new ObjectRestResponse<Entity>().rel(true).data(entity);
	}

	/**
	 * 公共方法，根据主键查询实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<Entity> get(@PathVariable PK id) {
		return new ObjectRestResponse<Entity>().rel(true)
				.data(biz.findById(id));
	}

	/**
	 * 公共方法，修改实体
	 *
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjectRestResponse<Entity> update(@RequestBody Entity entity) {
//		biz.updateById(entity);
		biz.updateById(preHandler(entity));
		return new ObjectRestResponse<Entity>().rel(true);
	}

	/**
	 * 公共方法，根据主键删除实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<Entity> remove(@PathVariable PK id) {
		try {
			biz.removeById(id);
			return new ObjectRestResponse<Entity>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<Entity>().rel(false);
			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	/**
	 * 公共方法，查询所有
	 *
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public List<Entity> all(@RequestParam Map<String, Object> params) {
		return biz.findEntitysByCondition(params);
	}

	/**
	 * 分页查询全部
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);
		return model;

	}

	protected PageObject getPageObject(Map<String, String> params) {
		PageObject pageObject = new PageObject();
		String currPageStr = params.get("page");
		if (StringUtils.isBlank(currPageStr)) {
			currPageStr = "1";
		}
		String pageSizeStr = params.get("limit");
		String property = params.get("sort");// 排序字段
		String orderByClause = null;
		if (StringUtils.isNotBlank(property)) {
			orderByClause = getColumn(property) + " " + params.get("order"); // 升序，降序
		}

		try {
			pageObject.setCurrPage(Integer.valueOf(currPageStr));
			pageObject.setPageSize(Integer.valueOf(pageSizeStr));
		} catch (NumberFormatException e) {
			pageObject.setCurrPage(1);
			pageObject.setPageSize(Integer.MAX_VALUE);
		}
		pageObject.addCondition("orderByClause", orderByClause);
		return pageObject;
	}

	private String getColumn(String property) {
		StringBuffer buffer = new StringBuffer(property);
		for (int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if (Character.isUpperCase(c)) {
				buffer.insert(i++, '_');
			}
		}
		return buffer.toString();
	}
	/**
	 * 实体预处理
	 * 
	 * @param entity
	 * @return
	 */
	public Entity preHandler(Entity entity) {
		return entity;
	}
}
