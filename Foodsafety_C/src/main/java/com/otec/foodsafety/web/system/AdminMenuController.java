package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.MenuTree;
import com.otec.foodsafety.entity.system.SysMenu;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.system.SysMenuService;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单controller
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 */
@Controller
@RequestMapping("api/admin/menu")
public class AdminMenuController extends
        VueBaseController<SysMenuService, SysMenu, Long> {
	@Autowired
	private SysMenuService menuService;
	@Autowired
	private SessionFilter sessionFilter;
	private static final Logger log = LoggerFactory
			.getLogger(AdminUserController.class);

	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	@ResponseBody
	public List<MenuTree> getTree(HttpServletRequest req,
			@RequestParam(name = "menuName", required = false) String menuName,
			@RequestParam(name = "menuType", required = false) String menuType) {
        List<MenuTree> list = menuService.tree(true, false, menuName,menuType,false);
		return list;
	}

	@RequestMapping(value = "/treeManage", method = RequestMethod.GET)
	@ResponseBody
	public List<MenuTree> treeManage(HttpServletRequest req,
								  @RequestParam(name = "menuName", required = false) String menuName) {
		List<MenuTree> list = menuService.tree(true, false, menuName,null,true);
		return list;
	}

	@RequestMapping(value = "/userTree", method = RequestMethod.GET)
	@ResponseBody
	public List<MenuTree> getTree(HttpServletRequest req) {
		SysUser sysUser = sessionFilter.getJWTUser(req);
        List<MenuTree> list = menuService.userMenuTree(true, false, sysUser.getUserName());
		return list;
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<SysMenu> get(@PathVariable("id") Long id) {
		SysMenu menu = menuService.getMenuById(id);

		return new ObjectRestResponse<SysMenu>().rel(true).data(menu);

	}

}
