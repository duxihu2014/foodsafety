import axios from "axios"
import store from "../../store";
import { Message, MessageBox } from 'element-ui';

axios.defaults.withCredentials = true

// respone拦截器
axios.interceptors.response.use(
  response => {
    /**
     * 下面的注释为通过response自定义code来标示请求状态，当code返回如下情况为权限有问题，登出并返回到登录页
     * 如通过xmlhttprequest 状态码标识 逻辑可写在下面error中
     */
    let res = response.data;
    // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
    if (response.status === 401 || res.status === 40101 || res.status === 40301) {
      //debugger
      MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('FedLogOut').then(() => {
          location.reload(); // 为了重新实例化vue-router对象 避免bug
        });
      })
      return Promise.reject('error');
    }
    if (response.status !== 200) {
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      });
    } else if (res.status && res.status !== 200) {
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      });
      return Promise.reject('error');
    } else {
      return response.data;
    }
  },
  error => {
    console.log(error); // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export function getColumn(columnId) {
  return new Promise((resolve, reject) => {
    axios.get(
      "publish/column/"+columnId+".json", {
        params : {
          DateFlag: new Date().getTime()
        }
      }).then(data => {
      // console.log(data);
      resolve(data);
    }) .catch(error => {
      reject(error);
      // console.log(error);
    });
  });
}

export function getHotContent(columnId) {
  return new Promise((resolve, reject) => {
    axios.get(
      "publish/"+columnId+"/hot.json", {
        params : {
          DateFlag: new Date().getTime()
        }
      }).then(data => {
      // console.log(data);
      resolve(data);
    }) .catch(error => {
      reject(error);
      // console.log(error);
    });
  });
}

export function getContentPage(contentUrl) {
  return new Promise((resolve, reject) => {
    axios.get(contentUrl, {
        params : {
          DateFlag: new Date().getTime()
        }
      }).then(data => {
      // console.log(data);
      resolve(data);
    }) .catch(error => {
      reject(error);
      // console.log(error);
    });
  });
}

export function getContentBody(forwardUrl) {
  return new Promise((resolve, reject) => {
    axios.get(forwardUrl, {
      params : {
        DateFlag: new Date().getTime()
      }
    }).then(data => {
      // console.log(data);
      resolve(data);
    }) .catch(error => {
      reject(error);
      // console.log(error);
    });
  });
}
