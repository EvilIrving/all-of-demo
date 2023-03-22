import { EnvConfig } from '@/types';
const ProdServerApi = '/api/';
const devServerApi = 'https://slgh.lucheng.gov.cn/api/';
// const devServerApi = 'http://192.168.2.124:8073/';

const dev: EnvConfig = {
  env: 'development',
  baseApi: devServerApi
};

const prod: EnvConfig = {
  env: 'production',
  baseApi: ProdServerApi
};

export const config: EnvConfig = import.meta.env.DEV ? dev : prod;
