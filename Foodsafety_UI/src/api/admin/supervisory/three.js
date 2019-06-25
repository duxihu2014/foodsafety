import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/supervisory/personnalThree/list',
    method: 'get',
    params: query
  });
}

export function addObj(obj) {
  return fetch({
    url: '/api/admin/supervisory/personnalThree',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/supervisory/personnalThree/' + id,
    method: 'get'
  })
}
export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/supervisory/personnalThree/' + id,
    method: 'put',
    data: obj
  })
}
export function changeStatus(data) {
  return fetch({
    url: '/api/admin/supervisory/personnalThree/changeStatus',
    method: 'put',
    params: data
  })
}

//获取所有
export function getAllPersonnalThree(obj) {
  return fetch({
    url: '/api/admin/supervisory/personnalThree/all',
    method: 'get',
    params: obj
  })
}
