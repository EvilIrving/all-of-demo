<template>
  <div class="project-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart">
      <div class="chart-tabs">
        <span
          :class="[tabType == '条件选择' ? 'active' : '']"
          @click="changeTabType('条件选择')"
          >条件选择</span
        >
        <span
          :class="[tabType == '区域' ? 'active' : '']"
          @click="changeTabType('区域')"
          >区域</span
        >
        <span
          :class="[tabType == '规模' ? 'active' : '']"
          @click="changeTabType('规模')"
          >规模</span
        >
        <span
          :class="[tabType == '类型' ? 'active' : '']"
          @click="changeTabType('类型')"
          >类型</span
        >
        <div class="title-right">
          <i @click="resetParam()" class="icon-btn-reset1"></i>
        </div>
      </div>
      <div class="chart-box">
        <el-row :gutter="20" v-if="tabType == '条件选择'">
          <el-col :span="24" style="margin-bottom: 10px">
            <span style="color: #fff">行政区划：</span>
            <el-select v-model="adcd" placeholder="行政区划">
              <el-option
                v-for="item in adcdOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
          <el-col
            :span="24"
            style="margin-bottom: 10px"
            v-if="sideType == '水源'"
          >
            <span style="color: #fff">水源类型：</span>
            <el-select v-model="waterType" placeholder="水源类型">
              <el-option
                v-for="item in waterTypeOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
          <el-col
            :span="24"
            style="margin-bottom: 10px"
            v-if="sideType == '水源'"
          >
            <span style="color: #fff">水源规模：</span>
            <el-select v-model="waterScale" placeholder="水源规模">
              <el-option
                v-for="item in waterScaleOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
          <el-col
            :span="24"
            style="margin-bottom: 10px"
            v-if="sideType == '水厂'"
          >
            <span style="color: #fff">水厂类型：</span>
            <el-select v-model="factoryType" placeholder="水厂类型">
              <el-option
                v-for="item in factoryTypeOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
          <el-col
            :span="24"
            style="margin-bottom: 10px"
            v-if="sideType == '水厂'"
          >
            <span style="color: #fff">水厂规模：</span>
            <el-select v-model="factoryScale" placeholder="水厂规模">
              <el-option
                v-for="item in factoryScaleOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
        </el-row>

        <echart-capsule
          style="height: 250px"
          v-if="tabType == '区域'"
          ref="chartCapsule"
          :series-data="statisticData"
          :extra-option="extraOption"
          @clickItem="getCountryData"
        >
        </echart-capsule>

        <div class="pie-box" v-if="tabType == '规模' || tabType == '类型'">
          <div id="pieChart" style="width: 50%; height: 100%"></div>
          <div class="legend-panel">
            <div
              class="legend-item"
              v-for="(item, index) in pieData"
              :key="index"
            >
              <div>
                <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                <span class="text">{{ item.name }}</span>
              </div>
              <span class="num">{{ item.value }} 个</span>
            </div>
          </div>
        </div>
      </div>
      <i class="split-line icon-split-line1"></i>
    </div>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList">
      <div class="catalog-title" ref="catalogTitle">
        <div class="type-area">
          <div class="tab-area">
            <div class="tab-active tabList">
              <div>
                总数<span>{{ listTitleTotal }}</span>
              </div>
            </div>
          </div>
          <div class="search-area" ref="searchArea">
            <div class="left-area">
              <el-input
                @input="getTbList"
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
              <i class="icon-liebiao-detail"></i>
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
        >
        </screen-table>
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
        <water-detail-dialog
          v-if="detailDialog"
          :sideType="sideType"
        ></water-detail-dialog>
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { ScreenTable, PrimaryMapTable, PrimaryDialog } from "@/components/";
import WaterDetailDialog from "../dialog/WaterDetailDialog";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";

