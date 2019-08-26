import Cookies from 'js-cookie';
import { getData ,getAreaData} from 'api/remoteSearch';
import { log } from 'util';
const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get('sidebarStatus')
    },
    theme: 'default',
    livenewsChannels: Cookies.get('livenewsChannels') || '[]',
    visitedViews: [{ name: '首页', path: '/' }],
    staticData: undefined,
    screenHeight: undefined,
    areaData: undefined,
    columnParams: undefined
  },
  mutations: {
    SET_SCREENHEIGHT: (state, screenHeight) => {
      state.screenHeight = screenHeight;
    },
    SET_AREADATA:(state,areaData)=>{
      state.areaData = areaData;
    },
    SET_COLUMNPARAMS: (state, columnParams) => {
      state.columnParams = columnParams;
    },
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1);
      } else {
        Cookies.set('sidebarStatus', 0);
      }
      state.sidebar.opened = !state.sidebar.opened;
    },
    ADD_VISITED_VIEWS: (state, view) => {
      // debugger
      if (state.visitedViews.some(v => v.path === view.path)) return
      state.visitedViews.push({ name: view.name, path: view.path });
    },
    DEL_VISITED_VIEWS: (state, view) => {
      let index
      for (const [i, v] of state.visitedViews.entries()) {
        if (v.path === view.path) {
          index = i
          break
        }
      }
      state.visitedViews.splice(index, 1)
    },
    SET_STATIC_DATA: (state, data) => {
      console.log,("app.js----50"+state,data);
      state.staticData = data;
    },
    SAVE_VIEWS: (state) => {

      Cookies.set('visitedViews', state.visitedViews);
    },
    SET_VIEWS: (state, views) => {

      if (state.visitedViews && state.visitedViews.length > 0) {
        if (views) {
          views.forEach(v => {
            if (v.path !== '/') {
              state.visitedViews.push(v);
            }
          });
        }
      }
      else {
        state.visitedViews = views;
      }
      //
    }
  },
  actions: {
    setViews: ({ commit }, views) => {
      commit('SET_VIEWS', views);
    },
    ToggleSideBar: ({ commit }) => {
      commit('TOGGLE_SIDEBAR')
    },
    addVisitedViews: ({ commit }, view) => {
      // debugger
      commit('ADD_VISITED_VIEWS', view)
      commit("SAVE_VIEWS");
    },
    delVisitedViews: ({ commit }, view) => {
      commit('DEL_VISITED_VIEWS', view);
      commit("SAVE_VIEWS");
    },
    getAreaData: ({ commit }) => {
      return new Promise((resolve, reject) => {
        getAreaData().then(response => {
          const data = response;
          commit('SET_AREADATA', data);
          resolve(response);
        }).catch(error => {
          reject(error);
        });
      });
    },
    getStaticData: ({ commit }) => {
      return new Promise((resolve, reject) => {
        getData().then(response => {
          // Object.keys(response).forEach((v,i)=>{
          //   console.log(response[v]);
          // })
          // console.log("app.js---105--getStaticData------"+Object.keys(response));
          const data = response;
          commit('SET_STATIC_DATA', data);
          resolve(response);
        }).catch(error => {
          reject(error);
        });
      });
    }
  }
};

export default app;
