<!--
 * @Date: 2022-06-13 16:20:51
 * @Author: 
 * @Description: 工程水位曲线
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-13 16:21:25
 * @FilePath: \lucheng-app\src\components\WaterEchartsLine.vue
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
      option = {
        tooltip: {
          trigger: "axis",
          backgroundColor: "#40CD96",
          borderColor: "#40CD96",
          formatter: (params) => {
            return `<div>水位：${params[0].value}m</div>`;
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
          inverse: true,
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
            barWidth: 8,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#036EFF" },
                { offset: 1, color: "#66C9FF" },
              ]),
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