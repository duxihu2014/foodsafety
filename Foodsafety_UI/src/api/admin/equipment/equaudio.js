import fetch from 'utils/fetch';


/**
 * 根据音频ID得到已绑摄像头
 * **/
export function getBindCameraByAudioId(query) {
    return fetch({
        url: '/api/admin/audioequR/bindCameraData',
        method: 'get',
        params: query
    });
}

/**
 *  根据音频ID得到未绑摄像头
 * **/
export function getNoBindCameraByAudioId(query) {
    return fetch({
        url: '/api/admin/audioequR/noBindCameraData',
        method: 'get',
        params: query
    })
}
/**
 * 根据摄像头ID得到已绑音频
 * **/
export function getBindAudioByCameraId(query) {
    return fetch({
        url: '/api/admin/audioequR/bindAudioData',
        method: 'get',
        params: query
    });
}

/**
 *  根据摄像头ID得到未绑音频
 * **/
export function getNoBindAudioByCameraId(query) {
    return fetch({
        url: '/api/admin/audioequR/noBindAudioData',
        method: 'get',
        params: query
    })
}

/*绑定 **/
export function bindCamera(id,params) {
    return fetch({
        url: '/api/admin/audioequR/bindCamera/'+id,
        method: 'get',
        params: params
    })
}

export function bindAudio(id,params) {
    return fetch({
        url: '/api/admin/audioequR/bindAudio/'+id,
        method: 'get',
        params: params
    })
}

/**
 * 通过ID获取该id的数据
 * **/
export function getObj(id) {
    return fetch({
        url: '/api/admin/audioequR/' + id,
        method: 'get'
    })
}

/**
 * 提交新增的数据
 * **/
export function addObj(obj) {
    return fetch({
        url: '/api/admin/audioequR',
        method: 'post',
        data: obj
    });
}

/**
 * 提交更新的数据
 * **/
export function putObj(id, obj) {
    return fetch({
        url: '/api/admin/audioequR/' + id,
        method: 'put',
        data: obj
    })
}

/**
 * 通过ID删除数据
 * **/
export function delObj(id) {
    return fetch({
        url: '/api/admin/audioequR/' + id,
        method: 'delete'
    })
}
/**解绑 */
export function delObjs(ids) {
    return fetch({
        url: '/api/admin/audioequR/all/' + ids,
        method: 'delete'
    })
}

