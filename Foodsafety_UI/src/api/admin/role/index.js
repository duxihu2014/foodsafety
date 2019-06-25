import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/role/list',
    method: 'get',
    params: query
  });
}
//获取所有部门
export function getAllRole(obj) {
  return fetch({
    url: '/api/admin/role/all',
    method: 'get',
    params: obj
  })
}
export function addObj(obj) {
  return fetch({
    url: '/api/admin/role',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/role/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/admin/role/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/role/' + id,
    method: 'put',
    data: obj
  })
}

export function getUsers(id) {
  return fetch({
    url: '/api/admin/role/' + id + '/users',
    method: 'get'
  });
}

export function modifyUsers(roleId, userIds) {
  const obj = {};
  obj.roleId = roleId;
  obj.userIds = userIds;

  return fetch({
    url: '/api/admin/role/' + roleId + '/users',
    method: 'post',
    params: obj
  })
}


export function modifyMenuAuthority(roleId, data) {
  return fetch({
    url: '/api/admin/role/' + roleId + '/authority',
    method: 'post',
    params: data
  });
}


export function getMenuAuthority(roleId) {
  return fetch({
    url: '/api/admin/role/' + roleId + '/authority',
    method: 'get'
  });
}



export function delObjs(ids) {
  return fetch({
    url: '/api/admin/role/all/' + ids,
    method: 'delete'
  })
}

export function changeStatus(data) {
  return fetch({
    url: '/api/admin/role/changeStatus',
    method: 'put',
    params: data
  })
}
