import { http } from "@/config/httpRequset";
const pre = '/data-management';
import { saveFile } from '@/utils/utils.js';

export const dataManage = {
	/** 获取表单字段配置 */
	getTableColumnConfig: function(tableId) {
		return http.get(pre + `/tableDataCommon/getTableColumnConfig/${tableId}`);
	},
	getTableColumnConfigWithNoAuth:function(tableId) {
		return http.get(pre + `/tableDataCommon/getTableColumnConfigWithNoAuth/${tableId}`);
	},
	/** 查询数据记录 */
	getTableAllDataCommon: function(data) {
		return http.post(pre + '/tableDataCommon/getTableAllDataCommon', data);
	},
	/** 新增 */
	addRowData: function(data) {
		return http.put(pre + '/tableDataCommon/addTableRowData', data);
	},
	/** 修改 */
	updateRowData: function(data) {
		return http.put(pre + '/tableDataCommon/updateTableRowData', data);
	},
	/** 删除 */
	deleteTableRowById: function(data) {
		return http.delete(pre + '/tableDataCommon/deleteTableRowById', { data });
	},
	/** 查询单个列的数据源详情 */
	queryCellData: function(data) {
		return http.post(pre + '/tableDataCommon/queryCellData', data);
	},
	/** 切换字段数据源 */
	switchTableColumnDataSource: function(data) {
		return http.putJson(pre + '/tableDataCommon/switchTableColumnDataSource', data);
	},
	/** 删除字段源 */
	deleteTableColumnDataSource: function(data) {
		return http.delete(pre + '/tableDataCommon/delTableColumnDataSource', { data });
	},
	/** 修改字段源信息 */
	updateTableColumnDataSource: function(data) {
		return http.put(pre + '/tableDataCommon/updateTableColumnDataSource', data);
	},
	/** 字段历史 */
	columnHistory: function(data) {
		return http.post(pre + '/busRecordApproval/columnHistory', data);
	},
	/** 获取全部字段 */
	tableColumns: function(data) {
		return http.post(pre + '/busDataTableColumn/tableColumns', data);
	},
	/** 获取当前字段关联角色 */
	getBindColumnRoles: function(data) {
		return http.post(pre + '/sysDataManagementRole/columnRoles', data);
	},
	/** 字段绑定角色 */
	bindColumnPermissionToRole: function(data) {
		return http.post(pre + '/sysDataManagementRole/bindColumnPermissionToRole', data);
	},
	/** 字段绑定部门 */
	bindColumnToDepartment: function(data) {
		return http.post(pre + '/busDataTableColumn/bindColumnToDepartment', data);
	},
	
};

export const dataApprove = {
	/** 我提交的 */
	mySubmitApproveList: function(data) {
		return http.post(pre + '/busRecordApproval/mySubmit', data);
	},
	/** 导出我提交的列表 */
	exportMySubmitExport: function(data) {
		return new Promise((resolve, reject) => {
			request
				.post(pre + '/busRecordApproval/exportMySubmit', data, {
					responseType: 'blob' // 设置响应数据类型
				})
				.then(res => {
					if (res.status == 200) {
						let blob = new Blob([res.data]);
						saveFile(blob, '我提交的审批.xlsx');
						resolve(res);
					}
				})
				.catch(err => {
					reject(err);
				});
		});
	},
	/** 已驳回+已通过 */
	myPastAuditApproveList: function(data) {
		return http.post(pre + '/busRecordApproval/myPastAudit', data);
	},
	/** 待审核 */
	myAuditApproveList: function(data) {
		return http.post(pre + '/busRecordApproval/myAudit', data);
	},
	/** 审核一个流程 */
	auditOneApprove: function(data) {
		return http.post(pre + '/busRecordApproval/audit', data);
	},
	/** 删除一个流程 */
	delOneApprove: function(data) {
		return http.post(pre + '/busRecordApproval/deleteMyApproval', data);
	},
	/** 撤回一个流程 */
	undoOneApprove: function(data) {
		return http.post(pre + '/busRecordApproval/revokeMyApproval', data);
	},
	/** 发送处理信息 */
	handleMessage: function(data) {
		return http.post(pre + '/busRecordApproval/handle', data);
	}
};

