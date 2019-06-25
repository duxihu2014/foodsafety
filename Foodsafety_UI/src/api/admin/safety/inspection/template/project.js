import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/list',
    method: 'get',
    params: query
  });
}
export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/' + id,
    method: 'get'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/' + id,
    method: 'put',
    data: obj
  })
}

export function changeStatus(data) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/changeStatus',
    method: 'put',
    params: data
  })
}

//获取所有项目
export function checkProjectName(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/checkProjectName',
    method: 'get',
    params: obj
  })
}

export function getTemplateProject(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/getTemplateProject',
    method: 'get',
    params: obj
  })
}

export function getTemplateAllProject(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/getTemplateAllProject',
    method: 'get',
    params: obj
  })
}

export function getUnbandingProject(obj) {
  return fetch({
    url: '/api/admin/safety/inspection/template/project/getUnbandingProject',
    method: 'get',
    params: obj
  })
}

