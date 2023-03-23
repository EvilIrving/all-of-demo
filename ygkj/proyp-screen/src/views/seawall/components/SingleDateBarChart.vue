<!--
 * @Date: 2023-02-14 17:14:51
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-16 15:23:57
 * @FilePath: \proyp-screen\src\views\seawall\components\SingleDateBarChart.vue
-->

<template>
  <div class="chart_wrap">
    <span class="reset" @click='clean()' v-show="chartData.isReset">还原</span>
    <div class="myChart" :id="chartData.id" v-onEchartResize></div>
  </div>
</template>

<script>
// import echarts from "echarts";
export default {
  name: "",
  props: {
    chartData: {
      type: Object,
      required: true,
    },
    // 示例
    // chartData: {
    // title: "海塘长度",
    // id: "htglsChart",
    // xData: [],
    // yData: [],
    // adcdList: [],
    // allowClick: false,
    // isReset: false,
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
      handler(newVal) {
        newVal.yData.map((item) => (parseFloat(item) >= 100 ? 100 : item));
        this.$nextTick(() => {
          this.initChart();
        });
      },
      deep: true,
      immediate: false,
    },
    selectAdcd(val) {
      if (val == "") {
        this.initChart();
      }
    },
  },
  mounted() {
  },
  methods: {
    initChart() {
      let myChart = this.$echarts.init(
        document.getElementById(this.chartData.id)
      );
      let option = {
        legend: {
          show: true,
          data: [{
            name: this.chartData.title,
            icon: 'roundRect',
            textStyle: {
              color: 'white'
            }
          }]
        },
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
          top: "25",
          left: "0",
          right: "26",
          bottom: "0",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#ffffff",
            },
            // formatter: (value) => {
            //   return this.chartData.xShupai
            //     ? value.split("").join("\n")
            //     : value.split("").join("\n");
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
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#ffffff",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#142848",
            },
          },
        },
        series: [
          {
            name: this.chartData.title,
            type: "bar",
            cursor: this.chartData.allowClick ? 'pointer' : 'default',
            barWidth: 9,
            itemStyle: {
              barBorderRadius: [3, 3, 0, 0],
              color: this.$echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  { offset: 0, color: '#23EBF6' },
                  { offset: 1, color: '#258ED9' },
                ]
              )
            },
            data: this.chartData.yData,
            label: {
              show: false,
            },
          },
        ],
      };
      if (this.chartData.yInterval) option.yAxis.interval = this.chartData.yInterval;
      myChart.setOption(option);
      if (this.chartData.allowClick) {
        myChart.getZr().off("click");
        let that = this;
        myChart.getZr().on("click", function (params) {
          let pointInPixel = [params.offsetX, params.offsetY];
          let zIndex = 0;
          if (myChart.containPixel("grid", pointInPixel))
            zIndex = myChart.convertFromPixel({ seriesIndex: 0 }, [
              params.offsetX,
              params.offsetY,
            ])[0];
          myChart.setOption(option);
          that.$emit("barClick", zIndex);
        });
      }
    },
    clean() {
      this.$emit("barClick", '', true);
    }
  },
};
</script>

<style lang="scss" scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  position: relative;
  .myChart {
    width: 100%;
    height: 100%;
  }
  .reset {
    position: absolute;
    right: 40px;
    top: -36px;
    padding: 3px 6px;
    background: #19549b;
    border-radius: 3px;
    color: #fff;
    cursor: pointer;
    z-index: 9999;
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
