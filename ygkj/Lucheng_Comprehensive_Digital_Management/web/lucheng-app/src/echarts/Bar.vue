<!--
 * @Author: dyn
 * @Date: 2021-11-15 14:23:18
 * @LastEditTime: 2021-12-16 20:44:28
 * @LastEditors: dtb
 * @Description: 柱状图
 * @FilePath: \lucheng-app\src\echarts\Bar.vue
-->
<template>
  <div class="chart-wrap">
    <div class="myChart" ref="Bar"></div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    yData: {
      type: Array,
      default: () => [],
    },
    xData: {
      type: Array,
      default: () => [],
    },
    colorList: {
      type: Array,
      default: () => ["#036EFF"],
    },
    unit: {
      type: String,
      default: "个",
    },
    grid: {
      type: Object,
      default: () => {
        return {
          top: 40,
          bottom: 30,
          left: 40,
          right: 20,
        };
      },
    },
  },
  components: {},
  data() {
    return {
      myChart: null,
    };
  },
  mounted() {},
  methods: {
    initChart() {
      this.myChart && this.myChart.dispose();
      this.myChart = this.$echarts.init(this.$refs.Bar, null, {renderer: 'svg'});
      let option = {
        title: {
          show: true,
          text: this.unit,
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            lineHeight: 24,
          },
        },
        xAxis: {
          type: "category",
          data: this.xData,
          axisLine: {
            lineStyle: {
              color: "#E3E3E3",
            },
          },
          axisLabel: {
            show: true,
            color: "#333333",
            interval: 0,
          },
        },
        yAxis: {
          type: "value",
          axisLabel: {
            color: "#333333",
          },
          axisLine: {
            lineStyle: {
              color: "#E3E3E3",
            },
          },
          splitLine: {
            show: false,
          },
        },
        grid: this.grid,
        series: [
          {
            data: this.yData,
            type: "bar",
            color: this.colorList,
            barWidth: 14,
          },
        ],
      };
      this.myChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
.chart-wrap {
  width: 100%;
  height: 100%;
  .myChart {
    width: 100%;
    height: 100%;
  }
}
</style>
