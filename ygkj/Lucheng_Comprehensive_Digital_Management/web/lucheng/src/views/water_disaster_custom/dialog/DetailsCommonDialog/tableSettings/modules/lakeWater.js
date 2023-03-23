//@ts-check
/**
 * @typedef {import('../index.js').ColumnProp}  ColumnProp
 */

/** @type {ColumnProp[]} */
const 美丽河湖_Tab = [
	{ prop: "projectName", label: "项目名称", width: "76" },
	{ prop: "projectStatus", label: "工程状态" },
	{ prop: "sectionName", label: "河段名称" },
	{ prop: "startLocation", label: "起点位置" },
	{ prop: "endLocation", label: "终点位置" },
	{ prop: "planInvest", label: "计划投资(万元)", width: "115" },
	{ prop: "completeInvestment", label: "完成投资(万元)", width: "115" },
	{ prop: "investmentProportion", width: "70", label: "比例(%)" },
	{ prop: "planScale", label: "计划规模(km)", width: "110" },
	{ prop: "completeScale", label: "完成规模(km)", width: "110" },
	{ prop: "scaleRatio", width: "70", label: "比例(%)" },
	{ prop: "overallProgress", label: "总体形象进度(%)", width: "130" }
].map(item => ({ ...item, conditions: ["activeTabBeauty", "eq", "美丽河湖"] }));

/** @type {ColumnProp[]} */
const 中小河流_Tab = [
	{ prop: "projectName", label: "项目名称" },
	{ prop: "projectStatus", label: "工程状态" },
	{ prop: "sectionName", label: "河段名称" },
	{ prop: "startLocation", label: "起点位置" },
	{ prop: "endLocation", label: "终点位置" },
	{ prop: "totalProjectInvestment", label: "项目总投资(万元)", width: "140" },
	{ prop: "completeProjectInvestment", label: "完成总投资(万元)", width: "140" },
	{ prop: "investmentProportion", label: "比例(%)" },
	{ prop: "overallProgress", label: "总体形象进度(%)", width: "130" }
].map(item => ({ ...item, conditions: ["activeTabBeauty", "eq", "中小河流"] }));

/** @type {ColumnProp[]} */
const 水美乡镇_Tab = [
	{ prop: "projectName", label: "项目名称" },
	{ prop: "projectStatus", label: "工程状态" },
	{ prop: "sectionName", label: "河段名称" },
	{ prop: "startLocation", label: "起点位置" },
	{ prop: "endLocation", label: "终点位置" },
	{ prop: "planInvestment", label: "计划投资(万元)", width: "120" },
	{ prop: "completeInvestment", label: "完成投资(万元)", width: "120" },
	{ prop: "investmentProportion", label: "比例(%)" },
	{ prop: "overallProgress", label: "总体形象进度(%)", width: "130" }
].map(item => ({ ...item, conditions: ["activeTabBeauty", "eq", "水美乡镇"] }));

/** @type {ColumnProp[]} */
const 农村池塘_Tab = [
	{ prop: "projectName", label: "项目名称" },
	{ prop: "projectStatus", label: "工程状态" },
	{ prop: "poudName", label: "池塘名称" },
	{ prop: "planInvestment", label: "计划投资(万元)", width: "120" },
	{ prop: "completeInvestment", label: "完成投资(万元)", width: "120" },
	{ prop: "investmentProportion", label: "比例(%)" },
	{ prop: "overallProgress", label: "总体形象进度(%)", width: "130" }
].map(item => ({ ...item, conditions: ["activeTabBeauty", "eq", "农村池塘"] }));

/** @type {ColumnProp[]} */
const 美丽河湖 = [...美丽河湖_Tab, ...中小河流_Tab, ...水美乡镇_Tab, ...农村池塘_Tab];

export default 美丽河湖;
