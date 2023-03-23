<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-28 17:42:23
 * @Description: 柱状图
 * @FilePath: /rscp-big-screen/src/components/BarChartSimple.vue
-->
<template>
  <div class="chart_wrap">
    <div class="chart_title">
      <img src="../assets/images/chart_item_icon.png" alt="">
      {{ chartData.title }}
    </div>
    <div v-if="chartData.xData.length" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">暂无数据</div>
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
    //   title: "海塘分布",
    //   id: 'htfbChart',
    //   xData: [],
    //   yData: [],
    //   unit: ''
    // },
  },
  components: {},
  data() {
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
    }
  },
  mounted() {},
  methods: {
    initChart() {
      var colorList0 = []
      var colorList1 = []
      for (let i = 0; i < this.chartData.xData.length; i++) {
        colorList0.push('#6e9ff2')
        colorList1.push('#6e9ff2')
      }
      let myChart = this.$echarts.init(document.getElementById(this.chartData.id));
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: "16",
          left: "4%",
          right: "3%",
          bottom: "8%",
          containLabel: true
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            // inside: true,
            textStyle: {
              color: "#999999",
              // color: function (params){
              //   return colorList3[params.dataIndex];
              // },
            }
          },
          axisTick: {
            show: false
          },
          axisLine: {
            show: false
          },
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
            type: "bar",
            barWidth: 15,
            // color: '#6e9ff2',
            itemStyle: {
              color: function (params){
                return colorList1[params.dataIndex];
              },
            },
            
            data: this.chartData.yData,
            label: {
                show: true,
                position: 'top',
                color: '#333',
                // color: function (params){
                //   return colorList1[params.dataIndex];
                // },
                fontWeight: 500
            },
          },
          
        ]
      }
      myChart.setOption(option);
      myChart.getZr().on('click', function(params) {
        let pointInPixel = [params.offsetX, params.offsetY]
        let zIndex = 0
        if (myChart.containPixel('grid', pointInPixel)) {
          zIndex = myChart.convertFromPixel({ seriesIndex: 0 }, [params.offsetX, params.offsetY])[0]
        }
        for (var i=0;i<colorList1.length;i++){
          colorList1[i] = colorList0[i];
          colorList1[zIndex]='#f37e69';
          myChart.setOption(option);
        }
      })

    }
  }
};
</script>

<style lang='scss' scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .myChart {
    width: 100%;
    height: calc(100% - 40px);
  }
  .noData{
    text-align: center;
    font-size: 14px;
    color: #999;
    padding-top: 40px;
  }
}
</style>