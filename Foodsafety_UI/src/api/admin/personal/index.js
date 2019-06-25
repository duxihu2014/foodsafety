import fetch from 'utils/fetch';

export function getEnterpriseCount(query) {
  return fetch({
    url: '/api/personal/enterprise/count',
    method: 'get',
    params: query
  })
}

export function getSuperviseCount(query) {
  return fetch({
    url: '/api/personal/supervise/count',
    method: 'get',
    params: query
  })
}
