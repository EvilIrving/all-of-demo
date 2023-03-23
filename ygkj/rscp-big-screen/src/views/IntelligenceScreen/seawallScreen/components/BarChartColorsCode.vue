<!--
 * @Author: hanyu
 * @LastEditTime: 2023-02-06 16:02:58
 * @Description: 柱状图不点击
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\components\BarChartColorsCode.vue
-->
<template>
  <div class="chart_wrap">
    <div v-if="chartData.xData.length" class="myChart" :id="chartData.id" v-on-echart-resize></div>
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

  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        this.$nextTick(() => {
          this.initChart('all');
        });
      },
      deep: true,
      immediate: false,
    },
    
  },
  mounted() {
  },
  methods: {
    initChart(showColor) {
      document.getElementById(this.chartData.id).innerHTML = ""
      document.getElementById(this.chartData.id).removeAttribute("_echarts_instance_");
      let series = []
      if(showColor == 'all'){
        series = [
          {
            type: "bar",
            barWidth: 15,
            color: '#6eec61',
            stack: 'code',
            data: this.chartData.gData,
            label: {
              show: false,
            },
          },
          {
            type: "bar",
            barWidth: 15,
            color: '#ded052',
            stack: 'code',
            data: this.chartData.yData,
            label: {
              show: false,
            },
          },
          {
            type: "bar",
            barWidth: 15,
            color: '#e4443d',
            stack: 'code',
            data: this.chartData.rData,
            label: {
              show: false,
            },
          }
        ]
      }else if(showColor == 'red'){
        series = [
          {
            type: "bar",
            barWidth: 15,
            color: '#e4443d',
            stack: 'code',
            data: this.chartData.rData,
            label: {
              show: false,
            },
          }
        ]
      }else if(showColor == 'yellow'){
        series = [
          {
            type: "bar",
            barWidth: 15,
            color: '#ded052',
            stack: 'code',
            data: this.chartData.yData,
            label: {
              show: false,
            },
          }
        ]
      }else if(showColor == 'green'){
        series = [
          {
            type: "bar",
            barWidth: 15,
            color: '#6eec61',
            stack: 'code',
            data: this.chartData.gData,
            label: {
              show: false,
            },
          }
        ]
      }else{
        series = []
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
          formatter(data){
            let html = ''
            switch(showColor){
              case '':
              case 'all':
                return `${data[0].axisValue}<br />红码: ${data[2].value}<br />黄码: ${data[1].value}<br />绿码: ${data[0].value}`
                break;
              case 'red':
                return `${data[0].axisValue}<br />红码: ${data[0].value}`
                break;
              case 'yellow':
                return `${data[0].axisValue}<br />黄码: ${data[0].value}`
                break;
              case 'green':
                return `${data[0].axisValue}<br />绿码: ${data[0].value}`
                break;
            }
          },
          // formatter: "{b0}<br />红码: {c0}<br />黄码: {c1}<br />绿码: {c2}"
          // formatter: "{b0}" 
          // + showColor == 'red' ? '<br />红码: {c0}' : '' 
          // + showColor == 'yellow' ? '<br />黄码: {c0}' : '' 
          // + showColor == 'green' ? '<br />绿码: {c0}' : ''  
        },
        grid: {
          top: "20",
          left: "16",
          right: "0",
          bottom: "8%",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#B5D7D7",
            },
            formatter: function (value) {
              if (value.length > 3) {
                let v = value.slice(0, 3) + '...'
                return v.split('').join('\n')
              } else {
                return value.split('').join('\n')
              }

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
              color: "#B5D7D7",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#375052",
              type: 'dashed'
            },
          },
        },
        series: series,
      };
      // if (this.chartData.noWords) {
      //   delete option.series[0].label;
      // }
      
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
  .myChart {
    width: 100%;
    height: 100%;
  }
}
</style>
