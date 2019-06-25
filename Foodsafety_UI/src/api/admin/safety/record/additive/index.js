import fetch from 'utils/fetch';


/**
 * 获取list数据
 * **/
export function page(query) {
    return fetch({
        url: '/api/admin/additiveUseage/list',
        method: 'get',
        params: query
    });
}

/**
 * 通过ID获取该id的数据
 * **/
export function getObj(id) {
    return fetch({
        url: '/api/admin/additiveUseage/' + id,
        method: 'get'
    })
}

/**
 * 提交新增的数据
 * **/
export function addObj(obj) {
    return fetch({
        url: '/api/admin/additiveUseage',
        method: 'post',
        data: obj
    });
}

/**
 * 提交更新的数据
 * **/
export function putObj(id, obj) {
    return fetch({
        url: '/api/admin/additiveUseage/' + id,
        method: 'put',
        data: obj
    })
}

/**
 * 通过ID删除数据
 * **/
export function delObj(id) {
    return fetch({
        url: '/api/admin/additiveUseage/' + id,
        method: 'delete'
    })
}

export function delObjs(ids) {
    return fetch({
        url: '/api/admin/additiveUseage/all/' + ids,
        method: 'delete'
    })
}
//得到指定企业ID下的采购编号
export function getPurchaseCombo(params) {
    return fetch({
      url: '/api/admin/additiveUseage/getPurchaseCombo',
      method: 'get',
      params:params
    })
  }