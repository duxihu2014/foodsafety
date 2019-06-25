import fetch from 'utils/fetch';

export function fetchTree(query) {
  //console.log(query);
  return fetch({
    url: '/api/admin/menu/tree',
    method: 'get',
    params: query
  });
}

export function fetchTreeManage(query) {
  //console.log(query);
  return fetch({
    url: '/api/admin/menu/treeManage',
    method: 'get',
    params: query
  });
}

export function fetchAll() {
  return fetch({
    url: '/api/admin/menu/tree',
    method: 'get'
  });
}
export function fetchUserTree() {
  return fetch({
    url: '/api/admin/menu/userTree',
    method: 'get'
  });
}
export function addObj(obj) {
  return fetch({
    url: '/api/admin/menu',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/menu/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/admin/menu/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/menu/' + id,
    method: 'put',
    data: obj
  })
}
