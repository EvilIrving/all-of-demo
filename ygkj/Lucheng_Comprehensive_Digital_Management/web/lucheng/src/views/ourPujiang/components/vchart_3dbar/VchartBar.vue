<!--
 * @Author: your name
 * @Date: 2021-07-13 14:01:36
 * @LastEditTime: 2022-05-13 10:00:38
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \lucheng\src\views\ourPujiang\components\vchart_3dbar\VchartBar.vue
-->
<template>
  <ve-histogram ref="chart" height="100%" width="100%" :data="barData" :settings="barOption.setting" :extend="barOption.extend" :grid="barOption.grid" :data-zoom="barOption.dataZoom" :after-config="barConfig" :tooltip-visible="false" :legend-visible='true' />
</template>

<script>
export default {
  name: '',
  props: {
    barData: {
      type: Object,
      default: () => {
        return {};
      }
    },
    colorArr: {
      type: Array,
      default: () => {
        return ['#15E2DB', '#9DFDFB', '#A6FFFF'];
      }
    },
    labelColor: {
      type: String,
      default: '#1EF8F2'
    },
    barName: {
      type: String,
      default: '数据'
    },
    unit: {
      type: String,
      defauly: 'mm'
    },
    barWidth: {
      type: Number,
      defauly: 30
    }
  },
  watch: {
    labelColor: function (val) {
      this.barOption.extend.legend.textStyle.color = val;
    },
    unit: function (val) {
      this.barOption.extend.yAxis.name = val;
    }
  },
  data() {
    return {
      barOption: {
        dataZoom: {
          type: 'inside',
          startValue: 0,
          endValue: 4
        },
        grid: {
          top: 30,
          bottom: '1%',
          containLabel: true
        },
        extend: {
          'textStyle.color': '#fff',
          barWidth: 15,
          legend: {
            right: 10,
            textStyle: {
              color: '#fff'
            },
            data: [
              {
                name: '累计降雨量'
              },
              {
                name: '雨量柱状图'
              },

            ]
          },
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize
            }
          },
          yAxis: {
            name: 'mm',
            position: 'left',
            nameTextStyle: {
              color: 'rgba(255,255,255,0.60)',
              fontSize: this.$commonSetting.chartFontSize
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              color: 'rgba(255,255,255,0.60)',
              fontSize: this.$commonSetting.chartFontSize
            },
            splitLine: {
              lineStyle: {
                color: '#fff',
                opacity: 0.5
              }
            }
          }
        }
      }
    };
  },
  components: {},
  created() { },
  mounted() {
    let myChart = this.$refs['chart'].echarts;
    let _this = this
    myChart.getZr().on('click', function (param) {
      // 获取 点击的 触发点像素坐标
      const pointInPixel = [param.offsetX, param.offsetY];
      // 判断给定的点是否在指定的坐标系或者系列上
      if (myChart.containPixel('grid', pointInPixel)) {
        // 获取到点击的 x轴 下标  转换为逻辑坐标
        let xIndex = myChart.convertFromPixel({ seriesIndex: 0 }, pointInPixel)[0];
        // 做一些其他事情
        _this.$emit('barClick', xIndex);
      }
    });
  },
  methods: {
    resize() {
      this.$refs['chart'].echarts.resize();
    },
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = this.colorArr;
      let labelColor = this.labelColor;
      let color = {
        type: 'linear',
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0]
          },
          {
            offset: 0.5,
            color: colorArr[0]
          },
          {
            offset: 0.5,
            color: colorArr[1]
          },
          {
            offset: 1,
            color: colorArr[1]
          }
        ]
      };
      let barWidth = this.barWidth;
      let constData = [];
      let showData = [];
      let lineData = [];
      data.reduce((prev, cur) => {
        lineData.push(prev + cur)
        return prev + cur
      }, 0)
      data.filter(function (item) {
        if (item) {
          constData.push(1);
          showData.push(item);
        } else {
          constData.push(0);
          showData.push({
            value: 1,
            itemStyle: {
              normal: {
                borderColor: 'rgba(0,0,0,0)',
                borderWidth: 2,
                color: 'rgba(0,0,0,0)'
              }
            }
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: "雨量柱状图",
          type: 'bar',
          barWidth: barWidth,
          barGap: '0%',
          data: data,
          itemStyle: {
            normal: {
              color: color
            }
          },
          label: {
            show: true,
            fontSize: 18,
            position: 'top',
            color: labelColor
            // formatter: ({ value }) => Number(value).toFixed(1),
          }
        },
        {
          z: 2,
          name: "",
          type: 'pictorialBar',
          data: constData,
          symbol: 'diamond',
          symbolOffset: ['0%', '50%'],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 3,
          name: '',
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData,
          symbol: 'diamond',
          symbolOffset: ['0%', '-50%'],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2]
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 4,
          name: '累计降雨量',
          type: 'line',
          data: lineData,
          itemStyle: {
            normal: {
              borderColor: '#00BAFF',
              borderWidth: 2,
              color: '#00BAFF'
            }
          },
          tooltip: {
            show: false
          }
        },
      ];
      return options;
    }
  }
};
</script>