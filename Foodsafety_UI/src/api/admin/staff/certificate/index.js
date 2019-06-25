import fetch from 'utils/fetch';



export function add(obj) {
  return fetch({
    url: '/api/staff/certificate/add',
    method: 'post',
    data: obj
  });
}
export function update(obj) {
  return fetch({
    url: '/api/staff/certificate/update',
    method: 'post',
    data: obj
  });
}
export function getObj(id) {
  return fetch({
    url: '/api/catering/staff/' + id,
    method: 'get'
  })
}
export function page(query) {
  return fetch({
    url: 'api/staff/certificate/getCertificateList',
    method: 'get',
    params: query
  });
}



