import {
  constantRouterMap
} from 'src/router'
import { fetchUserTree } from 'api/admin/menu/index';
// import Layout from '../../views/layout/Layout';
const _import = require('router/_import_' + process.env.NODE_ENV);

/**
 * 通过authority判断是否与当前用户权限匹配
 * @param menus
 * @param route
 */
/* function hasPermission(menus, route) {
  if (route.authority) {
    if (menus[route.authority] !== undefined) {
      return menus[route.authority];
    } else {
      return false;
    }
  } else {
    return true
  }
} */

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param roles
 */
/** function filterAsyncRouter(asyncRouterMap, menus, menuDatas) {
  const accessedRouters = asyncRouterMap.filter(route => {
    if (hasPermission(menus, route)) {
      route.name = menuDatas[route.authority].title;
      route.icon = menuDatas[route.authority].icon;
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, menus, menuDatas);
      }
      return true
    }
    return false
  })
  return accessedRouters
}**/

function generateRouter(menuChildren, parent) {
  const children = [];

  for (let i = 0; i < menuChildren.length; i++) {
    const router = {};
    const menu = menuChildren[i];

    router.path = menu.path;
    router.icon = menu.iconCls;
    router.name = menu.text;
    router.id = menu.id;
    if (parent && menu.path.length > 0) {
      try {
        router.component = _import(menu.path);
      } catch (e) {

      }

    }
    if (menu.children && menu.children.length) {
      //router.component = Layout;
      router.component = _import('layout/Layout');
      router.children = generateRouter(menu.children, menu);
    }

    children.push(router);

  }
  return children;
}


function filterRouter(asyncRouterMap, menus) {
  //console.log('~~~~~~~~~~~~~~~~~~~~~~~~~'+menus);
  //console.log(menus);
  //console.log(asyncRouterMap);
  const accessedRouters = asyncRouterMap.filter(router => {
    if (menus[router.id] !== undefined) {
      return true;
    }
    else {
      return false;
    }
  });
  //console.log('~~~~~~~~~~~~result router~~~~~~~~~~~~~'+menus);
  //console.log(menus);
  return accessedRouters;
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      // debugger
      state.addRouters = state.addRouters.concat(routers)
      state.routers = constantRouterMap.concat(state.addRouters)
      // console.log(state.routers);
    }
  },
  actions: {
    GenerateRoutes({
                     commit
                   }, menus) {
      return new Promise(resolve => {
        fetchUserTree().then(data => {
          if (data && data.length) {
            //const menuChildren = data[0].children;
            const asyncRouterMap = generateRouter(data, true);
            // console.log(JSON.stringify(asyncRouterMap));
            const accessedRouters = filterRouter(asyncRouterMap, menus);
            //const accessedRouters = asyncRouterMap;
            commit('SET_ROUTERS', accessedRouters);
            resolve();
          }


        });
      })
    }
  }
};

export default permission;
