import fetch from 'utils/fetch';
/*获取用户企业表的信息*/
export function getUserEnterprise(query) {
  return fetch({
    url: 'api/admin/user/getUserEnterprise',
    method: 'get',
    params: query
  })
}

/*获取企业规模修改信息表*/
export function getEnterpriseBaseChange(query) {
  return fetch({
    url: 'api/enterpriseBase/getChange',
    method: 'get',
    params: query
  })
}
/*根据企业id获取企业基本信息*/
export function getEnterpriseBase(query) {
  return fetch({
    url: 'api/enterpriseBase/getInfo',
    method: 'get',
    params: query
  })
}

/*修改企业基本信息*/
export function changeEnterpriseBase(obj) {
  return fetch({
    url: 'api/enterpriseBase/change',
    method: 'post',
    data: obj
  })
}

export function getEnterpriseByGrid(query) {
  return fetch({
    url: 'api/enterpriseBase/getEnterpriseByGrid',
    method: 'get',
    params: query
  })
}

export function getEnterpriseByName(query) {
  return fetch({
    url: 'api/enterpriseBase/all',
    method: 'get',
    params: query
  })
}

/******************企业基本信息审批接口--start*********************/
/*获取基本信息审批数据*/
export function getBaseVerifyPage(query) {
  return fetch({
    url: 'api/enterpriseVerify/getPage',
    method: 'get',
    params: query
  })
}
/*根据变更id获取企业基本信息变更表数据*/
export function getChangeById(id) {
  return fetch({
    url: 'api/enterpriseBase/getChange/' + id,
    method: 'get',
  })
}
/*基本信息审核*/
export function verifyEnterpriseBase(obj) {
  return fetch({
    url: 'api/enterpriseBase/verify' ,
    method: 'post',
    data:obj,
  })
}
/******************企业基本信息审批接口--end*********************/
/******************基本信息审批查询接口---start*****************/
export function baseVerifyQuery(query) {
  return fetch({
    url: 'api/enterpriseBase/verify/query' ,
    method: 'get',
    params:query,
  })
}
/******************基本信息审批查询接口---end*****************/
