<!--
 * @Author: lsh
 * @Date: 2021-01-26 10:53:47
 * @LastEditors: lsh
 * @LastEditTime: 2021-01-27 15:53:06
 * @Descripttion: 
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudge\components\charts\BarChart.vue
-->
<template>
  <div style="width: 100%; height: 100%">
    <ve-histogram
      :data="chartData"
      :settings="setting"
      :extend="extend"
      :colors="colors"
      height="100%"
    ></ve-histogram>
  </div>
</template>

<script>
import echarts from "echarts";

export default {
  name: "BarChart",
  components: {},
  props: {
    chartData: {
      type: Object,
      default: () => {},
    },
    setting: {
      type: Object,
      default: () => {},
    },
    subtext: {
      type: String,
      default: "mm",
    },
  },
  data() {
    return {
      //图表数据
      // chartData: {
      //   // columns: ["date", "PV", "Order", "OrderRate"],
      //   rows: [
      //     {
      //       adnm: "2019-1",
      //       tertiary: "123.83"
      //     },
      //     {
      //       adnm: "2019-2",
      //       tertiary: "41.83"
      //     },
      //     {
      //       adnm: "2019-3",
      //       tertiary: "123.83"
      //     }
      //   ]
      // },
      // setting: {
      //   metrics: ["primary", "tertiary", "secondary"],
      //   dimension: ["adnm"],
      //   // 别称
      //   labelMap: {
      //     primary: "第一产业用水量",
      //     secondary: "第二产业用水量",
      //     tertiary: "第三产业用水量",
      //     adnm: "地区"
      //   }
      // },
      extend: {
        series: {
          //柱子宽度设置barWidth
          barMaxWidth: 20,
          itemStyle: {
            //柱子圆角设置barBorderRadius
            // emphasis: {
            //   barBorderRadius: 8
            // },
            normal: {
              // barBorderRadius: 8,
              // color: function(params) {
              //   var colorList = ["#eccb95", "#EBAC43", "#eccb95"];
              //   return colorList[params.dataIndex];
              // }
            },
            //鼠标悬停时：
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
        title: {
          show: true,
          text: "",
          subtext: this.subtext,
          subtextStyle: {
            color: "#239def", // 副标题文字颜色
          },
          left: 20,
          // textAlign:'center',
        },
        // 图标顶部的标题及按钮
        legend: {
          show: false,
        },
        //X轴线
        xAxis: {
          axisLine: {
            show: false,
          },
          // x轴对应的竖线
          splitLine: {
            show: false,
          },
          axisLabel: {
            show: true,
            color: "#239def",
          },
        },
        yAxis: {
          axisLine: {
            show: false,
          },
          // y轴对应的竖线
          splitLine: {
            show: true,
            interval: 0,
            lineStyle: {
              color: "#112948",
              width: 1,
            },
          },
          axisLabel: {
            show: true,
            color: "#239def",
          },
        },

        // 滚动组件参数
        dataZoom: [
          {
            type: "inside",
            show: true,
          },
        ],

        // // 柱形区域
        grid: {
          left: 20,
          right: 10,
          top: 40,
          bottom: 10,
          // containLabel:false,
        },

        // // 每个柱子
        // series(v) {
        //   //
        //   // 设置柱子的样式
        //   v.forEach(i => {
        //     i.barWidth = 50;
        //     (i.itemStyle = {
        //       // barBorderRadius: [10, 10, 10, 10],
        //       // color: "#FF6633",
        //       // borderWidth: 0,
        //       color: {
        //         type: "bar",
        //         x: 0, //X轴起始坐标
        //         y: 1, //Y轴起始坐标
        //         x2: 0, //X轴终点坐标
        //         y2: 0, //Y轴终点坐标
        //         colorStops: [
        //           {
        //             offset: 0,
        //             color: "#0c171b" // 0% 处的颜色
        //           },
        //           {
        //             offset: 0.5,
        //             color: "#084470" // 50% 处的颜色
        //           },
        //           {
        //             offset: 1,
        //             color: "#0091FF" // 100% 处的颜色
        //           }
        //         ],
        //         globalCoord: false // 缺省为 false
        //       }
        //     }),
        //       (i.label = {
        //         color: "rgba(255,255,255,0.88)",
        //         show: true,
        //         position: "top"
        //         // backgroundColor:'yellow',
        //       });
        //   });
        //   return v;
        // }
      },
      colors: [
        new echarts.graphic.LinearGradient(0, 1, 0, 0, [
          {
            offset: 0,

            color: "#51F1F6",
          },
          {
            offset: 0.5,

            color: "#3ED4F7",
          },
          {
            offset: 1,

            color: "#23ABF7",
          },
        ]),
      ],
      isShow: false,
    };
  },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang='scss' scoped>
</style>
