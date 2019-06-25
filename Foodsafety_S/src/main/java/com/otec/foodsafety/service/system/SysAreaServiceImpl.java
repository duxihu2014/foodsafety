package com.otec.foodsafety.service.system;

import com.otec.foodsafety.service.system.SysAreaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.AreaTree;
import com.otec.foodsafety.entity.system.SysArea;
import com.otec.foodsafety.mapper.system.SysAreaMapper;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.TreeUtil;

/**
 * 区域serviceImpl
 * 
 * @author yy.w
 *
 */
@Service
@Transactional
public class SysAreaServiceImpl extends BaseServiceImpl<SysArea, Long> implements SysAreaService {
	@Autowired
	private SysAreaMapper mapper;

	@Autowired
	public void setMapper(SysAreaMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public List<AreaTree> listTree(boolean expanded, boolean hideDisableMenus) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 查询显示所有的区域
		condition.put("orderByClause", "ADMINISTRATIVE_LEVEL , SORT_ID asc");
		condition.put("areaStatus", 1);
		List<SysArea> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);

		List<AreaTree> nodeList = new ArrayList<AreaTree>();
		for (SysArea menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus) {
				showNode = false;
			}
			if (showNode && menu.getpAreaId() != null) { // 过滤掉父节点不存在的脏数据
				AreaTree node = new AreaTree(menu);
				if ("1".equals(menu.getAdministrativeLevel())) {// 一级节点为根节点
					node.setRoot(true);
				}
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<AreaTree> tree = TreeUtil.getResult(nodeList);
		return tree;
	}

	@Override
	public String listAreaTree(boolean expanded, boolean hideDisableMenus) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 查询显示所有的菜单
		condition.put("orderByClause", "ADMINISTRATIVE_LEVEL , SORT_ID asc");
		condition.put("areaStatus", 1);
		List<SysArea> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);
		List<AreaTree> nodeList = new ArrayList<AreaTree>();
		for (SysArea menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus) {
				showNode = false;
			}
			if (showNode && menu.getpAreaId() != null) { // 过滤掉父节点不存在的脏数据
				AreaTree node = new AreaTree(menu);
				if ("1".equals(menu.getAdministrativeLevel())) {
					node.setRoot(true);
				}
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<AreaTree> tree = TreeUtil.getResult(nodeList);
		return JSONUtils.toJson(tree);
	}

	@Override
	public List<AreaTree> listAreaTree(boolean expanded, boolean hideDisableMenus, String areaId) {
		Map<String, Object> condition = new HashMap<String, Object>();
		List<String> listId = new ArrayList();
		listId = this.listAllChildId(areaId, listId);
		String areaIdStr = "";
		for (String id : listId) {
			areaIdStr = areaIdStr + "," + id;
		}
		areaIdStr = areaIdStr.substring(1, areaIdStr.length());
		condition.put("extendSql", " AREA_ID in (" + areaIdStr + ")");
		condition.put("orderByClause", "ADMINISTRATIVE_LEVEL , SORT_ID asc");
		condition.put("areaStatus", 1);

		List<SysArea> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
		// removeDisableNodes(allMenuList);
		List<AreaTree> nodeList = new ArrayList<AreaTree>();
		for (SysArea menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus) {
				showNode = false;
			}
			if (menu.getAreaId() == Integer.parseInt(areaId)) {
				AreaTree node = new AreaTree(menu);
				nodeList.add(node);
				node.setExpanded(expanded);
				node.setRoot(true);
			} else {
				if (showNode && menu.getpAreaId() != null) {
					AreaTree node = new AreaTree(menu);
					node.setExpanded(expanded);
					nodeList.add(node);
				}
			}

		}
		List<AreaTree> tree = TreeUtil.getResult(nodeList);
//		return JSONUtils.toJson(tree);
        return tree;
	}

	@Override
	public List<AreaTree> listTwoLevelTree(boolean expanded, boolean hideDisableMenus) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 查询显示所有的菜单
		condition.put("orderByClause", "ADMINISTRATIVE_LEVEL , SORT_ID asc");