export default {
  name: "WaterResourSide",
  data() {
    return {
      sideType: "",
      tabType: "条件选择",

      statisticData: [0, 1, 2, 3, 4, 5, 6], //统计图数据
      extraOption: {
        yAxis: {
          data: [
            "永嘉县",
            "永嘉县",
            "永嘉县",
            "永嘉县",
            "永嘉县",
            "永嘉县",
            "永嘉县",
          ],
        },
      },

      totalLength: 4,
      pieData: [
        { name: "县级以上", value: 1 },
        { name: "县级以上1", value: 1 },
        { name: "县级以上2", value: 1 },
        { name: "县级以上3", value: 1 },
      ],

      adcd: "",
      adcdOption: [],
      waterType: "",
      waterTypeOption: [
        { label: "全部", value: "" },
        { label: "山塘", value: "山塘" },
        { label: "河流湖泊", value: "河流湖泊" },
        { label: "溪沟堰坝", value: "溪沟堰坝" },
        { label: "蓄水池", value: "蓄水池" },
        { label: "地下水", value: "地下水" },
        { label: "海水淡化", value: "海水淡化" },
        { label: "山泉水", value: "山泉水" },
        { label: "其他", value: "其他" },
      ],
      waterScale: "",
      waterScaleOption: [
        { label: "全部", value: "" },
        { label: "县级以上", value: "县级以上" },
        { label: "千吨万人", value: "千吨万人" },
        { label: "200~1000吨", value: "200~1000吨" },
        { label: "200吨以下", value: "200吨以下" },
      ],

      factoryType: "",
      factoryTypeOption: [
        { label: "全部", value: "" },
        { label: "县级以上", value: "城市" },
        { label: "乡镇", value: "乡镇" },
        { label: "联村", value: "联村" },
        { label: "单村", value: "单村" },
      ],
      factoryScale: "",
      factoryScaleOption: [
        { label: "全部", value: "" },
        { label: "县级以上", value: "县级以上" },
        { label: "千吨万人", value: "千吨万人" },
        { label: "200~1000吨", value: "200~1000吨" },
        { label: "200吨以下", value: "200吨以下" },
      ],

      listTitleTotal: 0,
      searchVal: "",
      tableColumns: [],
      dataList: [],
      tbLoading: false,
      tableListStyle: {}, //表格样式
      tableHeight: 0,

      detailDialog: false,
    };
  },
  computed: {},
  components: {
    ScreenTable,
    PrimaryMapTable,
    PrimaryDialog,
    WaterDetailDialog,
    EchartCapsule,
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
    window.addEventListener("resize", this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    // 获取简表数据
    getTbList() {
      switch (this.sideType) {
        case "水源":
          this.tableColumns = [
            {
              prop: "resName",
              label: "名称",
            },
            {
              prop: "resName",
              label: "位置",
            },
            {
              prop: "resName",
              label: "类型",
            },
            {
              prop: "resName",
              label: "可供水量(万m³)",
              width: "120px",
            },
          ];
          break;
        case "水厂":
          this.tableColumns = [
            {
              prop: "resName",
              label: "名称",
            },
            {
              prop: "resName",
              label: "位置",
            },
            {
              prop: "resName",
              label: "人口",
            },
            {
              prop: "resName",
              label: "供水对象",
              width: "120px",
            },
          ];
      }
    },
    getCountryData(data, checkName) {},
    // 饼图
    pieChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("pieChart");
      console.log(111, chartDom);
      var myChart = echarts.init(chartDom);
      var option;
      let colorList = [
        "#19af47",
        "#008fe8",
        "#e89100",
        "#6267ff",
        "#A000C8",
        "#ff551f",
        "#eec80b",
        "#98d89a",
        "#004fe3",
      ];
      let color = [];
      colorList.forEach((item) => {
        let obj = {
          type: "linear",
          x: 0,
          y: 0,
          x2: 1,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item, // 0% 处的颜色
            },
            {
              offset: 1,
              color: "rgba(0,0,0)", // 100% 处的颜色
            },
          ],
          global: false, // 缺省为 false
        };
        color.push(obj);
      });
      option = {
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter: this.totalLength + "个\n总数",
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieData,
            color: color,
          },
        ],
      };
      option && myChart.setOption(option);
    },
    // 重置
    resetParam() {},
    // 点击简表行
    rowClick() {},
    // 切换统计类型
    changeTabType(val) {
      this.tabType = val;
      switch (this.tabType) {
        case "条件选择":
          break;
        case "区域":
          break;
        case "规模":
          this.$nextTick(() => {
            this.pieChartOpt();
          });
          break;
        case "类型":
          break;
      }

      this.handleWindowResize();
    },
    handleWindowResize() {},
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
      @include flexbox;
      @include flexAC;
      margin-bottom: 10px;
      span {
        box-sizing: border-box;
        display: inline-block;
        padding: 4px 12px;
        // width: 80px;
        height: 32px;
        color: #ffffff;
        border: 1px solid rgba(255, 255, 255, 0.6);
        text-align: center;
        line-height: 20px;
        font-size: 14px;
        margin-right: 10px;
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

  .icon-circle {
    width: 12px !important;
    height: 12px !important;
    border-radius: 50%;
  }
  .icon-color-1 {
    @include chart-icon();
    background: #19af47;
  }
  .icon-color-2 {
    @include chart-icon();
    background: #008fe8;
  }
  .icon-color-3 {
    @include chart-icon();
    background: #e89100;
  }
  .icon-color-4 {
    @include chart-icon();
    background: #6267ff;
  }
  .icon-color-5 {
    @include chart-icon();
    background: #a000c8;
  }
  .icon-color-6 {
    @include chart-icon();
    background: #ff551f;
  }
  .icon-color-7 {
    @include chart-icon();
    background: #eec80b;
  }
  .icon-color-8 {
    @include chart-icon();
    background: #98d89a;
  }
  .icon-color-9 {
    @include chart-icon();
    background: #004fe3;
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