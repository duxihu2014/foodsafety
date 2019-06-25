import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/gateLog/list',
    method: 'get',
    params: query
  });
}