//		condition.put("areaStatus", 1);
		condition.put("extendSql", " ADMINISTRATIVE_LEVEL <3");
		List<SysArea> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);
		List<AreaTree> nodeList = new ArrayList<AreaTree>();
		for (SysArea menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus) {
				showNode = false;
			}
			if (showNode && menu.getpAreaId() != null) { // 过滤掉父节点不存在的脏数据
				AreaTree node = new AreaTree(menu);
				if ("1".equals(menu.getAdministrativeLevel())) {
					node.setRoot(true);
				}

				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}

		List<AreaTree> tree = TreeUtil.getResult(nodeList);
		return tree;
	}

	@Override
	public void updateAreaByIds(String[] menuIds,String status) {
		for (String menuId : menuIds) {
			updateAreaById(Long.parseLong(menuId),status);
		}
	}

	@Override
	public List listAllChildId(String id, List list) {
		if (null != id) {
			list.add(id);
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("pAreaId", id);
		condition.put("areaStatus", 1);
		List<SysArea> areaList = mapper.findEntitysByCondition(condition);
		for (SysArea area : areaList) {
			listAllChildId(area.getAreaId().toString(), list);
		}
		return list;
	}

	@Override
	public Map<String, String> getAddressInfo() {
		String addressInfo;
		Map<String, String> addressMap = new HashMap<String, String>();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("administrativeLevel", 4);
		List<SysArea> list2 = mapper.findEntitysByCondition(condition);
		for (SysArea sa : list2) {
			StringBuffer bf2 = new StringBuffer();
			ArrayList listStirng2 = new ArrayList();
			listStirng2 = findParentInfo(sa, listStirng2);
			Collections.reverse(listStirng2);
			for (int i = 0; i < listStirng2.size(); i++) {
				bf2.append(listStirng2.get(i).toString());
			}
			addressMap.put(sa.getAreaId().toString(), bf2.toString());
		}

		condition.put("administrativeLevel", 5);
		List<SysArea> list3 = mapper.findEntitysByCondition(condition);
		for (SysArea sa : list3) {
			StringBuffer bf3 = new StringBuffer();
			ArrayList listStirng3 = new ArrayList();
			listStirng3 = findParentInfo(sa, listStirng3);
			Collections.reverse(listStirng3);
			for (int i = 0; i < listStirng3.size(); i++) {
				bf3.append(listStirng3.get(i).toString());
			}
			addressMap.put(sa.getAreaId().toString(), bf3.toString());
		}

		condition.put("administrativeLevel", 6);
		List<SysArea> list = mapper.findEntitysByCondition(condition);
		for (SysArea sa : list) {
			StringBuffer bf = new StringBuffer();
			ArrayList listStirng = new ArrayList();
			listStirng = findParentInfo(sa, listStirng);
			Collections.reverse(listStirng);
			for (int i = 0; i < listStirng.size(); i++) {
				bf.append(listStirng.get(i).toString());
			}
			addressMap.put(sa.getAreaId().toString(), bf.toString());
		}

		return addressMap;
	}

	/**
	 * 根据最后一级地址 返回完整的具体的地址
	 */
	@Override
	public Map<String, String> getAddressInfo(String areaId) {
		String addressInfo;
		Map<String, String> addressMap = new HashMap<String, String>();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("areaId", areaId);
		List<SysArea> list2 = mapper.findEntitysByCondition(condition);
		for (SysArea sa : list2) {
			StringBuffer bf2 = new StringBuffer();
			ArrayList listStirng2 = new ArrayList();
			listStirng2 = findParentInfo(sa, listStirng2);
			Collections.reverse(listStirng2);
			for (int i = 0; i < listStirng2.size(); i++) {
				bf2.append(listStirng2.get(i).toString());
			}
			addressMap.put(sa.getAreaId().toString(), bf2.toString());
		}
		return addressMap;
	}

	@Override
	public List getChildAreas(Integer id) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("pAreaId", id);
		condition.put("areaStatus", 1);
		List<SysArea> areaList = mapper.findEntitysByCondition(condition);
		return areaList;
	}

	@Override
	public Long getParentArea(Long areaId, Integer customerLevel) {
		SysArea area = mapper.findById(Long.valueOf(areaId));
		// 从下往上查询,直到查到节点的级别与用户级别相同,终止递归
		if (area.getAdministrativeLevel().equals(customerLevel)) {
			return areaId;
		} else {
			return getParentArea(area.getpAreaId(), customerLevel);
		}
	}

	@Override
	public List<SysArea> getParentsByAreaId(long areaId) {
		return  mapper.getParentsByAreaId(areaId);
	}

	/**
	 * 查找传入area的父类信息
	 * @param sa
	 * @param listStirng
	 * @return
	 */
	public ArrayList findParentInfo(SysArea sa, ArrayList listStirng) {
		listStirng.add(sa.getAreaName());
		SysArea list = new SysArea();
		/**
		 * 如果当前area的级别不是1级的话，则查找其父类area信息，反之,当前级别是1级，则查找当前area信息
		 */
		if (!"1".equals(sa.getAdministrativeLevel())) {
			list = mapper.findById(sa.getpAreaId());
			findParentInfo(list, listStirng);
		} else {
			//list = mapper.findById(Long.valueOf(1));
			list = mapper.findById(Long.valueOf(sa.getAreaId()));
		}
		return listStirng;
	}

	/**
	 * 递归第删除菜单和子菜单
	 * 
	 * @param areaId
	 */
	private void updateAreaById(long areaId,String status) {

		SysArea sysArea=mapper.findById(areaId);
		sysArea.setAreaStatus(status);
		mapper.updateById(sysArea);

		Map<String, Object> condition = new HashMap<String, Object>();
		 condition.put("pAreaId", areaId);
		//condition.put("areaId", areaId);
		List<SysArea> list = mapper.findEntitysByCondition(condition); // 查询当前菜单和下一层子菜单
		if (list != null && list.size() > 0) {
			for (SysArea area : list) {
				updateAreaById(area.getAreaId(),status);
			}
		}
	}

	/**
	 * 删除父节点不存在或父节点被禁用的子节点
	 * 
	 * @param allMenuList
	 */
	private void removeDisableNodes(List<SysArea> allMenuList) {
		for (int i = 0; i < allMenuList.size(); i++) {
			SysArea menu = allMenuList.get(i);
			checkParentNode(menu, allMenuList);
		}
	}

	private void checkParentNode(SysArea menu, List<SysArea> menuList) {
		boolean hasParent = false;
		long pid = menu.getpAreaId();
	/*	if (menu.getAreaId() == 1) {
			return;
		}*/
		if (pid == 0) {
			return;
		}
		for (SysArea m : menuList) {
			if (m.getAreaId() == pid) {
				hasParent = true;
			}
			if (hasParent && !"1".equals(m.getAreaStatus())) {
				menu.setAreaStatus(0 + ""); // 附菜单被禁用，自动禁用子菜单
			}
			if (hasParent) {
				break;
			}
		}
		if (!hasParent) {
			menu.setpAreaId(null); // 屏蔽掉父节点不存在的脏数据
		}
	}

}
