import fetch from 'utils/fetch';



export function getChangeById(id) {
  return fetch({
    url: 'api/enterpriseMaterial/getChange/' + id,
    method: 'get',
  })
}

export function verifyMaterial(obj) {
  return fetch({
    url: 'api/enterpriseMaterial/verify',
    method: 'post',
    data: obj,
  })
}

/*获取企业产品信息列表*/
export function page(query) {
  return fetch({
    url: '/api/enterpriseMaterial/list',
    method: 'get',
    params: query
  });
}
/**
 * 通过ID企业原料数据
 * */

export function getObj(id) {
  return fetch({
    url: '/api/enterpriseMaterial/' + id,
    method: 'get'
  })
}
/**
 * 通过ID企业原料数据包含变更状态
 * */
export function getEnterpriseMaterial(id) {
  return fetch({
    url: '/api/enterpriseMaterial/getEnterpriseMaterial/' + id,
    method: 'get'
  })
}
/*新增*/
export function addEnterpriseMaterial(obj) {
  return fetch({
    url: 'api/enterpriseMaterial/add',
    method: 'post',
    data: obj
  })
}

/*修改*/
export function changeEnterpriseMaterial(obj) {
  return fetch({
    url: 'api/enterpriseMaterial/change',
    method: 'post',
    data: obj
  })
}

export function delObj(id) {
  return fetch({
    url: 'api/enterpriseMaterial/delete/' + id,
    method: 'post'
  })
}
/******************企业原料审批查询接口---start*****************/
export function verifyQuery(query) {
  return fetch({
    url: 'api/enterpriseMaterial/verify/query' ,
    method: 'get',
    params:query,
  })
}
/******************企业原料审批查询接口---end*****************/