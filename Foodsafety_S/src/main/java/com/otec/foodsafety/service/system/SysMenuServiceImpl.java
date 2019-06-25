package com.otec.foodsafety.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.otec.foodsafety.service.system.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.system.MenuTree;
import com.otec.foodsafety.entity.system.SysMenu;
import com.otec.foodsafety.mapper.system.SysMenuMapper;
import com.otec.foodsafety.mapper.system.SysRoleMenuMapper;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.TreeUtil;

@Service
@Transactional
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu, Long>
		implements SysMenuService {

	@Autowired
	private SysMenuMapper mapper;

	@Autowired
	private SysRoleMenuMapper roleMenuMapper;

	@Autowired
	public void setMapper(SysMenuMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public int removeById(Long id) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("pMenuId", id);
		int flag = mapper.deleteByCondition(condition);
		return flag;
	}

	/**
	 * 删除选中的菜单，并删除关联表数据（菜单绑定角色）
	 */
	public void deleteMenuByIds(String[] menuIds) {
		for (String menuId : menuIds) {
			int count = 0;
			deleteMenuById(Long.parseLong(menuId), count);
		}
	}

	/**
	 * 递归第删除菜单和子菜单
	 * 
	 * @param menuId
	 */
	private void deleteMenuById(long menuId, int count) {
		if (count >= 5) {
			return;
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("pMenuId", menuId);
		List<SysMenu> list = mapper.findEntitysByCondition(condition); // 查询当前菜单和下一层子菜单
		count++;
		if (list != null && list.size() > 0) {
			for (SysMenu menu : list) {
				if (menu.getMenuId() == menuId) { // 删除当前菜单
					mapper.removeById(menuId);
					// 删除菜单和角色的绑定关系
					Map<String, Object> relativeCondition = new HashMap<String, Object>();
					relativeCondition.put("menuId", menuId);
					roleMenuMapper.deleteByCondition(relativeCondition);
				} else if (menu.getMenuPid() == menuId) { // 子菜单，递归删除
					deleteMenuById(menu.getMenuId(), count);
				}
			}
		}
	}

	@Override
	public String listTree(boolean expanded, boolean hideDisableMenus) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 查询显示所有的菜单，包括被禁用的
		condition.put("orderByClause", "MENU_LEVEL,MENU_ORDER asc");
		List<SysMenu> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);
		List<MenuTree> nodeList = new ArrayList<MenuTree>();
		for (SysMenu menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus && menu.getMenuStatus() != 1) {
				showNode = false;
			}
			if (showNode && menu.getMenuPid() != null) { // 过滤掉父节点不存在的脏数据
				MenuTree node = new MenuTree(menu);
				if (menu.getMenuId() == 1) {
					node.setRoot(true);
				}
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<MenuTree> tree = TreeUtil.getResult(nodeList);
		System.out.println(JSONUtils.toJson(tree));
		return JSONUtils.toJson(tree);
	}

	@Override
	public String listUserTree(Long userId, boolean expanded) {
		List<SysMenu> allMenuList = mapper.getAllMenus(userId);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);
		List<MenuTree> nodeList = new ArrayList<MenuTree>();
		for (SysMenu menu : allMenuList) {
			if (menu.getMenuPid() != null && menu.getMenuStatus() == 1) { // 屏蔽掉父节点不存在的脏数据，且屏蔽掉被禁用的菜单
				MenuTree node = new MenuTree(menu);
				if (menu.getMenuId() == 0) {
					node.setRoot(true);
				}
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<MenuTree> tree = TreeUtil.getResult(nodeList);
		return JSONUtils.toJson(tree);
	}

	@Override
	public List<MenuTree> tree(boolean expanded, boolean hideDisableMenus, String menuName,String menuType,boolean showType) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 查询显示所有的菜单，包括被禁用的
		condition.put("orderByClause", "MENU_LEVEL,MENU_ORDER asc");
		condition.put("menuNameLike", menuName);
		 condition.put("menuStatus", "1");
		condition.put("menuType", menuType);
		List<SysMenu> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);
		if(showType) {
			SysMenu menu1 = new SysMenu();
			menu1.setMenuId(-1L);
			menu1.setMenuLevel(0);
			menu1.setMenuName("系统级");
			menu1.setMenuType(1);
			menu1.setMenuOrder(1);
			menu1.setMenuStatus(1);
			menu1.setMenuUrl("");
			SysMenu menu2 = new SysMenu();
			menu2.setMenuId(-2L);
			menu2.setMenuLevel(0);
			menu2.setMenuName("监督级");
			menu2.setMenuType(2);
			menu2.setMenuOrder(2);
			menu2.setMenuStatus(1);
			menu2.setMenuUrl("");
			SysMenu menu3 = new SysMenu();
			menu3.setMenuId(-3L);
			menu3.setMenuLevel(0);
			menu3.setMenuName("监管级");
			menu3.setMenuType(3);
			menu3.setMenuOrder(3);
			menu3.setMenuStatus(1);
			menu3.setMenuUrl("");
			SysMenu menu4 = new SysMenu();
			menu4.setMenuId(-4L);
			menu4.setMenuLevel(0);
			menu4.setMenuName("企业级");
			menu4.setMenuType(4);
			menu4.setMenuOrder(4);
			menu4.setMenuStatus(1);
			menu4.setMenuUrl("");

			for (SysMenu menu : allMenuList) {
				if (menu.getMenuPid() == null) {
					if (menu.getMenuType() == menu1.getMenuType()) {
						menu.setMenuPid(menu1.getMenuId());
					} else if (menu.getMenuType() == menu2.getMenuType()) {
						menu.setMenuPid(menu2.getMenuId());
					} else if (menu.getMenuType() == menu3.getMenuType()) {
						menu.setMenuPid(menu3.getMenuId());
					} else if (menu.getMenuType() == menu4.getMenuType()) {
						menu.setMenuPid(menu4.getMenuId());
					}
				}
			}
			allMenuList.add(menu1);
			allMenuList.add(menu2);
			allMenuList.add(menu3);
			allMenuList.add(menu4);
		}

		List<MenuTree> nodeList = new ArrayList<MenuTree>();

		for (SysMenu menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus && menu.getMenuStatus() != 1) {
				showNode = false;
			}
			if (showNode) { // 过滤掉父节点不存在的脏数据
				MenuTree node = new MenuTree(menu);
                if (menu.getMenuPid() == null) {
                    node.setRoot(true);
                }
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<MenuTree> tree = TreeUtil.getResult(nodeList);
		//System.out.println(JSONUtils.toJson(tree));

		return tree;
	}

	@Override
	public List<MenuTree> userMenuTree(boolean expanded, boolean hideDisableMenus, String userName) {

		List<SysMenu> allMenuList = mapper.getPermissionByUsername(userName);
		// 删除父节点不存在或父节点被禁用的子节点
		removeDisableNodes(allMenuList);
		List<MenuTree> nodeList = new ArrayList<MenuTree>();
		for (SysMenu menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus && menu.getMenuStatus() != 1) {
				showNode = false;
			}
			if (showNode) {
				MenuTree node = new MenuTree(menu);
				if (menu.getMenuPid() == null) {
					node.setRoot(true);
				}
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<MenuTree> tree = TreeUtil.getResult(nodeList);
//		System.out.println(JSONUtils.toJson(tree));

		return tree;
	}

	/**
	 * 删除父节点不存在或父节点被禁用的子节点
	 * 
	 * @param allMenuList
	 */
	private void removeDisableNodes(List<SysMenu> allMenuList) {
		for (int i = 0; i < allMenuList.size(); i++) {
			SysMenu menu = allMenuList.get(i);
			checkParentNode(menu, allMenuList);
		}
	}

	private void checkParentNode(SysMenu menu, List<SysMenu> menuList) {
		boolean hasParent = false;
		Long pid = menu.getMenuPid();
		for (SysMenu m : menuList) {
			if (m.getMenuId() .equals( pid)) {
				hasParent = true;
			}
			if (hasParent && m.getMenuStatus() != 1) {
				menu.setMenuStatus(0); // 附菜单被禁用，自动禁用子菜单
			}
			if (hasParent) {
				break;
			}
		}
		if (!hasParent) {
			menu.setMenuPid(null); // 屏蔽掉父节点不存在的脏数据
		}
	}

	public List<SysMenu> selectMenusByUserId(Long userId) {
		return mapper.getAllMenus(userId);
	}

	@Override
	public List<SysMenu> getMenuListByUser(Long userId) {

		List<SysMenu> menuList = selectMenusByUserId(userId);

		return getAllMenuList(menuList);
	}

	/**
	 * 对菜单进行分类整理
	 * 
	 * @param menuList
	 * @return
	 */
	private List<SysMenu> getAllMenuList(List<SysMenu> menuList) {

		List<SysMenu> trees = new ArrayList<SysMenu>();

		for (SysMenu node : menuList) {
			if (node.getMenuPid() == 0) {
				trees.add(findChildren(node, menuList));
			}
		}

		return trees;
	}

	private SysMenu findChildren(SysMenu node, List<SysMenu> menuList) {

		for (SysMenu menu : menuList) {
			if (node.getMenuId() == menu.getMenuPid()) {
				if (node.getChildren() == null) {
					node.setChildren(new ArrayList<SysMenu>());
				}
				node.getChildren().add(findChildren(menu, menuList));
			}
		}
		return node;

	}

	@Override
	public List<SysMenu> getPermissionByUsername(String username) {

		return mapper.getPermissionByUsername(username);
	}

	@Override
	public SysMenu getMenuById(Long id) {

		return mapper.getMenuById(id);
	}

	@Override
	public void persist(SysMenu entity) {
		entity.setCreateTime(new Date());

		super.persist(entity);
	}

}
