<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-13 15:37:48
 * @Description: 饼图
 * @FilePath: \rscp-big-screen\src\components\PieChart.vue
-->
<template>
  <div class='chart_wrap'>
    <div class="chart_title" v-if="!chartData.noTitle">
      <img src="../assets/images/chart_item_icon.png" alt="">
      {{ chartData.title }}
      <span v-if="chartData.id == 'htsjfhbzChart' && leftSelectPreventLevel != ''" class="clean_btn" @click="cleanClickStatus(1)">清除</span>
      <span v-if="(chartData.id == 'htjbtjChart' || chartData.id == 'skgmChart') && leftSelectProjectScale != ''" class="clean_btn" @click="cleanClickStatus(2)">清除</span>
    </div>
    <div v-if="chartData.series.length" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">
      <img src="../assets/images/noData.png" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    chartData: {
      type: Object,
      required: true
    }
    // 示例
    // htsjfhbzData: {
    //   title: "海塘设计防洪标准统计",
    //   id: 'htsjfhbzChart',
    //   unit: '',
    // legend: {
    //   orient: 'vertical',
    //   left: '60%',
    //   height: '70%'
    // },
    //   color: ['#1BC57E', '#6F9DF2', '#5D7092', '#F7C739', '#EB7E65', '#E02020'],
    //   series: [],
    // },
  },
  components: {

  },
  computed: {
    leftSelectPreventLevel() {
      return this.$store.state.leftSelectPreventLevel
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale
    }
  },
  data() {
    return {
    };
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        setTimeout(() => {
          this.$nextTick(() => {
            this.initChart();
          })
        }, 200);

      },
      deep: true,
      immediate: false
    },
    leftSelectPreventLevel(val) {
      if (val == '') {
        if (this.chartData.id == 'htsjfhbzChart') {
          this.initChart()
        }
      }
    },
    leftSelectProjectScale(val) {
      if (val == '') {
        if (this.chartData.id == 'htjbtjChart' || this.chartData.id == 'skgmChart') {
          this.initChart()
        }
      }
    }
  },
  mounted() { },
  methods: {
    initChart() {
      let legend = []
      if (this.chartData.series.length) {
        this.chartData.series.map(v => {
          legend.push(v.name)
        })
      }
      let myChart = this.$echarts.init(document.getElementById(this.chartData.id));
      myChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}' + this.chartData.unit + ' ({d}%)'
        },
        
        series: [
          {
            name: '区域分布',
            type: 'pie',
            radius: ['30%', '60%'],
            center: ['50%', '50%'],
            data: this.chartData.series,
            zlevel: 1,
            selectedMode: 'single',
            itemStyle: {
              normal: {
                color: function (params) {
                  return "#fff"
                },
                label: {
                  show: true,
                  position: 'outside',
                  textStyle: {
                    color: '#000'
                  },
                  formatter: "{b}"
                },
              },
            },
            color: this.chartData.color,

            // labelLine: {
            //     show: false
            // },
          },
          {
            name: '区域分布',
            type: 'pie',
            radius: ['30%', '60%'],
            center: ['50%', '50%'],
            data: this.chartData.series,
            zlevel: 2,
            selectedMode: 'single',
            color: this.chartData.color,
            itemStyle: {
              normal: {
                // color: function (params) {
                //     //自定义颜色
                //     var colorList = [
                //         '#5B8FF9', '#5AD8A6', '#A1ABD7', '#64D9F5', '#E4B297', '#8059ED', '#6DC8EC', '#9270CA', '#DD62A9', '#269A99', '#FF99C3', '#DFC972', '#857BDA', '#70A4E9', '#99C046', '#5DC4AE', '#59842D', '#97F7E3', '#E4F3A5', '#70C4D1'
                //     ];
                //     return colorList[params.dataIndex]
                // },
                label: {
                  show: true,
                  position: 'inner',
                  textStyle: {
                    color: '#fff'
                  },
                  formatter: "{c}"
                }
              }
            }
          }
        ]
      })
      myChart.on("click", this.clickChart);
    },
    clickChart(param) {
      this.$emit('pieClick', param.data)
    },
    cleanClickStatus(type) {
      if (type == 1) {
        this.$store.commit('SET_LEFT_SELECT_PREVENT_LEVEL', '')
      } else {
        this.$store.commit('SET_LEFT_SELECT_PROJECT_SCALE', '')
      }
    }
  }
}
</script>

<style lang='scss' scoped>
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
      width: 30%;
    }
  }
}
</style>