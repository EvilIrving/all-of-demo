// src/utils/axios.ts
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage, ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import * as nProgress from 'nprogress'
import { useMainStore } from '../store'

axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 5000,
    withCredentials: true
})

axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';

axios.interceptors.request.use((config: AxiosRequestConfig) => {
    nProgress.start()
    if (useMainStore().getAuthorization && localStorage.getItem('Authorization')) {
        if (config.headers) {
            config.headers.Authorization = useMainStore().getAuthorization
        }
    }
    return config;
}, ((error: any) => {
    ElNotification.error('请求错误！')
    return Promise.reject(error);
}))

axios.interceptors.response.use((response: AxiosResponse) => {
    nProgress.done()
    switch (response.status as number) {
        case 401: {
            ElMessage.warning('请先登录')
            const router = useRouter()
            router.push('/login')
            break
        }
        case 403: {
            ElMessage.warning('拒绝访问')
            break
        }
    }
    return response;
}, ((error: any) => {
    ElNotification.error('响应错误！')
    return Promise.reject(error);
}
))

export default axios