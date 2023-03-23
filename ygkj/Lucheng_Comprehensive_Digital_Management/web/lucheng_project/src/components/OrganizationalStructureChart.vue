<template>
  <div class="chart_wrap">
    <div class="myChart" :id="chartData.id"></div>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    chartData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {}
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        this.$nextTick(() => {
          this.initChart()
        })
      },
      deep: true,
      immediate: false,
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  methods: {
    initChart() {
      let myChart = this.$echarts.init(
        document.getElementById(this.chartData.id)
      )
      let option = {
        series: [
          {
            type: 'tree',
            edgeShape: 'polyline', //链接线是折现还是曲线
            orient: 'TB',
            top: '10%',
            bottom: '40%',
            data: this.chartData.data,
            symbolSize: 1,
            //连接线长度
            layerPadding: 5,
            //结点间距
            nodePadding: 10,
            edgeForkPosition: '30%',
            initialTreeDepth: 2,
            label: {
              normal: {
                position: [-90, 0],
                verticalAlign: 'middle',
                align: 'left',
                backgroundColor: '#fff',
                borderColor: '#1492FF',
                color: '#fff',
                padding: [5, 50],
                borderWidth: 1,
                fontWeight: 'bold',
                formatter: ['{box|{b}}'].join('\n'),
                rich: {
                  box: {
                    height: 20,
                    color: '#1492FF',
                    padding: [0, 5],
                    align: 'center',
                    fontSize: 14,
                    fontWeight: 600,
                  },
                },
              },
            },
            leaves: {
              label: {
                normal: {
                  position: [-10, 10],
                  verticalAlign: 'center',
                  align: 'left',
                  backgroundColor: '#ffffff',
                  borderColor: '#1492FF',
                  padding: [10, 10],
                  fontSize: 12,
                  fontWeight: 'normal',
                  width: 5,
                  height: 150,
                  borderWidth: 1,
                  formatter: function (param) {
                    let name = param.name.split('').join('\n')
                    return [`{name|${name}}`]
                  },
                  rich: {
                    name: {
                      height: 20,
                      color: '#1492FF',
                      padding: [0, 3],
                      align: 'center',
                      fontSize: 14,
                      fontWeight: 600,
                    },
                  },
                },
              },
            },
            lineStyle: {
              color: '#909090',
            },
            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750,
          },
        ],
      }

      // if (this.chartData.noWords) {
      //   delete option.series[0].label
      // }
      // if (this.chartData.yInterval) {
      //   option.yAxis.interval = this.chartData.yInterval
      // }
      myChart.setOption(option)
      myChart.on('click', this.getInfo)
    },
    getInfo(params) {
      console.log(params, 'params');
      // this.$emit('getInfo', params)
    }
  },
}
</script>

<style lang="scss" scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .myChart {
    width: 100%;
    height: 100%;
  }
}
</style>
