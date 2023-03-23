<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-24 18:55:00
 * @Description: 柱状图
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/components/BarChartTwo.vue
-->
<template>
  <div class="chart_wrap">
    <div class="myChart" :id="chartData.id" v-on-echart-resize></div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    chartData: {
      type: Object,
      required: true
    }
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
    };
  },
  computed: {
    
  },
  watch:{
    chartData:{
      handler(newVal, oldVal){
        this.$nextTick(()=>{
          this.initChart();
        })
      },
      deep: true,
      immediate: false
    }
  },
  mounted() {
    this.$nextTick(()=>{
      this.initChart();
    })
  },
  methods: {
    initChart() {
      let myChart = this.$echarts.init(document.getElementById(this.chartData.id));
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: this.chartData.noWords ? '{b}' : '{b} <br> 开闸下泄(座): {c} ' + this.chartData.unit + '<br>' + '自由溢洪(座): {c1} ' + this.chartData.unit
        },
        grid: {
          top: "40",
          left: "4%",
          right: "3%",
          bottom: "16",
          containLabel: true
        },
        legend: {
          data: ['开闸下泄(座)', '自由溢洪(座)']
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#999999",
            },
            
          },
          axisTick: {
            show: false
          },
          axisLine: {
            show: false
          }
        },
        yAxis: {
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            textStyle: {
              color: "#999999"
            }
          },
          splitLine: {
            lineStyle: {
              color: '#dfdfdf'
            }
          }
        },
        series: [
          {
            name: '开闸下泄(座)',
            type: "bar",
            barWidth: 15,
            color: '#F2B26F',
            data: this.chartData.yData,
            label: {
              show: true,
              position: 'top',
              color: '#333',
              fontWeight: 500
            },
          },
          {
            name: '自由溢洪(座)',
            type: "bar",
            barWidth: 15,
            color: '#6F9DF2',
            data: this.chartData.yData2,
            label: {
              show: true,
              position: 'top',
              color: '#333',
              fontWeight: 500
            },
            
          },
        ]
      }
      if(this.chartData.noWords){
        delete option.series[0].label
      }
      if(this.chartData.yInterval){
        option.yAxis.interval = this.chartData.yInterval
      }
      myChart.setOption(option);
 
    },
  }
};
</script>

<style lang='scss' scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .clean_btn{
    float: right;
    color: #789fec;
    cursor: pointer;
    font-size: 14px;
  }
  .myChart {
    width: 100%;
    height: 100%;
  }
  .noData{
    width: 100%;
    height: 100%;
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img{
      width: 35%;
    }
  }
}
</style>