import fetch from 'utils/fetch';

export function getOnsiteResult(query) {
  return fetch({
    url: '/api/admin/safety/onsite/result/getOnsiteVerificationResult',
    method: 'get',
    params: query
  })
}

export function getOnsiteResultItem(query) {
  return fetch({
    url: '/api/admin/safety/onsite/result/item/getOnsiteVerificationResultItem',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/onsite/result/create',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/form-data'}
  });
}

export function putObj(obj) {
  return fetch({
    url: '/api/admin/safety/onsite/result/update',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/form-data'}
  });
}

