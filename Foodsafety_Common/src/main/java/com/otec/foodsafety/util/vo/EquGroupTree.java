package com.otec.foodsafety.util.vo;

import com.otec.foodsafety.util.Tree;

import java.io.Serializable;

public class EquGroupTree extends Tree<EquGroupTree> implements Serializable {

	public EquGroupTree() {
	}

	public EquGroupTree(String id, String name, String pid, String level) {
		this.setText(name);
		this.setId(id);
		this.setExpanded(true);
		this.setPid(pid);
		this.setLevel(level);
	}

}
