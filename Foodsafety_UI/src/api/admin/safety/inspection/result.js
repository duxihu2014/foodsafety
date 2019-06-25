import fetch from 'utils/fetch';

export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/result/create',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/form-data'}
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/safety/inspection/result/' + id,
    method: 'get'
  })
}

export function getInspectionResult(query) {
  return fetch({
    url: '/api/admin/safety/inspection/result/getInspectionResult',
    method: 'get',
    params: query
  })
}

export function getInspectionResultItem(query) {
  return fetch({
    url: '/api/admin/safety/inspection/result/item/getInspectionResultItem',
    method: 'get',
    params: query
  })
}
