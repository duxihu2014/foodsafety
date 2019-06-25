import fetch from 'utils/fetch';


/**
 * 获取list数据
 * **/
export function page(query) {
    return fetch({
        url: '/api/admin/audio/list',
        method: 'get',
        params: query
    });
}

/**
 * 通过ID获取该id的数据
 * **/
export function getAudio(id) {
    return fetch({
        url: '/api/admin/audio/' + id,
        method: 'get'
    })
}

/**
 * 提交新增的数据
 * **/
export function addAudio(obj) {
    return fetch({
        url: '/api/admin/audio',
        method: 'post',
        data: obj
    });
}

/**
 * 提交更新的数据
 * **/
export function putAudio(id, obj) {
    return fetch({
        url: '/api/admin/audio/' + id,
        method: 'put',
        data: obj
    })
}

/**
 * 通过ID删除数据
 * **/
export function delAudio(id) {
    return fetch({
        url: '/api/admin/audio/' + id,
        method: 'delete'
    })
}

export function delObjs(ids) {
    return fetch({
        url: '/api/admin/audio/all/' + ids,
        method: 'delete'
    })
}
/**
 * 检查音频编码是否存在
 * @param {*} param
 */
export function checkAudioNo(param) {
    return fetch({
        url: '/api/admin/audio/checkAudioNo',
        params: param,
        method: 'get'
    })
}
/**
 * 检查音频名称是否存在
 * @param {*} param
 */
export function checkAudioName(param) {
    return fetch({
        url: '/api/admin/audio/checkAudioName',
        params: param,
        method: 'get'
    })
}
//得到所有状态为3的企业信息
export function getAllEnterpriseMap() {
    return fetch({
      url: '/api/admin/audio/getAllEnterpriseMap',
      method: 'get'
    })
}

export function getAudioMap(param) {
  return fetch({
    url: '/api/admin/audio/getAudioMap',
    params: param,
    method: 'get'
  })
}
