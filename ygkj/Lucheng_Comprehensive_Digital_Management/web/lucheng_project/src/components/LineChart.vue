<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-18 16:01:51
 * @Description: 柱状图
 * @FilePath: /lucheng_project/src/components/BarChart.vue
-->
<template>
  <div class="chart_wrap">
    <div class="lineChart" :id="chartData.id"></div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    chartData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {};
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        this.$nextTick(() => {
          this.initChart();
        });
      },
      deep: true,
      immediate: false,
    },
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      let myChart = this.$echarts.init(
        document.getElementById(this.chartData.id)
      );
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: this.chartData.noWords
            ? "{b}"
            : "{b}: {c} " + this.chartData.unit,
        },
        grid: {
          top: "40",
          left: "16",
          right: "16",
          bottom: "16",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#666666",
            },
          },
        },
        yAxis: {
          name: "",
          type: "value",
          nameLocation: "center",
          offset: 0,
          position: "left",
          nameGap: 32,
          nameTextStyle: {
            fontSize: 13,
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          splitLine: {
            lineStyle: {
              color: "#EBEBEB",
            },
          },
          // max: wMax,
          // min: wMin,
          max: max,
          min: min,
        },
        series: [
          {
            name: "内河水位(m)",
            type: "line",
            symbol: "none",
            yAxisIndex: 0,
            emphasis: {
              focus: "series",
            },
            color: "#79B2E9",
            data: this.chartData.yData,
          },
        ],
      };
      if (this.chartData.noWords) {
        delete option.series[0].label;
      }
      if (this.chartData.yInterval) {
        option.yAxis.interval = this.chartData.yInterval;
      }
      option && myChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
.chart_wrap {
  width: 100%;
  // height: 100%;
  height: 500px;
  .myChart {
    width: 100%;
    height: 500px;
  }
}
</style>
