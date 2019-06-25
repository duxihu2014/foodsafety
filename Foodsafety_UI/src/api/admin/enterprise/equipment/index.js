import fetch from 'utils/fetch';



export function getChangeById(id) {
  return fetch({
    url: 'api/enterpriseEquipment/getChange/' + id,
    method: 'get',
  })
}

export function verifyEquipment(obj) {
  return fetch({
    url: 'api/enterpriseEquipment/verify',
    method: 'post',
    data: obj,
  })
}

/*获取企业产品信息列表*/
export function page(query) {
  return fetch({
    url: '/api/enterpriseEquipment/list',
    method: 'get',
    params: query
  });
}
/**
 * 通过ID企业设备数据
 * */
export function getObj(id) {
  return fetch({
    url: '/api/enterpriseEquipment/' + id,
    method: 'get'
  })
}

export function getEnterpriseEqu(id) {
  return fetch({
    url: '/api/enterpriseEquipment/getEnterpriseEqu/' + id,
    method: 'get'
  })
}

/*新增*/
export function addEnterpriseEqu(obj) {
  return fetch({
    url: 'api/enterpriseEquipment/add',
    method: 'post',
    data: obj
  })
}

/*修改*/
export function changeEnterpriseEqu(obj) {
  return fetch({
    url: 'api/enterpriseEquipment/change',
    method: 'post',
    data: obj
  })
}

export function delObj(id) {
  return fetch({
    url: 'api/enterpriseEquipment/delete/' + id,
    method: 'post'
  })
}
/******************企业设备审批查询接口---start*****************/
export function verifyQuery(query) {
  return fetch({
    url: 'api/enterpriseEquipment/verify/query' ,
    method: 'get',
    params:query,
  })
}
/******************企业设备审批查询接口---end*****************/