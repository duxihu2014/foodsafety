import fetch from 'utils/fetch';

export function addObj(obj) {
  return fetch({
    url: '/api/materials/create',
    method: 'post',
    data: obj
  });
}
export function getObj(id) {
  return fetch({
    url: '/api/materials/' + id,
    method: 'get'
  })
}
export function updateObj(obj) {
  return fetch({
    url: '/api/materials/update',
    method: 'post',
    data: obj
  })
}
export function page(query) {
  return fetch({
    url: '/api/materials/list',
    method: 'get',
    params: query
  });
}
export function changeStatus(data) {
  return fetch({
    url: '/api/materials/changeStatus',
    method: 'put',
    params: data
  })
}
/**
 *  获取企业对应的所有有效供货商
 * @param query
 */
export function getSuppliers(enterpriseId) {
  return fetch({
    url: '/api/materials/getSuppliers/'+enterpriseId,
    method: 'get'
  });

}

