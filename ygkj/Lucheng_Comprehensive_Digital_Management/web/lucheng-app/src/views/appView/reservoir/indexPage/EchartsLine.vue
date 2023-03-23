<!--
 * @Date: 2022-06-15 14:12:31
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-24 11:06:03
 * @FilePath: \lucheng-app\src\views\appView\reservoir\indexPage\EchartsLine.vue
-->
<template>
  <div class="bar-box">
    <div class="bar" ref="line"></div>
  </div>
</template>

<script>
import { merge } from "lodash";
export default {
  props: {
    // 表示需要特殊定制的配置
    // 一般UI会规定一个统一的设计规范（比如颜色，字体，图例格式，位置等）
    // 但不排除某个图标会和设计规范不同，需要特殊定制样式，所以开放这个配置，增强灵活性
    extraOption: {
      type: Object,
      default: () => {
        return {};
      },
    },
    // 正常的业务数据，对应echarts饼图配置中series[0].data
    seriesData: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  watch: {
    extraOption: {
      handler: function (val) {
        if (val) {
          this.init();
        }
      },
      deep: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.init();
    });
  },
  methods: {
    init() {
      let chartDom = this.$refs.line;
      let myChart = this.$echarts.init(chartDom);
      let echarts = this.$echarts;
      let option;
      option = {
        tooltip: {
          trigger: "axis",
          backgroundColor: "#40CD96",
          borderColor: "#40CD96",
          formatter: (params) => {
            console.log(params[0].seriesName,'params[0].seriesName');
            return `<div>${params[0].seriesName}：${params[0].value != undefined ? params[0].value : "-"
              }m</div>`;
          },
          z: 0,
          confine: true,
          axisPointer: {
            type: "line",
            lineStyle: {
              color: "#E6E6E6",
              type: "dashed",
              width: 0,
            },
          },
          position: function (point, params, dom, rect, size) {
            return point;
          },
        },
        // dataZoom:[
        //   {
        //     type: 'inside',
        //     orient: 'horizontal'
        //   }
        // ],
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
            show: true,
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
          boundaryGap: false,
        },
        yAxis: {
          type: "value",
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
          bottom: 30,
        },
        series: [
          {
            data: [],
            type: "line",
            smooth: true,
            symbolSize: 4,
            itemStyle: {
              color: "#036EFF",
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "rgba(3, 110, 255, 0.55)" },
                { offset: 1, color: "rgba(3, 110, 255, 0)" },
              ]),
            },
            lineStyle: {
              color: "#036EFF",
            },
            emphasis: {
              itemStyle: {
                color: "#40CD96",
                borderColor: "#ffffff",
              },
              scale: false
            },
            markLine: {
              symbol: "none",
              label: {
                position: "insideEndTop",
                color: "#FF0000",
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
      // let optins = merge{ ...option, ...this.extraOption };
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