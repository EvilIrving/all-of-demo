<!--
 * @Author: hanyu
 * @LastEditTime: 2022-01-19 10:06:24
 * @Description: 柱状图不点击
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\components\BarChartNoClick.vue
-->
<template>
  <div class="chart_wrap">
    <div
      v-if="chartData.xData.length"
      class="myChart"
      :id="chartData.id"
      v-on-echart-resize
    ></div>
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
    // 示例
    // chartData: {
    // title: "海塘分布",
    // id: 'htfbChart',
    // xData: [],
    // yData: [],
    // unit: ''
    // },
  },
  components: {},
  data() {
    return {
      adnm: "",
    };
  },
  computed: {
    
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
    this.adnm = this.$localData("get", "userInfo").adnm;
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
          top: "20",
          left: "24",
          right: "0",
          bottom: "8%",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#B5D7D7",
            },
            formatter: function(value) {
              // return value.split('').join('\n')
              return value
            }
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        yAxis: {
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#B5D7D7",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#375052",
              type: 'dashed'
            },
          },
        },
        series: [
          {
            type: "bar",
            barWidth: 15,
            color: 'rgb(30, 231, 231)',
            data: this.chartData.yData,
            label: {
              show: true,
              position: "top",
              color: "#fff",
              fontWeight: 500,
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
