import fetch from 'utils/fetch';


/******************企业证照管理接口--start*********************/
export function page(query) {
  return fetch({
    url: '/api/enterpriseCertificate/getPage',
    method: 'get',
    params: query
  });
}
/**
 * 通过ID企业证照数据
 * */
export function getObj(id) {
  return fetch({
    url: '/api/enterpriseCertificate/' + id,
    method: 'get'
  })
}
/*修改企业证照信息*/
export function changeEnterpriseCertificate(obj) {
  return fetch({
    url: 'api/enterpriseCertificate/change',
    method: 'post',
    data: obj
  })
}
/*新增企业证照信息*/
export function addEnterpriseCertificate(obj) {
  return fetch({
    url: 'api/enterpriseCertificate/add',
    method: 'post',
    data: obj
  })
}
/*删除企业证照信息*/
export function delObj(id) {
  return fetch({
    url: 'api/enterpriseCertificate/delete/'+id,
    method: 'post'
  })
}
/*获取证照变更数据*/
export function getChangeByCertificateId(query) {
  return fetch({
    url: '/api/enterpriseCertificate/getChangeByCertificateId',
    method: 'get',
    params: query
  });
}
/******************企业证照管理接口--end*********************/


/******************企业证照审批接口--start*********************/
/*获取证照审批数据*/
export function getCertificateVerifyPage(query) {
  return fetch({
    url: 'api/enterpriseVerify/getPage',
    method: 'get',
    params: query
  })
}
/*根据变更id获取企业证照变更表数据*/
export function getChangeById(id) {
  return fetch({
    url: 'api/enterpriseCertificate/getChange/' + id,
    method: 'get',
  })
}
/*证照审核*/
export function verifyEnterpriseCertificate(obj) {
  return fetch({
    url: 'api/enterpriseCertificate/verify' ,
    method: 'post',
    data:obj,
  })
}
/******************企业证照审批接口--end*********************/
/******************企业证照审批查询接口---start*****************/
export function certificateVerifyQuery(query) {
  return fetch({
    url: 'api/enterpriseCertificate/verify/query' ,
    method: 'get',
    params:query,
  })
}
/******************企业证照审批查询接口---end*****************/
