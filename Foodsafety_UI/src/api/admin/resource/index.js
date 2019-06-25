import fetch from 'utils/fetch';

export function getResource(id) {
  return fetch({
    url: '/api/resource/' + id,
    method: 'get'
  })
}
