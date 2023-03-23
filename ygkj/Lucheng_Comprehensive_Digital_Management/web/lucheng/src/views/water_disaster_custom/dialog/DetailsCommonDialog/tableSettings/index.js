//@ts-check
import seawall from "./modules/seawall.js";
import reservoir from "./modules/reservoir.js";
import lakeWater from "./modules/lakeWater.js";
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
const 水情 = [
  { sortable: 'custom', prop: 'rnName', label: '名称' },
  { sortable: 'custom', prop: 'waterLevel', label: '水位' },
  {
    sortable: 'custom',
    prop: 'overWarn',
    label: '汛限水位',
    slot: (row) => ((!row.aaaaa ? '-' : row.aaaaa > 10 ? '故障' : row.aaaaa))
  }
];

const 工情 = [
  { sortable: 'custom', prop: 'rnName', label: '名称' },
  { sortable: 'custom', prop: 'waterLevel', label: '水位' },
  {
    sortable: 'custom',
    prop: 'overWarn',
    label: '汛限水位',
  }
];

/** @type {ColumnProp[]} */
const 河网 = [
  { prop: "rnName", width: "80", label: "名称" },
  { prop: "bas", width: "80", label: "流域" },
  { prop: "areaName", width: "100", label: "行政区划" },
  { sortable: "custom", prop: "waterLevel", label: "水位（m）" },
  { sortable: "custom", prop: "mainStName", label: "水位站名称" },
  { sortable: "custom", prop: "warningLevel", label: "警戒水位（m）" },
  { sortable: "custom", prop: "overWarn", label: "超警戒水位（m）" },
  { sortable: "custom", prop: "guranteeLevel", label: "保证水位（m）" },
  { sortable: "custom", prop: "overGuarantee", label: "超保证水位（m）" }
];

/** @type {ColumnProp[]} */
const 潮位 = [
  { prop: "stName", label: "名称" },
  { prop: "bas", width: "80", label: "流域" },
  { prop: "areaName", width: "80", label: "行政区划" },
  { sortable: "custom", prop: "tdz", width: "150", label: "实时潮位(m)" },
  { prop: "warningLevel", label: "警戒水位(m)" },
  { prop: "overWarn", label: "超警戒潮位(m)" },
  { prop: "astroTide", label: "天文潮(m)" },
  { prop: "subTide", label: "增水(m)" }
];

/** @type {ColumnProp[]} */
const 病险工程 = [
  { sortable: "custom", prop: "projectName", width: "100", label: "名称" },
  { prop: "bas", label: "流域" },
  { prop: "adnm", label: "行政区划" },
  { prop: "engScal", label: "工程规模" },
  { prop: "projLocName", label: "所在乡镇" },
  { prop: "lastCheckTime", label: "鉴定时间" },
  { prop: "lastCheckResult", label: "安全鉴定结论" },
  { prop: "rectifyFinishTime", label: "整改完成时间" },
  { conditions: ["projType", "eq", "水库"], prop: "damType", width: "100", label: "坝型" },
  { conditions: ["projType", "eq", "水库"], prop: "totCap", width: "120", label: "总库容(万m³)" },
  { conditions: ["projType", "eq", "水库"], prop: "waterLevel", width: "120", label: "实时水位(m)" },
  { conditions: ["projType", "eq", "水库"], prop: "limitLevel", width: "120", label: "汛限水位(m)" },
  { conditions: ["projType", "eq", "水库"], prop: "floodLevel", width: "140", label: "防洪高水位(m)" }
];

/** @type {ColumnProp[]} */
const 风险隐患 = [
  { prop: "projName", label: "名称" },
  { prop: "projType", label: "工程类型" },
  {
    prop: "recType",
    label: "风险类型",
    slot: row =>
      (row.sbSign == "251" ? "隐患点" : row.sbSign == "252" ? "高风险点" : row.sbSign == "253" ? "薄弱点" : "-")
  },
  { prop: "dangerType", label: "隐患类型" },
  { prop: "recCondition", label: "整改情况" },
  { prop: "county", label: "行政区划" },
  { prop: "reportPerson", label: "上报人员" },
  { prop: "dutyUnit", label: "整改责任单位" },
  { prop: "reportTime", label: "发现时间" }
];

