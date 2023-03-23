<template>
  <div class="detail-table">
    <el-dialog
      top="10vh"
      width="1000px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog emergency-scheme-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">模型分析</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="tab-area">
          <span
            :class="[tabActive == '双指数插值' ? 'active' : '']"
            @click="changeTab('双指数插值')"
            >双指数插值</span
          >
          <span
            :class="[tabActive == '趋势分析' ? 'active' : '']"
            @click="changeTab('趋势分析')"
            >趋势分析</span
          >
        </div>
        <div class="configuration-area">
          <span class="configuration-label">参数配置</span>
          <!-- 双指数插值配置 -->
          <div class="configuration-item" v-if="tabActive == '双指数插值'">
            <span>平滑指数: {{ smoothness / 100 }}</span>
            <div class="slider-box">
              <el-slider
                v-model="smoothness"
                :max="80"
                :format-tooltip="formatTooltip"
              ></el-slider>
            </div>
          </div>
          <div class="configuration-item" v-if="tabActive == '双指数插值'">
            <span>用水效率控制指标:</span>
            <div class="slider-box">
              <el-input v-model="waterUse" style="width: 100px"></el-input>
              <span>%</span>
            </div>
          </div>

          <!-- 趋势分析配置 -->
          <div class="configuration-item" v-if="tabActive == '趋势分析'">
            <span>年经济增长率:</span>
            <div class="slider-box">
              <el-input v-model="economicGrow" style="width: 100px"></el-input>
              <span>%</span>
            </div>
          </div>
          <div class="configuration-item" v-if="tabActive == '趋势分析'">
            <span>用水效率控制指标:</span>
            <div class="slider-box">
              <el-input v-model="controlTarget" style="width: 100px"></el-input>
              <span>%</span>
            </div>
          </div>
          <div class="btn confirm-btn" @click="btnClickHandle">确认</div>
        </div>
        <div class="chart-area">
          <div class="panel-item__content--tabs">
            <span
              v-for="(item, index) in actionTabs"
              :key="index"
              :class="[index == activeActionTab && 'active', 'tab-button']"
              @click="handleActionTabChange(index)"
            >
              {{ item }}
            </span>
          </div>
          <div class="chart-box">
            <echart-bar
              ref="chartBar"
              :extraOption="extraOption"
              :seriesData="statisticData"
            ></echart-bar>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import EchartBar from "../charts/echart_3dbar/EchartBar.vue";

import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { generateDataZoom } from "../cards/utils/chart.js";

