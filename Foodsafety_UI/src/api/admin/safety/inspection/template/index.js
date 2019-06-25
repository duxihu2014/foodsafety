import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/safety/inspection/template/list',
    method: 'get',
    params: query
  });
}

export function all(query) {
  return fetch({
    url: '/api/admin/safety/inspection/template/all',
    method: 'get',
    params: query
  });
}

export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/safety/inspection/template/' + id,
    method: 'get'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/' + id,
    method: 'put',
    data: obj
  })
}

export function changeStatus(data) {
  return fetch({
    url: '/api/admin/safety/inspection/template/changeStatus',
    method: 'put',
    params: data
  })
}

export function checkTemplateName(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/checkTemplateName',
    method: 'get',
    params: obj
  })
}

export function bandingProject(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/templateProject',
    method: 'post',
    data: obj
  });
}

export function unbandingProject(id) {
  return fetch({
    url: '/api/admin/safety/inspection/templateProject/' + id,
    method: 'delete'
  })
}

export function batchUnbandingProject(data) {
  return fetch({
    url: '/api/admin/safety/inspection/templateProject/unbanding/',
    method: 'put',
    params: data
  })
}

export function setProjectScore(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/templateProject/setProjectScore',
    method: 'put',
    params: obj
  })
}
