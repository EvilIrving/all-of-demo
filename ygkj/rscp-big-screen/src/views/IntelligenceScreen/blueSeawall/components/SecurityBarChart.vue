<!--
 * @Date: 2022-01-12 15:01:12
 * @Author: dtb
 * @Description: 安全鉴定柱状图
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-01-14 14:17:59
 * @FilePath: /rscp-big-screen/src/views/skzgScreen/components/SecurityBarChart.vue
-->

<template>
  <div class="chart_wrap">
    <div v-if="chartData.xData.length" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">
      <img src="imgUrl" alt="" />
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
      imgUrl: 'https://cdn.ygwjg.com/rscp/screen/1.0/skzg/noData-black.png'
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
  mounted() {},
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
          formatter: this.chartData.noWords ? '{b}' : '{b} <br> 可供水: {c} ' + this.chartData.unit},
        grid: {
          top: "16",
          left: "2%",
          right: "2%",
          bottom: "2%",
          containLabel: true
        },
        legend: {
          show:false,
          data: ['可供水', '可纳洪'],
          textStyle: {
            color: '#B5D7D7',
          },
          itemStyle: {
            borderColor: '#7AD9FB',
            borderWidth: 1,
            borderRadius: 0,
            borderType:"solid"
          },
          top: 10,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#B5D7D7",
            },
            formatter: function(value) {
              // return value.split('').join('\n')
              return value
            }
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
              color: "#B5D7D7"
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dashed',
              color: '#375052'
            }
          }
        },
        series: [
          {
            name: '',
            type: "bar",
            barWidth: 15,
            // itemStyle: {
            //   color: function (params){
            //     return colorList1[params.dataIndex];
            //   },
            // },
            color: 'rgb(30, 231, 231)',
            stack: 'total',
            data: this.chartData.yData,
            label: {
               show: true,
                position: 'top',
                color: '#fff',
                fontWeight: 500
            },
          },
          {
            name: '可纳洪',
            type: "bar",
            barWidth: 15,
            // itemStyle: {
            //   color: function (params){
            //     return colorList1[params.dataIndex];
            //   },
            // },
            color: 'rgba(255, 255, 255, 0)',
            stack: 'total',
            data: this.chartData.yData2,
            label: {
                show: false,
                position: 'top',
                color: '#fff',
                fontWeight: 500
            },
            itemStyle: {
                normal: {
                color: 'rgba(255, 255, 255, 0.3)',
                barBorderColor: 'RGBA(230,247,255,0.08)',
                barBorderWidth: 1,
                barBorderRadius: 0,
                borderType:"solid"
              }
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
      // let that = this
      // myChart.getZr().on('click', function(params) {
      //   let pointInPixel = [params.offsetX, params.offsetY]
      //   let zIndex = 0
      //   if (myChart.containPixel('grid', pointInPixel)) {
      //     zIndex = myChart.convertFromPixel({ seriesIndex: 0 }, [params.offsetX, params.offsetY])[0]
      //   }
      //   for (var i=0;i<colorList1.length;i++){
      //     colorList1[i] = colorList0[i];
      //     colorList1[zIndex]='#f37e69';
      //     myChart.setOption(option);
      //   }
      //   that.$emit('barClick', zIndex)
      // })
    },
    // cleanClickStatus(){
    //   this.$store.commit('SET_LEFT_SELECT_ADCD', '')
    // }
  }
};
</script>

<style lang='scss' scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .clean_btn{
    float: right;
    color: #B5D7D7;
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