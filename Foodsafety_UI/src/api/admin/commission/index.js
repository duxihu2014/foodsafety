import fetch from 'utils/fetch';

export function save(obj) {
  return fetch({
    url: 'api/commission/contract/add',
    method: 'post',
    data: obj
  });
}
export function getEnterprise() {
  return fetch({
    url: 'api/commission/contract/getEnterprise',
    method: 'get'
  });
}
export function page(query) {
  return fetch({
    url: 'api/commission/contract/list',
    method: 'get',
    params:query
  });
}
export function getCommissionEnterprise(query) {
  return fetch({
    url: 'api/commission/contract/getCommissionEnterprise',
    method: 'get',
    params:query
  });
}
