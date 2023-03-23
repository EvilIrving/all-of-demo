<template>
  <div class="dike-card" style="width:400px height:320px">
    <panel-item
      title="河流分布"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['行政区划', '河段等级','健康分析']"
      @change="handleTabChange"
    >
      <template #label-description></template>

      <template #title-switch>
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('河流分布')"
          style="margin-left: 10px"
        />
      </template>
      <!-- 第一个tab -->
      <div class="area-box" v-if="activeTab == 0">
        <!-- 堤防 -->
        <div class="rain-forecast-card">
          <div class="rainfall-style">
            <p>堤防总数 <span> 199</span> 条</p>
          </div>
        </div>
        <!-- 1h, 3h, 6h, 12h, 24h, 72h 实时降雨量数据 -->
        <card-tabss
          v-model="realtimePreCardSelect"
          v-loading="realtimePreCardLoading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          :tabs-active="cardSelect == 'realtime'"
          :list="realtimePreCardList"
          @change="realtimePreCardClick"
          >kkk</card-tabss
        >
      </div>
      <!-- 第二个tab -->
      <div class="is-water-box" v-if="activeTab == 1">
        <ve-histogram
          ref="chart"
          height="150%"
          width="100%"
          :data="barData"
          :settings="barOption.setting"
          :extend="barOption.extend"
          :grid="barOption.grid"
          :data-zoom="barOption.dataZoom"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig"
        />

        <!-- <div class="rain-forecast-card">
          <div class="rainfall-style">
            <p>总数 <span> 200</span> 个</p>
          </div>
        </div> -->
      </div>

      <!-- </div> -->
    </panel-item>
  </div>
</template>
<script>
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import CardTabss from "../../WaterDisasterPreventionScreen/components/CardTabss.vue";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
// import { oaApi } from "@/api/governmentOaApi";
// import { generateDataZoom } from "./utils/chart.js";

// import * as echarts from "echarts"; // echart插件

