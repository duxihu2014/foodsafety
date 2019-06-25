import fetch from 'utils/fetch';

/******************企业产品管理接口--start*********************/
/*获取企业产品信息列表*/
export function page(query) {
  return fetch({
    url: '/api/enterpriseProduct/list',
    method: 'get',
    params: query
  });
}
/**
 * 通过ID企业产品数据
 * */
export function getObj(id) {
  return fetch({
    url: '/api/enterpriseProduct/' + id,
    method: 'get'
  })
}

export function getEnterpriseProduct(id) {
  return fetch({
    url: '/api/enterpriseProduct/getEnterpriseProduct/' + id,
    method: 'get'
  })
}

/*新增*/
export function addEnterpriseProduct(obj) {
  return fetch({
    url: 'api/enterpriseProduct/add',
    method: 'post',
    data: obj
  })
}

/*修改*/
export function changeEnterpriseProduct(obj) {
  return fetch({
    url: 'api/enterpriseProduct/change',
    method: 'post',
    data: obj
  })
}

export function delObj(id) {
  return fetch({
    url: 'api/enterpriseProduct/delete/'+id,
    method: 'post'    
  })
}

export function fetchAllProductTypeMap(query) {
  return fetch({
      url: '/api/enterpriseProduct/getAllProductTypeMap',
      method: 'get',
      params: query
  })
}

export function fetchProductTypeTree() {
  return fetch({
    url: '/api/enterpriseProduct/productTypetree',
    method: 'get',
  });
}

/******************企业证照管理接口--end*********************/

export function getChangeById(id) {
  return fetch({
    url: 'api/enterpriseProduct/getChange/' + id,
    method: 'get',
  })
}

export function verifyProduct(obj) {
	return fetch({
     url: 'api/enterpriseProduct/verify',
     method: 'post',
     data:obj,
  })
}

export function getProductTypeMap() {
return fetch({
     url: 'api/enterpriseProduct/getProductTypeMap',
     method: 'get'
  })
}

/******************企业产品审批查询接口---start*****************/
export function verifyQuery(query) {
  return fetch({
    url: 'api/enterpriseProduct/verify/query' ,
    method: 'get',
    params:query,
  })
}
/******************企业产品审批查询接口---end*****************/