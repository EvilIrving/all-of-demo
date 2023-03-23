<template>
  <div class="project-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart">
      <div class="chart-tabs">
        <!-- <span :class="[typeTab=='类别'?'active':'']" @click="typeTabChange('类别')">类别</span> -->
        <span
          :class="[typeTab == '行政区划' ? 'active' : '']"
          @click="typeTabChange('行政区划')"
          >行政区划</span
        >
        <span :class="[typeTab == '流域' ? 'active' : '']" @click="typeTabChange('流域')"
          >流域</span
        >
        <div class="title-right">
          <i @click="resetParam()" class="icon-btn-reset"></i>
        </div>
      </div>
      <div class="chart-box" :style="chartStyle">
        <echart-capsule
          v-if="typeTab == '行政区划' || typeTab == '流域'"
          ref="chartCapsule"
          :series-data="statisticData"
          :extra-option="extraOption"
          @clickItem="getCountryData"
        >
        </echart-capsule>
        <div class="split-line"></div>
      </div>
    </div>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="catalog-title" ref="catalogTitle">
        <div class="title-left">
          <i class="icon-judge-icon"></i>
          <span>{{ listTitle }}</span>
        </div>
      </div>
      <div class="search-area" ref="searchArea">
        <div class="left-area">
          <el-input
            @input="getProjectList"
            v-model="searchVal"
            placeholder="请输入关键字查询"
          >
            <template #prepend>
              <el-button slot="append" icon="el-icon-search"></el-button>
            </template>
          </el-input>
        </div>
        <div class="right-area">
          <span @click="lookDetail">查看详表</span>
        </div>
      </div>
      <div class="table-list" :style="tableListStyle">
        <screen-table
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
        ></screen-table>
        <!-- <primary-map-table 
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :paginationFlag="true"
        ></primary-map-table> -->
      </div>
    </div>

    <!-- 详情弹框 -->
    <primary-dialog
      mainTitle="详表"
      :visible="detailDialog"
      @handleClose="handleDetailClose"
      width="50%"
      :hasAnalysis="false"
    >
      <div class="dialog-content scope-content">
        <detail-table v-if="detailDialog" :projType="projectType"></detail-table>
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { ScreenTable, PrimaryMapTable, PrimaryDialog } from "@/components/";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
import DetailTable from "../dialog/DetailTable";
export default {
  name: "project-side",
  data() {
    return {
      statisticData: [], //统计图数据
      extraOption: {},
      projectType: "水库", //工程类型
      searchVal: "",
      tableHeight: 0,
      tableColumns: [],
      dataList: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typeTab: "行政区划",
      basinName: "", //选中流域字段
      areaCode: "", //选中行政区划
      detailDialog: false,
    };
  },
  computed: {
    chartStyle() {
      let obj = {
        height: "280px",
      };
      if (this.typeTab == "行政区划") {
        obj = {
          height: "280px",
        };
      } else if (this.typeTab == "流域") {
        obj = {
          height: "180px",
        };
      }
      return obj;
    },
    listTitle() {
      let text = "统计列表(" + this.projectTotal + ")";
      if (this.areaCode) {
        text = text + "(" + this.areaCode + ")";
      } else {
        text = "统计列表(" + this.projectTotal + ")";
      }
      return text;
    },
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
    projectTotal() {
      return this.dataList.length;
    },
  },
  components: {
    ScreenTable,
    PrimaryMapTable,
    EchartCapsule,
    PrimaryDialog,
    DetailTable,
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.calcDomHeight();
        this.calcTableHeight();
        this.projectType = val.query.type ? val.query.type : "";
        this.getProjectList(this.projectType);
        this.projectCount("行政区划", this.projectType);
        this.typeTab = "行政区划";
        this.$nextTick(() => {
          this.resetParam();
        });
      },
      // 深度观察监听
      deep: true,
    },
  },
  mounted() {
    this.calcDomHeight();
    this.calcTableHeight();
    this.projectType = this.$route.query.type ? this.$route.query.type : "";
    this.getProjectList(this.projectType);
    this.projectCount("行政区划", this.projectType);
    window.addEventListener("resize", this.handleWindowResize);
    this.$nextTick(() => {
      this.resetParam();
    });
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    //计算表格父容器高度
    calcDomHeight() {
      this.$nextTick(() => {
        const height =
          this.$refs.stationSide.offsetHeight -
          this.$refs.statisticsChart.offsetHeight +
          "px";
        this.listStyle = {
          height,
        };
      });
    },
    //计算表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        setTimeout(() => {
          this.tableHeight =
            this.$refs.statisticsList.offsetHeight -
            this.$refs.catalogTitle.offsetHeight -
            this.$refs.searchArea.offsetHeight;
          this.tableListStyle = {
            height: this.tableHeight + "px",
          };
        }, 100);
      });
    },
    handleWindowResize() {
      this.calcDomHeight();
      this.calcTableHeight();
    },
    rowClick(data) {
      let lng =
        data.longitude ||
        data.lowLeftLong ||
        data.dtMidLong ||
        data.startLong ||
        data.pustLong ||
        data.startLong ||
        data.hystLong;
      let lat =
        data.latitude ||
        data.lowLeftLat ||
        data.dtMidLat ||
        data.startLat ||
        data.pustLat ||
        data.startLat ||
        data.hystLat;
      if(lng && lat)
        this.mapObj.setView(lng, lat);
      // 有问题
      this.$parent.showDialog(data);
    },
    //重置筛选条件
    resetParam() {
      this.basinName = "";
      this.areaCode = "";
      this.searchVal = "";
      this.mapObj.resetMap();
      this.mapObj.clearAreaLayer();
      this.mapObj.clearBasinLayer();
      this.getProjectList(this.projectType);
    },
    //统计分析tab改变
    typeTabChange(type) {
      this.resetParam();
      this.typeTab = type;
      this.projectCount(type, this.projectType);
    },
    async getProjectList(type, unloadPoint) {
      // console.log(this.$route.query.type);
      const opt = {
        type: this.$route.query.type || "水库",
        projectName: this.searchVal,
        areaCode: this.areaCode,
        bas: this.basinName,
      };
      if (type == "水库" || type == "水闸") {
        opt.withWaterLevel = true;
      }
      this.tableColumns = [
        {
          prop: "resName",
          label: "名称",
        },
      ];
      let arr = [
        {
          prop: "engScal",
          label: "工程规模",
          sortable: true,
        },
        {
          prop: "totCap",
          label: "总库容(万m³)",
          sortable: true,
        },
      ];
      switch (type) {
        case "水库":
          {
            arr = [
              {
                prop: "engScal",
                label: "工程规模",
                sortable: true,
              },
              {
                prop: "totCap",
                label: "总库容(万m³)",
                sortable: true,
              },
            ];
          }
          break;
        case "山塘":
          {
            arr = [
              {
                prop: "mnpdCate",
                label: "山塘类型",
                sortable: true,
              },
              {
                prop: "totalVol",
                label: "总容积(万m³)",
                sortable: true,
              },
            ];
          }
          break;
        case "堤防":
          {
            arr = [
              {
                prop: "dikeType",
                label: "堤防类别",
              },
              {
                prop: "dikeLen",
                label: "堤防长度(m)",
                sortable: true,
              },
            ];
          }
          break;
        case "海塘":
          {
            arr = [
              {
                prop: "seawallLevel",
                label: "海塘级别",
                sortable: true,
              },
              {
                prop: "reachStandardLength",
                label: "海塘长度(m)",
                sortable: true,
              },
            ];
          }
          break;
        case "泵站":
          {
            arr = [
              {
                prop: "engScal",
                label: "泵站规模",
                sortable: true,
              },
              {
                prop: "insFlow",
                label: "装机流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "水闸":
          {
            arr = [
              {
                prop: "engScal",
                label: "水闸规模",
                sortable: true,
              },
              {
                prop: "desLockDisc",
                label: "最大过闸流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "闸站":
          {
            arr = [
              {
                prop: "engScal",
                label: "闸站规模",
                sortable: true,
              },
              {
                prop: "maxFlow",
                label: "过闸流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "灌区":
          {
            arr = [
              {
                prop: "engScal",
                label: "灌区等级",
                sortable: true,
              },
            ];
          }
          break;
        case "农饮水":
          {
            arr = [];
          }
          break;
        case "水电站":
          {
            arr = [
              {
                prop: "engScal",
                label: "水电站规模",
                sortable: true,
              },
              {
                prop: "totInsCap",
                label: "总装机容量(kW)",
                sortable: true,
              },
            ];
          }
          break;
      }
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });

      let res = await hydraulicApi.getProjects(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      this.dataList = res.data;
      // console.log( this.$parent.showProjectPoint)
      if (!unloadPoint) {
        // console.log(type,111)
        if (type == "海塘") {
          this.mapObj.handleSeawallChange(this.dataList);
        } else {
          this.mapObj.showProjectPoint(this.dataList, this.projectType);
        }
      }
    },
    async getCountryData(data) {
      if (this.typeTab == "行政区划") {
        this.areaCode = data.name;
        this.mapObj.showAreaLine(this.areaCode);
      } else if (this.typeTab == "流域") {
        this.basinName = data.name;
        const bsName = this.basinName + "流域";
        // console.log(this.basinName);
        this.mapObj.basinCommand(bsName);
      }
      this.getProjectList(this.projectType, true);
    },
    //工程分类 统计
    async projectCount(type, stType) {
      // console.log(stType);
      let opt = {
        type,
        stType,
      };
      let res = await hydraulicApi.projectCount(opt);
      if (res.code == 0) {
        this.extraOption = {};
        this.statisticData = [];
        let xArr = [];
        // console.log(res.data,999);
        res.data.forEach((item) => {
          const x = item.areaName || item.key;
          xArr.push(x);
          const y = item.number || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        // console.log(this.statisticData,123);
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //详表弹窗关闭
    handleDetailClose() {
      this.detailDialog = false;
    },
    //查看详表
    lookDetail() {
      this.detailDialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.project-side {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  .statistics-chart {
    position: relative;
    flex: 1;
    .title-right {
      position: absolute;
      right: 0;
      .icon-btn-reset {
        cursor: pointer;
        height: 30px;
      }
    }
    .chart-tabs {
      margin-top: 10px;
      @include flexbox;
      @include flexAC;
      span {
        display: inline-block;
        padding: 4px 8px;
        width: 80px;
        // height: 28px;
        color: #4898fb;
        border: 1px solid #4898fb;
        text-align: center;
        line-height: 16px;
        font-size: 12px;
        margin-right: 16px;
        cursor: pointer;
        &.active {
          color: #a6e9fb;
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #23e3fb;
        }
      }
    }
    .chart-box {
      padding-top: 8px;
      .split-line {
        display: inline-block;
        width: 100%;
        height: 4px;
      }
      ::v-deep .dv-capsule-chart {
        height: 100%;
      }
      .type-chart {
        padding: 16px 0;
        .type-chart-item {
          @include flexbox;
          @include flexflow(row, nowrap);
          color: #00c1ff;
          margin-bottom: 20px;
          .chart-item-title {
            width: 25px;
            letter-spacing: 10px;
            border-right: 1px solid #00c1ff;
          }
          .chart-item-content {
            @include flex(1);
            @include flexbox;
            @include flexflow(row, nowrap);
            padding-left: 18px;
            .param-item {
              width: 49%;
              @include flexbox;
              @include flexAC;
              @include flexflow(row, nowrap);
              cursor: pointer;
              .param-item-icon {
                width: 43px;
                height: 43px;
              }
              .param-item-data {
                margin-left: 10px;
                p {
                  line-height: 24px;
                  span {
                    font-weight: bold;
                    color: #1cfef4;
                    margin-right: 6px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .statistics-list {
    @include flex(2);
    .table-list {
      height: 100%;
      width: 100%;
    }
  }
}
</style>
