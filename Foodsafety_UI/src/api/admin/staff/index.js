import fetch from 'utils/fetch';



export function save(obj) {
  return fetch({
    url: '/api/catering/staff/save',
    method: 'post',
    data: obj
  });
}
export function getObj(id) {
  return fetch({
    url: '/api/catering/staff/' + id,
    method: 'get'
  })
}
export function putObj(id, obj) {
  return fetch({
    url: '/api/catering/staff/' + id,
    method: 'put',
    data: obj
  })
}
export function page(query) {
  return fetch({
    url: '/api/catering/staff/list',
    method: 'get',
    params: query
  });
}

export function changeStatus(data) {
  return fetch({
    url: '/api/catering/staff/changeStatus',
    method: 'put',
    params: data
  })
}


