<template>
  <div class="project-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart">
      <div class="chart-tabs">
        <div id="pieChart" style="width: 100%; height: 100%"></div>
      </div>
      <div class="chart-box">
        <el-row :gutter="20">
          <el-col :span="24" style="margin-bottom: 10px">
            <span style="color: #fff">行政区划：</span>
            <el-select
              v-model="adcd"
              placeholder="行政区划"
              @change="adcdChange"
            >
              <el-option
                v-for="item in adcdOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
        </el-row>
      </div>
      <i class="split-line icon-split-line1"></i>
    </div>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList">
      <div class="catalog-title" ref="catalogTitle">
        <div class="type-area">
          <div class="search-area" ref="searchArea">
            <div class="left-area">
              <el-input
                @input="getTbList"
                v-model="searchVal"
                placeholder="输入工程名称进行搜索"
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
            <!-- <div class="right-area" @click="lookDetail">
              <i class="icon-liebiao-detail"></i>
            </div> -->
          </div>
        </div>
      </div>

      <!-- 简表 -->
      <div class="table-list" :style="tableListStyle">
        <screen-table
          :columnList="tableColumns"
          :dataList="dataList"
          :loading="tbLoading"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
        >
        </screen-table>
      </div>
    </div>

    <!-- 详情弹框 -->
    <ProductionProjDialog
      ref="ProductionProjDialog"
      v-if="ProductionProjDialogShow"
      :visible="ProductionProjDialogShow"
      @handleClose="handleDetailClose"
    />
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { ScreenTable } from "@/components/";
import ProductionProjDialog from "../dialog/ProductionProjDialog";

