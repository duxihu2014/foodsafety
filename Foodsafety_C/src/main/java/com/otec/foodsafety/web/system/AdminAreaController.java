package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.AreaTree;
import com.otec.foodsafety.entity.system.SysArea;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.web.VueBaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 区域controller
 * 
 * @author yy.w
 * 
 */
@RestController
@RequestMapping("api/admin/area")
public class AdminAreaController extends VueBaseController<SysAreaService, SysArea, Long> {

	@Autowired
	private SysAreaService areaService;

	private static final Logger log = LoggerFactory.getLogger(AdminAreaController.class);

	/**
	 * 得到区域的树型结果数据
	 * 
	 * @param menuName
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<AreaTree> getTree(@RequestParam(required = false) String menuName) {
		return areaService.listTree(true, false);
	}

    @RequestMapping(value = "/childTree", method = RequestMethod.GET)
    public List<AreaTree> childTree(@RequestParam(value = "areaId", required = false) String areaId) {
		if (StringUtils.isNotBlank(areaId))
        	return areaService.listAreaTree(true, false, areaId);
		else
			return areaService.listTree(true, false);
    }

	/**
	 * 得到2级树结构
	 * 
	 * @param menuName
	 * @return
	 */
	@RequestMapping(value = "/twoLevelTree", method = RequestMethod.GET)
	public List<AreaTree> getTwoLevelTree(@RequestParam(required = false) String menuName) {
		return areaService.listTwoLevelTree(true, false);
	}

	/**
	 * 添加
	 */
	@Override
	public ObjectRestResponse<SysArea> add(@RequestBody SysArea entity) {
		entity.setCreateTime(new Date());
		return super.add(entity);
	}

	/**
	 * 修改
	 */
	@Override
	public ObjectRestResponse<SysArea> update(@RequestBody SysArea entity) {
		entity.setUpdateTime(new Date());
		return super.update(entity);
	}

	@RequestMapping(value = "revocer/{id}", method = RequestMethod.PUT)
	public ObjectRestResponse<SysArea> revocer(@PathVariable String id) {
//		SysArea sysArea = areaService.findById(id);
//		sysArea.setAreaStatus("1");// 默认有效
//		return super.update(sysArea);
		try {
			areaService.updateAreaByIds(id.split(","),"1");
			return new ObjectRestResponse<SysArea>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysArea>().rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public ObjectRestResponse<SysArea> delete(@PathVariable String id) {
		try {
			areaService.updateAreaByIds(id.split(","),"0");
			return new ObjectRestResponse<SysArea>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<SysArea>().rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}

	/**
	 * 获取省级MAP
	 * */
	@RequestMapping(value = "/getProvinceMap", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String,Object>> getProvinceMap(){

		Map<String,Object> map2=new HashMap<String, Object>();
		map2.put("areaStatus", "1");
		map2.put("administrativeLevel", "1");
		List<SysArea>  sysAreaList=areaService.findEntitysByCondition(map2);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>(sysAreaList.size());
		for(SysArea sysArea:sysAreaList){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("value" , String.valueOf(sysArea.getAreaId()));
			map.put("text" ,  sysArea.getAreaName());
			list.add(map);
		}
		return  new ObjectRestResponse<List<Map<String,Object>>>().rel(true).data(list);
	}
	
	/**
	 * 获取子类区域Map
	 * */
	@RequestMapping(value = "/getChildAreaMap/{id}", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String,Object>> getChildAreaMap(@PathVariable("id") String id){
		Map<String,Object> map2=new HashMap<String, Object>();
		map2.put("pAreaId", id);
		map2.put("areaStatus", "1");
		List<SysArea>  sysAreaList=areaService.findEntitysByCondition(map2);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>(sysAreaList.size());
		for(SysArea sysArea:sysAreaList){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("value" , String.valueOf(sysArea.getAreaId()));
			map.put("text" ,  sysArea.getAreaName());
			list.add(map);
		}
		return  new ObjectRestResponse<List<Map<String,Object>>>().rel(true).data(list);
	}
	
	/**
	 * 获取全部区域
	 * */
	@RequestMapping(value = "/getAreaMap", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String,Object>> getAreaMap(){

		Map<String,Object> map2=new HashMap<String, Object>();
		map2.put("areaStatus", "1");
		//map2.put("administrativeLevel", "1");
		List<SysArea>  sysAreaList=areaService.findEntitysByCondition(map2);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>(sysAreaList.size());
		for(SysArea sysArea:sysAreaList){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("value" , String.valueOf(sysArea.getAreaId()));
			map.put("text" ,  sysArea.getAreaName());
			list.add(map);
		}
		return  new ObjectRestResponse<List<Map<String,Object>>>().rel(true).data(list);
	}

	/**
	 * 获取父级地区
	 * */
	@RequestMapping(value = "/getParentsByAreaId/{id}", method = RequestMethod.GET)
	public ObjectRestResponse<Map<String,Object>> getParentsByAreaId(@PathVariable("id") String id){

		List<SysArea>  sysAreaList=areaService.getParentsByAreaId(Long.parseLong(id));
		return  new ObjectRestResponse<List<Map<String,Object>>>().rel(true).data(sysAreaList);
	}

}
