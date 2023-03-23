import request from '@/utils/request';
const pre = 'river-service';

interface ListParams {
  pageNum?: number;
  pageSize?: number;
  /** 当前项目阶段 1-开工 2-施工 3-完工 */
  stage?: '1' | '2' | '3';
  /** 当前处理状态 1-未处理 2-已处理  */
  type?: '1' | '2';
  /** 处理结果 0-正常 1-异常 */
  result?: '0' | '1';
}
/**
 * 巡查列表
 */
export function getBlueInspectList(params: ListParams) {
  return request({
    method: 'get',
    url: pre + '/blueInspect/list',
    params
  });
}

/**
 * 本次巡查详情信息
 */
export function getBlueInspectDetail(params: {
  /** 巡查id */
  inspectionId: string;
}) {
  return request({
    method: 'get',
    url: pre + '/blueInspect/detail',
    params
  });
}

/**
 * 巡查反馈
 */
export function feedbackBlueInspectDetail(data: {
  problems: any[];
  /** type 1-首次巡查 2-复核巡查  */
  type: '1' | '2';
}) {
  return request({
    method: 'post',
    url: pre + '/blueProblem/patrolFeedback',
    data,
    headers: { 'Content-Type': 'application/json' }
  });
}

//标签数量
export function getStatusCount() {
  return request({
    method: 'get',
    url: pre + '/blueProblem/statusCount'
  });
}
