import axios from '_l/api.request'

/**
 * 添加资源目录树表
 * @param directory
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addDirectory = (directory) => {
  return axios.request({
    url: '/data-manage/dataResource/save',
    method: 'post',
    data: directory
  })
}

/**
 * 修改资源目录树表
 * @param directory
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateDirectory = (directory) => {
  return axios.request({
    url: '/data-manage/dataResource/update',
    method: 'put',
    data: directory
  })
}

/**
 * 删除资源目录树表
 * @param directoryId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDirectoryById = (directoryId) => {
  return axios.request({
    url: '/data-manage/dataResource/delById/' + directoryId,
    method: 'delete'
  })
}

/**
 * 获取tree数据
 * @param tree 是否带子集 true带子集(树形) false不带子集
 * @param parentId 父id
 * @returns {AxiosPromise}
 */
export const getDirectoryTreeTableList = (treeFlag, parentId) => {
  return axios.request({
    url: '/data-manage/dataResource/list',
    method: 'post',
    params: {
      tree: treeFlag,
      parentId: parentId
    }
  })
}
