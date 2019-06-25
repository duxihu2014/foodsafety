import fetch from 'utils/fetch';

/**
 * 获取废弃物处理的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/producesafety/recordWaste/list',
    method: 'get',
    params: query
  });
}

/**
获取企业MAP
**/
export function getEnterpriseBaseMap() {
 return fetch({
    url: '/api/admin/producesafety/recordWaste/getEnterpriseBaseMap',
    method: 'get'
  });
}


/**
获取相应的(1供货商、2生产商、3餐饮用具消毒、4废弃物回收)
**/
export function getSupplierMap(id) {
 return fetch({
    url: '/api/admin/producesafety/recordWaste/getSupplierMap/' + id,
    method: 'get'
  });
}


/**
 * 通过ID获取该废弃物的数据
 * **/
export function getRecordWaste(id) {
  return fetch({
    url: '/api/admin/producesafety/recordWaste/' + id,
    method: 'get'
  })
}

/**
 * 提交新增的废弃物数据
 * **/
export function addRecordWaste(obj) {
  return fetch({
    url: '/api/admin/producesafety/recordWaste/',
    method: 'post',
    data: obj
  });
}


/**
 * 更新的废弃物数据
 * **/
export function updateRecordWaste(id, obj) {
  return fetch({
    url: '/api/admin/producesafety/recordWaste/' + id,
    method: 'post',
    data: obj
  })
}
