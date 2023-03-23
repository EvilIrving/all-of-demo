<template>
  <div class="scope-content">
    <div class="table-box">
      <div class="select-group">
        <div class="select-box">
          行政区划:
          <el-select
            v-model="areaVal"
            class="primary-select"
            @change="projectsDetailList"
          >
            <el-option
              v-for="item in areaoOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div class="select-box" v-if="projType == '水库'">
          工程规模:
          <el-select
            v-model="engScalVal"
            class="primary-select"
            @change="projectsDetailList"
          >
            <el-option
              v-for="item in engScalOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div class="select-box search-area" style="width: 280px">
          工程名称:
          <el-input
            v-model="searchName"
            placeholder="请输入工程名称"
            @input="projectsDetailList"
          ></el-input>
        </div>
      </div>
      <primary-map-table
        :columnList="columnList"
        :tableIndex="tableIndex"
        :dataList="dataList"
        :tableLoading="tableLoading"
      ></primary-map-table>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from "@/components";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";

export default {
  name: "DetailTable",
  props: {
    projType: {
      type: String,
      default: "水库",
    },
  },
  data() {
    return {
      areaVal: "", // 行政区划
      areaoOptions: [
        {
          value: "",
          label: "全部",
        },
      ],
      engScalVal: "", // 工程规模
      engScalOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "大（Ⅰ）型",
          label: "大型",
        },
        {
          value: "中型",
          label: "中型",
        },
        {
          value: "小（Ⅰ）型",
          label: "小（Ⅰ）型",
        },
        {
          value: "小（Ⅱ）型",
          label: "小（Ⅱ）型",
        },
      ],
      tableIndex: 0,
      columnList: [
        {
          prop: "resName",
          label: "名称",
        },
        {
          prop: "resLoc",
          label: "所在位置",
        },
        {
          prop: "engScal",
          label: "工程规模",
        },
        {
          prop: "watShedArea",
          label: "坝址控制流域面积",
        },
        {
          prop: "resType",
          label: "类型",
        },
        {
          prop: "engGrad",
          label: "工程等别",
        },
        {
          prop: "normPoolStagCap",
          label: "正常蓄水位相应库容",
        },
        {
          prop: "totCap",
          label: "总库容",
        },
        {
          prop: "admDep",
          label: "归口管理部门",
        },
        {
          prop: "damSizeLen",
          label: "主坝坝长",
        },
        {
          prop: "damSizeHig",
          label: "主坝坝高",
        },
        {
          prop: "designFloodStandard",
          label: "设计洪水标准(年)",
        },
        {
          prop: "checkFloodStandard",
          label: "校核洪水标准(年)",
        },
      ],
      dataList: [],
      stationData: [],
      tableLoading: false, // 表格loading
      searchName: "",
    };
  },
  computed: {},
  components: {
    PrimaryMapTable,
  },
  created() {
    this.setColumnList(this.projType);
  },
  mounted() {
    this.projectsDetailList();
    this.getAdcdList();
  },
  watch: {},
  methods: {
    async getAdcdList() {
      let opt = {
        areaCode: "3303",
        level: 3,
      };
      let res = await hydraulicApi.getAdcdList(opt);
      if (res.code == 0) {
        this.areaoOptions = [
          {
            value: "",
            label: "全部",
          },
        ];
        res.data.forEach((item) => {
          let obj = {
            value: item.adcd,
            label: item.adnm,
          };
          this.areaoOptions.push(obj);
        });
      }
    },
    //设置表头
    setColumnList(val) {
      let columnArr = [];
      // this.columnList = [];
      switch (val) {
        case "水库":
          columnArr = [
            {
              prop: "resName",
              label: "名称",
            },
            {
              prop: "resLoc",
              label: "所在位置",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "watShedArea",
              label: "坝址控制流域面积",
            },
            {
              prop: "resType",
              label: "类型",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "normPoolStagCap",
              label: "正常蓄水位相应库容",
            },
            {
              prop: "totCap",
              label: "总库容",
            },
            {
              prop: "admDep",
              label: "归口管理部门",
            },
            {
              prop: "damSizeLen",
              label: "主坝坝长",
            },
            {
              prop: "damSizeHig",
              label: "主坝坝高",
            },
            {
              prop: "designFloodStandard",
              label: "设计洪水标准(年)",
            },
            {
              prop: "checkFloodStandard",
              label: "校核洪水标准(年)",
            },
          ];
          break;
        case "堤防":
          columnArr = [
            {
              prop: "dikeName",
              label: "名称",
            },
            {
              prop: "dikeGrad",
              label: "级别",
            },
            {
              prop: "dikeType",
              label: "堤防类型",
            },
            {
              prop: "dikePatt",
              label: "形式",
            },
            {
              prop: "dikeLen",
              label: "长度",
            },
            {
              prop: "flStaLen",
              label: "规划防洪(潮)标准[重现期](年)",
            },
          ];
          break;
        case "水电站":
          columnArr = [
            {
              prop: "hystName",
              label: "名称",
            },
            {
              prop: "bas",
              label: "所属流域",
            },
            {
              prop: "hystLoc",
              label: "所在位置",
            },
            {
              prop: "hystType",
              label: "类型",
            },
            {
              prop: "totInsCap",
              label: "总装机容量",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "mainBuildGrad",
              label: "主要建筑物级别",
            },
            {
              prop: "engStat",
              label: "工程建设情况",
            },
            {
              prop: "startDate",
              label: "开工时间",
            },
            {
              prop: "compDate",
              label: "竣工验收时间",
            },
            {
              prop: "amdDep",
              label: "归口管理部门",
            },
            {
              prop: "generatorsCount",
              label: "机组台数(台)",
            },
            {
              prop: "isUseResvoirWater",
              label: "是否利用水库发电",
            },
            {
              prop: "localWaterCollectionArea",
              label: "本流域集雨面积(平方千米)",
            },
            {
              prop: "damMaxHeight",
              label: "最大坝高(米)",
            },
          ];
          break;
        case "山塘":
          columnArr = [
            {
              prop: "mnpdName",
              label: "名称",
            },
            {
              prop: "adnm",
              label: "所在县",
            },
            {
              prop: "strName",
              label: "所在乡镇(街道)名称",
            },
            {
              prop: "vilName",
              label: "所在村",
            },
            {
              prop: "zygn",
              label: "主要功能",
            },
            {
              prop: "syqr",
              label: "所有权人(管理责任主体)",
            },
            {
              prop: "syqrdh",
              label: "所有权人电话",
            },
            {
              prop: "xcyxm",
              label: "巡查员姓名",
            },
            {
              prop: "xcydh",
              label: "巡查员电话",
            },
            {
              prop: "zgbm",
              label: "主管部门",
            },
            {
              prop: "jhhsP",
              label: "校核洪水(水位)",
            },
            {
              prop: "zcsxswSw",
              label: "正常蓄水位(水位)",
            },
            {
              prop: "rcdsArea",
              label: "坝址以上集雨面积",
            },
            {
              prop: "totalVol",
              label: "总容积",
            },
            {
              prop: "mnpdCate",
              label: "山塘类别",
            },
            {
              prop: "mnpdFunc",
              label: "山塘功能",
            },
            {
              prop: "dmtpEle",
              label: "坝顶高程",
            },
            {
              prop: "madmHeight",
              label: "最大坝高",
            },
            {
              prop: "madmWidth",
              label: "坝顶宽度",
            },
            {
              prop: "damLength",
              label: "坝长",
            },
            {
              prop: "dscncltp",
              label: "主要泄洪建筑物型式",
            },
          ];
          break;
        case "农村供水工程":
          columnArr = [
            {
              prop: "cwsName",
              label: "名称",
            },
            {
              prop: "cwsLoc",
              label: "所在位置",
            },
            {
              prop: "engType",
              label: "工程类型",
            },
            {
              prop: "wasuType",
              label: "供水方式",
            },
            {
              prop: "wasuRang",
              label: "供水范围",
            },
            {
              prop: "desWasuScal",
              label: "设计供水规模",
            },
            {
              prop: "desWasuPop",
              label: "设计供水入口",
            },
            {
              prop: "engStat",
              label: "工程建设情况",
            },
            {
              prop: "wtPro",
              label: "水处理工艺",
            },
            {
              prop: "dfType",
              label: "消毒设施设备",
            },
            {
              prop: "watersupplytesttype",
              label: "水源类型",
            },
            {
              prop: "watervillages",
              label: "供水村庄",
            },
            {
              prop: "managerunitname",
              label: "管理单位名称",
            },
          ];
          break;
        case "泵站":
          columnArr = [
            {
              prop: "pustName",
              label: "名称",
            },
            {
              prop: "pustLoc",
              label: "泵站位置",
            },
            {
              prop: "bas",
              label: "所属流域",
            },
            {
              prop: "insFlow",
              label: "装机流量",
            },
            {
              prop: "insPow",
              label: "装机功率",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
          ];
          break;
        case "水闸":
          columnArr = [
            {
              prop: "wagaName",
              label: "名称",
            },
            {
              prop: "wagaLoc",
              label: "所在位置",
            },
            {
              prop: "wagaType",
              label: "水闸类型",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "desLockDisc",
              label: "设计最大过闸流量",
            },
            {
              prop: "gaorNum",
              label: "闸口数量",
            },
            {
              prop: "mainBuildGrad",
              label: "主要建筑物级别",
            },
          ];
          break;
        case "海塘":
          columnArr = [
            {
              prop: "seawallName",
              label: "名称",
            },
            {
              prop: "startLocation",
              label: "所在乡镇(街道)名称",
            },
            {
              prop: "seawallLength",
              label: "海塘长度",
            },
            {
              prop: "designTideStandard",
              label: "设计防潮标准",
            },
          ];
          break;
        case "灌区":
          columnArr = [
            {
              prop: "irrName",
              label: "名称",
            },
            {
              prop: "city",
              label: "所在市",
            },
            {
              prop: "county",
              label: "所在县",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "desIrrArea",
              label: "设计灌溉面积",
            },
            {
              prop: "effIrrArea",
              label: "有效灌溉面积",
            },
            {
              prop: "manUnit",
              label: "管理单位",
            },
            {
              prop: "benRan",
              label: "受益范围",
            },
          ];
          break;
        case "闸站":
          columnArr = [
            {
              prop: "gateName",
              label: "名称",
            },
            {
              prop: "locCity",
              label: "所在市",
            },
            {
              prop: "locCounty",
              label: "所在县",
            },
            {
              prop: "locStreet",
              label: "所在街道",
            },
            {
              prop: "bas",
              label: "所属流域",
            },
            {
              prop: "river",
              label: "所在河流",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "singleInsFlow",
              label: "单台装机流量",
            },
            {
              prop: "singleInsPow",
              label: "单台装机功率",
            },
            {
              prop: "maxFlow",
              label: "最大过闸流量",
            },
          ];
          break;
        case "水库水位":
          columnArr = [
            {
              prop: "gateName",
              label: "名称",
            },
            {
              prop: "gateName",
              label: "流域",
            },
            {
              prop: "gateName",
              label: "行政区划",
            },
            {
              prop: "gateName",
              label: "水位(m)",
            },
            {
              prop: "gateName",
              label: "汛限水位(m)",
            },
            {
              prop: "gateName",
              label: "防洪高水位(m)",
            },
            {
              prop: "gateName",
              label: "蓄水率(%)",
            },
            {
              prop: "gateName",
              label: "至汛限纳蓄能力(mm)",
            },
          ];
          break;
        // case '河道水位':
        // case '堰闸水位':
        //   this.columnList[1].prop = 'name'
        //   this.columnList[2].prop = 'address'
        //   this.columnList[3].prop = 'typeName'
        //   break;
        // case '潮位':
        //   break;
        // case '雨情':
        //   break;
        // case '水闸工情':
        //   break;
        // case '积水':
        //   break;
        // case '流量':
        //   break;
      }
      this.columnList = columnArr;
      this.tableIndex = Math.random(10000);
      // columnArr.forEach(item=>{
      //   this.columnList.push(item);
      // })
    },
    // select选择
    areaTypeHandle() {},
    //详情列表
    async projectsDetailList() {
      let opt = {
        type: this.projType,
        level: this.engScalVal,
        areaCode: this.areaVal,
        projectName: this.searchName,
      };
      let res = await hydraulicApi.projectsDetailList(opt);
      if (res.code == 0) {
        this.dataList = res.data;
      } else {
        this.dataList = [];
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.scope-content {
  display: flex;
  height: 568px;
  padding: 0 22px;
  ::v-deep .el-input__inner {
    width: 100px;
    height: 32px;
    border: 1px solid #3f9bfc;
    border-radius: 4px;
  }
  ::v-deep .el-input__icon {
    line-height: 24px;
  }

  .table-wrap {
    height: 474px;
  }

  ::v-deep .el-table {
    tr {
      background-color: transparent;
    }
  }
  .table-box {
    color: #fff;
    width: 100%;
    .select-group {
      padding: 16px 0 24px;
      display: flex;
      .select-box {
        width: 171px;
        margin-right: 51px;
        &.search-area {
          ::v-deep .el-input {
            width: fit-content;
            .el-input__inner {
              width: 200px;
            }
          }
        }
      }
    }
  }
  ::v-deep .el-loading-mask {
    background: transparent;
  }
}
</style>
