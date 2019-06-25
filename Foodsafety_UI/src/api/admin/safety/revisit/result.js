import fetch from 'utils/fetch';

export function addObj(obj) {
  return fetch({
    url: '/api/admin/safety/revisitResult/create',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/form-data'}
  });
}
