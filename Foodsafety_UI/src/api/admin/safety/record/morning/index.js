import fetch from 'utils/fetch';

/**
 * 获取晨检的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/producesafety/recordMorningCheck/list',
    method: 'get',
    params: query
  });
}


/**
 * 通过ID获取该晨检的数据
 * **/
export function getRecordMorningCheck(id) {
  return fetch({
    url: '/api/admin/producesafety/recordMorningCheck/' + id,
    method: 'get'
  })
}


/**
 * 提交新增的晨检数据
 * **/
export function addRecordMorningCheck(obj) {
  return fetch({
    url: '/api/admin/producesafety/recordMorningCheck/',
    method: 'post',
    data: obj
  });
}


/**
 * 更新的废晨检数据
 * **/
export function updateRecordMorningCheck(id, obj) {
  return fetch({
    url: '/api/admin/producesafety/recordMorningCheck/' + id,
    method: 'put',
    data: obj
  })
}

export function getCateringStaffMap(query) {
 return fetch({
    url: '/api/admin/producesafety/recordMorningCheck/getCateringStaffMap',
    method: 'get',
    params: query
  });
}
