<!--
 * @Author: dyn
 * @Date: 2021-07-22 15:51:03
 * @LastEditTime: 2021-07-23 18:42:58
 * @LastEditors: Please set LastEditors
 * @Description: 应急救援（仓库/队伍）
 * @FilePath: \synthesizeAmap\src\views\hydraulic_amap\components\side\RescueSide.vue
-->
<template>
  <div class="rescue-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart">
      <div class="chart-tabs">
        <span
          :class="[typeTab == '条件选择' ? 'active' : '']"
          @click="typeTabChange('条件选择')"
          >条件选择</span
        >
        <span
          :class="[typeTab == '行政区划' ? 'active' : '']"
          @click="typeTabChange('行政区划')"
          >行政区划</span
        >
        <div class="title-right">
          <i @click="resetParam()" class="icon-btn-reset1"></i>
        </div>
      </div>
      <div class="chart-box" :style="chartStyle">
        <echart-capsule
          v-if="typeTab == '行政区划'"
          ref="chartCapsule"
          :series-data="statisticData"
          :extra-option="extraOption"
          @clickItem="getCountryData"
        >
        </echart-capsule>
        <div class="type-chart" v-if="typeTab == '条件选择'">
          <div class="custom-filter">
            <div class="filter-item" style="margin-bottom: 16px">
              <span class="filter-label filter-label-dropdown">行政区划：</span>
              <div>
                <el-select
                  v-model="areaVal"
                  style="width: 100%"
                  @change="handleArea"
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
            </div>
            <div class="filter-item" v-if="projectType == '防汛仓库'">
              <span class="filter-label filter-label-dropdown">仓库类型：</span>
              <div>
                <el-select
                  v-model="warehouseVal"
                  style="width: 100%"
                  @change="getProjectList"
                >
                  <el-option
                    v-for="item in warehouseOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </div>
            </div>
            <div class="filter-item" v-if="projectType == '救援队伍'">
              <span class="filter-label filter-label-dropdown">队伍类型：</span>
              <div>
                <el-select
                  v-model="ranksVal"
                  style="width: 100%"
                  @change="getProjectList"
                >
                  <el-option
                    v-for="item in ranksOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </div>
            </div>
          </div>
        </div>
      </div>
      <i class="split-line icon-split-line1"></i>
    </div>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="catalog-title" ref="catalogTitle">
        <div class="type-area">
          <div class="active tabList" @click="changeLayerType('总数')">
            <p>总数</p>
            <p>{{ listTitle }}</p>
          </div>
          <div class="search-area" ref="searchArea">
            <div class="left-area">
              <el-input
                @input="getProjectList"
                v-model="searchVal"
                placeholder="请输入关键字查询"
              >
                <template slot="prepend">
                  <el-button
                    slot="append"
                    icon="el-icon-search"
                    class="keyword-search"
                  ></el-button>
                </template>
              </el-input>
            </div>
            <div class="right-area" @click="lookDetail">
              <i class="icon-detail-table"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="table-list" :style="tableListStyle">
        <screen-table
          :columnList="tableColumns"
          :dataList="dataList"
          :loading="tbLoading"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
        ></screen-table>
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
        <detail-table
          v-if="detailDialog"
          :projType="projectType"
        ></detail-table>
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { ScreenTable, PrimaryMapTable, PrimaryDialog } from "@/components/";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
import DetailTable from "../dialog/DetailTable";
import { getDict } from "@/api/system";
export default {
  name: "rescue-side",
  data() {
    return {
      statisticData: [], //统计图数据
      extraOption: {},
      projectType: "防汛仓库", //工程类型
      searchVal: "",
      tableHeight: 0,
      tableColumns: [],
      dataList: [],
      //总数数据
      dataList2: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typeTab: "行政区划",
      areaCode: "", //选中行政区划
      areaCodeVal: "", // 选中个行政区划code
      detailDialog: false,
      //条件选择
      radioVal: "",
      tabType: "总数",
      areaVal: "", //行政区划
      ranksVal: "", //队伍
      warehouseVal: "", //仓库下拉
      areaoOptions: [
        {
          value: "",
          label: "全部",
        },
      ],
      warehouseOptions: [],
      ranksOptions: [],
      statisticsListHeight: 0,

      tbLoading: false,
    };
  },
  computed: {
    chartStyle() {
      let obj = {
        height: "320px",
      };
      if (this.typeTab == "行政区划") {
        obj = {
          height: "320px",
        };
      } else if (this.typeTab == "条件选择") {
        obj = {
          height: "120px",
        };
      }
      return obj;
    },
    listTitle() {
      let text = this.projectTotal;
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
    this.warehouseOptions = [
      {
        label: "全部",
        value: "",
      },
    ];
    this.ranksOptions = [
      {
        label: "全部",
        value: "",
      },
    ];
    this._getDict("warehouseType");
    this._getDict("teamType");
    this.getAdcdList();
    this.calcDomHeight();
    this.calcTableHeight();
    this.projectType = this.$route.query.type ? this.$route.query.type : "";
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
          this.$refs.statisticsChart.offsetHeight;
        ("px");
        this.statisticsListHeight = height;
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
            this.statisticsListHeight -
            this.$refs.catalogTitle.offsetHeight -
            16;
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
      if (lng && lat) this.mapObj.setView(lng, lat);
      //   this.$parent.showDialog(data);
    },
    //重置筛选条件
    resetParam() {
      this.basinName = "";
      this.areaCode = "";
      this.areaVal = "";
      this.areaCodeVal = "";
      this.searchVal = "";
      this.warehouseVal = "";
      this.ranksVal = "";
      this.mapObj.resetMap();
      this.mapObj.clearAreaLayer();
      this.getProjectList(true);
    },
    //统计分析tab改变
    typeTabChange(type) {
      this.resetParam();
      this.typeTab = type;
      if (type == "行政区划") {
        this.projectCount();
      }
      setTimeout(() => {
        this.handleWindowResize();
      }, 100);
    },
    handleArea(val) {
      let index = this.areaoOptions.findIndex((item) => item.value == val);
      let areaName = index > -1 ? this.areaoOptions[index].label : "";
      this.mapObj.showAreaLine(areaName);
      this.getProjectList();
    },
    async getProjectList() {
      this.tbLoading = true;
      const opt = {
        name: this.searchVal,
        areaCode: this.typeTab == "条件选择" ? this.areaVal : this.areaCodeVal,
        typeId: null,
      };
      opt.typeId =
        this.projectType == "救援队伍" ? this.ranksVal : this.warehouseVal;
      this.tableColumns = [];
      let arr = [
        {
          prop: "chargePerson",
          label: "负责人",
          width: 50,
        },
        {
          prop: "chargePhone",
          label: "联系电话",
        },
      ];
      switch (this.projectType) {
        case "防汛仓库":
          {
            arr.unshift({
              prop: "name",
              label: "物资点名称",
              width: 110,
            });
          }
          break;
        case "救援队伍":
          {
            arr.unshift({
              prop: "name",
              label: "抢险队伍名称",
              width: 110,
            });
          }
          break;
      }
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });
      let res = {};
      if (this.projectType == "救援队伍") {
        res = await hydraulicApi.teams(opt);
      } else {
        res = await hydraulicApi.warehouses(opt);
      }
      this.dataList =
        res.data.list && res.data.list.length > 0 ? res.data.list : [];
      this.mapObj.showProjectPoint(this.dataList, this.projectType, true);
      this.tbLoading = false;
    },
    async getCountryData(data, checkName) {
      if (checkName) {
        this.areaCode = data.name;
        this.mapObj.showAreaLine(this.areaCode);
        let index = this.areaoOptions.findIndex(
          (item) => item.label == data.name
        );
        this.areaCodeVal = index > -1 ? this.areaoOptions[index].value : "";
        this.getProjectList();
      } else {
        this.mapObj.clearAreaLayer();
        this.areaCodeVal = "";
        this.getProjectList();
      }
    },
    //工程分类 统计
    async projectCount() {
      let res = {};
      if (this.projectType == "防汛仓库") {
        res = await hydraulicApi.warehousesGroupByArea();
      } else {
        res = await hydraulicApi.teamGroupByArea();
      }
      if (res.code == 0) {
        this.extraOption = {};
        this.statisticData = [];
        this.$refs.chartCapsule.updateChartView();
        let xArr = [];
        res.data.forEach((item) => {
          const x = item.areaName || item.key;
          xArr.push(x);
          const y = item.number || item.value;
          this.statisticData.unshift(y);
        });
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.statisticData = this.statisticData.reverse();
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
    async _getDict(dictCode) {
      let res = await getDict(dictCode);
      let arr = res.data;
      if (dictCode == "warehouseType") {
        arr.map((item) => {
          this.warehouseOptions.push({
            label: item.dictName,
            value: item.id,
          });
        });
      } else {
        arr.map((item) => {
          this.ranksOptions.push({
            label: item.dictName,
            value: item.id,
          });
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "./customFilter.scss";
.rescue-side {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  .statistics-chart {
    position: relative;
    // flex: 1;
    .title-right {
      position: absolute;
      right: 0;
      .icon-btn-reset1 {
        cursor: pointer;
        height: 32px;
      }
    }
    .split-line {
      display: inline-block;
      width: 100%;
      height: 4px;
    }
    .chart-tabs {
      @include flexbox;
      @include flexAC;
      span {
        box-sizing: border-box;
        display: inline-block;
        padding: 4px 8px;
        width: 80px;
        height: 32px;
        color: #ffffff;
        border: 1px solid rgba(255, 255, 255, 0.6);
        text-align: center;
        line-height: 20px;
        font-size: 14px;
        margin-right: 16px;
        cursor: pointer;
        &.active {
          color: #56fefe;
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #56fefe;
          box-shadow: inset 0 0 5px 0 #56fefe;
        }
      }
    }
    .chart-box {
      padding-top: 8px;
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
  .catalog-title {
    width: 100%;
  }
  .statistics-list {
    @include flex(2);
    .type-area {
      width: 100%;
      margin-top: 12px;
      @include flexbox;
      @include flexflow(row, nowrap);
      @include flexAC;
      justify-content: space-between;
      .search-area {
        @include flex(1);
        margin-left: 10px;
        .right-area {
          margin-left: 10px;
          cursor: pointer;
        }
        .left-area {
          @include flex(2);
          ::v-deep .el-input {
            background-color: rgba(0, 0, 0, 0);
            border: 1px solid rgba(255, 255, 255, 0.6);
            border-radius: 2px;
            .keyword-search {
              color: #ffffff;
            }
            .el-input__inner {
              background-color: rgba(0, 0, 0, 0);
            }
          }
        }
      }
      .tabList {
        // @include flex(1);
        @include flexbox;
        @include flexflow(column, nowrap);
        @include flexAC;
        &.active {
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #56fefe;
          box-shadow: inset 0 0 5px 0 #56fefe;
          color: #56fefe;
        }
        color: #fff;
        height: 50px;
        text-align: center;
        line-height: 18px;
        font-size: 14px;
        border: 1px solid #ffffff;
        cursor: pointer;
        padding: 6px 8px;
        > div {
          width: 32px;
        }
      }
      .right-area {
        cursor: pointer;
        margin-left: 12px;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
</style>
