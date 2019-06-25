import fetch from 'utils/fetch';
//图片上传(单个图片)
export function upload(obj){
  return fetch({
    url: '/api/admin/img/upoad',
    method: 'post',
    data: obj
  });
}

export  function uploadDo2(obj){
  return fetch({
    url: '/api/admin/img/uploadDo2',
    method: 'post',
    data: obj
  });
}
