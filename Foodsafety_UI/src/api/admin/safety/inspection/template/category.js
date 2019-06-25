import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category/list',
    method: 'get',
    params: query
  });
}
export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category/' + id,
    method: 'get'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category/' + id,
    method: 'put',
    data: obj
  })
}

export function changeStatus(data) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category/changeStatus',
    method: 'put',
    params: data
  })
}

export function checkCategoryName(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category/checkCategoryName',
    method: 'get',
    params: obj
  })
}

//获取所有类型
export function getAllCategory(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/category/all',
    method: 'get',
    params: obj
  })
}
