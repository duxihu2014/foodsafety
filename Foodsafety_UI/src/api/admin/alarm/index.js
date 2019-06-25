import fetch from 'utils/fetch';
export function alertPage(query) {
  return fetch({
    url: '/api/admin/alert/list',
    method: 'get',
    params: query
  });
}
export function getAlertObj(id) {
  return fetch({
    url: '/api/admin/alert/' + id,
    method: 'get'
  })
}

export function addAlertObj(obj) {
  return fetch({
    url: '/api/admin/alert',
    method: 'post',
    data: obj
  });
}

export function putAlertObj(id, obj) {
  return fetch({
    url: '/api/admin/alert/' + id,
    method: 'put',
    data: obj
  })
}

export function delAlert(id) {
  return fetch({
    url: '/api/admin/alert/' + id,
    method: 'delete'
  })
}

export function delAlertObjs(ids) {
  return fetch({
    url: '/api/admin/alert/all/' + ids,
    method: 'delete'
  })
}

export function getCount(query) {
  return fetch({
    url: '/api/admin/alert/count',
    method: 'get',
    params: query
  })
}


export function processing(id,obj) {
  return fetch({
    url: '/api/admin/alert/processing/'+id,
    method: 'post',
    data: obj
  });
}
