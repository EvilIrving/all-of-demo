<!--
 * @Date: 2021-12-24 10:03:05
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-25 17:36:00
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\reservoirStorage\components\BarChartOne.vue
-->

<template>
  <div class="chart_wrap">
    <div v-if="chartData.xData.length" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">
      <img src="../../../../assets/images/noData.png" alt="" />
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
  },
  components: {},
  data() {
    return {};
  },
  computed: {},
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
  mounted() { },
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
            : "{b} <br> 可供水: {c} " +
            this.chartData.unit
        },
        grid: {
          top: "35",
          left: "4%",
          right: "3%",
          bottom: "8%",
          containLabel: true,
        },
        legend: {
          show:false
          // data: ["可供水", "可纳洪"],
        },
        // legend: {
        //   orient: 'vertical',
        //   // left: this.chartData.legend.left,
        //   // height: this.chartData.legend.height,
        //   top:'center',
        //   // icon: 'circle',
        //   // data: legend,
        //   itemGap: 10,
        //   textStyle: {
        //     color: '#8c8c8c'
        //   }
        // },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#999999",
            },
            formatter: function (value) {
              return value.split("").join("\n");
            },
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
            name: "可供水",
            type: "bar",
            barWidth: 15,
            itemStyle: {
              color: function (params) {
                return colorList1[params.dataIndex];
              },
            },
            color: "#6e9ff2",
            stack: "total",
            data: this.chartData.yData,
            label: {
              show: false,
              // position: 'top',
              color: "#333",
              fontWeight: 500,
            },
          },
          {
            name: "可纳洪",
            type: "bar",
            barWidth: 15,
            itemStyle: {
              color: function (params) {
                return colorList1[params.dataIndex];
              },
            },
            color: "#dd836b",
            stack: "total",
            data: this.chartData.yData2,
            label: {
              show: false,
              position: "top",
              color: "#333",
              fontWeight: 500,
            },
            itemStyle: {
              normal: {
                color: "#eef4ff",
                barBorderColor: "#6e9ff2",
                barBorderWidth: 1,
                barBorderRadius: 0,
                borderType: "dashed",
              },
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
      let that = this;
      myChart.getZr().on("click", function (params) {
        let pointInPixel = [params.offsetX, params.offsetY];
        let zIndex = 0;
        if (myChart.containPixel("grid", pointInPixel)) {
          zIndex = myChart.convertFromPixel({ seriesIndex: 0 }, [
            params.offsetX,
            params.offsetY,
          ])[0];
        }
        for (var i = 0; i < colorList1.length; i++) {
          colorList1[i] = colorList0[i];
          colorList1[zIndex] = "#f37e69";
          myChart.setOption(option);
        }
        that.$emit("barClick", zIndex);
      });
    },
    // cleanClickStatus(){
    //   that.$emit('cleanClick', zIndex)
    // }
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
    height: calc(100% - 40px);
  }
  .noData {
    width: 100%;
    height: calc(100% - 40px);
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img {
      width: 35%;
    }
  }
}
</style>
