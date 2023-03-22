import request from '@/utils/request';
/** 测试接口 */
export function login<T = any>(data: { username: string; password: string }) {
  return request<request.BaseResponse<T>>({
    method: 'post',
    url: '/auth-service/auth/login',
    data: data
  });
}
