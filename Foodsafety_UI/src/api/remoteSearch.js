import fetch from 'utils/fetch';

export function userSearch(name) {
  return fetch({
    url: '/search/user',
    method: 'get',
    params: { name }
  });
}


export function getData() {
  return fetch({
    url: '/jwt/loadStaticData',
    method: 'get'
  });
}
export function getAreaData() {
  return fetch({
    url: 'register/api/area/tree',
    method: 'get'
  });
}

