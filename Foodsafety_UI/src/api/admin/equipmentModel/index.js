import fetch from 'utils/fetch';


/**
 * 获取设备类型的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/equipmentModel/list',
    method: 'get',
    params: query
  });
}

/**
 * 通过地区ID获取该设备类型的数据
 * **/
export function getEquipmentModel(id) {
  return fetch({
    url: '/api/admin/equipmentModel/' + id,
    method: 'get'
  })
}

/**
 * 提交更新的设备类型数据
 * **/
export function putEquipmentModel(id, obj) {
  return fetch({
    url: '/api/admin/equipmentModel/' + id,
    method: 'put',
    data: obj
  })
}

/**
 * 提交新增的设备类型数据
 * **/
export function addEquipmentModel(obj) {
  return fetch({
    url: '/api/admin/equipmentModel',
    method: 'post',
    data: obj
  });
}

/**
 * 通过ID删除设备类型
 * **/
export function delEquipmentModel(id) {
  return fetch({
    url: '/api/admin/equipmentModel/all/' + id,
    method: 'delete'
  })
}

/**
恢复设备型号

**/
export function recoverEquipmentModel(ids){
 return fetch({
    url: '/api/admin/equipmentModel/revocer/' + ids,
    method: 'put'
  })
}

/**获取厂商Map**/
export function getVenderMap(){
  return fetch({
    url:'/api/admin/equipmentModel/getVenderMap',
    method:'get'
  })
}

/**判断设备型号是否存在**/

export function isExistEquModel(no){
 return fetch({
    url: '/api/admin/equipmentModel/isExistEquModel/' + no,
    method: 'put'
  })
}

/**获取型号Map**/
export function getModelMap(){
  return fetch({
    url:'/api/admin/equipmentModel/getModelMap',
    method:'get'
  })
}
