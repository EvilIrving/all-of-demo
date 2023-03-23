<!--
 * @Author: dyn
 * @Date: 2021-11-15 14:22:44
 * @LastEditTime: 2021-12-16 20:44:34
 * @LastEditors: dtb
 * @Description: 折线图
 * @FilePath: \lucheng-app\src\echarts\Line.vue
-->
<template>
  <div class="chart-wrap">
    <div class="myChart" ref="Line"></div>
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
      this.myChart = this.$echarts.init(this.$refs.Line ,null, {renderer: 'svg'});
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
            type: "line",
            smooth: true,
            symbol: "none",
            itemStyle: {
              normal: {
                color: "#fff",
                fontSize: 14,
                lineStyle: {
                  width: 6, //折线宽度
                  //折线颜色渐变
                  color: new this.$echarts.graphic.LinearGradient(0, 0, 1, 0, [
                    {
                      offset: 0,
                      color: "#E77B3A",
                    },
                    {
                      offset: 1,
                      color: "#F5D974",
                    },
                  ]),
                },
              },
              emphasis: {
                //鼠标经过时折点小圆圈样式
                borderColor: "rgba(0,196,132,0.2)",
                borderWidth: 10,
              },
            },
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
