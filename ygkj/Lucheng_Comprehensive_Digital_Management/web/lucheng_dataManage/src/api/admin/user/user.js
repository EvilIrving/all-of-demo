import { applicationAxios } from '_l/api.request'

/**
 * 登录
 * @param data
 * @returns {AxiosPromise}
 */
export const login = (data) => {
  return applicationAxios.request({
    url: '/auth-service/auth/login',
    method: 'post',
    data: data,
    doNotStringify: true
  })
}

/**
 * 获取用户信息
 * @returns {AxiosPromise}
 */
export const getInfo = () => {
  return applicationAxios.request({
    url: '/auth-center/getUserInfo',
    method: 'get'
  })
}

/**
 * 退出
 * @returns {AxiosPromise}
 */
export const logout = () => {
  return applicationAxios.request({
    url: '/auth-center/logout',
    method: 'post'
  })
}

/**
 * 根据部门获取用户列表
 * @returns {AxiosPromise}
 */
export const getUserListByDeptId = (deptId) => {
  return applicationAxios.request({
    url: '/system-center/system/user/page',
    method: 'get',
    params: {
      current: 1,
      size: 9999,
      deptId: deptId
    }
  })
}

/**
 * 根据用户id查询已授权的角色列表
 * @param userId
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getAuthRoleListByUserId = (userId) => {
  return applicationAxios.request({
    url: '/system-center/system/user/getAuthRoleListByUserId',
    params: {
      userId: userId
    }
  })
};

/**
 * 根据用户id查询已授权的用户组列表
 * @param userId
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getAuthGroupListByUserId = (userId) => {
  return applicationAxios.request({
    url: '/system-center/system/user/getAuthGroupListByUserId',
    params: {
      userId: userId
    }
  })
};

/**
 * 根据用户名获取用户信息
 * @param username 用户名
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getUserInfoByUsername = (username) => {
  return applicationAxios.request({
    url: '/system-center/system/user/getUserInfoByUsername',
    params: {
      username: username
    }
  })
};

/**
 * 根据用户id查询岗位列表
 * @param userId
 * @returns {*}
 */
export const getPostListByUserId = (userId) => {
  return applicationAxios.request({
    url: '/system-center/system/user/getPostListByUserId',
    params: {
      userId: userId
    }
  })
};

/**
 * 根据登录人获取目录和菜单路由
 * @param projectId
 * @returns {AxiosPromise}
 */
export const getRouterList = (projectId) => {
  return applicationAxios.request({
    url: '/system-center/system/resource/getRouterList',
    params: {
      projectId: projectId
    }
  })
}

/**
 * 根据pId获取部门树
 * @param parentId
 * @returns {AxiosPromise}
 */
export const getDeptTree = () => {
  return applicationAxios.request({
    url: '/system-center/system/dept/getDeptTreeTableList',
    method: 'get',
    params: {
      parentId: "1517064422162219009"
    }
  })
}

