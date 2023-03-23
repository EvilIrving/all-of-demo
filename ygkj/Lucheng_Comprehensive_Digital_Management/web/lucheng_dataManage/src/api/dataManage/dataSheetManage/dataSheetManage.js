import axios from '_l/api.request'

/**
 * 添加数据源列表数据
 * @param dataSource
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addDataSource = (dataSource) => {
  return axios.request({
    url: '/data-manage/dataSource/save',
    method: 'post',
    data: dataSource
  })
}

/**
 * 修改数据源列表数据
 * @param dataSource
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateDataSource = (dataSource) => {
  return axios.request({
    url: '/data-manage/dataSource/update',
    method: 'put',
    data: dataSource
  })
}

/**
 * 删除数据源列表数据
 * @param dataSourceId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDataSourceById = (dataSourceId) => {
  return axios.request({
    url: '/data-manage/dataSource/delById/' + dataSourceId,
    method: 'delete'
  })
}

/**
 * 获取数据源列表数据
 * @param page true分页 false和不传不分页
 * @returns {AxiosPromise}
 */
export const getDataSourceList = (data) => {
  return axios.request({
    url: '/data-manage/dataSource/page',
    method: 'post',
    data: data
  })
}

/**
 * 数据表关联资源目录
 * @param dataResourceId 资源目录id
 * @param relList 表列表
 * @returns {AxiosPromise}
 */
export const linkResource = (data) => {
  return axios.request({
    url: '/data-manage/resourceSourceRel/batchSave',
    method: 'post',
    data: data
  })
}

/**
 * 修改表别名
 * @param data
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateTableAlias = (data) => {
  return axios.request({
    url: '/data-manage/dataTable/batchSave',
    method: 'post',
    data: data
  })
}

/**
 * 获取数据表列数据
 * @param dataSourceId 数据源id
 * @param tableName 所属表格名称
 * @returns {AxiosPromise}
 */
export const getTableColumn = (data) => {
  return axios.request({
    url: '/data-manage/dataColumn/list',
    method: 'post',
    data: data
  })
}

/**
 * 添加数据源列表数据申请
 * @param columnData
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addColumnData = (columnData) => {
  return axios.request({
    url: '/data-manage/dataApply/applySave',
    method: 'post',
    data: columnData
  })
}

/**
 * 修改数据源列表数据申请
 * @param columnData
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateColumnData = (columnData) => {
  return axios.request({
    url: '/data-manage/dataApply/applyUpdate',
    method: 'post',
    data: columnData
  })
}

/**
 * 删除数据源列表数据申请
 * @param columnData
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteColumnDataById = (columnData) => {
  return axios.request({
    url: '/data-manage/dataApply/applyDel',
    method: 'post',
    data: columnData
  })
}

/**
 * 添加数据列归属部门
 * @param data
 * @returns {AxiosPromise}
 */
export const addAssignedData = (data) => {
  return axios.request({
    url: '/data-manage/dataDeptRel/assignedData',
    method: 'post',
    data: data
  })
}


/**
 * 删除数据列归属部门
 * @param data
 * @returns {AxiosPromise}
 */
export const deleteAssignedData = (data) => {
  return axios.request({
    url: '/data-manage/dataDeptRel/delAssigned',
    method: 'post',
    data: data
  })
}

/**
 * 更新列别名
 * @param data
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateColumnAlias = (data) => {
  return axios.request({
    url: '/data-manage/dataColumn/batchSave',
    method: 'post',
    data: data
  })
}