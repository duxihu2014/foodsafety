import fetch from 'utils/fetch';

/**
 * 根据条件查询获取全部地区的树形结构数据
 * **/
export function fetchTree(query) {
  return fetch({
    url: '/api/admin/area/tree',
    method: 'get',
    params: query
  });
}

/**
 * 获取2级地区的树形结构数据
 * **/
export function fetchTwoLevelTree() {
  return fetch({
    url: '/api/admin/area/twoLevelTree',
    method: 'get'
   // params: query
  });
}
/**
 * 获取地区的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/area/list',
    method: 'get',
    params: query
  });
}

/**
 * 通过地区ID获取该地区的数据
 * **/
export function getArea(id) {
  return fetch({
    url: '/api/admin/area/' + id,
    method: 'get'
  })
}

/**
 * 提交更新的地区数据
 * **/
export function putArea(id, obj) {
  return fetch({
    url: '/api/admin/area/' + id,
    method: 'put',
    data: obj
  })
}

/**
 * 提交新增的地区数据
 * **/
export function addArea(obj) {
  return fetch({
    url: '/api/admin/area',
    method: 'post',
    data: obj
  });
}

/**
 * 通过地区ID删除地区
 * **/
export function delArea(id) {
  return fetch({
    url: '/api/admin/area/delete/' + id,
    method: 'delete'
  })
}
/**
恢复地区

**/
export function recoverArea(id){
 return fetch({
    url: '/api/admin/area/revocer/' + id,
    method: 'put'
  })
}


export function getProvinceMap() {
  return fetch({
    url: '/api/admin/area/getProvinceMap',
    method: 'get'
  });
}

export function getChildAreaMap(id) {
  return fetch({
    url: '/api/admin/area/getChildAreaMap/'+id,
    method: 'get'
  });
}

export function getAreaMap() {
  return fetch({
    url: '/api/admin/area/getAreaMap',
    method: 'get'
  });
}

/**
 * 查询地区父结点
 * @param id
 */
export function getParentsByAreaId(id) {
  return fetch({
    url: '/api/admin/area/getParentsByAreaId/'+id,
    method: 'get'
  });
}

/**
 * 查询某个区域下的所以子区域的树状结构
 * @param id
 */
export function childTree(query) {
  return fetch({
    url: '/api/admin/area/childTree',
    method: 'get',
    params: query
  });
}
