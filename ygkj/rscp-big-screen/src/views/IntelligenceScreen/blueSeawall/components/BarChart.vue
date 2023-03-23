<template>
  <div class="chart_wrap">
    <div class="chart_title" v-if="!chartData.noTitle">
      <img src="../../../../assets/images/chart_item_icon.png" alt="" />
      {{ chartData.title }}

    </div>
    <!-- <span v-if="leftSelectAdcd != ''" class="clean_btn" @click="cleanClickStatus()">清除</span> -->
    <div v-if="chartData.xData.length" class="myChart" :class="{ myChartHeight: chartData.noTitle }" :id="chartData.id" v-on-echart-resize></div>
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
    return {
      adnm: "",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectAdnm() {
      return this.$store.state.leftSelectAdnm;
    },
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        newVal.yData.map((item) => (parseFloat(item) >= 100 ? 100 : item));
        this.$nextTick(() => {
          this.initChart();
        });
      },
      deep: true,
      immediate: false,
    },
    leftSelectAdcd: {
      handler(newVal, oldVal) {
        this.initChart();
      },
      deep: true,
      immediate: false,
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
    this.adnm = this.$localData("get", "userInfo").adnm;
  },
  methods: {
    initChart() {
      var colorList0 = [];
      var colorList1 = [];
      for (let i = 0; i < this.chartData.xData.length; i++) {
        colorList0.push("#46FDFF");
        colorList1.push("#46FDFF");
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
          top: "24",
          left: "4%",
          right: "3%",
          bottom: "4%",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#fff",
              fontSize: 17
            },
            formatter: (value) => {
              return value;
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        title: {
          show: true,
          text: `单位：${this.chartData.unit}`,
          right: 14,// 距离左边位置
          top: -4,// 距离上面位置
          textStyle: { // 设置二级标题的样式
            color: "#fff",
            fontWeight: 'normal',
            fontSize: 14
          }
        },
        yAxis: {
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: false,
            textStyle: {
              color: "#fff",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: "#375052",
              type: "dashed",
            },
          },
        },
        series: [
          {
            type: "bar",
            barWidth: 15,
            itemStyle: {
              color: function (params) {
                return colorList1[params.dataIndex];
              },
            },
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
      myChart.getZr().off("click");
      myChart.setOption(option);
      // let that = this;
      // myChart.getZr().on("click", function (params) {
      //   let pointInPixel = [params.offsetX, params.offsetY];
      //   let zIndex = 0;
      //   if (myChart.containPixel("grid", pointInPixel)) {
      //     zIndex = myChart.convertFromPixel({ seriesIndex: 0 }, [
      //       params.offsetX,
      //       params.offsetY,
      //     ])[0];
      //   }
      //   for (var i = 0; i < colorList1.length; i++) {
      //     colorList1[i] = colorList0[i];
      //     colorList1[zIndex] = "#fff";
      //     myChart.setOption(option);
      //   }
      //   that.$emit("barClick", zIndex);
      // });
    },
    cleanClickStatus() {
      this.$store.commit("SET_LEFT_SELECT_ADCD", "");
      this.$store.commit(
        "SET_LEFT_SELECT_ADNM",
        this.$localData("get", "userInfo").adnm
      );
    },
    cleanClickAdnm() {
      this.$store.commit(
        "SET_LEFT_SELECT_ADNM",
        this.$localData("get", "userInfo").adnm
      );
      this.initChart();
    },
  },
};
</script>

<style lang="scss" scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .clean_btn {
    position: absolute;
    top: 100px;
    float: right;
    color: #fff;
    cursor: pointer;
    font-size: 14px;
  }
  .myChart {
    width: 100%;
    height: 100%;
    &.myChartHeight {
      height: 100%;
    }
  }
  .noData {
    width: 100%;
    height: 100%;
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img {
      width: 30%;
    }
  }
}
</style>
