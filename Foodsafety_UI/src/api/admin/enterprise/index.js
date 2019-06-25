import fetch from 'utils/fetch';

export function getRegisterEnterprisePage(query) {
  return fetch({
    url: 'api/admin/enterprise/register/list',
    method: 'get',
    params: query
  });
}
export function getResource(id) {
  return fetch({
    url: 'api/admin/resouce/'+ id,
    method: 'get'
  });
}

export function registerEnterpriseAudit(obj) {
  return fetch({
    url: 'api/admin/enterprise/register/audit',
    method: 'post',
    data: obj,
  });
}
//获取所有已建档企业
export function getEnterprise(query) {
  return fetch({
    url: '/api/enterprise/all',
    method: 'get',
    params: query
  })
}

//分页获取所有已建档企业
export function getEnterprisePage(query) {
  return fetch({
    url: '/api/enterpriseBase/list',
    method: 'get',
    params: query
  })
}

//分页获取所有已建档企业
export function getOnsiteTask(query) {
  return fetch({
    url: '/api/enterpriseBase/getOnsiteTask',
    method: 'get',
    params: query
  })
}


/***企业档案查询*****/

export function getEnterpriseAllInfo(query) {
  return fetch({
    url: 'api/enterpriseBase/getEnterpriseAllInfo' ,
    method: 'get',
    params:query,
  })
}


export function getEnterpriseDetail(id) {
  return fetch({
    url: 'api/enterpriseBase/getEnterpriseDetail/'+id ,
    method: 'get'
  })
}


export function getEnterpriseScale(id) {
  return fetch({
    url: 'api/enterpriseScale/getEnterpriseScale/'+id ,
    method: 'get'
  })
}


export function getCertificateDetail(id) {
  return fetch({
    url: 'api/enterpriseCertificate/getCertificateDetail/'+id ,
    method: 'get'
  })
}


export function getEquipmentDetail(id) {
  return fetch({
    url: 'api/enterpriseEquipment/getEquipmentDetail/'+id ,
    method: 'get'
  })
}


export function getProductDetail(id) {
  return fetch({
    url: 'api/enterpriseProduct/getProductDetail/'+id ,
    method: 'get'
  })
}


export function setRegulatoryLevel(obj) {
  return fetch({
    url: '/api/enterpriseBase/addRegulatoryLevel',
    method: 'post',
    data: obj,
  });
}

