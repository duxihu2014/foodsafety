import fetch from 'utils/fetch';

export function page(query) {
  return fetch({
    url: '/api/admin/user/list',
    method: 'get',
    params: query
  });
}
export function userPage(query) {
  return fetch({
    url: '/api/admin/user/getUserList',
    method: 'get',
    params: query
  });
}
export function addObj(obj) {
  return fetch({
    url: '/api/admin/user/add',
    method: 'post',
    data: obj
  });
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/user/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/admin/user/' + id,
    method: 'delete'
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/user/' + id,
    method: 'put',
    data: obj
  })
}


export function delObjs(ids) {
  return fetch({
    url: '/api/admin/user/all/' + ids,
    method: 'delete'
  })
}
//检查用户名是否重复
export function checkUserName(query) {
  return fetch({
    url: '/api/admin/user/checkUserName',
    method: 'get',
    params: query
  })
}


export function getRoles(id) {
  return fetch({
    url: '/api/admin/user/' + id + '/roles',
    method: 'get'
  });
}

export function modifyRoles(userId, roleIds) {
  const obj = {};
  obj.userId = userId;
  obj.roleIds = roleIds;

  return fetch({
    url: '/api/admin/user/' + userId + '/roles',
    method: 'post',
    params: obj
  })
}

export function getDepts(id) {
  return fetch({
    url: '/api/admin/user/' + id + '/depts',
    method: 'get'
  });
}

export function modifyDepts(userId, deptIds) {
  const obj = {};
  obj.userId = userId;
  obj.deptIds = deptIds;

  return fetch({
    url: '/api/admin/user/' + userId + '/depts',
    method: 'post',
    params: obj
  })
}

/** 检查是否关联了操作员 */
export function checkCompanyRationOper(ids) {
  return fetch({
    url: '/api/admin/user/checkCompanyBindOperator/' + ids,
    method: 'get'
  })
}

export function changeStatus(data) {
  return fetch({
    url: '/api/admin/user/changeStatus',
    method: 'put',
    params: data
  })
}


export function resetUserPwd(data) {
  return fetch({
    url: '/api/admin/user/resetUserPwd',
    method: 'put',
    data: data
  })
}

export function updateUserPwd(data) {
  return fetch({
    url: '/api/admin/user/updateUserPwd',
    method: 'put',
    data: data
  })
}

export function getAllUserMap() {
  return fetch({
    url: '/api/admin/user/getAllUserMap',
    method: 'get'
  })
}