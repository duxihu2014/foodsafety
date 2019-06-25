import fetch from 'utils/fetch';


/**
 * 获取厂商的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/vender/list',
    method: 'get',
    params: query
  });
}

/**
 * 通过ID获取该厂商的数据
 * **/
export function getVender(id) {
  return fetch({
    url: '/api/admin/vender/' + id,
    method: 'get'
  })
}

/**
 * 提交更新的厂商数据
 * **/
export function putVender(id, obj) {
  return fetch({
    url: '/api/admin/vender/' + id,
    method: 'put',
    data: obj
  })
}

/**
 * 提交新增的厂商数据
 * **/
export function addVender(obj) {
  return fetch({
    url: '/api/admin/vender',
    method: 'post',
    data: obj
  });
}

/**
 * 通过ID删除厂商
 * **/
export function delVender(id) {
  return fetch({
    url: '/api/admin/vender/all/' + id,
    method: 'delete'
  })
}

/**
恢复厂商

**/
export function recoverVender(ids){
 return fetch({
    url: '/api/admin/vender/revocer/' + ids,
    method: 'put'
  })
}

/**判断厂商编码是否存在**/

export function isExistVenderNo(no){
 return fetch({
    url: '/api/admin/vender/isExistVenderNo/' + no,
    method: 'put'
  })
}