<!--
 * @Date: 2022-06-15 15:10:59
 * @Author: 
 * @Description: 
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-25 17:28:18
 * @FilePath: \lucheng-app\src\views\appView\reservoir\indexPage\EchartsBar.vue
-->
<template>
  <div class="bar-box">
    <div class="bar" ref="pillar"></div>
  </div>
</template>

<script>
import { merge } from "lodash";
export default {
  props: {
    extraOption: {
      type: Object,
      default: () => {
        return {};
      },
    },
    seriesData: {
      type: Array,
      default: () => [],
    },
    inverse: {
      type: Boolean,
      default: false,
    },
    widthIndex: {
      type: Number,
      default: 0,
    },
  },
  watch: {
    extraOption: {
      handler: function (val) {
        if (val) {
          this.init();
        }
      },
      deep: true,
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.init();
    });
  },
  methods: {
    init() {
      let chartDom = this.$refs.pillar;
      let myChart = this.$echarts.init(chartDom);
      let echarts = this.$echarts;
      let option;
      let barWidth = 8;
      switch (this.widthIndex) {
        case 5:
          barWidth = 2;
          break;
        case 4:
          barWidth = 3;
          break;
        case 3:
          barWidth = 4;
          break;
        case 2:
          barWidth = 5;
          break;
        case 1:
          barWidth = 6;
          break;
        default:
          barWidth = 8;
          break;
      }
      option = {
        tooltip: {
          trigger: "axis",
          backgroundColor: "#40CD96",
          borderColor: "#40CD96",
          formatter: (params) => {
            return `<div>${params[0].seriesName}：${
              params[0].value || "-"
            }mm</div>`;
          },
          confine: true,
          axisPointer: {
            type: "line",
            z: 0,
            lineStyle: {
              color: "#276AF0",
              type: "dashed",
            },
          },
          position: function (point, params, dom, rect, size) {
            return point;
          },
        },
        xAxis: {
          type: "category",
          data: [],
          splitLine: {
            show: true,
            lineStyle: {
              color: "#E6E6E6",
            },
          },
          axisTick: {
            show: true,
            alignWithLabel: true,
          },
          axisLabel: {
            show: false,
            color: "#AAAAAA",
            formatter: (params) => {
              let text = params.split(" ");
              return [text[0], text[1]].join("\n");
            },
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: "#E6E6E6",
            },
          },
        },
        yAxis: {
          type: "value",
          inverse: this.inverse,
          axisTick: {
            show: false,
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: "#E6E6E6",
            },
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: "#E6E6E6",
            },
          },
          axisLabel: {
            show: true,
            color: "#AAAAAA",
          },
        },
        grid: {
          top: 10,
          right: 10,
          left: 30,
          bottom: 20,
        },
        series: [
          {
            data: [],
            type: "bar",
            barWidth: barWidth,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#036EFF" },
                { offset: 1, color: "#66C9FF" },
              ]),
            },
            markLine: {
              symbol: "none",
              label: {
                position: "insideEndTop",
                color: "#FF0000",
                formatter: () => {
                  return `限值线`;
                },
              },
              lineStyle: {
                color: "#FF0000",
                width: 2,
                type: "dotted",
              },
              data: [],
            },
          },
        ],
      };
      let optins = merge(
        option,
        {
          series: this.seriesData,
        },
        this.extraOption
      );
      myChart.setOption(optins);
    },
  },
};
</script>

<style lang="scss" scoped>
.bar-box {
  width: 100%;
  height: 100%;
  .bar {
    width: 100%;
    height: 100%;
  }
}
</style>