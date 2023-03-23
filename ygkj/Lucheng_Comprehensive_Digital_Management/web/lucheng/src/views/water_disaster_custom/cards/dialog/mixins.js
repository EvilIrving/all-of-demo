/**
 * 用于详表对话框的混入方法,详情看导出的各个实现
 */

import { disasterPreventionApi } from "@/api/RightSildePanel/index.js";
/**
 * 分页混入
 * @description el-pagination[:current-page.sync='pageNum'][:page-size='pageSize'][:total='total'] `current-change`事件按实际情况
 */
export const pageMixin = {
	data() {
		return {
			pageSize: 10,
			pageNum: 1,
			total: 0
		};
	},
	methods: {
		/**
		 * 表格序号项编号 用于 el-table-column[type='index'][:index='indexMethod']
		 * @param {number} index
		 * @returns {number}
		 */
		indexMethod(index) {
			return this.pageSize * (this.pageNum - 1) + index + 1;
		}
	}
};

/**
 * 表格方法混入
 */
export const tableMixin = {
	data() {
		return {
			orderColumn: "",
			ascOrDesc: ""
		};
	},
	methods: {
		/**
		 * 表格列数据格式化
		 * @description 用于el-table-column
		 * @param {number|string} value
		 */
		columnFormatter(row, column, value, index) {
			if (value) {
				if (typeof value == "number" && parseFloat(value) != parseInt(value)) {
					return value.toFixed(2);
				}
				if (typeof value == "string" && value.split(",").length > 1) {
					return parseInt(value.split(",")[0]).toFixed(1);
				}
				return value;
			} else {
				return "-";
			}
		},
		/**
		 * 自定义排序实现关键方法
		 */
		sortChange({ prop, order }, cb) {
			if (order) {
				this.orderColumn = prop;
				this.ascOrDesc = order != "ascending";
			} else {
				this.orderColumn = "";
				this.ascOrDesc = "";
			}
			cb && cb();
		}
	}
};

/**
 * 部分公用筛选项混入
 * @description 直接使用 el-select>el-option[v-for="item in areaList"][:key='item.value'][:label=item.adnm][:value='item.adcd'] `流域` el-select>el-option[v-for="item in basinList"][:key='item'][:label='item'][:value='item']
 */
export const filterMixin = {
	data() {
		return {
			areaCode: "",
			/**
			 * 行政区划列表
			 * @type {{adnm:string;adcd:string}[]}
			 */
			areaList: [],
			/** 行政区划树状 */
			areaTree: [],

			/** 流域列表 */
			basin: "",
			basinList: ["飞云江", "瓯江", "鳌江"],

			/** 工程规模 */
			projectScaleValue: "",
			projectScaleData: [
				{ label: "全部", value: "" },
				{ label: "大中型", value: "大中型" },
				{ label: "小（Ⅰ）型", value: "小（Ⅰ）型" },
				{ label: "小（Ⅱ）型", value: "小（Ⅱ）型" }
			]
		};
	},
	mounted() {
		this.getAreaList();
	},
	methods: {
		async getAreaList() {
			const res = await disasterPreventionApi.getArea({ areaCode: 3303, level: 3 });
			if (res.code == 0) {
				this.areaList = res.data;
			}
		}
	}
};
