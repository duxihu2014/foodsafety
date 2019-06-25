import fetch from 'utils/fetch';

/******************企业规模管理接口--start*********************/
/*获取企业规模信息*/
export function getEnterpriseScale(query) {
  return fetch({
    url: 'api/enterpriseScale/all',
    method: 'get',
    params: query
  })
}
/*获取企业规模修改信息表*/
export function getEnterpriseScaleChange(query) {
  return fetch({
    url: 'api/enterpriseScale/getChange',
    method: 'get',
    params: query
  })
}
/*新增企业规模信息*/
export function addEnterpriseScale(obj) {
  return fetch({
    url: 'api/enterpriseScale/add',
    method: 'post',
    data: obj
  })
}

/*修改企业规模信息*/
export function updateEnterpriseScale(obj) {
  return fetch({
    url: 'api/enterpriseScale/update',
    method: 'post',
    data: obj
  })
}

/******************企业规模管理接口--end*********************/
/******************企业规模审批接口--start*********************/
/*获取规模审批数据*/
export function getScaleVerifyPage(query) {
  return fetch({
    url: 'api/enterpriseVerify/getPage',
    method: 'get',
    params: query
  })
}
/*根据变更id获取企业规模变更表数据*/
export function getChangeById(id) {
  return fetch({
    url: 'api/enterpriseScale/getChange/' + id,
    method: 'get',
  })
}
/*规模审核*/
export function verifyEnterpriseScale(obj) {
  return fetch({
    url: 'api/enterpriseScale/verify' ,
    method: 'post',
    data:obj,
  })
}
/******************企业规模审批接口--end*********************/
/******************企业规模审批查询接口---start*****************/
export function scaleVerifyQuery(query) {
  return fetch({
    url: 'api/enterpriseScale/verify/query' ,
    method: 'get',
    params:query,
  })
}
/******************企业规模审批查询接口---end*****************/
