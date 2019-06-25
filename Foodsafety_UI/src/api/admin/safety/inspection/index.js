import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/safety/inspection/plan/getEnterpriseInspectionPlanTask',
    method: 'get',
    params: query
  });
}

export function pageQury(query) {
  return fetch({
    url: '/api/admin/safety/inspection/plan/getEnterpriseInspectionPlan',
    method: 'get',
    params: query
  });
}

export function getInspectionPlan(id) {
  return fetch({
    url: '/api/admin/safety/inspection/plan/getInspectionPlan/' + id,
    method: 'get'
  })
}