export default {
  name: "dikeCard",
  components: {
    CardTabss,
    PanelItem,
    EchartPie,
  },
  // computed: {
  //   ...mapGetters(["previewModule"]),
  //   // map() {
  //   //   return this.$store.state.screen.mapComponent;
  //   // },
  // },

  data() {
    return {
      //两个卡片选择中
      cardSelect: "",
      maxSelect: "",

      barData: {
        columns: ["name", "data"],
        rows: [],
      },

      //实时降雨图表
      barOption: {
        dataZoom: {
          type: "inside",
          startValue: 0,
          endValue: 4,
        },
        grid: {
          top: 40,
          bottom: "1%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["条"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: "bold",
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
      activeTab: 0,
      //实时降雨图表tab
      realtimePreChartTabList: ["行政区划", "流域", "水库"],
      // realtimePreChartTab: 0,
      //实时降雨
      realCardIndex: 0,
      realtimePreCardSelect: 0,
      realtimePreCardLoading: false,

      timer: null,

      monthOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      checkMonth: "",

      totalLength: 0,
    };
  },
  mounted() {
    this.$nextTick(() => {
      // this.getWaterLoggingList();
      this.getWaterLoggingStats({ statsType: 1 }); // 行政区划
      this.getWaterLoggingStats({ statsType: 2 }); // 是否有积水
      this.timer = setInterval(() => {
        // this.getWaterLoggingList();
        this.getWaterLoggingStats({ statsType: 1 });
        this.getWaterLoggingStats({ statsType: 2 });
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  computed: {
    
    /** 实时降雨卡片数据 */
    realtimePreCardList() {
      return [
        {
          label: "近1小时",
          data: { number: "0", unit: "mm" },
        },
        {
          label: "前1小时",
          data: { number: "0", unit: "mm" },
        },
        {
          label: "前3小时",
          data: { number: "0", unit: "mm" },
        },
      ];
    },
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetDikeCount"]),
/** 选中1，3，6小时降雨卡片降切换 */
    precipitationCardClick(val) {
      if (val == null) {
        this.ScreenMap.removeLayerByName("rainfall");
        this.ScreenMap.clearTextLayer();
        this.$emit("legend-change", "实时降雨量", {});
        this.$emit("legend-change", "预报降雨量", {});
        return;
      }
      let valMap = [1, 3, 6];
      this.cardSelect = "forecast";
      let data = this.forecastPreList[`rainfall_${valMap[val]}_data`];
     
    },

    realtimePreCardClick(val) {
      this.realCardIndex = val;
      // this.realtimePreChartTab = 0;
    },

    // echarts

    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
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
      let barWidth = 30;
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
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#1EF8F2",
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
      return options;
    },
    // 获取积水统计数据
    async getWaterLoggingStats(opt) {
      let res = await disasterPreventionApi.waterLoggingStats(opt);
      if (opt.statsType == 1) {
        // 按行政区划统计
        if (res.code == 0) {
          let rows = [];
          for (let item of res.data.data) {
            rows.push({ name: item.key, data: item.value });
          }
          this.barData.rows = rows;
          this.$refs["chart"].echarts.resize();
        }
      } else {
        // 按是否有积水统计
        if (res.code == 0) {
          let info = res.data.data;
          this.pieData = [
            {
              value: info.noWater ? info.noWater : 0,
              name: "无积水",
              total: info.total,
              color: "#1CFFF5",
            },
            {
              name: "积水",
              value: info.hasWater ? info.hasWater : 0,
              total: info.total,
              color: "#FF7144",
            },
            {
              value: 0,
              name: "",
            },
          ];
        }
      }
      // // 获取c位数据
			// 	this.SetDikeCount({
			// 		peopleTotal: res.data.peopleTotal,
			// 		costTotal: res.data.costTotal
			// 	});
      
    },

    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.$emit("openDialog");
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.government-dike-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .is-water-box {
    display: flex;
  }
  .pie-box {
    width: 100%;
    height: calc(100% - 24px);
  }
  .pie-legend {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 60%;
    height: 100%;
    font-size: 16px;
    padding-left: 30px;
    > div {
      display: flex;
      justify-content: space-between;
      line-height: 30px;

      &:last-child .legend-circle {
        background: #ff7144;
      }
      .legend-circle {
        display: inline-block;
        height: 14px;
        width: 14px;
        border-radius: 50%;
        background: #1cfff5;
        vertical-align: middle;
        margin-right: 10px;
      }
      .legend-num {
        font-size: 20px;
        color: #1cfff5;
        margin-right: 7px;
      }
    }
  }

  .el-dropdown {
    cursor: pointer;
    top: -2px;
    right: 10px;
    .el-dropdown-link {
      display: flex;
      align-items: center;
      &.dropdown-active {
        span,
        i {
          color: #1cfff5;
        }
      }
    }
  }
  .el-icon-caret-bottom {
    margin-left: 3px;
  }
}
#barChart {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss">
.el-dropdown-menu.year-dropdown {
  max-height: 200px;
  overflow: auto;
  background: rgba(2, 10, 28, 0.7);
  //   background: #061639;
  border: 1px solid #1cfff5;
  .el-dropdown-menu__item {
    line-height: 26px;
  }
  .el-radio {
    color: #fff;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1cfff5;
    background: transparent;
    &::after {
      width: 8px;
      height: 8px;
      background: #1cfff5;
    }
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #1cfff5;
  }
  .el-radio__inner {
    background: transparent;
    border: 1px solid #fff;
  }
}
.el-dropdown-menu.year-dropdown.el-popper[x-placement^="bottom"]
  .popper__arrow::after {
  border-bottom-color: rgba(2, 10, 28, 0.7);
}
</style>

<style lang="scss">
.dike-card .panel-item .panel-item__content--tabs .tab-button,
.dike-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.dike-card .panel-item .panel-item__content--tabs .tab-button.active,
.dike-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>

<style lang="scss" scoped>
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #15e2db;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
}
.chart-panel {
  height: 200px;
}
.pie-box {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
  .legend-panel {
    width: 40%;
    .legend-item {
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      .text {
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
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
  height: 100%;
  .flood-discharge {
    user-select: none;
    color: #fff;
    width: 100%;
    overflow-y: scroll;
    &::-webkit-scrollbar {
      display: none;
    }
    .flood-discharge-item {
      padding: 10px 0;
      border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
      position: relative;
      &.white-bg {
        &:after {
          content: "";
          position: absolute;
          height: 100%;
          width: 200%;
          left: 0;
          top: 0;
          opacity: 0.5;
          background-image: linear-gradient(
            90deg,
            #ffffff33 0%,
            #f9f9f900 100%
          );
        }
      }
      &:nth-child(even) {
        border-right: 0;
      }
      .flood-discharge-item__content {
        .item-title {
          font-size: 16px;
          text-align: center;
          padding-top: 5px;
        }
        .item-text {
          display: flex;
          padding-top: 5px;
          font-size: 18px;
          justify-content: center;
          > i {
            margin: 5px 10px 0 0;
          }
          .item-label {
            font-size: 14px;
            .number {
              margin: 0 5px;
              font-size: 24px;
              color: #1cfff5;
            }
          }
        }
      }
    }
  }
}
::v-deep .rivertooltip__item {
  padding: 4px 8px;
  font-size: 18px;
  &.rivertooltip__item-error {
    color: #ff7144;
  }
  &.rivertooltip__item-warn {
    color: #eec80b;
  }
}
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}
.reservoir-sty {
  > p {
    @include flexbox;
    @include flexflow(row, nowrap);
    line-height: 32px;
    padding: 4px 0;
    span {
      color: #fff;
      font-size: 24px;
    }
    display: flex;
    // flex-direction: column;
    align-items: center;
  }
  // > p:nth-child(1) {
  //   margin-right: 16px;
  //   > span {
  //     color: #1cfff5;
  //   }
  // }
  > p:nth-child(2) {
    background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat
      top center;
    background-size: 100% 100%;
  }
}
.reservoir-style {
  $color: #1cfff485;
  // position: absolute;
  content: "";
  width: 100%;
  // height: 2.5rem;
  left: 0;
  background: linear-gradient(to bottom, $color, $color) left top no-repeat,
    linear-gradient(to bottom, $color, $color) left top no-repeat,
    linear-gradient(to bottom, $color, $color) right top no-repeat,
    linear-gradient(to bottom, $color, $color) right top no-repeat,
    linear-gradient(to bottom, $color, $color) left bottom no-repeat,
    linear-gradient(to bottom, $color, $color) left bottom no-repeat,
    linear-gradient(to bottom, $color, $color) right bottom no-repeat,
    linear-gradient(to bottom, $color, $color) right bottom no-repeat;
  /*设置大小*/
  background-size: 12px 2px, 2px 12px;
  border-radius: 3px;
}
::v-deep .el-carousel {
  .el-carousel__container {
    height: 100%;
  }
}

.carousel-item-content {
  // padding: 0 16px;
  @include flexbox;
  @include flexflow(column, nowrap);
  @include flexJC(space-around);
}
.reservoir-sty1 {
  margin: 0;
  @include flexbox;
  @include flexJC(space-between);
  p {
    margin: 0;
    line-height: 32px;
    width: 50%;
    margin: 12px 0;
    @include flexbox;
    @include flexflow(column, nowrap);
    @include flexAC;
    @include flexJC(center);
    background: url(~assets/images/WaterDisastersImage/rainfall-bg.png)
      no-repeat;
    background-size: 100% 100%;
  }
  > p:nth-child(1) {
    margin-right: 16px;
    > span {
      color: #1cfff5;
      font-size: 16px;
    }
  }
  > p:nth-child(2) {
    background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat
      top center;
    background-size: 100% 100%;
    > span {
      font-size: 16px;
      > span {
        color: #eec80b;
      }
    }
  }
}
.area-style {
  padding: 5px !important;
  // margin: 0;
  margin: 0;
  height: 40px;
  p {
    @include flexbox;
    @include flexflow(row, nowrap);
    @include flexAC;
    span {
      margin: 0;
    }
  }

  > p:nth-child(1) {
    line-height: 1.8rem;
    color: #1cfff5;
    font-weight: 500;
    @include flexbox;
    > i {
      vertical-align: text-top;
      margin-top: 2px;
      margin-right: 0;
    }
    > span {
      padding-left: 2px;
      color: #1cfff5;
    }
  }
  > p:nth-child(2) {
    line-height: 25px;
    color: #fff;
    > span {
      // font-size: 28px;
      color: #1ff1f0;
      padding: 0 6px;
    }
  }
  > p:nth-child(3) {
    line-height: 25px;
    color: #fff;
    > i {
      vertical-align: text-top;
      margin-top: 7px;
      margin-right: 0;
    }
    > span {
      font-size: 28px;
      color: #eec80b;
      padding: 0 6px;
    }
  }
}
.diagram-sty {
  // margin: 15px 0;
  color: #fff;
  height: calc(100% - 60px);
  .diagram-sty-content {
    height: 100%;
  }
  .border-L {
    height: 90%;
    border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
  }
  ::v-deep.border-R::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 0; /*高宽分别对应横竖滚动条的尺寸*/
  }
  .border-R {
    padding-left: 10px;
    height: 90%;
    overflow-y: auto;
    font-size: 16px;
    color: #1cfff5;
    .classOption-sty {
      padding: 5px 0;
      display: flex;
      justify-content: space-between;
      > p:nth-child(1) {
        // width: 100px;
        overflow-x: auto;
        overflow: hidden;
      }
    }
  }
}
.storage-capacity {
  margin-top: 9px;
  background-image: linear-gradient(
    64deg,
    rgba(0, 193, 255, 0) 5%,
    #00c1ff 100%
  );
  width: 15%;
  height: 2px;
}
.storage-capacity-sty {
  background-image: linear-gradient(
    64deg,
    #00c1ff 5%,
    rgba(0, 193, 255, 0) 100%
  );
  margin-top: 9px;
  width: 20%;
  height: 2px;
}

.icon-sty {
  margin-right: 10px;
  &.icon-year-100 {
    @include chart-icon();
    background: #19af47;
  }
  &.icon-year-50 {
    @include chart-icon();
    background: #eec80b;
  }
  &.icon-year-20 {
    @include chart-icon();
    background: #e89100;
  }
  &.icon-year-10 {
    @include chart-icon();
    background: #dd00db;
  }
  &.icon-year-5 {
    @include chart-icon();
    background: #e80000;
  }
  &.risk-legend {
    width: 16px;
    height: 16px;
    border-radius: 50%;
    display: inline-block;
    @include chart-icon();
  }
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
  height: 100%;
  // padding: 0 16px;
  .rainfall-style {
    @include flexbox;
    @include flexJC(center);
    margin-top: 16px;
  }
  ::v-deep .panel-item {
    .panel-item__content {
      @include flexbox;
      @include flexflow(column, nowrap);
      @include flexJC(space-around);
    }
  }
}
::v-deep .rivertooltip__item {
  padding: 4px 8px;
  font-size: 18px;
  &.rivertooltip__item-error {
    color: #ff7144;
  }
  &.rivertooltip__item-warn {
    color: #eec80b;
  }
}
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}

.water-gate-sty {
  padding: 10px;
  margin-bottom: 10px;
  background: url(~assets/images/WaterDisastersImage/shui-zha.png) no-repeat top
    center;
  background-size: 100% 100%;
  > .area-style {
    background: none;
    p {
      color: #fff;
      font-size: 18px;
      vertical-align: middle;
      > span {
        font-size: 26px;
        color: #1cfff5;
        margin: 0 3px;
      }
    }
    > p:last-child {
      span.yellow {
        color: #eec80b;
      }
    }
  }
  > .area-style:nth-child(1) {
    align-items: center;
  }

  > div:nth-child(1) {
    border-bottom: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
  }
  > div:nth-child(2) {
    > p:nth-child(1) {
      > i {
        font-size: 17px;
        line-height: 30px;
        margin-top: 2px;
        color: #fff;
      }
      font-size: 16px;
      color: #fff;
      display: flex;
      justify-content: left;
      ::v-deep.el-input {
        width: 32px;
        height: 32px;
      }
      ::v-deep.el-input-number {
        position: relative;
        display: inline-block;
        width: 60px;
        border: 1px solid #ffffff;
        border-radius: 4px;
      }
      ::v-deep.el-input__inner {
        background: none;
        padding: 0;
      }
      ::v-deep.el-input-number__decrease {
        width: 24px;
        border-left: 0.0625rem solid #fff;
        background: none;
        > i {
          color: #fff;
        }
      }
      ::v-deep.el-input-number__increase {
        width: 24px;
        height: 16px;
        border-left: 0.0625rem solid #fff;
        background: none;
        > i {
          color: #fff;
        }
      }
    }
    > p:nth-child(2) {
      font-size: 16px;
      > span {
        padding: 0;
        margin: 0;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.dialog-content {
  padding: 8px 0 16px;
}
.primary-dialog-tabs {
  padding: 8px 0;
  span {
    border: 1px solid #ffffff;
    color: #fff;
    padding: 4px 16px;
    opacity: 0.6;
    margin-right: 16px;
    cursor: pointer;
    &.active {
      border: 1px solid #56fefe;
      color: #56fefe;
    }
  }
}
.content-box {
  padding-top: 8px;
  .search-row {
    padding-top: 12px;
    span {
      display: inline-block;
      width: 80px;
      text-align: right;
      padding-right: 12px;
      color: #fff;
    }
  }
}
.rain-card {
  height: 100%;
  // padding: 0 16px;
  .dialog-content {
    padding: 8px 0 16px;
  }
  .primary-dialog-tabs {
    padding: 8px 0;
    span {
      border: 1px solid #ffffff;
      color: #fff;
      padding: 4px 16px;
      opacity: 0.6;
      margin-right: 16px;
      cursor: pointer;
      &.active {
        border: 1px solid #56fefe;
        color: #56fefe;
      }
    }
  }
  .content-box {
    padding-top: 8px;
    .search-row {
      padding-top: 12px;
      span {
        display: inline-block;
        width: 80px;
        text-align: right;
        padding-right: 12px;
        color: #fff;
      }
    }
  }

  ::v-deep .panel-item__title--switch {
    i {
      cursor: pointer;
    }
  }
  .typhoon-search {
    width: 360px;
    height: 100%;
    @include flexbox;
    @include flexflow(column, nowrap);
    > p {
      padding: 8px 0 12px 0;
      color: #fff;
    }
    .search-row {
      margin-bottom: 12px;
      line-height: 44px;
      .search-label {
        color: #fff;
        height: 100%;
        @include flexbox;
        @include flexAC;
        @include flexJC(center);
      }
    }
    .primary-btn {
      color: #fff;
      margin: 6px 0;
    }
    .typhoon-list {
      height: 270px;
    }
  }

  .prediction-sty {
    text-align: center;
    color: #ffffff;
    padding-top: 15px;
    padding-bottom: 15px;
    .border-sty {
      border-left: 1px solid rgba(35, 140, 239, 0.4);
      border-right: 1px solid rgba(35, 140, 239, 0.4);
    }
    .rainfall-sty {
      font-size: 20px;
      color: #1cfff5;
    }
  }

  .areal-rainfall-sty {
    display: flex;
    justify-content: space-between; /* 横向中间自动空间 */
    > div:nth-child(1) {
      vertical-align: middle;
      > i {
        vertical-align: middle;
      }
      > span {
        color: #fff;
      }
    }
    > div:nth-child(2) {
      line-height: 21px;
      .tab-switch-sty {
        padding: 2px 10px;
        border-radius: 15px;
        background: #00c1ff;
      }
      > span {
        color: #fff;
        margin-left: 15px;
        font-size: 15px;
      }
    }
  }

  .early-warning-sty {
    font-size: 20px;
    font-weight: bold;
    padding: 15px 20px;
    background-image: linear-gradient(
      to right,
      rgba(117, 124, 52, 0.3),
      rgba(0, 0, 0, 0)
    );
    > p {
      color: #eec80b;
    }
    > p:nth-child(2) {
      margin-top: 16px;
      > span {
        color: #eec80b;
        > span {
          color: #fff;
          font-size: 14px;
        }
      }
    }
  }
  .early-warning-style {
    font-size: 20px;
    font-weight: bold;
    padding: 15px 20px;
    background-image: linear-gradient(
      to right,
      rgba(192, 110, 63, 0.3),
      rgba(0, 0, 0, 0)
    );
    > p {
      color: #ff7144;
    }
    > p:nth-child(2) {
      margin-top: 16px;
      > span {
        color: #ff7144;

        > span {
          color: #fff;
          font-size: 14px;
        }
      }
    }
  }

  .switch-list {
    ::v-deep.el-carousel--horizontal {
      overflow-x: hidden;
      overflow-y: hidden;
    }
  }

  .sidebar-list {
    .warning-style {
      font-size: 16px;
      color: #ffffff;
      line-height: 24px;
      font-weight: 400;
    }
    ::v-deep.el-carousel--horizontal {
      overflow-x: hidden;
      overflow-y: hidden;
      height: unset;
    }
  }
}
</style>
<style lang="scss" scoped>
.rain-card .v-charts-component-loading {
  background: transparent;
}
.rain-card .station-list {
  display: flex;
  .statistic {
    width: 50%;
    margin-right: 16px;
    margin-top: 8px;
    padding: 8px 0;
    color: #fff;
    font-size: 18px;
    display: flex;
    justify-content: center;
    align-items: baseline;
    background: url(~assets/images/WaterDisastersImage/rainfall-bg.png)
      no-repeat top center;
    background-size: 100% 100%;
    .number {
      margin: 0 3px;
      font-size: 28px;
      color: #1ef8f2;
      line-height: 24px;
    }
    &:last-child {
      margin-right: 0;
    }
  }
}
</style>