/** @type {ColumnProp[]} */
const 山洪 = [
  { prop: "projectName", label: "行政区划" },
  { prop: "projectType", label: "乡镇" },
  { prop: "areaName", label: "可能性很大的村落" },
  { prop: "waterLevel", label: "可能性较大的村落" },
  { prop: "warningLevel", label: "影响人口" }
];

/** @type {ColumnProp[]} */
const 短临预报 = [
  { prop: "key", label: "名称" },
  { sortable: true, prop: "rainfall_1", label: "1小时(mm)" },
  { sortable: true, prop: "rainfall_3", label: "3小时(mm)" },
  { sortable: true, prop: "rainfall_6", label: "6小时(mm)" }
];

/** @type {ColumnProp[]} */
const 水电站 = [
  { prop: "hystName", label: "水电站名称", width: "150" },
  { prop: "hystCode", label: "水电站编码", width: "150" },
  { prop: "areaName", label: "行政区划" },
  {
    prop: "hystType",
    label: "水电站类型",
    width: "150",
    slot: row => ["混合式", "坝式(河床)", "饮水式", "坝式(坝后)", "抽水蓄能"][row.hystType]
  },
  { prop: "totInsCap", label: "总装机容量(m³)", sortable, width: "150" },
  { prop: "insCap", label: "装机流量(m³/s)", sortable, width: "150" },
  { prop: "aprvEcoFlowYear", label: "核定生态流量(m³/s)", sortable, width: "150" },
  {
    prop: "standardRate",
    label: "生态流量达标率(%)",
    sortable,
    width: "150",
    slot: row => `${(row.standardRate * 100).toFixed(2)}%`
  },
  {
    prop: "onlineRate",
    label: "设备在线率",
    sortable,
    width: "120",
    slot: row => `${(row.onlineRate * 100).toFixed(2)}%`
  }
];

/** @type {ColumnProp[]} */
const 河长巡查 = [
  { prop: "areaName", label: "行政区划" },
  { prop: "riverName", label: "河段名称", width: "100" },
  { prop: "realName", label: "发现人", width: "70" },
  { prop: "address", label: "发现地址", width: "180" },
  { prop: "createDate", label: "开始时间", width: "150" },
  { prop: "deadline", label: "截止时间", width: "150" },
  { width: "80", prop: "level", label: "问题程度", slot: row => ["轻微", "一般问题", "重大问题"][row.level] },
  { prop: "content", label: "问题详情", width: "200" },
  {
    prop: "state",
    label: "处置状态",
    width: "80",
    slot: row => ["", "未受理", "处理中", "已处理待评价", "已处理已评价"][row.state]
  }
];

/** @type {ColumnProp[]} */
const 防汛仓库 = [
  { prop: "name", label: "物资仓库名", sortable },
  { prop: "areaName", label: "行政区划", sortable },
  { prop: "type", label: "仓库类型" },
  { prop: "address", label: "地址" },
  { prop: "materialPrice", label: "物资价值(万元)" },
  { prop: "chargePerson", label: "负责人" },
  { prop: "chargePhone", label: "联系电话" },
  { prop: "acreage", sortable, label: "仓库面积(km²)" }
];
/** @type {ColumnProp[]} */
const 防汛队伍 = [
  { prop: "name", label: "抢险队伍名称", sortable },
  { prop: "areaName", label: "行政区划", sortable },
  { prop: "typeName", label: "队伍类型" },
  { prop: "address", label: "地址" },
  { prop: "chargePerson", label: "负责人" },
  { prop: "chargePhone", label: "联系电话" },
  { prop: "existingNumber", label: "人数(万人)", sortable }
];

/** @type {ColumnProp[]} */
const 视频监控 = [
  { prop: "cameraName", label: "摄像头名称", sortable },
  { prop: "adnm", label: "行政区划" },
  { prop: "projectName", label: "所属工程" },
  { prop: "projectType", label: "工程类型", sortable },
  {
    prop: "status",
    label: "视频状态",
    sortable,
    slot: row => ((row.status == 0 ? "离线" : row.status == 1 ? "在线" : "-"))
  },
  { prop: "statusDuration", label: "持续时长(h)" }
];

