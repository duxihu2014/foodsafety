import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/list',
    method: 'get',
    params: query
  });
}

export function pageAll(query) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/listAll',
    method: 'get',
    params: query
  });
}

export function addObj(obj) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/create',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/form-data'}
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/' + id,
    method: 'get'
  })
}

export function putObj(obj) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/update',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/form-data'}
  })
}

export function changeStatus(data) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/changeStatus',
    method: 'put',
    params: data
  })
}

export function checkSupplierName(obj) {
  return fetch({
    url: '/api/admin/purchase/materials/supplier/checkSupplierName',
    method: 'get',
    params: obj
  })
}
