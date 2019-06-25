package com.otec.foodsafety.service.enterprise;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.cykj.grcloud.service.impl.base.BaseServiceImpl;

import com.otec.foodsafety.entity.enterprise.EnterpriseProductType;
import com.otec.foodsafety.entity.enterprise.EnterpriseProductTypeTree;
import com.otec.foodsafety.entity.system.AreaTree;
import com.otec.foodsafety.entity.system.SysArea;
import com.otec.foodsafety.mapper.enterprise.EnterpriseProductTypeMapper;
import com.otec.foodsafety.util.TreeUtil;

@Service
@Transactional
public class EnterpriseProductTypeServiceImpl extends BaseServiceImpl<EnterpriseProductType, Long>
		implements EnterpriseProductTypeService {
	
	@Autowired
	EnterpriseProductTypeMapper mapper;
	
	@Autowired
	public void setMapper(EnterpriseProductTypeMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public List<EnterpriseProductTypeTree> listTree(boolean expanded, boolean hideDisableMenus) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 查询显示所有的区域
//		condition.put("orderByClause", "ADMINISTRATIVE_LEVEL , SORT_ID asc");
//		condition.put("areaStatus", 1);
		List<EnterpriseProductType> allMenuList = findEntitysByCondition(condition);
		// 删除父节点不存在或父节点被禁用的子节点
//		removeDisableNodes(allMenuList);

		List<EnterpriseProductTypeTree> nodeList = new ArrayList<EnterpriseProductTypeTree>();
		for (EnterpriseProductType menu : allMenuList) {
			boolean showNode = true;
			if (hideDisableMenus) {
				showNode = false;
			}
			if (showNode && menu.getProductTypeId() != null) { // 过滤掉父节点不存在的脏数据
				EnterpriseProductTypeTree node = new EnterpriseProductTypeTree(menu);
				if ("0".equals(menu.getProductTypePId().toString())) {// 一级节点为根节点
					node.setRoot(true);
				}
				node.setExpanded(expanded);
				nodeList.add(node);
			}
		}
		List<EnterpriseProductTypeTree> tree = TreeUtil.getResult(nodeList);
		return tree;
	}

	private void removeDisableNodes(List<EnterpriseProductType> allMenuList) {
		for (int i = 0; i < allMenuList.size(); i++) {
			EnterpriseProductType menu = allMenuList.get(i);
			checkParentNode(menu, allMenuList);
		}
	}

	private void checkParentNode(EnterpriseProductType menu, List<EnterpriseProductType> menuList) {
		boolean hasParent = false;
		long pid = menu.getProductTypeId();
		/*
		 * if (menu.getAreaId() == 1) { return; }
		 */
		if (pid == 0) {
			return;
		}
		for (EnterpriseProductType m : menuList) {
			if (m.getProductTypeId() == pid) {
				hasParent = true;
			}
			if (hasParent) {
				break;
			}
		}
		if (!hasParent) {
			menu.setProductTypePId(null); // 屏蔽掉父节点不存在的脏数据
		}
	}
}
