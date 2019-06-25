import fetch from 'utils/fetch';

/**
 * 获取餐具消毒的list数据
 * **/
export function page(query) {
  return fetch({
    url: '/api/admin/producesafety/recordDisinfection/list',
    method: 'get',
    params: query
  });
}


/**
 * 通过ID获取餐具消毒的数据
 * **/
export function getRecordDisinfection(id) {
  return fetch({
    url: '/api/admin/producesafety/recordDisinfection/' + id,
    method: 'get'
  })
}


/**
 * 提交新增的消毒数据
 * **/
export function addRecordDisinfection(obj) {
  return fetch({
    url: '/api/admin/producesafety/recordDisinfection/',
    method: 'post',
    data: obj
  });
}


/**
 * 更新的消毒数据
 * **/
export function updateRecordDisinfection(id, obj) {
  return fetch({
    url: '/api/admin/producesafety/recordDisinfection/' + id,
    method: 'put',
    data: obj
  })
}