import fetch from 'utils/fetch';

export function page(query) {
    return fetch({
        url: '/api/admin/dict/list',
        method: 'get',
        params: query
    });
}

export function addObj(obj) {
    return fetch({
        url: '/api/admin/dict',
        method: 'post',
        data: obj
    });
}

export function getObj(id) {
    return fetch({
        url: '/api/admin/dict/' + id,
        method: 'get'
    })
}

export function delObj(id) {
    return fetch({
        url: '/api/admin/dict/delete/' + id,
        method: 'delete'
    })
}

export function putObj(id, obj) {
    return fetch({
        url: '/api/admin/dict/' + id,
        method: 'put',
        data: obj
    })
}
