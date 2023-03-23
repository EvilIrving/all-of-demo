import request from '@/utils/request';
const pre = '/river-service';
export function projectListApi() {
  return request({
    method: 'post',
    url: pre + '/blueProj/projectList',
    data: {
      pageNum: 1,
      pageSize: 99999,
    },
    headers: { 'Content-Type': 'application/json' }
  });
}

export function saveOrUpdateApi(data: {
  projectId: String,
  informContent: String
}) {
  return request({
    method: 'post',
    url: pre + '/projectInform/saveOrUpdate',
    data,
    headers: { 'Content-Type': 'application/json' }
  });
}