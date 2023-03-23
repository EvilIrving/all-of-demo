<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-18 16:18:35
 * @Description: 饼图
 * @FilePath: /lucheng_project/src/components/PieChart.vue
-->
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
      required: true
    }
    // 示例
    // {
    //   title: "海塘设计防洪标准统计",
    //   id: 'htsjfhbzChart',
    //   unit: '',
    //   legend: {
    //     orient: 'vertical',
    //     left: '60%',
    //     height: '70%'
    //   },
    //   color: ['#1BC57E', '#6F9DF2', '#5D7092', '#F7C739', '#EB7E65', '#E02020'],
    //   series: [],
    // },
  },
  components: {
    
  },
  data () {
    return {
    };
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
    },
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart(){
      let legend = []
      if(this.chartData.series.length){
        this.chartData.series.map(v=>{
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
    }
  }
}
</script>

<style lang='scss' scoped>
  .chart_wrap{
    width: 100%;
    height: 100%;
    .myChart{
      width: 100%;
      height: 100%;
    }
  }
</style>