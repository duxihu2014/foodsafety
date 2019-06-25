import fetch from 'utils/fetch';

//获取某地区下的所有网格
export function getAreaGrid(obj) {
  return fetch({
    url: '/api/admin/supervisory/grid/all',
    method: 'get',
    params: obj
  })
}


export function page(query) {
  return fetch({
    url: '/api/admin/supervisory/grid/list',
    method: 'get',
    params: query
  });
}

export function addObj(obj) {
  return fetch({
    url: '/api/admin/supervisory/grid',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/supervisory/grid/' + id,
    method: 'get'
  })
}
export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/supervisory/grid/' + id,
    method: 'put',
    data: obj
  })
}
export function changeStatus(data) {
  return fetch({
    url: '/api/admin/supervisory/grid/changeStatus',
    method: 'put',
    params: data
  })
}

/**
 *查询本机构下的 除gridId ={id}的其它网格坐标
 * @param id
 */
export function getOtherGrids(obj) {
  return fetch({
    url: '/api/admin/supervisory/grid/otherGrids',
    method: 'get',
    params: obj
  })
}

