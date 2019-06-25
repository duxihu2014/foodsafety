// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
import VueAMap from 'vue-amap'
import echarts from 'echarts'
require('echarts/theme/dark');
import 'element-ui/lib/theme-chalk/index.css';
//import 'assets/custom-theme/index.css'; // 换肤版本element-ui css
import NProgress from 'nprogress'; // Progress 进度条
import 'nprogress/nprogress.css';// Progress 进度条 样式
import 'normalize.css/normalize.css';// normalize.css 样式格式化
import 'assets/iconfont/iconfont'; // iconfont 具体图标见https://github.com/PanJiaChen/vue-element-admin/wiki
import 'font-awesome/css/font-awesome.css';
import * as filters from './filters'; // 全局vue filter
import Multiselect from 'vue-multiselect';// 使用的一个多选框组件，element-ui的select不能满足所有需求
import 'vue-multiselect/dist/vue-multiselect.min.css';// 多选框组件css
import vueWaves from './directive/waves';// 水波纹指令
import './mock/index.js';// 该项目所有请求使用mockjs模拟
import { getToken } from 'utils/auth';
import axios from 'axios';
import 'jquery-slimscroll';
import  'jquery.actual';
import 'echarts-amap';

Vue.component('multiselect', Multiselect);
Vue.use(ElementUI);
Vue.use(VueAMap);
Vue.use(vueWaves);


Vue.prototype.echarts = echarts;
Vue.prototype.$http = axios;


Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
});

VueAMap.initAMapApiLoader({
    key: '6ae79a0afe60b2e51857c7516b9c82f0',
    plugin: ['Autocomplete', 'Geocoder', 'PlaceSearch', 'Scale', 'OverView', 'ToolBar', 'ControlBar', 'MapType', 'PolyEditor', 'CircleEditor', 'Geolocation', 'Heatmap', 'MarkerClusterer'],
    // uiVersion: '1.0.11', // 版本号
    v: '1.4.10'
});

// register global progress.
const whiteList = ['/login', '/authredirect'];// 不重定向白名单
router.beforeEach((to, from, next) => {
  NProgress.start(); // 开启Progress

  if (getToken()) { // 判断是否有token
    if (to.path === '/login') {
      next({ path: '/' });
    } else {
      if (store.getters.staticData === undefined) {

        store.dispatch('getStaticData').then(() => {
        });
        store.dispatch('getAreaData').then(() => {
        });
      }
      if (store.getters.menus === undefined) { // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(info => { // 拉取user_info
          const menus = {};
          for (let i = 0; i < info.menus.length; i++) {
            menus[info.menus[i].menuId] = true;
          }
          store.dispatch('GenerateRoutes', menus).then(() => { // 生成可访问的路由表
            // debugger
            router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
            next({ ...to }); // hack方法 确保addRoutes已完成
          })
        }).catch(() => {
          store.dispatch('FedLogOut').then(() => {
            next({ path: '/login' });
          })
        })
      } else {
        // console.log(to.path);
        // console.log(store.getters.routers);
        //  else {
        //   // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        //   if (hasPermission(store.getters.roles, to.meta.role)) {
        //     next();//
        //   } else {
        next();
        //   }
        //   // 可删 ↑
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next('/login'); // 否则全部重定向到登录页
      NProgress.done(); // 在hash模式下 改变手动改变hash 重定向回来 不会触发afterEach 暂时hack方案 ps：history模式下无问题，可删除该行！
    }
  }
});

router.afterEach(() => {

  NProgress.done(); // 结束Progress
});

Vue.config.productionTip = false;


new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})


