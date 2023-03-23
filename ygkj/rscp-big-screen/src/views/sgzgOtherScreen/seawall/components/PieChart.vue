<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-19 17:04:34
 * @Description: 饼图
 * @FilePath: \rscp-big-screen\src\components\PieChart.vue
-->
<template>
  <div class='chart_wrap'>
    <div class="chart_title" v-if="!chartData.noTitle">
      {{ chartData.title }}
      <span v-if="chartData.id == 'htsjfhbzChart' && leftSelectPreventLevel != ''" class="clean_btn" @click="cleanClickStatus(1)">清除</span>
      <span v-if="(chartData.id == 'htjbtjChart' || chartData.id == 'skgmChart') && leftSelectProjectScale != ''" class="clean_btn" @click="cleanClickStatus(2)">清除</span>
    </div>
    <div v-if="chartData.series.length" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">
      <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    chartData: {
      type: Object,
      required: true
    },
    legendShow: {
      type: Boolean,
      default: true
    }
  },
  components: {},
  computed: {
    leftSelectPreventLevel() {
      return this.$store.state.leftSelectPreventLevel;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    }
  },
  data() {
    return {};
  },
  watch: {
    chartData: {
      handler() {
        setTimeout(() => {
          this.$nextTick(() => {
            this.initChart();
          });
        }, 200);
      },
      deep: true,
      immediate: false
    },
    leftSelectPreventLevel(val) {
      if (val == "") {
        if (this.chartData.id == "htsjfhbzChart") {
          this.initChart();
        }
      }
    },
    leftSelectProjectScale(val) {
      if (val == "") {
        if (
          this.chartData.id == "htjbtjChart" ||
          this.chartData.id == "skgmChart"
        ) {
          this.initChart();
        }
      }
    }
  },
  mounted() {},
  methods: {
    initChart() {
      let legend = [];
      let total = 0;
      if (this.chartData.series.length) {
        this.chartData.series.map(v => {
          legend.push(v.name);
          total += v.value;
        });
      }
      let myChart = this.$echarts.init(
        document.getElementById(this.chartData.id)
      );
      myChart.setOption({
        title: {
          text: total,
          subtext: "海塘总数",
          textStyle: {
            fontSize: 31,
            color: "#D6E7FF"
          },
          subtextStyle: {
            fontSize: 19,
            color: "#D6E7FF"
          },
          textAlign: "center",
          x: this.legendShow ? "24%" : "49%",
          y: this.legendShow ? "35%" : "32%"
        },
        tooltip: {
          trigger: "item",
          formatter: "{b}: {c}" + this.chartData.unit + " ({d}%)"
        },
        legend: {
          show: this.legendShow,
          orient: this.chartData.legend.orient
            ? this.chartData.legend.orient
            : "vertical",
          height: this.chartData.legend.height,
          left: this.chartData.legend.left,
          top: "middle",
          icon: "rect",
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 12,
          data: legend,
          textStyle: {
            rich: {
              title: {
                color: "#D6E7FF",
                fontSize: 18,
                fontFamily: "PingFangSC-Regular, PingFang SC",
                fontWeight: 400
              },
              value: {
                color: "#fff",
                fontSize: 24,
                fontFamily: "youshe"
              }
            }
          },
          formatter: name => {
            return (
              "{title|" +
              name +
              "}         {value|" +
              this.chartData.series.filter(e => e.name === name)[0].value +
              "}"
            );
          }
        },
        series: [
          {
            name: "区域分布",
            type: "pie",
            radius: this.legendShow ? ["45%", "60%"] : ["55%", "70%"],
            center: this.legendShow ? ["25%", "50%"] : ["50%", "50%"],
            data: this.chartData.series,
            zlevel: 2,
            selectedMode: "single",
            color: this.chartData.color,
            itemStyle: {
              normal: {
                label: {
                  show: !this.legendShow,
                  textStyle: {
                    color: "#fff"
                  },
                  formatter: "{b}"
                }
              }
            }
          }
        ]
      });
      myChart.on("click", this.clickChart);
    },
    clickChart(param) {
      this.$emit("pieClick", param.data);
    },
    cleanClickStatus(type) {
      if (type == 1) {
        this.$store.commit("SET_LEFT_SELECT_PREVENT_LEVEL", "");
      } else {
        this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "");
      }
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../style/common.scss";
.chart_wrap {
  width: 100%;
  height: 100%;
  .chart_title {
    @include chartTitle();
    .clean_btn {
      @include chartClean();
    }
  }
  .clean_btn {
    float: right;
    color: #789fec;
    cursor: pointer;
    font-size: 14px;
  }
  .myChart {
    width: 100%;
    height: calc(100% - 55px);
    padding-bottom: 12px;
  }
  .noData {
    width: 100%;
    height: calc(100% - 43px);
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img {
      width: 30%;
    }
  }
}
</style>