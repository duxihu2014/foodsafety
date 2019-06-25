import fetch from 'utils/fetch';

/**
 * 获取回访结果的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/safety/revisitResult/list',
    method: 'get',
    params: query
  });
}

export function getRevisitPlanTask(query) {
  return fetch({
    url: '/api/admin/safety/revisitResult/getRevisitPlanTask',
    method: 'get',
    params: query
  });
}

/**
 * 通过ID获取该回访结果数据
 * **/
export function getRevisitResult(id) {
  return fetch({
    url: '/api/admin/safety/revisitResult/' + id,
    method: 'get'
  })
}

/**
获取监督人员MAP
**/
export function getPersonnelMap() {
 return fetch({
    url: '/api/admin/safety/revisitResult/getPersonnelMap',
    method: 'get'
  });
}

/**
获取回访机构MAP
**/
export function getInstitutionMap() {
 return fetch({
    url: '/api/admin/safety/revisitResult/getInstitutionMap',
    method: 'get'
  });
}

export function getRevisitResultByPlan(id) {
  return fetch({
    url: '/api/admin/safety/revisitResult/getRevisitResultByPlan/' + id,
    method: 'get'
  })
}


export function getRevisitPlan(id) {
  return fetch({
    url: '/api/admin/safety/revisitResult/getRevisitPlan/' + id,
    method: 'get'
  })
}
