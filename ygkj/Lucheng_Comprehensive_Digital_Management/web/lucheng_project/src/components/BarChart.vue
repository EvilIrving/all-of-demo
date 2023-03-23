<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-09 17:49:42
 * @Description: 柱状图
 * @FilePath: \lucheng_project\src\components\BarChart.vue
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
        legend: {
          show: false,
          // icon: 'rect',
          // itemWidth: 20,
          // itemHeight: 10,
          // itemGap: 10,
          // data: ["南宁-曼芭", "桂林-曼芭", "南宁-甲米"],
          // right: "4%",
          // textStyle: {
          //   fontSize: 12,
          //   color: "#F1F1F3",
          // },
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
            type: "bar",
            barWidth: 15,
            color: "#4092f7",
            data: this.chartData.yData,
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#62A3FF" },
                { offset: 1, color: "#84C6FF" },
              ]),
            },
            label: {
              show: false,
              position: "top",
              color: "#333",
              fontWeight: 500,
              // formatter: (value) => {
              //   return value.data + '%';
              // },
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
      myChart.setOption(option);
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
