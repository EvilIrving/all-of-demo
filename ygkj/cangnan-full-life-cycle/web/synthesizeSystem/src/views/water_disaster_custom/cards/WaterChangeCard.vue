<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-08-23 10:51:36
 * @LastEditors: ysl
 * @Description: 水域变化
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/WaterChangeCard.vue
-->
<template>
  <div class="water-card">
    <panel-item
      title="水域变化"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['水域面积', '水域占补']"
      @change="handleTabChange"
    >
      <template slot="title-switch">
        <i
          title="地图落点"
          :class="[
            sybtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
          @click="drawPointArea()"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水域变化')"
        />
      </template>

      <div class="area-box">
        <div class="count-panel">
          <div class="count-item" v-if="activeTab == 0">
            <i class="icon-count icon-sybh-count-1"></i>
            <div class="text">
              <p>{{ getYear }}年水域面积</p>
              <p class="num">{{ countObj.waterArea }} <span>k㎡</span></p>
            </div>
          </div>
          <div class="count-item" v-if="activeTab == 0">
            <i class="icon-count icon-sybh-count-2"></i>
            <div class="text">
              <p>{{ getYear }}年水面率</p>
              <p class="num">{{ countObj.waterRate }} <span>%</span></p>
            </div>
          </div>
          <div class="count-item" v-if="activeTab == 1">
            <i class="icon-count icon-sybh-count-3"></i>
            <div class="text">
              <p>{{ getYear }}年占用面积</p>
              <p class="num">{{ countObj.occupiedArea }} <span>k㎡</span></p>
            </div>
          </div>
          <div class="count-item" v-if="activeTab == 1">
            <i class="icon-count icon-sybh-count-4"></i>
            <div class="text">
              <p>{{ getYear }}年补偿面积</p>
              <p class="num">
                {{ countObj.compensationArea }} <span>k㎡</span>
              </p>
            </div>
          </div>
        </div>

        <div class="bar-panel" v-if="activeTab == 0">
          <div id="stackBar" style="width: 100%; height: 100%"></div>
        </div>
        <div class="bar-panel" v-if="activeTab == 1">
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
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterChangeCard",
  layerName: "waterChangeOverlay",
  legendName: ["水域面积", "水域占补"],
  components: {
    PanelItem
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      activeTab: 0,
      sybtn: false,

      stackBarData: {
        name: [],
        total: [],
        listObj: {
          hd: [],
          rg: [],
          st: [],
          sk: [],
          qt: []
        }
      },
      countObj: {},

      barData: {
        columns: ["name", "data1", "data2"],
        rows: [
          { name: "2019", data1: 3, data2: 5 },
          { name: "2020", data1: 5, data2: 4 },
          { name: "2021", data1: 4, data2: 5 },
          { name: "2022", data1: 5, data2: 3 },
          { name: "2023", data1: 3, data2: 6 }
        ]
      },
      barOption: {
        grid: {
          top: "24%",
          left: "0%",
          right: "0%",
          bottom: "5%",
          containLabel: true
        },
        setting: {
          yAxisName: ["k㎡"],
          labelMap: { data1: "占用面积", data2: "补偿面积" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1)
          }
        },
        extend: {
          legend: {
            right: "0%",
            icon: "circle",
            itemWidth: 10,
            textStyle: { color: "#FFF" }
          },
          "textStyle.color": "#fff",
          barWidth: 15,
          yAxis: {
            nameTextStyle: { color: "#ffffff99" },
            max: value => {
              return value.max > 1 ? null : 1;
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize
            },
            splitLine: {
              lineStyle: {
                color: "#00C1FF",
                opacity: 0.5
              }
            }
          },
          "series.0.itemStyle": { color: "#00C1FF" },
          "series.1.itemStyle": { color: "#EEC80B" }
        }
      },

      pointList: [],
      pointList2: []
    };
  },
  computed: {
    ...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime"]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    getYear() {
      let date = new Date();
      return date.getFullYear();
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.histogramWaterArea();
      this.overallStatisticsWaterArea();
      this.waterAreaEachAdministrative();
      this.waterCompensationEachAdministrative();
      this.timer = setInterval(() => {
        this.histogramWaterArea();
        this.overallStatisticsWaterArea();
        this.waterAreaEachAdministrative();
        this.waterCompensationEachAdministrative();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterUseCount"]),

    // 水域面积 柱状图
    async histogramWaterArea() {
      this.stackBarData = {
        name: [],
        total: [],
        listObj: {
          hd: [],
          rg: [],
          st: [],
          sk: [],
          qt: []
        }
      };
      await waterResourceApi.histogramWaterArea().then(res => {
        for (let i in res.data) {
          this.stackBarData.name.push(i);
          res.data[i].forEach(item => {
            switch (item.type) {
              case "1":
                this.stackBarData.listObj.hd.push(item.waterArea);
                break;
              case "2":
                this.stackBarData.listObj.qt.push(item.waterArea);
                break;
              case "3":
                this.stackBarData.listObj.rg.push(item.waterArea);
                break;
              case "4":
                this.stackBarData.listObj.st.push(item.waterArea);
                break;
              case "5":
                this.stackBarData.listObj.sk.push(item.waterArea);
                break;
              case "6":
                this.stackBarData.total.push(item.waterArea);
                break;
            }
          });
        }
        this.stackBarOpt();
      });
    },
    // 获取水域面积总数
    async overallStatisticsWaterArea() {
      let opt = {
        year: this.getYear
      };
      await waterResourceApi.overallStatisticsWaterArea(opt).then(res => {
        this.countObj = res.data;
      });
    },
    // 获取水域占补总数
    async statisticsReclaimedAreaWater() {
      let opt = {
        year: this.getYear
      };
      await waterResourceApi.statisticsReclaimedAreaWater(opt).then(res => {
        this.countObj = res.data;
      });
    },
    // 获取水域占补 柱状图
    async statisticsReclaimedByYear() {
      await waterResourceApi.statisticsReclaimedByYear().then(res => {
        this.barData.rows = [];
        res.data.forEach(item => {
          let data1 = 0;
          let data2 = 0;
          item.list.forEach(itemm => {
            data1 = data1 + itemm.occupiedArea;
            data2 = data1 + itemm.compensationArea;
          });
          var obj = {
            name: item.year,
            data1: data1.toFixed(2),
            data2: data2.toFixed(2)
          };
          this.barData.rows.push(obj);
        });
      });
    },
    // 水域面积 落点
    async waterAreaEachAdministrative() {
      let opt = {
        yesr: 2021
      };
      await waterResourceApi.waterAreaEachAdministrative(opt).then(res => {
        this.pointList = res.data;
      });
    },
    // 水域占补 落点
    async waterCompensationEachAdministrative() {
      let opt = {
        yesr: 2021
      };
      await waterResourceApi.waterCompensationEachAdministrative(opt).then(res => {
        this.pointList2 = res.data;
      });
    },

    // 落点
    drawPointArea(btn) {
      this.sybtn = !this.sybtn;
      let label = this.activeTab == 0 ? "水域面积" : "水域占补";
      if (this.sybtn) {
        this.$emit("legend-change", label, {
          label: label,
          children: [
            {
              label: "最大值",
              icon: "water-max"
            },
            {
              label: "最小值",
              icon: "water-min"
            },
            {
              label: "正常值",
              icon: "water-normal"
            }
          ]
        });
        this.map.addWaterChangeFixedOverlay(
          this.activeTab == 0 ? this.pointList : this.pointList2,
          "waterChangeOverlay",
          label
        );
      } else {
        this.$emit("legend-change", label, {});
        this.map.removeOverlayByName("waterChangeOverlay");
      }
    },
    // 水域面积 堆叠柱状图
    stackBarOpt() {
      const _this = this;
      var chartDom = document.getElementById("stackBar");
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        color: ["#00C1FF", "#19AF47", "#e89100", "#e80000", "#004fe3"],
        grid: {
          top: "8%",
          left: "0%",
          right: "0%",
          bottom: "5%",
          containLabel: true
        },

        dataZoom: generateDataZoom(6, this.stackBarData.name.length),
        xAxis: {
          type: "category",
          data: this.stackBarData.name,
          axisLabel: {
            color: "#fff",
            fontSize: this.$commonSetting.chartFontSize
          },
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize
            }
          }
        },
        yAxis: {
          type: "value",
          name: "k㎡",
          nameTextStyle: {
            color: "#ffffff99",
            fontSize: this.$commonSetting.chartFontSize
          },
          axisLine: {
            show: false
          },
          axisLabel: {
            color: "#fff",
            fontSize: this.$commonSetting.chartFontSize
          },
          splitLine: {
            lineStyle: {
              show: false,
              color: "#fff",
              opacity: 0.2
            }
          }
        },
        legend: {
          show: false,
          icon: "circle",
          left: "right",
          textStyle: {
            color: "#fff",
            fontSize: 12
          },
          itemGap: 4,
          itemWidth: 10,
          itemHeight: 10,
          selectedMode: false
        },
        tooltip: {
          appendToBody: true,
          trigger: "axis",
          formatter: function (data) {
            let ddom = "";
            data.forEach(item => {
              let dom =
                "<p class='sybh-tooltip-item'><span class='sybh-tooltip-name'><i class='sybh-tooltip-circle' style='background-color: " +
                item.color +
                "'></i>" +
                item.seriesName +
                "</span><span class='sybh-tooltip-num'>" +
                item.value +
                "</span>k㎡</p>";

              ddom = ddom + dom;
            });
            return (
              "<span style='font-size: 18px;'>" +
              data[0].name +
              "</span>" +
              "<p class='sybh-tooltip-item'><span class='sybh-tooltip-name'>总面积</span><span class='sybh-tooltip-num'>" +
              _this.stackBarData.total[data[0].dataIndex] +
              "</span>k㎡</p>" +
              ddom
            );
          },
          confine: true
        },
        series: [
          {
            name: "河道水域",
            type: "bar",
            stack: "total",
            data: this.stackBarData.listObj.hd,
            barWidth: 30
          },
          {
            name: "水库水域",
            type: "bar",
            stack: "total",
            data: this.stackBarData.listObj.sk,
            barWidth: 30
          },
          {
            name: "人工水道水域",
            type: "bar",
            stack: "total",
            data: this.stackBarData.listObj.rg,
            barWidth: 30
          },
          {
            name: "山塘水道水域",
            type: "bar",
            stack: "total",
            data: this.stackBarData.listObj.st,
            barWidth: 30
          },
          {
            name: "其他水域",
            type: "bar",
            stack: "total",
            data: this.stackBarData.listObj.qt,
            barWidth: 30
          }
        ]
      };
      option && myChart.setOption(option);
    },
    // 水域占比 双柱状图
    barConfig(options) {
      let data1 = options.series[0].data;
      let data2 = options.series[1].data;
      let colorArr1 = ["#06a2d4", "#62d9ff", "#64d9ff"];
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
            color: colorArr1[0]
          },
          {
            offset: 0.5,
            color: colorArr1[0]
          },
          {
            offset: 0.5,
            color: colorArr1[1]
          },
          {
            offset: 1,
            color: colorArr1[1]
          }
        ]
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
            color: colorArr2[0]
          },
          {
            offset: 0.5,
            color: colorArr2[0]
          },
          {
            offset: 0.5,
            color: colorArr2[1]
          },
          {
            offset: 1,
            color: colorArr2[1]
          }
        ]
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
                color: "rgba(0,0,0,0)"
              }
            }
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
                color: "rgba(0,0,0,0)"
              }
            }
          });
        }
      });

      options.legend = {
        show: true,
        selectedMode: false,
        icon: "circle",
        left: "right",
        textStyle: {
          color: "#fff"
        }
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
          name: "占用面积",
          type: "bar",
          barWidth: barWidth,
          barGap: "30%",
          data: data1,
          itemStyle: {
            normal: {
              color: color1
            }
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#06a2d4"
          }
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
              color: color1
            }
          },
          tooltip: {
            show: false
          }
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
              color: colorArr1[2]
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 1,
          name: "补偿面积",
          type: "bar",
          barWidth: barWidth,
          barGap: "30%",
          data: data2,
          itemStyle: {
            normal: {
              color: color2
            }
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#1EF8F2"
          }
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
              color: color2
            }
          },
          tooltip: {
            show: false
          }
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
              color: colorArr2[2]
            }
          },
          tooltip: {
            show: false
          }
        }
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
        analyseDialogShow: true
      });
    },
    handleTabChange(val) {
      this.activeTab = val;
      this.sybtn = false;
      this.map.removeOverlayByName("waterChangeOverlay");
      this.$emit("legend-change", this.activeTab == 0 ? "水域占补" : "水域面积", {});
      if (val == 0) {
        this.histogramWaterArea();
        this.overallStatisticsWaterArea();
      } else {
        this.statisticsReclaimedAreaWater();
        this.statisticsReclaimedByYear();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.area-box {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.water-card {
  height: 100%;

  .bar-panel {
    height: 100%;
  }

  .count-panel {
    display: flex;
    justify-content: space-between;
    .count-item {
      display: flex;
      align-items: center;
      margin: 2px 0;
      .text {
        font-size: 14px;
        margin-left: 10px;
        line-height: 25px;
        .num {
          font-size: 20px;
          color: #56fefe;
        }
        span {
          color: #fff;
          font-size: 12px;
        }
      }
    }
  }

  .icon-count {
    display: inline-block;
    width: 48px;
    height: 48px;
    background-size: 100% 100%;
  }
  .icon-sybh-count-1 {
    background-image: url(~assets/images/sybh-count-1.png);
  }
  .icon-sybh-count-2 {
    background-image: url(~assets/images/sybh-count-2.png);
  }
  .icon-sybh-count-3 {
    background-image: url(~assets/images/sybh-count-3.png);
  }
  .icon-sybh-count-4 {
    background-image: url(~assets/images/sybh-count-4.png);
  }
}
</style>

<style lang="scss">
.sybh-tooltip-item {
  margin-top: 4px;
}
.sybh-tooltip-name {
  display: inline-block;
  width: 120px;
}
.sybh-tooltip-circle {
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 5px;
}
.sybh-tooltip-num {
  font-size: 16px;
  color: #1cfff5;
  margin-right: 3px;
}

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
