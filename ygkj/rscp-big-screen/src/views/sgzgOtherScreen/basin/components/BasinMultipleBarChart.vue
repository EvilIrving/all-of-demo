<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-21 14:30:37
 * @Description: 多重数据柱状图
 * @FilePath: /rscp-big-screen/src/views/bigScreen/basin/components/BasinMultipleBarChart.vue
-->
<template>
  <div class="chart_wrap">
    <div
      v-if="chartData.xData.length"
      class="myChart"
      :id="chartData.id"
      v-on-echart-resize
    ></div>
    <div v-else class="noData">
      <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: "leftBarChart",
  props: {
    chartData: {
      type: Object,
      required: true,
    },
    // 示例
    // chartData: {
    // id: 'htfbChart',
    // xData: [],
    // barSeries: [],
    // },
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
        // newVal.yData.map((item) => (parseFloat(item) >= 100 ? 100 : item));
        this.$nextTick(() => {
          this.initChart();
        });
      },
      deep: true,
      immediate: false,
    }
  },
  mounted() {
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
          formatter: function(data){
            // console.log(data,'--data--');
            let name = data[0].name
            let html = ''
            if(name == '堤防'){
              html += `堤防<br>`
              html += `<i style="background:${data[4].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> 一级:${data[4].value}<br>`
              html += `<i style="background:${data[3].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> 二级:${data[3].value}<br>`
              html += `<i style="background:${data[2].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> 三级:${data[2].value}<br>`
              html += `<i style="background:${data[1].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> 四级:${data[1].value}<br>`
              html += `<i style="background:${data[0].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> 五级:${data[0].value}<br>`
            }else{
              html += `${data[4].name}<br>`
              html += `<i style="background:${data[4].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> ${data[4].seriesName}:${data[4].value}<br>`
              html += `<i style="background:${data[3].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> ${data[3].seriesName}:${data[3].value}<br>`
              html += `<i style="background:${data[2].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> ${data[2].seriesName}:${data[2].value}<br>`
              html += `<i style="background:${data[1].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> ${data[1].seriesName}:${data[1].value}<br>`
              html += `<i style="background:${data[0].color};width: 12px;height: 12px;border-radius: 6px;display: inline-block;"></i> ${data[0].seriesName}:${data[0].value}<br>`
              html += ``
            }
            return html
          }
        },
        color: this.chartData.color,
        grid: {
          top: "16",
          left: "16",
          right: "16",
          bottom: "24",
          containLabel: true
        },
        // legend: {
        //   data: ['可供水', '可纳洪']
        // },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#999999",
            },
            formatter: function(value) {
              return value.split('').join('\n')
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
              color: "#999999"
            }
          },
          splitLine: {
            lineStyle: {
              color: '#dfdfdf'
            }
          },
          max: this.chartData.max
        },
        series: this.chartData.barSeries
      }
      if(this.chartData.yInterval){
        option.yAxis.interval = this.chartData.yInterval
      }
      // [
      //   {
      //     name: '可供水',
      //     type: "bar",
      //     barWidth: 15,
      //     // itemStyle: {
      //     //   color: function (params){
      //     //     return colorList1[params.dataIndex];
      //     //   },
      //     // },
      //     color: '#6e9ff2',
      //     stack: 'total',
      //     data: this.chartData.yData,
      //     label: {
      //         show: false,
      //         // position: 'top',
      //         color: '#333',
      //         fontWeight: 500
      //     },
      //   },
      //   {
      //     name: '可纳洪',
      //     type: "bar",
      //     barWidth: 15,
      //     // itemStyle: {
      //     //   color: function (params){
      //     //     return colorList1[params.dataIndex];
      //     //   },
      //     // },
      //     color: '#dd836b',
      //     stack: 'total',
      //     data: this.chartData.yData2,
      //     label: {
      //         show: false,
      //         position: 'top',
      //         color: '#333',
      //         fontWeight: 500
      //     },
      //     itemStyle: {
      //         normal: {
      //         color: '#eef4ff',
      //         barBorderColor: '#6e9ff2',
      //         barBorderWidth: 1,
      //         barBorderRadius: 0,
      //         borderType:"dashed"
      //       }
      //     }, 
      //   },
      // ]
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
        that.$emit("barClick", zIndex);
      });
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
.point{
  
}
</style>
