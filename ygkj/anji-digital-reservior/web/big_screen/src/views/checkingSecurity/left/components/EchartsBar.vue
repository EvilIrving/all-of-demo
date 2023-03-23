<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 17:57:16
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyLeft\components\EchartsBar.vue
-->
<template>
  <div class="main-box">
    <div id="barMain"></div>
  </div>
</template>

<script>
export default {
  mounted() {
    this.init();
  },
  methods: {
    init() {
      var chartDom = document.getElementById("barMain");
      var myChart = this.$echarts.init(chartDom);
      var option;
      option = {
        title: {
          text: "次",
          textStyle: {
            color: "#ffffff",
            fontSize: 12,
            fontWeight: "normal",
          },
          left: 20,
        },
        legend: {
          top: 0,
          right: 0,
          textStyle: {
            color: "#ffffff",
            fontSize: 10,
          },
          orient: "vertical",
          itemWidth: 12,
          itemHeight: 4,
          itemGap: 3,
        },
        grid: {
          right: 8,
          left: 42,
          top: 35,
        },
        xAxis: {
          type: "category",
          data: ["老虎湾", "灯头坞", "姚坞里", "回车", "杨梅坞", "大官塘"],
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
            name: "绿化",
            type: "bar",
            stack: "Ad",
            emphasis: {
              focus: "series",
            },
            data: [5, 3, 4, 1, 3, 5],
            barWidth: 16,
            itemStyle: {
              color: "#00FBFF",
            },
          },
          {
            name: "保洁",
            type: "bar",
            stack: "Ad",
            emphasis: {
              focus: "series",
            },
            data: [3, 2, 3, 2, 1, 1],
            barWidth: 16,
            itemStyle: {
              color: "#3585E0",
            },
          },
          {
            name: "",
            type: "bar",
            barGap: "-100%",
            label: {
              show: true,
              color: "#fff",
              fontSize: 12,
              position: "top",
            },
            itemStyle: {
              color: "rgba(0,0,0,0)",
            },
            data: [8, 5, 7, 3, 4, 6],
            barWidth: 16,
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
#barMain {
  width: 100%;
  height: 100%;
}
</style>