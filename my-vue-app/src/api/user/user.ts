// api/user/user.ts
import axios from '../../utils/axios'
import * as qs from 'qs'

const baseAPI = import.meta.env.VITE_BASE_API

export const login = (data: any) => {
    return axios({
        method: 'POST',
        url: baseUrl + '/login',
        data: qs.stringify(data),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}