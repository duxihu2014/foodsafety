import fetch from 'utils/fetch';

/**
 * 获取网格企业数量统计信息
 * @param obj
 */
export function getGridsEnterpriseStatistics(obj) {
  return fetch({
    url: 'api/dashboard/grid/enterpriseStatistics',
    method: 'get',
    params: obj
  })
}

/**
 * 获取网格证照预警数量统计信息
 * @param obj
 */
export function getGridsCertificateStatistics(obj) {
  return fetch({
    url: 'api/dashboard/grid/certificateStatistics',
    method: 'get',
    params: obj
  })
}
/**
 * 获取网格事物数量统计信息
 * @param obj
 */
export function getGridsTaskStatistics(obj) {
  return fetch({
    url: 'api/dashboard/grid/taskStatistics',
    method: 'get',
    params: obj
  })
}
export function getEnterpriseByGrid(obj) {
  return fetch({
    url: 'api/enterpriseBase/getByGrid',
    method: 'get',
    params: obj
  })
}

/**
 * 根据网格分组获取企业监管等级分类数量统计
 *
 */
export function getSupervisionByGrid(obj) {
  return fetch({
    url: '/api/admin/supervisory/grid/getSupervisionByGrid',
    method: 'get',
    params: obj
  })
}
/**
 * 根据区域分组获取企业监管等级分类数量统计
 *
 */
export function getSupervisionByArea(obj) {
  return fetch({
    url: 'api/dashboard/getEnterpriseCountBySupervise',
    method: 'get',
    params: obj
  })
}
/**
 * 根据网格分组获取企业经济类型分类数量统计
 *
 */
export function getSubjectClassificationByGrid(obj) {
  return fetch({
    url: 'api/enterpriseBase/getSubjectClassificationByGrid',
    method: 'get',
    params: obj
  })
}
/**
 * 根据区域分组获取企业经济类型分类数量统计
 *
 */
export function getSubjectClassificationByArea(obj) {
  return fetch({
    url: 'api/dashboard/getEnterpriseCountBySubject',
    method: 'get',
    params: obj
  })
}
/**
 * 查询当前网格未完成的回访事件
 * @param obj
 */
export function getRevisitPlanTask(obj) {
  return fetch({
    url: 'api/dashboard/getRevisitPlanTask',
    method: 'get',
    params: obj
  })
}
/**
 * 查询当前网格未完成的日常检查计划
 * @param obj
 */
export function getEnterpriseInspectionPlanTask(obj) {
  return fetch({
    url: 'api/dashboard/getEnterpriseInspectionPlanTask',
    method: 'get',
    params: obj
  })
}
/**
 *查询当前网格未完成的现场核查事件
 * @param obj
 */
export function getOnsiteTask(obj) {
  return fetch({
    url: 'api/dashboard/getOnsiteTask',
    method: 'get',
    params: obj
  })
}

/**
 * 根据条件获取企业总数
 * @param obj
 */
export function getEnterpriseTotal(obj) {
  return fetch({
    url: 'api/dashboard/getEnterpriseTotal',
    method: 'get',
    params: obj
  })
}
/**
 * 根据条件获取待审核企业数量
 * @param obj
 */
export function getRegisterTotal(obj) {
  return fetch({
    url: 'api/dashboard/getRegisterTotal',
    method: 'get',
    params: obj
  })
}
/**
 * 根据条件获取网格分组企业数量
 * @param obj
 */
export function getGridEnterpriseCount(obj) {
  return fetch({
    url: 'api/dashboard/getGridEnterpriseCount',
    method: 'get',
    params: obj
  })
}
/**
 * 根据条件获取安全事物汇总数据
 * @param obj
 */
export function getSafetyGrid(obj) {
  return fetch({
    url: 'api/dashboard/getSafetyGrid',
    method: 'get',
    params: obj
  })
}
/**
 * 查询当前地区企业的积分排名
 * @param obj
 */
export  function  getCycleScore(obj) {
  return fetch({
    url: 'api/dashboard/getCycleScore',
    method: 'get',
    params: obj
  })
}

/**
 * 查询当前地区预警数量
 * @return
 */
export  function  getWarning(obj) {
  return fetch({
    url: 'api/dashboard/getWarning',
    method: 'get',
    params: obj
  })
}

/**
 * 查询当前企业健康证预警数量
 * @return
 */
export  function  getHealthWarning(obj) {
  return fetch({
    url: 'api/dashboard/getHealthWarning',
    method: 'get',
    params: obj
  })
}

/**
 * 获取企业信息
 * @param obj
 */
export  function  getEnterpriseInfo(obj) {
  return fetch({
    url: 'api/dashboard/getEnterpriseInfo',
    method: 'get',
    params: obj
  })
}
/**
 * 获取网格信息
 * @param obj
 */
export  function  getGridsInfo(obj) {
  return fetch({
    url: 'api/dashboard/getGridsInfo',
    method: 'get',
    params: obj
  })
}

/**
 * 统计日常检查结果
 * @param query
 */
export function inspectionResult(query) {
  return fetch({
    url: 'api/dashboard/inspectionResult',
    method: 'get',
    params: query
  })
}
export function getAlarmCount(query) {
  return fetch({
    url: 'api/dashboard/getAlarmCount',
    method: 'get',
    params: query
  })
}
/**
 * 根据条件查询温湿度报警数量 （一个月内）
 * @param query
 */
export function getSensorCount(query) {
  return fetch({
    url: 'api/dashboard/sensor/count',
    method: 'get',
    params: query
  })
}

/**
 * 根据条件查询温湿度报警数量一周内趋势
 * @param query
 */
export function getSensorTrend(query) {
  return fetch({
    url: 'api/dashboard/sensor/trend',
    method: 'get',
    params: query
  })
}

/**
 * 根据条件查询视频报警数量一周内趋势
 * @param query
 */
export function getVideoTrend(query) {
  return fetch({
    url: 'api/dashboard/video/trend',
    method: 'get',
    params: query
  })
}

export function getEnterpriseAlarmCount(query) {
  return fetch({
    url: 'api/dashboard/getEnterpriseAlarmCount',
    method: 'get',
    params: query
  })
}
export function getRecordMorningCheck(query) {
  return fetch({
    url: 'api/dashboard/getRecordMorningCheck',
    method: 'get',
    params: query
  })
}

/**
 * 根据条件查询立案投诉统计
 * @param query
 */
export function getComplainCount(query) {
  return fetch({
    url: 'api/dashboard/getComplainCount',
    method: 'get',
    params: query
  })
}

export function getAlarmRanking(query) {
  return fetch({
    url: 'api/dashboard/getAlarmRanking',
    method: 'get',
    params: query
  })
}
