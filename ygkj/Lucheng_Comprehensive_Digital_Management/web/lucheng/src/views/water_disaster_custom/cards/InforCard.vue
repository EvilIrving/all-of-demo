<template>
  <div class="rain-forecast-card">
    <panel-item
      :title="activeCardInd == 0 ? '预警信息' : '消息通知'"
      :inSimulation="previewModule"
      :has-switch="activeCardInd == 0"
      tc
      @tclick="tclick"
    >
      <template #title-switch>
        <i
          v-show="!previewModule"
          v-if="activeCardInd == 0"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('预警信息')"
        />
      </template>

      <el-carousel
        trigger="click"
        arrow="never"
        :autoplay="false"
        :interval="interval"
        @change="cardChange"
      >
        <!-- 预警信息 -->
        <el-carousel-item class="carousel-item-content">
          <div class="top-panel">
            <div
              v-for="(item, ind) in topList"
              :key="ind"
              :class="['top-item', activeItem == ind ? 'active' : '']"
              @click="itemClick('预警信息', ind)"
            >
              <p class="top-item-text">{{ item.KEY }}</p>
              <p class="top-item-num">{{ item.num }}</p>
            </div>
          </div>
          <div class="bot-panel">
            <ve-histogram
              ref="bar"
              height="100%"
              width="100%"
              :data="barData"
              :settings="barOption.setting"
              :extend="barOption.extend"
              :grid="barOption.grid"
              :tooltip-visible="false"
              :legend-visible="false"
              :after-config="barConfig"
            />
          </div>
        </el-carousel-item>

        <!-- 消息通知 -->
        <el-carousel-item class="carousel-item-content">
          <div class="top-panel">
            <div
              v-for="(item, ind) in topList"
              :key="ind"
              :class="['top-item', activeItem2 == ind ? 'active2' : '']"
              @click="itemClick('消息通知', ind)"
            >
              <p class="top-item-text">{{ item.KEY }}</p>
              <p class="top-item-num num2">{{ item.num }}</p>
            </div>
          </div>
          <div class="bot-panel pie-chart-panel">
            <div id="pieChart" style="width: 180px; height: 150px"></div>
            <div class="legend-panel">
              <div
                class="legend-item"
                v-for="(item, index) in pieData.rows"
                :key="index"
              >
                <i
                  style="vertical-align: middle"
                  :class="['icon-sty', 'icon-circle', 'icon-' + index]"
                />
                <div class="text">
                  <p>{{ item.name }}</p>
                </div>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { inforApi } from "@/api/inforApi";
import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import * as echarts from "echarts"; // echart插件

