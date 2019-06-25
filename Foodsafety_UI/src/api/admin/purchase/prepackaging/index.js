import fetch from 'utils/fetch';


/**
 * 获取list数据
 * **/
export function page(query) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/list',
        method: 'get',
        params: query
    });
}

/**
 * 通过ID获取该id的数据
 * **/
export function getObj(id) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/' + id,
        method: 'get'
    })
}

/**
 * 提交新增的数据
 * **/
export function addObj(obj) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/create',
        method: 'post',
        data: obj,
        headers: { 'Content-Type': 'multipart/form-data' }
    });
}

/**
 * 提交更新的数据
 * **/
export function putObj(obj) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/update',
        method: 'post',
        data: obj,
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}

/**
 * 通过ID删除数据
 * **/
export function delObj(id) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/' + id,
        method: 'delete'
    })
}

export function delObjs(ids) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/all/' + ids,
        method: 'delete'
    })
}
//获得 指定企业与原料类型的原料信息
export function getMaterialsMap(id, materialCategory) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/getMaterialsMap/' + id + '/' + materialCategory,
        method: 'get'
    })
}
//获得根据条件（企业Id或原料类型）的所有原料信息
export function getAllMaterialsMap(query) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/getMaterialsMapByConditon',
        method: 'get',
        params: query
    })
}
//
export function getMaterial(id) {
    return fetch({
        url: '/api/admin/purchase/prepackaging/getMaterialObj/' + id,
        method: 'get'
    })
}