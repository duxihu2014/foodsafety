import fetch from 'utils/fetch';

export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/cycle',
    method: 'post',
    data: obj
  });
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/safety/inspection/cycle/' + id,
    method: 'put',
    data: obj
  })
}

export function getInspectionCycle(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/cycle/getInspectionCycle',
    method: 'get',
    params: obj
  })
}
