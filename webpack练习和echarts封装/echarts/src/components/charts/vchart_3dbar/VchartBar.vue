<!--
 * @Author: dtb
 * @Date: 2021-07-13 14:01:36
 * @LastEditTime: 2023-03-21 16:15:54
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \demo2\demo\src\components\charts\vchart_3dbar\VchartBar.vue
-->
<template>
  <ve-histogram ref="charthistogram" height="100%" width="100%" :data="barData" :settings="barOption.setting" :extend="barOption.extend" :grid="barOption.grid" :data-zoom="barOption.dataZoom" :tooltip-visible="false" :legend-visible="false" :after-config="barConfig" />
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
      defauly: '个'
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
          top: 40,
          bottom: '1%',
          containLabel: true
        },
        extend: {
          barWidth: 50,
          legend: {
            show: true,
            right: 0,
            top: 0,
            icon: 'circle',
            itemWidth: 30,
            itemHeight: 8,
            textStyle: {
              color: '#15E2DB',
              fontSize: 12
            }
          },
          xAxis: {
            axisLabel: {
              color: 'rgba(255,255,255,0.70)',
              interval: 0,
              fontSize: '16'
            }
          },
          yAxis: {
            name: '个',
            position: 'left',
            nameTextStyle: {
              color: 'rgba(255,255,255,0.70)',
              fontSize: '16'
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              color: 'rgba(255,255,255,0.70)',
              fontSize: '16'
            },
            splitLine: {
              lineStyle: {
                type: 'dotted',
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
    let myChart = this.$refs['charthistogram'].echarts;
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
      this.$refs['charthistogram'].echarts.resize();
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
          name: this.barName,
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
          name: this.barName,
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
          name: this.barName,
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData,
          symbol: 'diamond',
          symbolOffset: ['0%', '-50%'],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
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
        }
      ];
      return options;
    }
  }
};
</script>
