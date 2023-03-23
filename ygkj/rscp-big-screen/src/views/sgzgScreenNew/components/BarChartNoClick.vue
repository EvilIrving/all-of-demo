<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-25 13:19:14
 * @Description: 柱状图不点击
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/components/BarChartNoClick.vue
-->
<template>
  <div class="chart_wrap">
    <div
      v-if="chartData.xData.length"
      class="myChart"
      :id="chartData.id"
      v-on-echart-resize
    ></div>
    <div v-else class="noData">
      <img src="@/assets/images/noData.png" alt="" />
    </div>
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
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
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
    // leftSelectAdcd(val) {
    //   if (val == "") {
    //     this.initChart();
    //   }
    // },
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    })
  },
  methods: {
    initChart() {
      var colorList0 = [];
      var colorList1 = [];
      for (let i = 0; i < this.chartData.xData.length; i++) {
        colorList0.push("#6e9ff2");
        colorList1.push("#6e9ff2");
      }
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
          top: "16",
          left: "4%",
          right: "3%",
          bottom: "24",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#999999",
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
              color: "#999999",
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
            itemStyle: {
              color: function(params) {
                return colorList1[params.dataIndex];
              },
            },
            // color: '#6e9ff2',
            data: this.chartData.yData,
            label: {
              show: true,
              position: "top",
              color: "#333",
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
  .clean_btn {
    float: right;
    color: #789fec;
    cursor: pointer;
    font-size: 14px;
  }
  .myChart {
    width: 100%;
    height: 100%;
  }
  .noData {
    width: 100%;
    height: 100%;
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img {
      width: 35%;
    }
  }
}
</style>
