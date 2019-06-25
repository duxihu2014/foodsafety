import Vue from 'vue';
import Router from 'vue-router';
const _import = require('./_import_' + process.env.NODE_ENV);
// in development env not use Lazy Loading,because Lazy Loading large page will cause webpack hot update too slow.so only in production use Lazy Loading

Vue.use(Router);

/* layout */
import Layout from '../views/layout/Layout';
import register from '../views/login/register';

/**
 * icon : the icon show in the sidebar
 * hidden : if `hidden:true` will not show in the sidebar
 * redirect : if `redirect:noredirect` will no redirct in the levelbar
 * noDropdown : if `noDropdown:true` will has no submenu
 * meta : { role: ['admin'] }  will control the page role
 **/
export const constantRouterMap = [{
  path: '/login',
  //component: _import('login/faceLogin'),
  component: _import('login/index'),
  hidden: true
},
{
  path: '/authredirect',
  component: _import('login/authredirect'),
  hidden: true
},
{
  path: '/404',
  component: _import('error/404'),
  hidden: true
},
{
  path: '/401',
  component: _import('error/401'),
  hidden: true
},
{
  path: '/',
  component: Layout,
  redirect: '/dashboard',
  name: '首页',
  hidden: true,
  children: [{
    path: 'dashboard',
    component: _import('dashboard/index')
  }]
},
  {
    path: '/personal/register',
    component: Layout,
    redirect: '/personal/register',
    name: '个人事务-企业注册',
    hidden: true,
    children: [{
      path: '/',
      component: _import('admin/enterprise/register/verify')
    }]
  },
  {
    path: '/personal/modify',
    component: Layout,
    redirect: '/personal/modify',
    name: '个人事务-档案更新',
    hidden: true,
    children: [{
      path: '/',
      component: _import('admin/personal/modify')
    }]
  },
  {
    path: '/personal/cert',
    component: Layout,
    redirect: '/personal/cert',
    name: '个人事务-证照预警',
    hidden: true,
    children: [{
      path: '/',
      component: _import('admin/personal/cert')
    }]
  },
  {
    path: '/personal/safety',
    component: Layout,
    redirect: '/personal/safety',
    name: '个人事务-安全检查',
    hidden: true,
    children: [{
      path: '/',
      component: _import('admin/personal/inspection')
    }]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
});

export const asyncRouterMap = [{
  path: '',
  component: Layout,
  name: '基础配置管理',
  icon: 'setting',
  authority: 'baseManager',
  children: [{
    path: 'admin/user',
    icon: 'fa-user',
    component: _import('admin/user/index'),
    name: '用户管理',
    authority: 'userManager'
  }, {
    path: 'menu',
    icon: 'category',
    component: _import('admin/menu/index'),
    name: '菜单管理',
    authority: 'menuManager'
  }, {
    path: 'role',
    icon: 'fa-users',
    component: _import('admin/role/index'),
    name: '角色管理',
    authority: 'roleManager'
  }, {
    path: 'gate_log',
    icon: 'viewlist',
    component: _import('admin/gateLog/index'),
    name: '操作日志管理',
    authority: 'gateLogManager'
  }/*, {
    path: 'area',
    icon: 'category',
    component: _import('admin/area/index'),
    name: '区域管理',
    authority: 'areaManager'
  }*/]
}];
