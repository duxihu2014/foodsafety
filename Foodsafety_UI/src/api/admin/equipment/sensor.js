import fetch from 'utils/fetch';


/**
 * 获取设备分组list数据
 * **/
export function page(query) {
    return fetch({
        url: '/api/admin/sensor/list',
        method: 'get',
        params: query
    });
}

/**
 * 提交新增的数据
 * **/
export function addEquip(obj) {
  return fetch({
    url: '/api/admin/sensor',
    method: 'post',
    data: obj
  });
}

/**
 * 提交更新的数据
 * **/
export function putEquip(id, obj) {
  return fetch({
    url: '/api/admin/sensor/' + id,
    method: 'put',
    data: obj
  })
}

/**
 * 通过ID删除数据
 * **/
export function delEquip(id) {
  return fetch({
    url: '/api/admin/sensor/' + id,
    method: 'delete'
  })
}

export function delObjs(ids) {
  return fetch({
    url: '/api/admin/sensor/all/' + ids,
    method: 'delete'
  })
}

export function checkEquNo(param) {
  return fetch({
    url: '/api/admin/sensor/checkSensorInfoNo',
    params: param,
    method: 'get'
  })
}

//监控报表数据
export function report(query) {
  return fetch({
    url: '/api/admin/sensor/report',
    method: 'get',
    params: query
  });
}
//当前温湿度
export function getCurrentMonitor(query) {
  return fetch({
    url: '/api/admin/sensor/getCurrentMonitor',
    method: 'get',
    params: query
  });
}
//报警监控报表数据
export function alarmReport(query) {
  return fetch({
    url: '/api/admin/sensor/alarmReport',
    method: 'get',
    params: query
  });
}