/** @type {ColumnProp[]} */
const 救援仓库 = [
  { prop: "name", label: "仓库名称" },
  { prop: "areaName", label: "仓库类型" },
  { prop: "streetName", label: "仓库面积" },
  {
    prop: "ponding",
    label: "仓库总价值",
    slot: row => `${row.ponding.toFixed(2)}`
  },
  { prop: "waterLevel", label: "详细地址" },
  { prop: "highestLevel", label: "联系人" },
  { prop: "appearTime", label: "联系方式" }
];

/** @type {ColumnProp[]} */
const 洪水风险图 = [
  { prop: "name", sortable, label: "流域名称" },
  { prop: "designStandard", label: "设计洪水标准", sortable },
  { prop: "floodedArea", label: "淹没面积(km²)", sortable },
  { prop: "submergedPopulation", label: "淹没人口(万人)", sortable },
  { prop: "floodLoss", label: "淹没区洪水损失(万元)", sortable }
];

/** @type {ColumnProp[]} */
const 降水量 = [
  { prop: "adnm", label: "行政区域" },
  { prop: "year", width: "80", label: "年份" },
  { prop: "rainfall", label: "年降雨量（mm）" },
  { prop: "yearAvgRain", label: "多年年均降雨（mm）" },
  { prop: "compareLastYear", label: "较上一年(%)" },
  { prop: "anomaly", label: "较多年(%)" },
  {
    prop: "watResState",
    label: "水资源划分",
    slot: row => ({
      props: { class: "table-order" },
      label: row.watResState == 0 ? "平水年" : row.watResState > 0 ? "丰水年" : "枯水年"
    })
  }
];

/** @type {ColumnProp[]} */
const 水资源量 = [
  { prop: "adnm", label: "行政区域" },
  { prop: "totalWaterResource", label: "水资源总量(亿m³)", width: 180, sortable },
  { prop: "twrOfCityRate", label: "占全市比例(%)", width: 160, sortable },
  { prop: "watResPerCapita", label: "人均水资源总量(m³)", width: 200, sortable },
  { prop: "wzWrpcRate", label: "相较温州市(%)", width: 200, sortable },
  { prop: "zjWrpcRate", label: "相较浙江省(m³)", width: 200, sortable },
  { prop: "cnWrpcRate", label: "相较全国(%)", width: 160, sortable }
];

/** @type {ColumnProp[]} */
const 供水量 = [
  { prop: "year", label: "年份" },
  { prop: "totalSupply", label: "供水总量(亿m³)", sortable },
  { prop: "supStoreProj", label: "蓄水量（亿m³）" },
  { prop: "supDiverProj", label: "引水量（亿m³）" },
  { prop: "supLiftProj", label: "提水量（亿m³）" }
];

