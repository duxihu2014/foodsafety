import fetch from 'utils/fetch';


/**
 * 获取设备分组list数据
 * **/
export function page(query) {
    return fetch({
        url: '/api/admin/equipment/list',
        method: 'get',
        params: query
    });
}

/**
 * 提交新增的数据
 * **/
export function addEquip(obj) {
  return fetch({
    url: '/api/admin/equipment',
    method: 'post',
    data: obj
  });
}

/**
 * 提交更新的数据
 * **/
export function putEquip(id, obj) {
  return fetch({
    url: '/api/admin/equipment/' + id,
    method: 'put',
    data: obj
  })
}

/**
 * 通过ID删除数据
 * **/
export function delEquip(id) {
  return fetch({
    url: '/api/admin/equipment/' + id,
    method: 'delete'
  })
}

export function delObjs(ids) {
  return fetch({
    url: '/api/admin/equipment/all/' + ids,
    method: 'delete'
  })
}

export function checkEquNo(param) {
  return fetch({
    url: '/api/admin/equipment/checkEquipmentNo',
    params: param,
    method: 'get'
  })
}

export function play(id) {
  return fetch({
      url: '/api/admin/equipment/play/' + id,
      method: 'get'
  })
}

/**
 *
 * **/
export function pageEquipment(query) {
  return fetch({
    url: '/api/admin/equipment/list',
    method: 'get',
    params: query
  });
}
