import {http} from "@/config/httpRequset";
import { saveFile } from '@/utils/utils.js';

/** 元数据 查询表 */
export function getDataSheet(data) {
	return http.post('/data-management/schema/tables/', data);
}

/** 元数据 查询表的字段 */
export function getDataColumnsInTable(data) {
	return http.post('/data-management/schema/columns', data);
}

/** 查询单表内容 */
export function getTableDetailDataCommon(data) {
	return http.post('/data-management/tableDataCommon/getTableDetailDataCommon', data);
}

/** 导出数据表 */
export function exportBusDataTable(data) {
	return new Promise((resolve, reject) => {
		request
			.post('/data-management/tableDataCommon/exportDataManagement', data, {
				responseType: 'blob' // 设置响应数据类型
			})
			.then(res => {
				if (res.status == 200) {
					let blob = new Blob([res.data]);
					saveFile(blob, '数据管理表.xlsx');
					resolve(res);
				}
			})
			.catch(err => {
				reject(err);
			});
	});
}

/** 基础数据分类树 */
export function getDataTableCategory(name) {
	return http.get('/data-management/busDataTableCategory' + name);
}

/** 修改基础数据分类树 */
export function modifyDataTableCategory(data) {
	return http.post('/data-management/busDataTableCategory', data);
}

/** 新增基础数据分类树 */
export function addDataTableCategory(data) {
	return http.post('/data-management/busDataTableCategory/add', data);
}

/** 删除基础数据分类树 */
export function delDataTableCategory(id) {
	return http.delete('/data-management/busDataTableCategory/' + id);
}

/** 数据分类树 */
export function getDataCategoryNoTable(name) {
	return http.get('/data-management/busDataTableCategory/categoryNoTable/' + name);
}

/** 基础数据单表信息 */
export function getBusDataTable(data) {
	return http.post('/data-management/busDataTable', data);
}

/** 新增数据表 */
export function addBusDataTable(data) {
	return http.post('data-management/busDataTable/add', data);
}

/** 数据表资源申请信息 */
export function getBusDataTableApply(data) {
	return http.post('/data-management/busDataTableApply', data);
}

/** 数据表资源申请历史 */
export function getBusDataApplyDetail(data) {
	return http.post('/data-management/busDataTableApply/applyDetail', data);
}
/** 数据表资源申请 */
export function applyBusDataTable(data) {
	return http.postJson('/data-management/busDataTableApply/apply', data);
}

/** 数据表资源待审核 */
export function getDataForExamine(data) {
	return http.post('/data-management/busDataTableApply/dataForExamine', data);
}

/** 数据表资源审批 */
export function examineBusData(data) {
	return http.post('/data-management/busDataTableApply/examine', data);
}

/** 数据表资源撤销审批 */
export function cancelBusDataApply(data) {
	return http.post('/data-management/busDataTableApply/cancel', data);
}
/** 初始化表的列 */
export function initTableColumns(data) {
	return http.post('/data-management/tableCellInit/InitTableColumn', data);
}
