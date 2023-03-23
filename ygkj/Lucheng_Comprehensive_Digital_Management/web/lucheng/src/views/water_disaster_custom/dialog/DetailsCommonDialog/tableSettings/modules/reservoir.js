//@ts-check
/**
 * @typedef {import('../index.js').ColumnProp}  ColumnProp
 */

/** @type {ColumnProp[]} */
const 水库 = [
	{ prop: "resName", width: "100", sortable: "custom", label: "名称" },
	{ prop: "bas", width: "70", label: "流域" },
	{ prop: "areaName", width: "80", label: "行政区划" },
	{ sortable: "custom", prop: "waterLevel", width: "100", label: "水位(m)" },
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		prop: "currentLimitLevel",
		sortable: "custom",
		width: "130",
		label: "汛限水位(m)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		sortable: "custom",
		prop: "uppLevFlco",
		width: "150px;",
		label: "防洪高水位(m)"
	},
	{ sortable: "custom", prop: "dataTime", width: "150", label: "更新时间" },
	{ prop: "safetyTime", width: "110", label: "鉴定时间" },
	{ prop: "safetyConclusion", width: "110", label: "鉴定结论" },
	{ prop: "reinforce", width: "110", label: "有无加固" },
	{ conditions: ["projectScaleValue", "neq", "大中型"], prop: "normalSpillwayCrestElevation", label: "溢洪道高程(m)" },
	{ conditions: ["projectScaleValue", "neq", "大中型"], prop: "damSizeHig", label: "坝高(m)" },
	{ conditions: ["projectScaleValue", "neq", "大中型"], prop: "waterLevelCapacity", label: "实时库容(万m³)" },
	{ conditions: ["projectScaleValue", "neq", "大中型"], prop: "totCap", label: "总库容(万m³)" },
	{
		conditions: ["projectScaleValue", "eq", "大中型", "mediumSized", "eq", "大中型水库"],
		prop: "drainage24H",
		width: "150px;",
		label: "24累计出库量(万m³)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型", "mediumSized", "eq", "大中型水库"],
		prop: "drainage48H",
		width: "150px;",
		label: "48累计出库量(万m³)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型", "mediumSized", "eq", "大中型水库"],
		prop: "drainage72H",
		width: "150px",
		label: "72累计出库量(万m³)"
	},
	{ sortable: "custom", prop: "storageRatio", label: "蓄水率(%)", width: "120" },
	{ conditions: ["projectScaleValue", "neq", "大中型"], prop: "normWatLev", width: "175px", label: "正常蓄水位(m)" },
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		prop: "waterLevelCapacity",
		width: "160px",
		label: "实时库容(万m³)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		prop: "limitLevelCapacity",
		width: "160px",
		label: "汛限水位库容(万m³)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		prop: "uppLevFlcoCapacity",
		width: "170px",
		label: "防洪高水位库容(万m³)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		prop: "limitStorageAbility",
		width: "160px",
		label: "至汛限纳蓄能力(mm)"
	},
	{
		conditions: ["projectScaleValue", "eq", "大中型"],
		prop: "floodStorageAbility",
		width: "175px",
		label: "至防洪高纳蓄能力(mm)"
	}
];

export default 水库;