/** @type {ColumnProp[]} */
const 用水量 = [
  { prop: "adnm", label: "年份" },
  { prop: "adnm", label: "行政区域" },
  { prop: "totalConsume", label: "用水总量(亿m³)", width: "150", sortable },
  { prop: "adnm", label: "占全市比例%" },
  { prop: "adnm", label: "人均综合用水总量" },
  { prop: "adnm", label: "相较全市平均" },
  { prop: "adnm", label: "相较全省平均" },
  { prop: "adnm", label: "相较全国平均" }
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

/** @type {ColumnProp[]} */
const 水资源承载能力 = [
  { prop: "adnm", label: "行政区划" },
  /* activeTabCarry == '水量评价' */
  { conditions: ["activeTabCarry", "eq", "水量评价"], prop: "consumeTotal", label: "用水总量W", sortable },
  { conditions: ["activeTabCarry", "eq", "水量评价"], prop: "consumeNorm", label: "用水总量控制指标W0", sortable },
  { conditions: ["activeTabCarry", "eq", "水量评价"], prop: "ratio", label: "W/W0", sortable },
  { conditions: ["activeTabCarry", "eq", "水量评价"], prop: "evaDesc", label: "评价结果", sortable },
  /* activeTabCarry == '水质评价' */
  { conditions: ["activeTabCarry", "eq", "水质评价"], prop: "wfzTotalPer", label: "水功能区水质达标率Q", sortable },
  {
    conditions: ["activeTabCarry", "eq", "水质评价"],
    prop: "wfzTotalPerNorm",
    label: "水功能区水质达标率要求Q0",
    sortable
  },
  { conditions: ["activeTabCarry", "eq", "水质评价"], prop: "ratio", label: "Q/Q0", sortable },
  { conditions: ["activeTabCarry", "eq", "水质评价"], prop: "evaDesc", label: "评价等级", sortable },
  /* activeTabCarry == '经济评价' */
  { conditions: ["activeTabCarry", "eq", "经济评价"], prop: "pop", label: "人口（万人）", sortable },
  { conditions: ["activeTabCarry", "eq", "经济评价"], prop: "totalWaterConsume", label: "总用水量（亿m³）", sortable },
  { conditions: ["activeTabCarry", "eq", "经济评价"], prop: "watConEachPop", label: "人均综合用水量（m³）", sortable },
  {
    conditions: ["activeTabCarry", "eq", "经济评价"],
    prop: "controlWaterConsume",
    label: "控制用水量（亿m³）",
    sortable
  },
  { conditions: ["activeTabCarry", "eq", "经济评价"], prop: "gdp", label: "国内地区生产总值（亿元）", sortable },
  {
    conditions: ["activeTabCarry", "eq", "经济评价"],
    prop: "watConPerGdp",
    label: "万元GDP用水量（m³/万元）",
    sortable
  },
  {
    conditions: ["activeTabCarry", "eq", "经济评价"],
    prop: "supportEco",
    label: "可支撑的万元GDP（亿元））",
    sortable
  },
  /* activeTabCarry == '人口评价' */
  { conditions: ["activeTabCarry", "eq", "人口评价"], prop: "currentPop", label: "人口（万人）", sortable },
  { conditions: ["activeTabCarry", "eq", "人口评价"], prop: "totalWaterConsume", label: "总用水量（亿m³）", sortable },
  { conditions: ["activeTabCarry", "eq", "人口评价"], prop: "watConEachPop", label: "人均综合用水量（m³）", sortable },
  {
    conditions: ["activeTabCarry", "eq", "人口评价"],
    prop: "controlWaterConsume",
    label: "控制用水量（亿m³）",
    sortable
  },
  {
    conditions: ["activeTabCarry", "eq", "人口评价"],
    prop: "supportPop",
    label: "控制指标可支撑人口（万人））",
    sortable
  }
];

/** @type {ColumnProp[]} */
const 预警信息 = [
  { prop: "type", label: "预警类型" },
  { prop: "content", label: "预警内容", width: "400" },
  { prop: "sendTime", label: "发送时间", sortable },
  { prop: "destinationUserName", label: "接收人" },
  { prop: "fromUserName", label: "发起人" }
];

/** @type {ColumnProp[]} */
const 流域生态流量 = [
  { prop: "sectName", label: "断面名称" },
  { prop: "bas", label: "流域", sortable },
  { prop: "flow", label: "实时流量(m³/s)", sortable },
  { prop: "orangeFlow", label: "橙色预警指标(m³/s)", sortable },
  { prop: "redFlow", label: "红色预警指标(m³/s)", sortable }
];

/** @type {ColumnProp[]} */
const 规划 = [
  { prop: "projName", label: "项目名称" },
  { prop: "projDuration", label: "起止年份" },
  { prop: "constructContent", label: "主要建设内容" },
  { label: "总投资(万元)", prop: "amountInvestTotal" },
  { label: _this => `${_this.mediumSized.split("|")[0] || ""}投资(万元)`, prop: "amountInvestDevelop" },
  { label: "已完成投资(万元)", prop: "amountInvested" },
  { label: "结转总投资(万元)", prop: "amountInvestIndirect" }
];

/** @type {ColumnProp[]} */
const 水厂 = [
  { prop: "cwsName", label: "水厂名称" },
  { prop: "adnm", label: "行政区划" },
  { prop: "cwsLoc", label: "水厂位置" },
  { prop: "desWasuPop", label: "人口（万人）" },
  { prop: "desWasuScal", label: "规模", sortable },
  { prop: "wasuRang", label: "供水规模", sortable },
  { prop: "engType", label: "类型" },
  { prop: "supplyFlow", label: "实时流量（m³/h）", sortable },
  { prop: "totalSupplyQuantity", label: "日供水量（m³）" }
];

/** @type {ColumnProp[]} */
const 水厂运行状态 = [
  { prop: "cwsName", label: "水厂名称" },
  { prop: "adnm", label: "行政区划" },
  { prop: "cwsLoc", label: "水厂位置" },
  { prop: "supplyFlow", label: "流量（m³/h）" },

  { label: "pH", sortable, slot: row => row.phValue || "-" },
  { prop: "clValue", label: "余氯", sortable },
  { prop: "ntuValue", label: "浑浊度" },
  {
    prop: "runtimeState",
    label: "运行状态",
    sortable,
    slot: row => ({
      props: { style: { color: ["#19AF47", "#EF1313 ", "#FF551F"][row.runtimeState] || "#E89100" } },
      label: ["正常供水", "供水预警", "水质预警"][row.runtimeState] || "水质供水预警"
    })
  }
];

/** @type {ColumnProp[]} */
const 水源地 = [
  { prop: "swhsName", label: "水源地名称" },
  { prop: "adnm", label: "水源地位置" },
  {
    label: "类型",
    slot: row =>
      ["", "水库", "山塘", "河流湖泊", "溪沟堰坝", "蓄水池", "地下水", "海水淡化", "山泉水"][row.wainWasoType] || "其他"
  },
  {
    prop: "destinationUserName",
    label: "规模",
    slot: row => ["", "县级以上", "千吨万人", "200吨-1000吨", "200吨以下"][row.wainWasoType] || "200吨以下"
  },
  { prop: "desAnnWasuPop", label: "可供水量(万m³)", width: 150, sortable },
  { prop: "withdrawal", label: "取水量(万m³/月)", width: 150, sortable },
  { prop: "waterMonth", label: "取水月份" },
  { prop: "cumulatAnnWain", label: "本年累计取水量(万m³)", width: 200, sortable },
  { prop: "updateTime", label: "数据更新时间", width: 150, sortable }
];

/** @type {ColumnProp[]} */
const 水量预警 = [
  { prop: "swhsName", label: "水源地名称" },
  { prop: "swhsName", label: "行政区划" },
  {
    label: "类型",
    slot: row =>
      ["", "水库", "山塘", "河流湖泊", "溪沟堰坝", "蓄水池", "地下水", "海水淡化", "山泉水"][row.wainWasoType] || "其他"
  },
  {
    label: "水量预警等级",
    sortable,
    slot: row => ({
      props: {
        style: {
          color: ["", "rgb(25, 175, 71)", "rgb(232, 145, 0)", "rgb(255, 85, 31)"][row.wainWasoType] || "rgb(232, 0, 0)"
        }
      },
      label: ["", "暂无风险", "黄色预警", "橙色预警"][row.wainWasoType] || "红色预警"
    })
  }
];

/** @type {ColumnProp[]} */
const 珊溪水质监测 = [
  { prop: "type", label: "站点名称" },
  { prop: "content", label: "时间", sortable },
  { prop: "sendTime", label: "COD" },
  { prop: "destinationUserName", label: "氨氮" },
  { prop: "destinationUserName", label: "总磷" }
];

/** @type {ColumnProp[]} */
const 巡查 = [
  { prop: "chaosName", label: "乱点名称" },
  { prop: "chaosType", label: "乱点类型" },
  { prop: "chaosStatus", label: "乱点状态" },
  { prop: "discoverTime", label: "发现时间" },
  { prop: "cityName", label: "所属县/市" },
  { prop: "areaName", label: "所在区域" }
];

/** @type {ColumnProp[]} */
const 取水户 = [
  { prop: "wiuName", label: "名称", width: "180" },
  { prop: "areaName", label: "行政区划" },
  { prop: "wiuType", label: "取水类型" },
  { prop: "intake", label: "当年取水量(万m³)" },
  { prop: "intakePermit", label: "许可取水量(万m³)" },
  { prop: "legRepr", label: "法人代表" },
  { prop: "legPhone", label: "联系电话" }
];

/** @type {ColumnProp[]} */
const 批后监管 = [
  { prop: "projectName", label: "项目名称", width: "200" },
  { prop: "projectCode", label: "项目编号", width: "150" },
  { prop: "adnm", label: "行政区划" },
  { prop: "year", label: "年份" },
  { prop: "riverName", label: "所属河道", width: "200" },
  { prop: "address", label: "地点", width: "200" },
  { prop: "destinationUserName", label: "监管次数" },
  { prop: "inspectionNum", label: "监管状态" },
  { prop: "lastInspectionTime", label: "最后一次监管时间", width: "190", sortable },
  { prop: "unRectifyProblemNum", label: "未整改问题数", width: "150", sortable },
  { prop: "unitName", label: "单位名称" },
  { prop: "legalRepresent", label: "法人代表" },
  { prop: "phone", label: "联系电话" }
];

/** @type {ColumnProp[]} */
const 水域变化 = [
  { prop: "areaName", label: "行政区划" },
  { prop: "year", label: "年份" },
  { prop: "waterArea", label: "水域面积(k㎡)" },
  { prop: "waterRate", label: "水面率(%)" },
  { prop: "occupiedArea", label: "占用面积(k㎡)" },
  { prop: "compensationArea", label: "补偿面积(k㎡)" }
];

/** @type {ColumnProp[]} */
const 清四乱 = [
  { prop: "riverName", label: "问题河道" },
  { prop: "address", label: "详细地址", width: "180" },
  {
    prop: "status",
    label: "问题状态",
    slot: row => ({
      props: { style: { color: ["#f2a600", "#1ef8f2", "#ff0000", "#c60000"][row.status] } },
      label: ["未处理", "已处理", "逾期未处理", "驳回"][row.status]
    })
  },
  { prop: "problemSource", label: "问题来源" },
  { prop: "fourType", label: "四乱类型" },
  { prop: "severity", label: "严重程度" },
  { prop: "problemDescription", label: "问题描述" },
  { prop: "createTime", label: "上传时间" },
  { prop: "rectificationRequirement", label: "整改范围" }
];

/** @type {ColumnProp[]} */
const 水库分析 = [
  { sortable, prop: "resName", label: "名称" },
  { sortable, prop: "lowestWaterLevel", label: "最低水位(m)" },
  { prop: "lowestWaterLevelTm", width: "150", label: "最低水位出现时间" },
  { sortable, prop: "highestWaterLevel", label: "最高水位(m)" },
  { sortable: "custom", prop: "dataTime", width: "100", label: "更新时间" },
  { prop: "safetyTime", width: "110", label: "鉴定时间" },
  { prop: "safetyConclusion", width: "110", label: "鉴定结论" },
  { prop: "reinforce", width: "110", label: "有无加固" },
  { prop: "highestWaterLevelTm", width: "150", label: "最高水位出现时间" },
  { sortable, width: "150", prop: "capacitySub", label: "累计拦蓄水量(万m³)" },
  { sortable, width: "150", prop: "drainage", label: "累计泄洪量(万m³)" },
  { sortable, width: "150", prop: "overWarnMinutes", label: "累计超警时间(min)" }
];

/** @type {ColumnProp[]} */
const 水闸分析 = [
  { sortable, prop: "wagaName", label: "名称" },
  { sortable, prop: "lowestUpz", label: "最小闸上水位" },
  { prop: "lowestUpzTm", label: "最小闸上水位出现时间" },
  { sortable, prop: "highestUpz", label: "最大闸上水位" },
  { prop: "highestUpzTm", label: "最大闸上水位出现时间" },
  { sortable, prop: "openGateMinutes", label: "累计开闸时间" },
  { sortable, prop: "drainage", label: "累计排洪量" }
];

/** @type {ColumnProp[]} */
const 河网分析 = [
  { sortable, prop: "rnName", label: "名称" },
  { sortable: "custom", prop: "mainStName", label: "水位站名称" },
  { sortable, prop: "lowestWaterLevel", label: "最低水位" },
  { prop: "lowestWaterLevelTm", label: "最低水位出现时间" },
  { sortable, prop: "highestWaterLevel", label: "最高水位" },
  { prop: "highestWaterLevelTm", label: "最高水位出现时间" },
  { prop: "overWarnMinutes", label: "累计超警时间" }
];

/** @type {ColumnProp[]} */
const 潮位分析 = [
  { sortable, prop: "stName", label: "名称" },
  { sortable, prop: "lowestRealTide", label: "最低水位" },
  { prop: "lowestRealTideTm", label: "最低水位出现时间" },
  { sortable, prop: "highestRealTide", label: "最高水位" },
  { prop: "highestRealTideTm", label: "最高水位出现时间" },
  { sortable, prop: "highestSubTide", label: "最大增水" },
  { sortable, prop: "overWarnMinutes", label: "累计超警时间" }
];

/** @type {ColumnProp[]} */
const 水利设施保险 = [
  { sortable, prop: "name", label: "合同编号" },
  { sortable, prop: "name", label: "合同名称" },
  { sortable, prop: "newest1hour", label: "行政区划" },
  { sortable, prop: "1hour", label: "堤防名称" },
  { sortable, prop: "3hour", label: "损失图片" },
  { sortable, prop: "6hour", label: "相关文件" },
  { sortable, prop: "24hour", label: "主管部门(甲)" },
  { sortable, prop: "24hour", label: "上传时间" },
  { sortable, prop: "24hour", label: "现场视频" },
  { sortable, prop: "24hour", label: "现场音频" },
  { sortable, prop: "24hour", label: "现场图片" },
  { sortable, prop: "24hour", label: "估评理赔金额(元)" },
  { sortable, prop: "24hour", label: "堪核时间" }
];

/**
 * 动态列表列设置
 * @type {{[key:string]:ColumnProp[]}}
 */
const columnSettings = {
  海塘: seawall,
  水库: reservoir,
  水情,
  工情,
  河网,
  潮位,
  病险工程,
  风险隐患,
  山洪,
  短临预报,
  水电站,
  河长巡查,
  防汛仓库,
  防汛队伍,
  视频监控,
  救援仓库,
  洪水风险图,
  降水量,
  水资源量,
  供水量,
  用水量,
  救援队伍,
  水资源承载能力,
  预警信息,
  流域生态流量,
  规划,
  水厂,
  水厂运行状态,
  水源地,
  水量预警,
  珊溪水质监测,
  巡查,
  取水户,
  批后监管,
  水域变化,
  清四乱,
  美丽河湖: lakeWater,
  水库分析,
  水闸分析,
  河网分析,
  潮位分析,
  水利设施保险
};

/**
 * 动态表格data
 * @type {{[key:string]:string|string[]}}
 */
const tableSettings = {
  海塘: 'seawallProjData',
  水库: 'WaterloggingData',
  水情: 'waterRegimeData',
  工情: 'workRegimeData',
  河网: 'riverwayData',
  潮位: 'seaLevelData',
  病险工程: 'dangerouslyData',
  风险隐患: 'potentialRisksData',
  山洪: 'dangerouslyData',
  短临预报: 'inTheShortData',
  水电站: 'hydroStaData',
  河长巡查: 'patrolData',
  防汛仓库: ['warehouseData', 'list'],
  防汛队伍: ['ranksData', 'list'],
  视频监控: 'videoData',
  救援仓库: ['waterLoggingData', 'list'],
  洪水风险图: 'floodRiskData',
  降水量: 'precipitationData',
  水资源量: 'waterResourceData',
  供水量: 'waterRupplyData',
  用水量: 'waterUseData',
  救援队伍: 'reservoirStorageData',
  水资源承载能力: 'waterResCarryData',
  预警信息: 'earlyData',
  流域生态流量: 'flowData',
  规划: ['planningData', 'data'],
  水厂: 'waterPointList',
  水厂运行状态: 'waterPointList',
  水源地: 'waterSourData',
  水量预警: 'waterWarnData',
  珊溪水质监测: 'sxWaterData',
  巡查: 'sxCheckData',
  取水户: 'wtaerIntakeData',
  批后监管: 'approSuperData',
  水域变化: 'wtaerChangeData',
  清四乱: 'fourChaosData',
  美丽河湖: 'beautyData',
  水库分析: ['reservoirAnalysisData', 'list'],
  水闸分析: 'lockAnalysisData',
  河网分析: 'riverNetworkData',
  潮位分析: 'waterAnalysisData',
  水利设施保险: 'realTimeData'
};

/** 表格组件公共prop */
const commonTableProps = {
  height: "100%",
  stripe: true,
  class: "table-wrapper",
  "element-loading-text": "拼命加载中",
  "element-loading-spinner": "el-icon-loading",
  "element-loading-background": "rgba(0, 0, 0, 0.8)"
};
export { tableSettings, columnSettings, commonTableProps };
