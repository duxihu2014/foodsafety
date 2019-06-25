package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.AreaTree;
import com.otec.foodsafety.entity.system.SysArea;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;
import java.util.Map;

/**
 * 区域service
 * 
 * @author yy.w
 *
 */
@RemoteService
public interface SysAreaService extends BaseService<SysArea, Long> {

	/**
	 * 加载所有的区域
	 * 
	 * @param expanded
	 *            是否展开树菜单
	 * @param hideDisableMenus
	 *            是否隐藏无效的菜单（被禁用的）
	 * @return
	 */
	List<AreaTree> listTree(boolean expanded, boolean hideDisableMenus);

	/**
	 * 
	 * @param expanded
	 *            是否展开树菜单
	 * @param hideDisableMenus
	 *            是否隐藏无效的菜单（被禁用的）
	 * @return
	 */
	String listAreaTree(boolean expanded, boolean hideDisableMenus);

	/**
	 * 获得指定区域下的所有子节点
	 * 
	 * @param expanded
	 * @param hideDisableMenus
	 * @param areaId
	 * @return
	 */
    List<AreaTree> listAreaTree(boolean expanded, boolean hideDisableMenus, String areaId);

	/**
	 * 只查询前二级的区域
	 * 
	 * @param expanded
	 *            是否展开树菜单
	 * @param hideDisableMenus
	 *            是否隐藏无效的菜单（被禁用的）
	 * @return
	 */
	List<AreaTree> listTwoLevelTree(boolean expanded, boolean hideDisableMenus);

	void  updateAreaByIds(String[] menuIds,String status);

	/**
	 * 所有的地址(key值为4级，5级和6级的areaId)
	 */
	Map<String, String> getAddressInfo();

	/**
	 * 根据最后一级地址 返回完整的具体的地址
	 */
	Map<String, String> getAddressInfo(String areaId);

	/* 获取节点下面所有节点 */
	List listAllChildId(String id, List list);

	/* 获取节点下面的最亲子节点 **/
	List getChildAreas(Integer id);

	/* 从下往上查询 **/
	Long getParentArea(Long areaId, Integer customerLevel);

	List<SysArea> getParentsByAreaId(long l);
}
