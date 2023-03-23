<!--
 * @Author: lsh
 * @Date: 2021-01-26 10:56:41
 * @LastEditors: lsh
 * @LastEditTime: 2021-01-27 15:52:46
 * @Descripttion: 
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudge\components\charts\LineChart.vue
-->
<template>
  <div style="width:100%;height:100%;">
    <ve-line :data="chartData" :settings="setting" :extend="extend" :mark-line="chartMarkLine" height="100%"></ve-line>
  </div>
</template>

<script>
export default {
  name: "LineChart",
  components: {},
  props: {
    chartData: {
      type: Object,
      default: () => {}
    },
    chartMarkLine: {
      type: Object,
      default: () => {}
    },
    setting: {
      type: Object,
      default: () => {}
    },
    subtext: {
      type: String,
      default: "m"
    }
  },
  data() {
    return {
      //图表数据
      // chartData: {
      //   // columns: ["date", "PV", "Order", "OrderRate"],
      //   rows: [
      //     {
      //       year: "2020",
      //       primary: "13.4",
      //       secondary: "35.6",
      //       tertiary: "90"
      //     },
      //     {
      //       year: "2021",
      //       primary: "45.4",
      //       secondary: "35.6",
      //       tertiary: "90"
      //     },
      //     {
      //       year: "2022",
      //       primary: "123.4",
      //       secondary: "35.6",
      //       tertiary: "90"
      //     }
      //   ]
      // },
      // setting: {
      //   metrics: ["primary", "tertiary", "secondary"],
      //   dimension: ["year"],
      //   别称
      //   labelMap: {
      //     primary: "第一产业用水量",
      //     secondary: "第二产业用水量",
      //     tertiary: "第三产业用水量",
      //     year: "年份"
      //   }
      // },
      extend: {
        title: {
          show: true,
          text: "",
          subtext: this.subtext,
          subtextStyle: {
            color: "#239def" // 副标题文字颜色
          },
          left: 20
          // textAlign:'center',
        },
        // 图标顶部的标题及按钮
        legend: {
          show: false
        },
        // backgroundColor:'red',//整个组件的背景颜色
        //X轴线
        xAxis: {
          show: true,
          type: "category",
          // 坐标轴轴线
          axisLine: {
            show: false,
            lineStyle: {
              // 属性lineStyle控制线条样式
              color: "#48b",
              width: 0, //设置为0也是不现在坐标线
              type: "solid"
            }
          },
          // 坐标轴每项的文字
          axisLabel: {
            showMaxLabel: true,
            showMinLabel: true,
            color: "#239def",
            rotate: 0, //刻度文字旋转，防止文字过多不显示
            margin: 8, //文字离x轴的距离
            boundaryGap: false, //边界间隙
            // backgroundColor:'#0f0',//横坐标文字颜色
            formatter: v => {
              //
              if (v.length > 19) {
                return v.substring(0, 19) + "...";
              }
              return v;
            }
          },
          // X轴下面的刻度小竖线
          axisTick: {
            show: false,
            alignWithLabel: true, //axisLabel.boundaryGap=true时有效
            interval: 0,
            length: 4 //长度
          },
          // x轴对应的竖线
          splitLine: {
            show: false,
            interval: 0,
            lineStyle: {
              color: "333b3f"
            }
          }
        },
        yAxis: {
          show: true,
          offset: 0,
          // 坐标轴轴线
          axisLine: {
            show: false
          },
          // y轴上对应的横线
          splitLine: {
            show: true,
            interval: 0,
            lineStyle: {
              color: "#112948",
              width: 1
            }
          },
          // 坐标轴刻度
          axisTick: {
            show: false
          },
          axisLabel: {
            show: true,
            color: "#239def"
          },
          max: 0.51,
        },

        // 滚动组件参数
        dataZoom: [
          {
            type: "inside",
            show: true
            // xAxisIndex: [0],
            // startValue: 0,
            // endValue: 4,
            // zoomLock: true, //阻止区域缩放
          }
        ],

        // // 柱形区域
        grid: {
          left: 20,
          right: 10,
          top: 40,
          bottom: 10
          // containLabel:false,
        },

        // // 每个柱子
        series(v) {
          //
          // 设置柱子的样式
          v.forEach(i => {
            // i.barWidth = 10;
            (i.itemStyle = {
              // color: "#08A500"
              // barBorderRadius: [10, 10, 10, 10],
              // borderWidth: 0,
              // color: {
              ////柱状渐变颜色
              // type: "bar",
              // x: 0, //X轴起始坐标
              // y: 1, //Y轴起始坐标
              // x2: 0, //X轴终点坐标
              // y2: 0, //Y轴终点坐标
              // colorStops: [
              //   {
              //     offset: 0,
              //     color: "#0c171b" // 0% 处的颜色
              //   },
              //   {
              //     offset: 0.5,
              //     color: "#084470" // 50% 处的颜色
              //   },
              //   {
              //     offset: 1,
              //     color: "#0091FF" // 100% 处的颜色
              //   }
              // ],
              // globalCoord: false // 缺省为 false
              // }
            }),
              //显示label配置
              (i.label = {
                show: false,
                color: "#333",
                position: "top",
                backgroundColor: "yellow"
              });
          });
          return v;
        }
      }
    };
  },
  created() {
  },
  mounted() {},
  methods: {}
};
</script>
<style lang='scss' scoped>
</style>
