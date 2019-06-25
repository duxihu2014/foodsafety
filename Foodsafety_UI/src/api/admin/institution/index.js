import fetch from 'utils/fetch';

export function addObj(obj) {
  return fetch({
    url: '/api/admin/institution',
    method: 'post',
    data: obj
  });
}
export function getObj(id) {
  return fetch({
    url: '/api/admin/institution/' + id,
    method: 'get'
  })
}
export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/institution/' + id,
    method: 'put',
    data: obj
  })
}
export function page(query) {
  return fetch({
    url: '/api/admin/institution/list',
    method: 'get',
    params: query
  });

}
//获取所有机构
export function getInstitutions(query) {
  return fetch({
    url: '/api/admin/institution/all',
    method: 'get',
    params: query
  })
}
export function changeStatus(data) {
  return fetch({
    url: '/api/admin/institution/changeStatus',
    method: 'put',
    params: data
  })
}
/**
 * 查询获取全部机构的树形结构数据
 * **/
export function fetchTree() {
  return fetch({
    url: '/api/admin/institution/tree',
    method: 'get'
  });
}