export const dutyManage = {
	/** 基础数据分类树 */
	categoryTree: function(name) {
		return http.get(pre + '/busDataTableCategory/myCategory/' + name);
	},
	/** 组织架构列表 */
	getOrganizationList: function(data) {
		return http.get('auth-service/dept/list', data);
	},
	/** 组织架构树 */
	getOrganizationTree: function(data) {
		return http.get('/user-system/contacts/getOrganSchema', data);
	},
	/** 用户list */
	getUserList: function(data) {
		return http.post(pre + '/systemData/users', data);
	},
	/** 角色list */
	getManagementRoleList: function(data) {
		return http.post(pre + '/sysDataManagementRole', data);
	},
	/** 删除角色 */
	deleteRole: function(data) {
		return http.post(pre + '/sysDataManagementRole/delete', data);
	},
	/** 获取部门审批链 */
	getApprovalChain: function(data) {
		return http.post(pre + '/sysDataManagementRole/heads', data);
	},
	/** 新增 */
	addRole: function(data) {
		return http.post(pre + '/sysDataManagementRole/add', data);
	},
	/** 修改 */
	updateRole: function(data) {
		return http.post(pre + '/sysDataManagementRole/update', data);
	},
	/** 获取表 */
	getTableData: function(data) {
		return http.post(pre + '/busDataTable', data);
	},
	/** 关联部门 */
	bindDepartmentToTable: function(data) {
		return http.post(pre + '/busDataTable/bindDepartmentToTable', data);
	},
	/** 表关联角色 */
	bindTablePermission: function(data) {
		return http.post(pre + '/sysDataManagementPermission/bindTablePermission', data);
	},
	/** 行关联角色 */
	bindRowPermission: function(data) {
		return http.post(pre + '/sysDataManagementRole/bindRowPermissionToRole', data);
	},
	/** 获取表字段列表 */
	getTableColumnList: function(data) {
		return http.post(pre + '/busDataTableColumn', data);
	},
	/** 字段绑定角色 */
	bindColumnPermissionToRole: function(data) {
		return http.post(pre + '/sysDataManagementRole/bindColumnPermissionToRole', data);
	},
	/** 用户关联角色 */
	bindUserRoleRelation: function(data) {
		return http.postJson(pre + '/sysDataManagementRole/bindUserRoleRelation', data);
	},
	/** 获取用户角色列表 */
	getUserRoles: function(data) {
		return http.post(pre + '/sysDataManagementRole/candidateUserRoles', data);
	}
};

export const traceability = {
	/** 基础数据分类树 */
	categoryTree: function(name) {
		return http.get(pre + '/busDataTableCategory/' + name);
	},
	/** 溯源列表 */
	getApprovalsList: function(data) {
		return http.post(pre + '/busRecordApproval/selectApprovals', data);
	},
	/** 获取具体表类型和表类型下的表 */
	getCategoriesWithTable: function(data) {
		return http.post(pre + '/busDataTable/categoriesWithTable', data);
	},
	/** 导出溯源列表 */
	exportApprovalsList: function(data) {
		return new Promise((resolve, reject) => {
			request
				.post(pre + '/busRecordApproval/exportSelectApprovals', data, {
					responseType: 'blob' // 设置响应数据类型
				})
				.then(res => {
					if (res.status == 200) {
						let blob = new Blob([res.data]);
						saveFile(blob, '溯源信息表.xlsx');
						resolve(res);
					}
				})
				.catch(err => {
					reject(err);
				});
		});
	}
};

export const settingManage = {
	/** 基础数据分类树 */
	categoryTree: function(name) {
		return http.get(pre + '/busDataTableCategory/' + name);
	},
	/** 更换顺序  */
	swapCategoryOrderNumber: function(data) {
		return http.post(pre + '/busDataTableCategory/swapCategoryOrderNumber', data);
	},
	/** 获取显示字段设置 */
	getTableColumnConfig: function(data) {
		return http.post(pre + '/busDataTableColumn', data);
	},
	/** 更新字段设置 */
	updateColumnConfig: function(data) {
		return http.post(pre + '/busDataTableColumn/columnConfigUpdate', data);
	}
	
};
