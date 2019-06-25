package com.otec.foodsafety.entity.enterprise;

import com.otec.foodsafety.util.Tree;

public class EnterpriseProductTypeTree extends Tree<EnterpriseProductTypeTree> {
	// 产品分类编号
	private String productTypeNo;

	public EnterpriseProductTypeTree() {

	}

	public EnterpriseProductTypeTree(EnterpriseProductType r) {
		this.setText(r.getProductTypeName());
		this.setId(r.getProductTypeId().toString());
		this.setExpanded(expanded);
		this.setPid(r.getProductTypePId().toString());
//	this.setLevel(r.getAdministrativeLevel().toString());
		this.setValue(r.getProductTypeId().toString());
		this.setLabel(r.getProductTypeName());
		this.setProductTypeNo(r.getProductTypeNo());
	}

	public String getProductTypeNo() {
		return productTypeNo;
	}

	public void setProductTypeNo(String productTypeNo) {
		this.productTypeNo = productTypeNo;
	}

}
