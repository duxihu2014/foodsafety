import fetch from 'utils/fetch';


export function page(query) {
    return fetch({
        url: '/api/admin/complainEvaluate/list',
        method: 'get',
        params: query
    });
}
export function getObj(id) {
    return fetch({
        url: '/api/admin/complainEvaluate/' + id,
        method: 'get'
    })
}
/**
 * 
 * **/
export function putObj(obj) {
    return fetch({
        url: '/api/admin/complainEvaluate/charge',
        method: 'post',
        data: obj
    })
}

/**
 * 
 * **/
export function addObj(obj) {
    return fetch({
        url: '/api/admin/complainEvaluate/add',
        method: 'post',
        data: obj
    });
}

export function delObj(id) {
    return fetch({
        url: '/api/admin/complainEvaluate/' + id,
        method: 'delete'
    })
}
export function delObjs(ids) {
    return fetch({
        url: '/api/admin/complainEvaluate/delete/' + ids,
        method: 'delete'
    })
}

/**受理回复 */
export function evaluateEdit(id, param) {
    return fetch({
        url: '/api/admin/complainEvaluate/evaluateEdit/' + id,
        params: param,
        method: 'get'
    })
}
/**受理 */
export function acceptance(id) {
    return fetch({
        url: '/api/admin/complainEvaluate/acceptance/' + id,
        // params: param,
        method: 'get'
    })
}
/**处理 */
export function handleEdit(id, param) {
    return fetch({
        url: '/api/admin/complainEvaluate/handleEdit/' + id,
        params: param,
        method: 'get'
    })
}