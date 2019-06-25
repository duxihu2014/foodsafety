import fetch from 'utils/fetch';

export function changeClassification(obj) {
  return fetch({
    url: 'api/classification/detail/changeClassification',
    method: 'post',
    data: obj
  });
}

/**
 *  查询企业监管等级变更记录
 * @param id
 */
export function changeRecord(id) {
  return fetch({
    url: 'api/classification/detail/changeRecord/'+id,
    method: 'get'
  });
}