export default {
  name: "ModuleAnalyDialog",
  data() {
    return {
      tabActive: "双指数插值",
      smoothness: 0,
      waterUse: 0,
      actionTabs: ["需水量预测", "经济预测"],
      activeActionTab: 0,
      statisticData: [],
      extraOption: {},
      economicGrow: 6.5,
      controlTarget: 17.5,
      chartData: {},
    };
  },
  props: {
    visible: {
      type: Boolean,
    },
  },
  watch: {},
  components: {
    EchartBar,
  },
  created() {},
  mounted() {},
  computed: {},
  methods: {
    init() {
      this.watGdpPreCurveByDouExpoModel();
    },
    // 双指数模型
    async watGdpPreCurveByDouExpoModel() {
      this.chartData = {
        name: [],
        now: [],
        pre: [],
        watConControlNorm: 0,
      };
      let opt = {
        factor: this.smoothness / 100,
        watPerGdpDecrRate: Number(this.waterUse),
      };
      await waterResourceApi.watGdpPreCurveByDouExpoModel(opt).then((res) => {
        this.chartData.watConControlNorm = res.data.watConControlNorm;
        res.data.historicCurve.forEach((item) => {
          this.chartData.name.push(item.year);
          this.chartData.now.push(
            this.activeActionTab == 0
              ? item.water.toFixed(2)
              : item.gdp.toFixed(2)
          );
          this.chartData.pre.push(null);
        });
        res.data.predictCurve.forEach((item) => {
          this.chartData.name.push(item.year);
          this.chartData.pre.push(
            this.activeActionTab == 0
              ? item.water.toFixed(2)
              : item.gdp.toFixed(2)
          );
        });
      });
      this.$nextTick(() => {
        this.getChartData();
      });
    },
    // 趋势模型
    async watGdpPreCurveByTrendPreModel() {
      this.chartData = {
        name: [],
        now: [],
        pre: [],
        watConControlNorm: 0,
      };
      let opt = {
        ecoIncrRate: Number(this.economicGrow),
        watPerGdpDecrRate: Number(this.controlTarget),
      };
      await waterResourceApi.watGdpPreCurveByTrendPreModel(opt).then((res) => {
        this.chartData.watConControlNorm = res.data.watConControlNorm;
        res.data.historicCurve.forEach((item) => {
          this.chartData.name.push(item.year);
          this.chartData.now.push(
            this.activeActionTab == 0
              ? item.water.toFixed(2)
              : item.gdp.toFixed(2)
          );
          this.chartData.pre.push(null);
        });
        res.data.predictCurve.forEach((item) => {
          this.chartData.name.push(item.year);
          this.chartData.pre.push(
            this.activeActionTab == 0
              ? item.water.toFixed(2)
              : item.gdp.toFixed(2)
          );
        });
      });
      this.$nextTick(() => {
        this.getChartData();
      });
    },
    btnClickHandle() {
      this.tabActive == "双指数插值"
        ? this.watGdpPreCurveByDouExpoModel()
        : this.watGdpPreCurveByTrendPreModel();
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    formatTooltip(val) {
      return val / 100;
    },
    handleActionTabChange(val) {
      this.activeActionTab = val;
      this.watGdpPreCurveByDouExpoModel();
    },
    getChartData() {
      let botData = [];
      let colorArr = ["#2886c6", "#50bfda", "#89e3ec"];
      let color = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[1],
          },
          {
            offset: 1,
            color: colorArr[1],
          },
        ],
      };
      let iconPath1 = require("@/assets/images/xushuiliang-1.png");
      let iconPath2 = require("@/assets/images/xushuiliang-dotted-1.png");
      this.extraOption = {
        dataZoom: generateDataZoom(6, this.chartData.name.length),
        grid: {
          left: this.activeActionTab == 0 ? "5%" : "8%",
          top: "10%",
          bottom: "10%",
        },
        yAxis: {
          name: this.activeActionTab == 0 ? "亿m³" : "亿元",
          nameTextStyle: {
            color: "#ffffff99",
            fontSize: "14",
          },
          max: this.activeActionTab == 0 ? 25 : null,
        },
        legend: {
          selectedMode: false,
          textStyle: {
            color: "#fff",
          },
          itemGap: 20,
          data: [
            {
              name: this.activeActionTab == 0 ? "需水量" : "万元GDP",
              icon: "image://" + iconPath1,
            },
            {
              name: this.activeActionTab == 0 ? "预测需水量" : "预测经济",
              icon: "image://" + iconPath2,
            },
          ],
        },
      };
      this.chartData.now.forEach((item) => {
        botData.push(0);
      });
      this.statisticData = [
        {
          z: 1,
          name: this.activeActionTab == 0 ? "需水量" : "万元GDP",
          type: "bar",
          barWidth: 30,
          barGap: "0%",
          data: this.chartData.now,
          markLine: {
            symbol: "none",
            data: [
              {
                silent: false,
                lineStyle: {
                  type: "solid",
                  color: "#E89100",
                  width: this.activeActionTab == 0 ? 1 : 0,
                },
                yAxis: 21,
                label: {
                  color: "#fff",
                  show: this.activeActionTab == 0 ? true : false,
                  position: "insideEndTop",
                  formatter: function () {
                    return "十四五用水总量控制";
                  },
                },
              },
            ],
          },
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 18,
            fontWeight: "bold",
            position: "top",
            color: "00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        {
          z: 2,
          name: "",
          type: "pictorialBar",
          data: botData,
          symbol: "diamond",
          symbolOffset: ["-35%", "50%"],
          symbolSize: [30, 10],
          itemStyle: {
            normal: {
              color: color,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "",
          type: "pictorialBar",
          symbolPosition: "end",
          data: this.chartData.now,
          symbol: "diamond",
          symbolOffset: ["-40%", "-50%"],
          symbolSize: [26, (10 * (30 - 4)) / 30],
          itemStyle: {
            normal: {
              borderColor: "#89e3ec",
              borderWidth: 2,
              color: "#89e3ec",
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          name: this.activeActionTab == 0 ? "预测需水量" : "预测经济",
          type: "bar",
          data: this.chartData.pre,
          barWidth: 20,
          itemStyle: {
            borderColor: "#1EF8F2",
            borderType: "dashed",
            borderWidth: 2,
            color: function (params) {
              let color = "rgba(0,0,0,0)";
              if (params.value && params.value > 4) {
                color = "#E89100";
              }
              return color;
            },
          },
          label: {
            show: true,
            fontSize: 18,
            fontWeight: "bold",
            position: "top",
            color: "#E89100",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
      ];
      this.extraOption = {
        ...this.extraOption,
        xAxis: {
          data: this.chartData.name,
        },
      };
      this.$refs["chartBar"].updateChartView();
    },
    changeTab(val) {
      this.tabActive = val;
      if (val == "双指数插值") {
        this.watGdpPreCurveByDouExpoModel();
      } else if (val == "趋势分析") {
        this.watGdpPreCurveByTrendPreModel();
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.emergency-scheme-dialog.primary-dialog {
  .top {
    margin-bottom: 25px;
  }
  p.comTit {
    font-family: PingFangSC-Semibold;
    font-size: 1.125rem;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 2rem;
    font-weight: 600;
    i {
      vertical-align: middle;
      margin-top: -2px;
    }
  }
  p.little {
    font-family: PingFangSC-Semibold;
    font-size: 0.875rem;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 1.5rem;
    font-weight: 400;
    i {
      vertical-align: middle;
      margin-top: -2px;
    }
  }
  .module-title {
    color: #1ef8f2;
    padding-left: 4px;
    line-height: 40px;
  }
  .divide {
    width: 100%;
    height: 1px;
    background: rgba(0, 193, 255, 0.2);
  }
  .label-name {
    margin-left: 5px;
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 24px;
    font-weight: 400;
    vertical-align: top;
  }
  .input {
    display: inline-block;
    padding: 4px;
    margin-right: 25px;
    border: 1px solid #ffffff;
    border-radius: 4px;
    font-family: PingFang-SC-Regular;
    font-size: 14px;
    color: #ffffff;
    line-height: 20px;
    font-weight: 400;
    width: 162px;
    .address {
      color: #009aff;
    }
  }
  .content {
    width: 419px;
  }
  .title {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 32px;
    font-weight: 600;
  }
  .t-title {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #33ffff;
    letter-spacing: 0;
    line-height: 24px;
    font-weight: 600;
    margin: 10px 0;
  }
  .box {
    display: flex;
    .warp {
      flex: 1;
    }
    .address {
      font-family: PingFang-SC-Regular;
      font-size: 14px;
      color: #ffffff;
      line-height: 20px;
      font-weight: 400;
    }
    .box-seven {
      display: inline-block;
    }
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }
  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 600px;
        border-top: 0;
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        @include flexbox;
        @include flexflow(column, nowrap);
        .tab-area {
          width: 100%;
          @include flexbox;
          @include flexAC;
          > span {
            display: inline-block;
            padding: 8px;
            width: 100px;
            color: #fff;
            border: 1px solid rgba(255, 255, 255, 0.8);
            text-align: center;
            line-height: 16px;
            font-size: 16px;
            margin-right: 16px;
            cursor: pointer;
            &.active {
              border: 1px solid #56fefe;
              color: #56fefe;
            }
          }
        }
        .chart-area {
          @include flex(1);
          @include flexbox;
          @include flexflow(column, nowrap);
          .panel-item__content--tabs {
            .tab-button {
              cursor: pointer;
              user-select: none;
              height: 24px;
              line-height: 24px;
              padding: 0 5px;
              border-radius: 13px;
              color: #fff;
              font-size: 14x;
              &.active {
                padding: 2px 7px;
                background-image: linear-gradient(
                  180deg,
                  #36c0e4 20%,
                  #0639ff 100%
                );
              }
            }
          }
          .panel-item__content--tabs {
            margin-top: 16px;
          }
          .chart-box {
            // @include flex(1);
            height: calc(100% - 60px);
          }
        }
        .configuration-area {
          width: 100%;
          height: 56px;
          border: 1px solid #1ef8f2;
          @include flexbox;
          @include flexflow(row, nowrap);
          align-items: center;
          position: relative;
          margin-top: 16px;
          .configuration-label {
            display: inline-block;
            font-size: 16px;
            color: #1ef8f2;
            position: absolute;
            top: -10px;
            left: 12px;
            background-image: linear-gradient(180deg, #1c3357 0%, #0b2045 100%);
            padding: 0 4px;
          }
          .configuration-item {
            @include flexbox;
            @include flexflow(row, nowrap);
            @include flexAC;
            height: 100%;
            margin-right: 16px;
            > span {
              font-size: 14px;
              color: #ffffff;
              padding: 0 16px;
              opacity: 0.6;
            }
            .slider-box {
              width: 240px;
              @include flex(1);
              > span {
                color: #fff;
                opacity: 0.6;
                padding-left: 10px;
              }
              ::v-deep.el-slider {
                .el-slider__runway {
                  .el-slider__bar {
                    background-color: #1ef8f2;
                  }
                }
              }
            }
          }

          .confirm-btn {
            color: #56fefe;
            height: 32px;
            line-height: 32px;
            border: 1px solid #56fefe;
            border-radius: 3px;
          }
        }
      }
      .el-radio__label {
        visibility: hidden;
      }
    }
  }

  ::v-deep.el-input-number {
    width: 5.25rem;
  }
  ::v-deep.el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  ::v-deep.el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  ::v-deep.el-select .el-input .el-select__caret {
    color: #fff;
  }
  ::v-deep input::-webkit-input-placeholder {
    color: #fff;
  }
  //   .el-table th {
  //     color: #1ef8f2;
  //   }
  //   .el-table thead tr {
  //     background-image: linear-gradient(
  //       90deg,
  //       rgba(44, 158, 191, 0.3) 3%,
  //       rgba(41, 147, 180, 0.1) 100%
  //     );
  //   }
  //   .el-table__row.el-table__row--striped {
  //     background-image: linear-gradient(
  //       90deg,
  //       rgba(255, 255, 255, 0.2) 0%,
  //       rgba(249, 249, 249, 0) 100%
  //     );
  //   }
  //   ::v-deep .el-input-number .el-input {
  //     width: 40px;
  //   }
  ::v-deep .el-input-number .el-input__inner {
    padding-left: 16px;
    padding-right: 0;
    background: none;
    text-align: left;
  }
  ::v-deep.el-input-number {
    position: relative;
    display: inline-block;
    width: 66px;
    border: 1px solid #ffffff;
    border-radius: 4px;
  }
  ::v-deep.el-input-number__decrease {
    border-left: 1px solid #fff;
    background: none;
    > i {
      color: #fff;
    }
  }
  ::v-deep.el-input-number__increase {
    height: 1.05rem;
    border-left: 1px solid #fff;
    background: none;
    > i {
      color: #fff;
    }
  }
  ::v-deep.el-input-number--small .el-input-number__decrease,
  ::v-deep.el-input-number--small .el-input-number__increase {
    width: 20px;
  }
}
</style>