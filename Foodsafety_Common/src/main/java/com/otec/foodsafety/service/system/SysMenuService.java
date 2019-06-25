package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.MenuTree;
import com.otec.foodsafety.entity.system.SysMenu;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.List;

@RemoteService
public interface SysMenuService extends BaseService<SysMenu, Long> {

	/**
	 * 鍔犺浇鎵?湁鐨勮彍鍗?
	 * 
	 * @param expanded
	 *            鏄惁灞曞紑鑿滃崟
	 * @param hideDisableMenus
	 *            鏄惁闅愯棌鏃犳晥鐨勮彍鍗曪紙琚鐢ㄧ殑锛?
	 * @return
	 */
	public String listTree(boolean expanded, boolean hideDisableMenus);

	/**
	 * 鍔犺浇鏌愪釜鐢ㄦ埛鎷ユ湁鏉冮檺鐨勮彍鍗?
	 * 
	 * @param userId
	 *            鐢ㄦ埛ID
	 * @param expanded
	 *            鏄惁灞曞紑鑿滃崟
	 * @return
	 */
	public String listUserTree(Long userId, boolean expanded);

	public List<SysMenu> selectMenusByUserId(Long userId);

	public void deleteMenuByIds(String[] menuIds);

	/**
	 * add by jiangxl 鏍规嵁鐢ㄦ埛鑾峰彇鍙互鏌ョ湅鐨勮彍鍗曞垪琛?
	 * 
	 * @param valueOf
	 * @return
	 */
	public List<SysMenu> getMenuListByUser(Long userId);

	public List<SysMenu> getPermissionByUsername(String username);

	public List<MenuTree> tree(boolean expanded, boolean hideDisableMenus, String menuName,String menuType,boolean showType);

	public SysMenu getMenuById(Long id);

	List<MenuTree> userMenuTree(boolean expanded, boolean hideDisableMenus, String userName);
}
