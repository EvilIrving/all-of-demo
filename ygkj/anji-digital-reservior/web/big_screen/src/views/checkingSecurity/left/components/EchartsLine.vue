<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 14:19:10
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyLeft\components\EchartsLine.vue
-->
<template>
  <div class="main-box">
    <div id="main"></div>
  </div>
</template>

<script>
export default {
  mounted() {
    this.init();
  },
  methods: {
    init() {
      var chartDom = document.getElementById("main");
      var myChart = this.$echarts.init(chartDom);
      var option;

      option = {
        title:{
          text: '分',
          textStyle:{
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'normal'
          },
          left: 20
        },
        grid: {
          right: 8,
          left: 42,
          top: 30,
        },
        xAxis: {
          type: "category",
          data: ["一月", "二月", "三月", "四月", "五月", "六月"],
          axisLine: {
            show: true,
            lineStyle: {
              type: "solid",
              width: "1", //坐标线的宽度
              opacity: 0.24,
            },
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: true,
            interval: 0,
            textStyle: {
              color: "#FFFFFF", //更改坐标轴文字颜色
              fontSize: 12, //更改坐标轴文字大小
            },
          },
        },
        yAxis: {
          type: "value",
          min: 0,
          max: 100,
          axisLine: {
            show: true,
            lineStyle: {
              type: "solid",
              color: "#41EDD9", //左边线的颜色
              width: "1", //坐标线的宽度
              opacity: 0.24,
            },
          },
          axisLabel: {
            show: true,
            showMinLabel: true,
            showMaxLabel: true,
            textStyle: {
              color: "#FFFFFF", //更改坐标轴文字颜色
              fontSize: 12, //更改坐标轴文字大小
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: "solid",
              color: "#41EDD9", //左边线的颜色
              width: "1", //坐标线的宽度
              opacity: 0.24,
            },
          },
        },
        series: [
          {
            data: [55, 90, 73, 62, 95, 87],
            type: "line",
            label: {
              show: true,
              color: "#fff",
              fontSize: 12,
            },
            lineStyle: {
              color: "#00FBFF",
            },
            itemStyle: {
              color: "#00FBFF",
              borderWidth: 2,
            },
          },
        ],
      };

      option && myChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
.main-box {
  height: 100%;
  padding-top: 10px;
}
#main {
  width: 100%;
  height: 100%;
}
</style>