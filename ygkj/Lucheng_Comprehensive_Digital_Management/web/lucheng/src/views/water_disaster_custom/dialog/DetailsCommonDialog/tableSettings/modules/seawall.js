//@ts-check
/**
 * @typedef {import('../index.js').ColumnProp}  ColumnProp
 */

/** @type {ColumnProp[]} */
const 海塘 = [
	{ prop: "seawallName", label: "海塘名称", sortable: "custom" },
	{ prop: "areaName", label: "行政区划" },
	{ prop: "seawallLevel", label: "海塘级别" },
	{ prop: "village", label: "所在乡镇" },
	{ prop: "seawallLength", label: "海塘长度(m)", sortable: "custom", width: "140px" },
	{ prop: "designTideStandard", label: "设计防潮标准(年)" }
];

export default 海塘;
