<template>
  <div class="myChart" :id="chartData.id" v-on-echart-resize></div>
</template>

<script>
export default {
  name: '',
  props: {
    chartData: {
      type: Object,
      required: true
    }
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        this.$nextTick(() => {
          this.initChart();
        })
      },
      deep: true,
      immediate: false
    },

  },
  mounted() { },
  methods: {
    initChart() {
      let myChart = this.$echarts.init(document.getElementById(this.chartData.id));
      myChart.setOption({
        series: [
          {
            name: '区域分布',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['45%', '37%'],
            data: this.chartData.series,
            zlevel: 1,
            selectedMode: 'single',
            color: this.chartData.color,
            label: {
              normal: {
                show: false,              //设为false
              },
            },
            labelLine: {
              show: false
            },
          },
          {
            name: '区域分布',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['45%', '37%'],
            data: this.chartData.series,
            zlevel: 2,
            selectedMode: 'single',
            color: this.chartData.color,
            label: {
              normal: {
                show: false,              //设为false
              },
            },
            labelLine: {
              show: false
            },
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
</style>