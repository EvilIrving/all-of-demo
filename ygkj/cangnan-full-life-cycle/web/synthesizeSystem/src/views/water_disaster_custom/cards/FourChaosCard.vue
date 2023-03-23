<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-07-06 22:04:35
 * @LastEditors: Please set LastEditors
 * @Description: 清“四乱”
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\WaterSupplyCard.vue
-->
<template>
  <div class="water-card">
    <panel-item title="清“四乱”" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          :class="[
            ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
          @click="drawPoint()"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('清四乱')"
        />
        <i
          @click="goToAmap('清四乱')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>

      <div class="area-box">
        <!-- <div class="chart-panel" v-if="activeTab == 0">
          <div class="all-count">
            四乱问题总数 <span>{{ 123 || "-" }}</span> 个
          </div>
          <div class="pie-box">
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
        </div> -->
        <div class="chart-panel">
          <ve-histogram
            ref="bar"
            height="100%"
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
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import PanelItem from "../components/PanelItem.vue";
import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import { Divider } from "vant";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { goAmap } from './utils/common';

export default {
  name: "FourChaosCard",
  layerName: ["qslLayer1", "qslLayer2", "qslLayer3", "qslLayer4", "qslLayer5"],
  legendName: "“四乱”问题",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      activeTab: 0,
      ldbtn: false,

      barData: {
        columns: ["county", "problemNum", "reviewStatusNum"],
        rows: [],
      },
      barOption: {
        grid: {
          top: "24%",
          left: "0%",
          right: "0%",
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["个"],
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 15,
          yAxis: {
            nameTextStyle: { color: "#ffffff99" },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
            axisLabel: {
              color: "#fff",
            },
            splitLine: {
              lineStyle: {
                color: "#00C1FF",
                opacity: 0.5,
              },
            },
          },
          "series.0.itemStyle": { color: "#00C1FF" },
        },
      },
      pointList: [],
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.statisProblemByArea();
      this.getFourChaosList();
      this.timer = setInterval(() => {
        this.statisProblemByArea();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterUseCount"]),

    // 柱状图数据
    async statisProblemByArea() {
      await waterResourceApi.statisProblemByArea().then((res) => {
        this.barData.rows = res.data;
      });
    },
    // 落点数据
    async getFourChaosList() {
      await waterResourceApi.loadPageFourChaos().then((res) => {
        this.pointList = res.data.list;
      });
    },

    // 落点
    drawPoint() {
      this.ldbtn = !this.ldbtn;
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/qsl-legend-ju-1.png"),
        src: require("@/assets/images/qsl-legend-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/qsl-legend-ju-2.png"),
        src: require("@/assets/images/qsl-legend-2.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/qsl-legend-ju-3.png"),
        src: require("@/assets/images/qsl-legend-3.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/qsl-legend-ju-4.png"),
        src: require("@/assets/images/qsl-legend-4.png"),
      };
      let style5 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/qsl-legend-ju-4.png"),
        src: require("@/assets/images/qsl-legend-4.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      let pointList5 = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.address,
          type: "清四乱",
          lng: Number(point.lng),
          lat: Number(point.lat),
          props: {
            四乱类型: `${point.fourType || "-"}`,
            严重程度: `${point.severity || "-"}`,
            更新时间: `${point.createTime || "-"}`,
          },
          obj: point,
        };
        if (point.fourType == "乱占") {
          pointList1.push(obj);
        } else if (point.fourType == "乱采") {
          pointList2.push(obj);
        } else if (point.fourType == "乱堆") {
          pointList3.push(obj);
        } else if (point.fourType == "乱建") {
          pointList4.push(obj);
        } else if (point.fourType == "其他") {
          pointList5.push(obj);
        }
      }
      if (this.ldbtn) {
        // console.log(2222, pointList1);
        this.$emit("legend-change", "“四乱”问题", {
          label: "“四乱”问题",
          children: [
            {
              label: "乱占",
              icon: "legend-qsl-1",
            },
            {
              label: "乱采",
              icon: "legend-qsl-2",
            },
            {
              label: "乱堆",
              icon: "legend-qsl-3",
            },
            {
              label: "乱建",
              icon: "legend-qsl-4",
            },
            {
              label: "其他",
              icon: "legend-qsl-5",
            },
          ],
        });
        this.map.drawClusterPoint(pointList1, style1, "qslLayer1");
        this.map.drawClusterPoint(pointList2, style2, "qslLayer2");
        this.map.drawClusterPoint(pointList3, style3, "qslLayer3");
        this.map.drawClusterPoint(pointList4, style4, "qslLayer4");
        this.map.drawClusterPoint(pointList5, style5, "qslLayer5");
      } else {
        this.$emit("legend-change", "“四乱”问题", {});
        this.map.drawClusterPoint([], {}, "qslLayer1");
        this.map.drawClusterPoint([], {}, "qslLayer2");
        this.map.drawClusterPoint([], {}, "qslLayer3");
        this.map.drawClusterPoint([], {}, "qslLayer4");
        this.map.drawClusterPoint([], {}, "qslLayer5");
      }
    },
    // 按行政区域 柱状图
    barConfig(options) {
      let data1 = options.series[0].data;
      let data2 = options.series[1].data;
      let colorArr1 = ["#e89100", "#fecb6e", "#fecb6e"];
      let colorArr2 = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
      let color1 = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr1[0],
          },
          {
            offset: 0.5,
            color: colorArr1[0],
          },
          {
            offset: 0.5,
            color: colorArr1[1],
          },
          {
            offset: 1,
            color: colorArr1[1],
          },
        ],
      };
      let color2 = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[1],
          },
          {
            offset: 1,
            color: colorArr2[1],
          },
        ],
      };
      let barWidth = 15;
      let constData1 = [];
      let showData1 = [];
      let constData2 = [];
      let showData2 = [];
      data1.filter(function (item) {
        if (item) {
          constData1.push(1);
          showData1.push(item);
        } else {
          constData1.push(0);
          showData1.push({
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
      data2.filter(function (item) {
        if (item) {
          constData2.push(1);
          showData2.push(item);
        } else {
          constData2.push(0);
          showData2.push({
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

      options.legend = {
        show: true,
        selectedMode: false,
        icon: "circle",
        left: "right",
        textStyle: {
          color: "#fff",
        },
      };

      // options.tooltip = {
      //   show: true,
      //   formatter: (data) => {
      //     return (
      //       data.name +
      //       "<br/><span style='font-size: 14px;'>今年：" +
      //       data1[data.dataIndex] +
      //       "亿m³</span> <br/><span style='font-size: 14px;'>去年：" +
      //       data2[data.dataIndex] +
      //       "亿m³</span>"
      //     );
      //   },
      // };

      options.series = [
        {
          z: 1,
          name: "问题数量",
          type: "bar",
          barWidth: barWidth,
          barGap: "30%",
          data: data1,
          itemStyle: {
            normal: {
              color: color1,
            },
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#e89100",
          },
        },
        {
          z: 2,
          name: "",
          type: "pictorialBar",
          data: constData1,
          symbol: "diamond",
          symbolOffset: ["-65%", "55%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color1,
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
          data: showData1,
          symbol: "diamond",
          symbolOffset: ["-92%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr1[2],
              borderWidth: 2,
              color: colorArr1[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 1,
          name: "整改数量",
          type: "bar",
          barWidth: barWidth,
          barGap: "30%",
          data: data2,
          itemStyle: {
            normal: {
              color: color2,
            },
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#1EF8F2",
          },
        },
        {
          z: 2,
          name: "",
          type: "pictorialBar",
          data: constData2,
          symbol: "diamond",
          symbolOffset: ["65%", "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color2,
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
          data: showData2,
          symbol: "diamond",
          symbolOffset: ["90%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr2[2],
              borderWidth: 2,
              color: colorArr2[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      options.dataZoom = generateDataZoom(4, this.barData.rows.length);
      return options;
    },

    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 打开详情弹框点击事件
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
    // 跳转一张图
    goToAmap(type) {
      goAmap(type);
      // this.$router.push(`/hydraulicAmap?selectType=${type}`);
    },
    handleTabChange(val) {
      this.activeTab = val;
      this.ldbtn = false;
      this.map.removeOverlayByName("waterChangeOverlay");
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-card {
  height: 100%;

  .area-box,
  .chart-panel {
    height: 100%;
  }

  .all-count {
    width: 100%;
    line-height: 35px;
    text-align: center;
    background: url(~assets/images/sygs-count-bg.png) no-repeat;
    background-size: 100% 100%;
    margin: 10px 0;
    span {
      margin: 0 10px;
      font-size: 20px;
      color: #1cfff5;
    }
  }

  .pie-box {
    // height: 200px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .legend-panel {
      width: 60%;
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
    background: #e82e00;
  }
  .icon-color-2 {
    @include chart-icon();
    background: #e89100;
  }
  .icon-color-3 {
    @include chart-icon();
    background: #19af47;
  }
  .icon-color-4 {
    @include chart-icon();
    background: #008fe8;
  }
  .icon-color-5 {
    @include chart-icon();
    background: #724ce8;
  }
}
</style>

<style lang="scss">
.water-card .panel-item .panel-item__content--tabs .tab-button,
.water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
