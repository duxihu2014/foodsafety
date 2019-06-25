import fetch from 'utils/fetch';


/**
 * 获取设备分组list数据
 * **/
export function page(query) {
    return fetch({
        url: '/api/admin/enterpriseGroup/list',
        method: 'get',
        params: query
    });
}

/**
 * 设备分组树
 */
export function groupTree(query) {
  return fetch({
    url: '/api/admin/enterpriseGroup/groupTree',
    method: 'get',
    params: query
  });
}

/**
 * 提交新增的数据
 * **/
export function addGroup(obj) {
  return fetch({
    url: '/api/admin/enterpriseGroup',
    method: 'post',
    data: obj
  });
}

/**
 * 提交更新的数据
 * **/
export function putGroup(id, obj) {
  return fetch({
    url: '/api/admin/enterpriseGroup/' + id,
    method: 'put',
    data: obj
  })
}

/**
 * 通过ID删除数据
 * **/
export function delGroup(id) {
  return fetch({
    url: '/api/admin/enterpriseGroup/' + id,
    method: 'delete'
  })
}

export function checkGroupVideo(param) {
  return fetch({
    url: '/api/admin/enterpriseGroup/checkGroupVideo',
    params: param,
    method: 'get'
  })
}

export function checkGroupName(param) {
  return fetch({
    url: '/api/admin/enterpriseGroup/checkEnterpriseGroupName',
    params: param,
    method: 'get'
  })
}
