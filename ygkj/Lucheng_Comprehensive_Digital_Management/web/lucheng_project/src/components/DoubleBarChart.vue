<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-10 17:31:05
 * @Description: 柱状图
 * @FilePath: \lucheng_project\src\components\DoubleBarChart.vue
-->
<template>
  <div class="chart_wrap">
    <div class="myChart" :id="chartData.id"></div>
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
    return {
      myChart: null,
    };
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
      //  if (this.myChart) {
      //    this.myChart.clear()
      //  }
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
      this.myChart = this.$echarts.init(
        document.getElementById(this.chartData.id)
      );
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
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
        legend: {
          show: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#666666",
            },
            // formatter: (value) => {
            //   return value.split("").join("\n");
            // },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        yAxis: {
          name: this.chartData.unit,
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#666666",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#dfdfdf",
            },
          },
        },
        series: [
          {
            name: '经费数额',
            type: "bar",
            barWidth: 15,
            color: "#4092f7",
            data: this.chartData.yData[0],
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#62A3FF" },
                { offset: 1, color: "#84C6FF" },
              ]),
            },
          },
          {
            name: '经费落实',
            type: "bar",
            barWidth: 15,
            color: "#35C966",
            data: this.chartData.yData[1],
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#35C966" },
                { offset: 1, color: "#83E6A4" },
              ]),
            },
          },
        ],
      };
      if (this.chartData.noWords) {
        delete option.series[0].label;
      }
      if (this.chartData.yInterval) {
        option.yAxis.interval = this.chartData.yInterval;
      }
      this.myChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .myChart {
    width: 100%;
    height: 100%;
  }
}
</style>
