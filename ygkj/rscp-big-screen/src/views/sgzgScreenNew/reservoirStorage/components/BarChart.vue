<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-24 10:06:16
 * @Description: 柱状图
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\reservoirStorage\components\BarChart.vue
-->
<template>
  <div class="chart_wrap">
    <div class="chart_title" v-if="!chartData.noTitle">
      <img src="../../../../assets/images/chart_item_icon.png" alt="" />
      {{ chartData.title }}
      <span
        v-if="leftSelectAdcd != '' && chartData.id != 'bxskfbChart'"
        class="clean_btn"
        @click="cleanClickStatus()"
        >清除</span
      >
      <!-- <span v-if="leftSelectAdnm != adnm && chartData.id == 'bxskfbChart'" class="clean_btn" @click="cleanClickAdnm()">清除</span> -->
    </div>
    <div
      v-if="chartData.xData.length"
      class="myChart"
      :id="chartData.id"
      v-on-echart-resize
    ></div>
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
    leftSelectAdcd(val) {
      if (val == "") {
        this.initChart();
      }
    },
  },
  mounted() {
    this.adnm = this.$localData("get", "userInfo").adnm;
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
          top: "20",
          left: "4%",
          right: "3%",
          bottom: "8%",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#999999",
            },
            formatter: (value) => {
              return this.chartData.xShupai
                ? value.split("").join("\n")
                : value.split("").join("\n");
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
      myChart.getZr().off("click");
      myChart.setOption(option);
      let that = this;
      myChart.getZr().on("click", function(params) {
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
        // console.log(zIndex, "zIndex");
        that.$emit("barClick", zIndex);
      });
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
