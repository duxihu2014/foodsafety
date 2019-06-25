package com.otec.foodsafety.entity.system;

import com.otec.foodsafety.util.Tree;

import java.util.List;


public class AreaTree extends Tree<AreaTree> {
	public AreaTree() {
	}

	public AreaTree(SysArea r) {
		this.setText(r.getAreaName());
		this.setId(r.getAreaId().toString());
		this.setExpanded(expanded);
		this.setPid(r.getpAreaId().toString());
		this.setLevel(r.getAdministrativeLevel().toString());
		this.setValue(r.getAreaId().toString());
		this.setLabel(r.getAreaName());	 
	}

	public void setChildren(List<SysArea> MenuList, boolean expanded) {
		/*if (VerifyObject.verifyCollection(MenuList)) {
			List<MenuTree> list = new ArrayList<MenuTree>();
			MenuTree rt;
			for (SysArea r : MenuList) {
				rt = new MenuTree();
				rt.setText(r.getMenuName());
				rt.setId(r.getMenuId().toString());
				rt.setExpanded(expanded);
				rt.setPid(r.getMenuPid().toString());
				list.add(rt);
			}
			this.setChildren(list);
		}*/
	}

	public void addChildren(AreaTree r, boolean expanded) {
		/*if (r != null) {
			r.setExpanded(expanded);
			if (this.getChildren() == null) {
				this.setChildren(new ArrayList<MenuTree>());
			}
			this.getChildren().add(r);
		}*/
	}

}
