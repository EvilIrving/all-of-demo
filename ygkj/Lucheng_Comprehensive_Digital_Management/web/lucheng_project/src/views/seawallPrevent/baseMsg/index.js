const sortable = true;

/**
 * @typedef {object} ColumnProp
 * @property {string[]} [conditions] 渲染条件
 * @property {string} [prop]
 * @property {string|((_this:any)=>string)} [label]
 * @property {string|'custom'|boolean} [sortable]
 * @property {number|string} [width]
 * @property {(row:any)=>string|{label:string,props:any}} [slot] 控制default slots 显示 ,(row)=>string 传入`#default="{row}"`的row,返回值为`{label:string,props:any}`时props会传入包裹文字的
 */

/** @type {ColumnProp[]} */
const 视频监控 = [
  { prop: "cameraName", label: "摄像头名称", sortable, width: 120 },
  { prop: "adnm", label: "行政区划" },
  { prop: "projectName", label: "所属工程" },
  { prop: "projectType", label: "工程类型", sortable },
  {
    prop: "status",
    label: "视频状态",
    sortable,
    slot: row => ((row.status == 0 ? "离线" : row.status == 1 ? "在线" : "-"))
  },
  { prop: "longitude", label: "经度" },
  { prop: "latitude", label: "纬度" },
  { prop: "altitude", label: "高程" },
  // {
  //   prop: "",
  //   label: "操作",
  //   slot: row => ({
  //     props: { style: { color: "#1492FF", cursor: 'pointer' } },
  //     label: "编辑",
  //   }),
  // },
];

/** @type {ColumnProp[]} */
const 救援仓库 = [
  { prop: 'name', label: '仓库名称' },
  { prop: 'type', label: '仓库类型' },
  { prop: 'acreage', label: '仓库面积(㎡)' },
  { prop: 'materialPrice', label: '仓库总价值(元)' },
  { prop: 'address', label: '详细地址' },
  { prop: 'chargePerson', label: '联系人' },
  { prop: 'chargePhone', label: '联系方式' }
];

/** @type {ColumnProp[]} */
const 救援队伍 = [
  { prop: "resName", label: "名称" },
  { prop: "year", label: "抢险专业" },
  { prop: "endYearStore", label: "单位性质" },
  { prop: "lastYearStore", label: "编制人数" },
  { prop: "storageSub", label: "现有人数" },
  { prop: "storageRate", label: "详细地址" },
  { prop: "storageRate", label: "联系方式" },
];

/**
 * 动态列表列设置
 * @type {{[key:string]:ColumnProp[]}}
 */
const columnSettings = {
  视频监控,
  救援仓库,
  救援队伍,
};

/**
 * 动态表格data
 * @type {{[key:string]:string|string[]}}
 */
const tableSettings = {
  视频监控: 'videoData',
  救援仓库: 'warehouseData',
  救援队伍: 'reservoirStorageData',
};

/** 表格组件公共prop */
const commonTableProps = {
  width: '100%',
  border: true,
  class: "table-wrapper",
  "element-loading-text": "拼命加载中",
  "element-loading-spinner": "el-icon-loading",
  "element-loading-background": "rgba(0, 0, 0, 0.8)"
};
export { tableSettings, columnSettings, commonTableProps };
