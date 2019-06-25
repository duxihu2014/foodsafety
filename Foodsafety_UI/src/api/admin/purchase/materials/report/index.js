import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/purchase/materialsInspectionReport/list',
    method: 'get',
    params: query
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/purchase/materialsInspectionReport/' + id,
    method: 'get'
  })
}