export default {
  name: "ProductionProjSide",
  data() {
    return {
      sideType: "",

      totalLength: 4,
      pieData: [
        { name: "现有项目", value: 1 },
        { name: "扰动工程", value: 1 },
      ],

      adcd: "",
      adcdOption: [],

      listTitleTotal: 0,
      searchVal: "",
      tableColumns: [],
      dataList: [],
      tbLoading: false,
      tableListStyle: {}, //表格样式
      tableHeight: 0,

      ProductionProjDialogShow: false, // 详表
    };
  },
  computed: {},
  components: {
    ScreenTable,
    ProductionProjDialog,
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.sideType = val.query.type ? val.query.type : "";
        this.getTbList();
      },
      // 深度观察监听
      deep: true,
    },
  },
  mounted() {
    this.sideType = this.$route.query.type ? this.$route.query.type : "";
    this.getTbList();
    this.getAreaList();
    this.getCount();
    window.addEventListener("resize", this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
  },
  methods: {
    // 获取统计饼图
    async getCount() {
      await hydraulicApi.projStatistics().then((res) => {
        if (res.code == 0) {
          this.pieData = [
            {
              name: "现有项目",
              value: res.data.totalNum == 0 ? "" : res.data.totalNum,
            },
            {
              name: "扰动工程",
              value: res.data.disturbNum == 0 ? "" : res.data.disturbNum,
            },
          ];
          this.$nextTick(() => {
            this.pieChartOpt();
          });
        }
      });
    },
    // 获取列表
    async getTbList() {
      this.getColumn();
      const opt = {
        name: this.searchVal,
        adcd: this.adcd,
        fetchAll: true,
      };
      this.tbLoading = true;
      await hydraulicApi.getProjs(opt).then((res) => {
        if (res.code == 0) {
          let list = res.data.list || [];
          this.dataList = list;
          let pointList = [];
          let imgSrc = require("@/assets/images/legend-rain10.png");
          list.forEach((item) =>
            pointList.push({
              name: item.projName,
              src: imgSrc,
              lng: item.lng,
              lat: item.lat,
              type: "生产建设项目",
              obj: item,
            })
          );
          this.mapObj._drawPoints(pointList, {}, "scjsxmPointLayer");
          this.tbLoading = false;
        }
      });
    },
    // 行政区划改变
    adcdChange(val) {
      let adcdName = this.adcdOption.find((item) => {
        return item.value == val;
      });
      this.mapObj.showAreaLine(adcdName.label);
      this.getTbList();
    },
    // 设置简表表头
    getColumn() {
      this.tableColumns = [
        {
          prop: "projName",
          label: "项目名称",
          width: "200",
        },
        {
          prop: "adnm",
          label: "所属区域",
        },
        {
          prop: "involveTown",
          label: "涉及乡镇",
        },
        {
          prop: "governArea",
          label: "治理面积(k㎡)",
          width: "120px",
        },
      ];
    },
    // 饼图
    pieChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("pieChart");
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        legend: {
          show: true,
          textStyle: {
            color: "#fff",
          },
          orient: "vertical",
          left: "right",
          align: "left",
        },
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            centen: [0, 0],
            avoidLabelOverlap: false,
            labelLine: {
              normal: {
                show: false,
              },
            },
            label: {
              normal: {
                show: false,
              },
            },
            data: this.pieData,
            color: ["#4b83ff", "#2ed47a"],
          },
        ],
      };
      option && myChart.setOption(option);
    },
    // 点击简表行
    rowClick(data) {
      this.lookDetail(data);
      if (data.lng && data.lat) this.mapObj.setView(data.lng, data.lat);
    },
    handleWindowResize() {
      this.$nextTick(() => {
        this.getCount();
      });
    },
    // 行政区划
    async getAreaList() {
      let res = await hydraulicApi.getArea({ areaCode: "3303", level: 3 });
      if (res.code == 0) {
        this.adcdOption.unshift({
          label: "全部",
          value: "",
        });
        for (let item of res.data) {
          this.adcdOption.push({
            label: item.adnm,
            value: item.adcd,
          });
        }
      }
    },
    //详表弹窗关闭
    handleDetailClose() {
      this.ProductionProjDialogShow = false;
    },
    //查看详表
    lookDetail(val) {
      this.ProductionProjDialogShow = true;
      this.$nextTick(() => {
        this.$refs.ProductionProjDialog.init(val);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "./customFilter.scss";

.project-side {
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
      margin-bottom: 16px;
    }

    .chart-tabs {
      width: 100%;
      height: 200px;
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

  .pie-box {
    height: 200px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .legend-panel {
      width: 50%;
      color: #fff;
      margin-left: 30px;
      .legend-item {
        float: left;
        line-height: 25px;
        display: flex;
        justify-content: space-between;
        .text {
          display: inline-block;
          width: 80px;
          font-size: 14px;
          margin-right: 10px;
        }
        .num {
          color: #1cfff5;
          font-size: 1rem;
        }
      }
    }
  }

  .table-list {
    ::v-deep .el-table__row {
      height: 40px;
    }
  }

  .statistics-list {
    @include flex(2);

    .type-area {
      width: 100%;
      @include flexbox;
      @include flexflow(row, nowrap);
      @include flexAC;
      justify-content: space-between;

      .tab-area {
        @include flexbox;
        @include flexflow(row, nowrap);
      }

      .search-area {
        @include flex(1);
        margin-left: 10px;

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

      .right-area {
        margin-left: 10px;
        cursor: pointer;
        float: right;
      }

      .tabList {
        @include flex(1);
        @include flexbox;
        @include flexflow(row, nowrap);
        @include flexAC;
        border: 1px solid #ffffff;
        color: #fff;

        &.tab-active {
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #56fefe;
          box-shadow: inset 0 0 5px 0 #56fefe;
          color: #56fefe;
        }

        > div {
          @include flex(1);
          @include flexbox;
          @include flexAC;
          @include flexJC(center);
          flex-flow: column;
          height: 50px;
          text-align: center;
          line-height: 18px;
          font-size: 14px;
          // cursor: pointer;
          padding: 0 10px;

          > span {
            font-size: 16px;
          }
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
::v-deep .el-select {
  height: 35px;
  .el-input {
    height: 100%;
    background-color: rgba(0, 0, 0, 0);
    border: 1px solid rgba(255, 255, 255, 0.6);
    border-radius: 4px;

    .el-input__inner {
      height: 100%;
      background-color: rgba(0, 0, 0, 0);
    }
    .el-input__icon {
      line-height: 35px;
    }
  }
}
</style>