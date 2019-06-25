import fetch from 'utils/fetch';
export function add(obj) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/add',
    method: 'post',
    data: obj
  });
}
export function getAllEnterprise(obj) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/getAllEnterprise',
    method: 'get',
  });
}
export function getPage(query) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/list',
    method: 'get',
    params: query
  });
}
export function getAllSupplyObject(query) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/all',
    method: 'get',
    params: query
  });
}
export function getObj(id) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/' + id,
    method: 'get'
  })
}
export function putObj(id,obj) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/' + id,
    method: 'put',
    data: obj
  })
}
export function delObjs(ids) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/all/' + ids,
    method: 'delete'
  })
}
export function getEnterprsieHasSupplyObject(query) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/getEnterprsieBase/list',
    method: 'get',
    params: query
  });
}
export function getAllEnterprsieHasSupplyObject(query) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/getEnterprsieBase/all',
    method: 'get',
    params: query
  });
}
export function getMapData(query) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/getMapData',
    method: 'get',
    params: query
  });
}

export function getSensorMonitor(query) {
  return fetch({
    url: 'api/admin/delivery/supplyObject/getSensorMonitor',
    method: 'get',
    params: query
  });
}
