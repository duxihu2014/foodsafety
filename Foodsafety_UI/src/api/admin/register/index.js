import fetch from 'utils/fetch';

export function getDict() {
  return fetch({
    url: 'register/api/loadStaticData',
    method: 'get',
  });
}

export function getAreaTree() {
  return fetch({
    url: 'register/api/area/tree',
    method: 'get',
  });
}
export function commitRegister(obj) {
  return fetch({
    url: 'register/api/commit',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/json'},
  });
}
export function validateUserName(query) {
  return fetch({
    url: 'register/api/validateUserName',
    method: 'get',
    params: query
  });
}
export function validateOrganizingInstitutionBarCode(query) {
  return fetch({
    url: 'register/api/validateOrganizingInstitutionBarCode',
    method: 'get',
    params: query
  });
}

export function registerLogin(query) {
  return fetch({
    url: 'register/api/login',
    method: 'get',
    params: query
  });
}

export function getResource(id) {
  return fetch({
    url: 'register/api/getResource/'+ id,
    method: 'get'
  });
}

export function updateRegister(obj) {
  return fetch({
    url: 'register/api/updateRegister',
    method: 'post',
    data: obj,
    headers: {'Content-Type': 'multipart/json'},
  });
}

export function getVerify(query) {
  return fetch({
    url: 'register/api/getVerify',
    method: 'get',
    params: query
  });
}

export function getMulResource(ids) {
  return fetch({
    url: 'register/api/getMulResource/'+ ids,
    method: 'get'
  });
}