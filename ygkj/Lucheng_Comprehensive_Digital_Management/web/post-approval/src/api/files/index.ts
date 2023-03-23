/**
 * @desc 文件相关api
 */
import { config } from '@/config';
import axios, { AxiosResponse } from 'axios';
import { storage } from '@/utils/storage';
import { UserInfo } from '@/types/user';
const pre = '/auth-service';

const request = axios.create({ baseURL: config.baseApi, timeout: 15000 });
const userInfo: UserInfo = storage.get('userInfo_obj') as UserInfo;
// 断言无法解决 storage.get('userInfo_obj') 可能为undefined的情况
const accessToken = userInfo?.accessToken || '';

export interface UploadResponse {
  msg: string;
  code: number;
  data: {
    ext: string;
    filePath: string;
    fileSize: string;
    id: string;
    originalName: string;
    success: boolean;
  };
}
/**
 * 上传文件
 */
export function uploadFile(data: FormData) {
  return new Promise<UploadResponse>((resolve, reject) => {
    request<AxiosResponse<UploadResponse>>({
      method: 'post',
      url: pre + `/upload`,
      data,
      headers: { 'Content-Type': 'multipart/form-data', authorization: accessToken }
    })
      .then(response => {
        const res = response.data;

        if (res.code == 0) {
          resolve(res);
        } else {
          reject(res);
        }
      })
      .catch(err => {
        reject(err);
      });
  });
}

/**
 * 获取文件
 */
export function getFileList(data: { ids: string }) {
  return request({ method: 'post', url: pre + '/file/load', data });
}