export default {
  name: "InforCard",
  components: {
    PanelItem,
  },
  data() {
    return {
      interval: 5000,
      timer: null,

      activeCardInd: 0,
      activeItem: 0,
      activeItem2: 0,
      topList: [],
      barData: {
        columns: ["type", "num"],
        rows: [],
      },
      barOption: {
        grid: {
          top: 32,
          bottom: "5%",
          right: "0%",
          left: "0%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["条"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#fff",
          },
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
            },
          },
          yAxis: {
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize,
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize,
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5,
              },
            },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
          },
        },
      },

      pieData: {
        totalLength: 0,
        rows: [],
      },
    };
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    this.statistic(this.activeCardInd);
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.statistic(this.activeCardInd);
      }, 300000);
    });
  },
  computed: {
    ...mapGetters(["ScreenMap", "previewModule"]),
  },
  watch: {},
  methods: {
    ...mapActions([
      "changeScreenLoading",
      "getForecastRainfallLayer",
      "SetDetailDialogParam",
    ]),
    // 获取统计图数据
    async statistic(val) {
      let opt = {
        type: val == 0 ? "预警" : "提醒",
      };
      await inforApi.statistic(opt).then((res) => {
        this.topList = res.data.statistic;
        if (val == 0) {
          this.barData.rows = res.data.chartStatistic;
        } else {
          this.pieData.rows = [];
          this.pieData.totalLength = res.data.statistic[0].num;
          res.data.chartStatistic.forEach((item) => {
            let obj = {
              name: item.type,
              value: item.num,
            };
            this.pieData.rows.push(obj);
          });
          this.barEchartConfig();
        }
      });
    },
    async chartStatistic(val, opt) {
      opt = {
        messageType: val == 0 ? "预警" : "提醒",
        startTime: opt.startTime,
        endTime: opt.endTime,
      };
      await inforApi.chartStatistic(opt).then((res) => {
        if (val == 0) {
          this.barData.rows = res.data;
        } else {
          let count = 0;
          this.pieData.rows = [];
          res.data.forEach((item) => {
            let obj = {
              name: item.type,
              value: item.num,
            };
            count = count + item.num;
            this.pieData.rows.push(obj);
          });
          this.pieData.totalLength = count;
          this.barEchartConfig();
        }
      });
    },
    // 柱状图
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#c7aa19", "#eec80b", "#eec80b"];
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
      let barWidth = 15;
      let constData = [];
      let showData = [];
      data.filter(function (item) {
        if (item) {
          constData.push(1);
          showData.push(item);
        } else {
          constData.push(0);
          showData.push({
            value: 1,
            itemStyle: {
              normal: {
                borderColor: "rgba(0,0,0,0)",
                borderWidth: 2,
                color: "rgba(0,0,0,0)",
              },
            },
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: "数据",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: data,
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#eec80b",
          },
        },
        {
          z: 2,
          name: "数据",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: ["0%", "50%"],
          symbolSize: [barWidth, 10],
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
          name: "数据",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: ["0%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      options.dataZoom = generateDataZoom(5, this.barData.rows.length);
      this.$refs["bar"].echarts.resize();
      return options;
    },
    // 饼图
    barEchartConfig() {
      var chartDom = document.getElementById("pieChart");
      var myChart = echarts.init(chartDom);
      var option;
      const color0 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#00EBC7", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color1 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#008FE8", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color2 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#19AF47", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color3 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#FF7D7D", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color4 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#89BA8B", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color5 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#E89100", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color6 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#FF7144", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color7 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#004FE3", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color8 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#A80BEE", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color9 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#8A8AE1", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color10 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#EE0B6B", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color11 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#28C9F6", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color12 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#FFEC36", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color13 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#2DDF3B", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color14 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#DBA380", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      option = {
        grig: {
          top: 50,
        },
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              data.name +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'> 条</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            // center: ["50%", "50%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter: this.pieData.totalLength + "\n总量", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieData.rows,
            color: [color0, color1, color2, color3, color4, color5, color6, color7, color8, color9, color10, color11, color12, color13, color14],
          },
        ],
      };
      option && myChart.setOption(option);
    },
    // 卡片切换
    cardChange(val) {
      this.activeCardInd = val;
      this.activeItem = 0;
      this.activeItem2 = 0;
      this.statistic(val);
    },
    // 切换统计类型
    itemClick(type, val) {
      if (type == "预警信息") {
        this.activeItem = val;
      } else {
        this.activeItem2 = val;
      }

      let date = new Date();
      let d = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      let m =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      let y = date.getFullYear();
      let opt = {};
      switch (val) {
        case 0:
          this.statistic(this.activeCardInd);
          break;
        case 1:
          opt.startTime = `${y}-${m}-${d} 00:00:00`;
          opt.endTime = `${y}-${m}-${d} 23:59:59`;
          this.chartStatistic(this.activeCardInd, opt);
          break;
        case 2:
          opt.startTime = `${y}-${m}-01 00:00:00`;
          opt.endTime = `${y}-${m}-${d} 23:59:59`;
          this.chartStatistic(this.activeCardInd, opt);
          break;
        case 3:
          opt.startTime = `${y}-01-01 00:00:00`;
          opt.endTime = `${y}-${m}-${d} 23:59:59`;
          this.chartStatistic(this.activeCardInd, opt);
          break;
      }
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
  height: 100%;
}

::v-deep .el-carousel {
  .el-carousel__container {
    height: 100%;
  }
}

.carousel-item-content {
  padding: 8px 0;
  .top-panel {
    width: 100%;
    display: flex;
    justify-content: space-between;
    .top-item {
      cursor: pointer;
      padding: 8px 8px 2px 8px;
      p {
        text-align: center;
        margin-bottom: 5px;
      }
      &-text {
        text-align: center;
        margin-bottom: 5px;
      }
      &-num {
        color: #e4c31e;
        font-size: 18px;
        font-weight: bold;
      }
      .num2 {
        color: #1cfff5;
      }
      &.active {
        box-shadow: inset 0 0.25rem 1.875rem 0 rgba(194, 255, 28, 0.2);
        background: linear-gradient(to bottom, #dbc96c, #dbc96c) left top
            no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) left top no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) right top no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) right top no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) left bottom no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) left bottom no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) right bottom no-repeat,
          linear-gradient(to bottom, #dbc96c, #dbc96c) right bottom no-repeat;
        background-size: 18px 2px, 2px 18px;
        p {
          color: #e4c31e;
        }
      }
      &.active2 {
        box-shadow: inset 0 0.25rem 1.875rem 0 rgba(28, 255, 245, 0.2);
        background: linear-gradient(to bottom, #35e3e3, #35e3e3) left top
            no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) left top no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) right top no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) right top no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) left bottom no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) left bottom no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) right bottom no-repeat,
          linear-gradient(to bottom, #35e3e3, #35cbe3) right bottom no-repeat;
        background-size: 18px 2px, 2px 18px;
        p {
          color: #1cfff5;
        }
      }
    }
  }
  .pie-chart-panel {
    height: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    .legend-panel {
      max-height: 100px;
      overflow: auto;
      .legend-item {
        margin-bottom: 5px;
        .text {
          display: inline-block;
          p {
            display: inline-block;
            font-size: 14px;
            margin-bottom: 8px;
            margin-right: 10px;
            &:last-child {
              margin-bottom: 0;
            }
          }
        }
      }
    }
  }
  .bot-panel {
    width: 100%;
    height: calc(100% - 50px);
  }

  .icon-sty {
    margin-right: 10px;
  }
  .icon-circle {
    width: 12px !important;
    height: 12px !important;
  }

  .icon-0 {
    @include chart-icon();
    background: #00EBC7;
  }
  .icon-1 {
    @include chart-icon();
    background: #008FE8;
  }
  .icon-2 {
    @include chart-icon();
    background: #19AF47;
  }
  .icon-3 {
    @include chart-icon();
    background: #FF7D7D;
  }
  .icon-4 {
    @include chart-icon();
    background: #89BA8B;
  }
  .icon-5 {
    @include chart-icon();
    background: #E89100;
  }
  .icon-6 {
    @include chart-icon();
    background: #FF7144;
  }
  .icon-7 {
    @include chart-icon();
    background: #004FE3;
  }
  .icon-8 {
    @include chart-icon();
    background: #A80BEE;
  }
  .icon-9 {
    @include chart-icon();
    background: #8A8AE1;
  }
  .icon-10 {
    @include chart-icon();
    background: #EE0B6B;
  }
  .icon-11 {
    @include chart-icon();
    background: #28C9F6;
  }
  .icon-12 {
    @include chart-icon();
    background: #FFEC36;
  }
  .icon-13 {
    @include chart-icon();
    background: #2DDF3B;
  }
  .icon-14 {
    @include chart-icon();
    background: #DBA380;
  }
}
</style>
