package com.otec.foodsafety.entity.system;

import com.otec.foodsafety.util.Tree;
import com.otec.foodsafety.util.VerifyObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuTree extends Tree<MenuTree> implements Serializable {
	public MenuTree() {
	}

	public MenuTree(SysMenu r) {
		this.setText(r.getMenuName());
		this.setId(r.getMenuId().toString());
		this.setExpanded(expanded);
		this.setPid(r.getMenuPid()==null?"":r.getMenuPid().toString());
		this.setPath(r.getMenuUrl());
		this.setIconCls(r.getMenuIcon());
		this.setType(r.getMenuType());
		// if (r.getMenuType() == 1) {
		// this.setIconCls("tree-folder");
		// } else if (r.getMenuType() == 2) {
		// this.setIconCls("tree-file");
		// } else if (r.getMenuType() == 3) {
		// this.setIconCls("tree-button");
		// }
	}

	public void setChildren(List<SysMenu> MenuList, boolean expanded) {
		if (VerifyObject.verifyCollection(MenuList)) {
			List<MenuTree> list = new ArrayList<MenuTree>();
			MenuTree rt;
			for (SysMenu r : MenuList) {
				rt = new MenuTree();
				rt.setText(r.getMenuName());
				rt.setId(r.getMenuId().toString());
				rt.setExpanded(expanded);
				rt.setPid(r.getMenuPid().toString());
				rt.setPath(r.getMenuUrl());
				rt.setIconCls(r.getMenuIcon());
				list.add(rt);
			}
			this.setChildren(list);
		}
	}

	public void addChildren(MenuTree r, boolean expanded) {
		if (r != null) {
			r.setExpanded(expanded);
			if (this.getChildren() == null) {
				this.setChildren(new ArrayList<MenuTree>());
			}
			this.getChildren().add(r);
		}
	}

}
