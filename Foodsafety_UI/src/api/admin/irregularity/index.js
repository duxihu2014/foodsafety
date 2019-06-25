import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/irregularity/list',
    method: 'get',
    params: query
  });
}

export function saveIrregularity(obj) {
  return fetch({
    url: '/api/admin/irregularity/saveIrregularity',
    method: 'post',
    data: obj
  });
}

export function saveCaseClose(obj) {
  return fetch({
    url: '/api/admin/irregularity/saveCaseClose',
    method: 'post',
    data: obj
  });
}

export function saveCasePunish(obj) {
  return fetch({
    url: '/api/admin/irregularity/saveCasePunish',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/irregularity/getIrregularity/' + id,
    method: 'get'
  })
}




