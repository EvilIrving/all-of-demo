<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-25 10:01:29
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringMonitor\components\SeepageMonitoring.vue
-->
<template>
  <div class="seepageMonitoring">
    <div class="search">
      <span style="font-size: 14px">月份：</span>
      <el-date-picker style="width: 180px; margin: 0 20px 0 15px" v-model="date" value-format="yyyy-MM" type="month" placeholder="请选择" clearable @change="getDailyCheckList">
      </el-date-picker>
      <el-checkbox-group class="checkbox_group" v-model="checkboxGroup">
        <el-checkbox-button v-for="typeName in types" :label="typeName" :key="typeName">{{ typeName }}</el-checkbox-button>
      </el-checkbox-group>
    </div>
    <div class="myChart" id="myChart"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      date: '',
      checkboxGroup: ['P1'],
      types: ['P1', 'P2', 'P3', '库水位', '渗流量'],
      chartData: {
        first: [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 260, 280],
        second: [220, 182, 191, 210, 230, 270, 270, 201, 154, 140, 240, 250],
        three: [150, 123, 23, 121, 190, 675, 234, 150, 182, 21, 89, 190],
        four: [150, 232, 201, 154, 190, 180, 210, 150, 182, 201, 154, 190],
        five: [150, 232, 201, 154, 190, 180, 210, 150, 182, 201, 154, 190],
      },
      option: {
        // title: {
        //   text: '',
        //   top: '5%',
        //   left: 'center',
        //   textStyle: {
        //     color: '#FFF',
        //     align: 'center',
        //   },
        // },
        grid: {
          left: '2%',
          right: '2%',
          top: '5%',
          bottom: '8%',
          containLabel: true,
        },
        tooltip: {
          show: true,
          trigger: 'item',
        },
        legend: {
          show: true,
          x: 'center',
          bottom: "1%",
          itemGap: 160,
          //   y: '35',
          //   icon: 'stack',
          //   itemWidth: 10,
          //   itemHeight: 10,
          textStyle: {
            color: '#333333',
          },
          data: ['库水位过程线图', 'P1实时渗压水位过程线图', 'P2实时渗压水位过程线图', 'P3实时渗压水位过程线图', '渗流量过程线图'],
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
              color: '#666666',
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#195384',
              },
            },
            data: [
              '1月',
              '2月',
              '3月',
              '4月',
              '5月',
              '6月',
              '7月',
              '8月',
              '9月',
              '10月',
              '11月',
              '12月',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '单位:m',
            nameTextStyle: {
              color: "#666666"
            },
            axisLabel: {
              formatter: '{value}',
              textStyle: {
                color: '#666666',
              },
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#EEEEEE',
              },
            },
          },
          {
            type: 'value',
            name: '单位:m3',
            nameTextStyle: {
              color: "#666666"
            },
            axisLabel: {
              formatter: '{value}',
              textStyle: {
                color: '#666666',
              },
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#EEEEEE',
              },
            },
          },
        ],
        series: [
          {
            name: '库水位过程线图',
            type: 'line',
            symbolSize: 4,
            itemStyle: {
              normal: {
                color: '#1492FF',
                lineStyle: {
                  color: '#1492FF',
                  width: 2,
                },
              },
            },
            // yAxisIndex: 0,
            data: [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 260, 280],
          },
          {
            name: 'P1实时渗压水位过程线图',
            type: 'line',
            symbolSize: 4,
            itemStyle: {
              normal: {
                color: '#01A59C',
                lineStyle: {
                  color: '#01A59C',
                  width: 2,
                },
              },
            },
            // yAxisIndex: 0,
            data: [],
          },
          {
            name: 'P2实时渗压水位过程线图',
            type: 'line',
            symbolSize: 4,
            itemStyle: {
              normal: {
                color: '#FFB70B',
                lineStyle: {
                  color: '#FFB70B',
                  width: 2,
                },
              },
            },
            // yAxisIndex: 0,
            data: [],
          },
          {
            name: 'P3实时渗压水位过程线图',
            type: 'line',
            // showSymbol:false,
            symbolSize: 1,
            itemStyle: {
              normal: {
                color: '#9207FF',
                lineStyle: {
                  color: '#9207FF',
                  width: 2,
                },
              },
            },
            // yAxisIndex: 0,
            data: [],
          },
          {
            name: '渗流量过程线图',
            type: 'line',
            symbolSize: 4,
            itemStyle: {
              normal: {
                color: '#FF07DC',
                lineStyle: {
                  color: '#FF07DC',
                  width: 2,
                },
              },
            },
            yAxisIndex: 1,
            data: [],
          },
        ],
      },
      chart: null,
    }
  },
  watch: {
    checkboxGroup: {
      deep: true,
      handler(val) {
        val.includes('P1') ? this.option.series[0].data = this.chartData.first : this.option.series[0].data = []
        val.includes('P2') ? this.option.series[1].data = this.chartData.second : this.option.series[1].data = []
        val.includes('P3') ? this.option.series[2].data = this.chartData.three : this.option.series[2].data = []
        val.includes('库水位') ? this.option.series[3].data = this.chartData.four : this.option.series[3].data = []
        val.includes('渗流量') ? this.option.series[4].data = this.chartData.five : this.option.series[4].data = []
        this.updateChartView();
      },
    },
  },
  mounted() {
    this.initChart()
  },
  methods: {
    getDailyCheckList() { },
    updateChartView() {
      if (!this.chart) return;
      this.chart.setOption(this.option, true);
    },
    initChart() {
      let chartDom = document.getElementById('myChart')
      this.chart = this.$echarts.init(chartDom)
      this.option && this.chart.setOption(this.option)
    },
  },
}
</script>

<style lang="scss" scoped>
.seepageMonitoring {
  height: calc(100% - 49px);
  padding: 17px 25px;
  box-sizing: border-box;
  .search {
    margin-bottom: 24px;
    .checkbox_group {
      display: inline-block;
      float: right;
    }
  }
  .myChart {
    width: 100%;
    height: calc(100% - 64px);
  }
}
</style>