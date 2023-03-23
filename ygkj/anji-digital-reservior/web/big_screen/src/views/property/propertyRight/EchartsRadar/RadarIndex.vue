<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 17:30:45
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyRight\EchartsRadar\RadarIndex.vue
-->
<template>
  <div class="main-box">
    <div id="radarMain"></div>
  </div>
</template>

<script>
export default {
  mounted() {
    this.init();
  },
  methods: {
    init() {
      let list = [
        {
          label: "老虎湾",
          value: 20,
        },
        {
          label: "灯头坞",
          value: 16,
        },
        {
          label: "姚坞里",
          value: 15,
        },
        {
          label: "回车",
          value: 15,
        },
        {
          label: "杨梅坞",
          value: 16,
        },
        {
          label: "大官塘",
          value: 17,
        },
      ];
      var chartDom = document.getElementById("radarMain");
      var myChart = this.$echarts.init(chartDom);
      var option;
      option = {
        // title: {
        //   text: "汛期非汛期巡查频次",
        //   textStyle: {
        //     color: "#ffffff",
        //     fontSize: 14,
        //     fontWeight: "normal",
        //   },
        //   left: 20,
        //   backgroundColor: "rgba(255, 255, 255, 0.16)",
        //   // borderColor:
        //   //   "linear-gradient(225deg, rgba(50, 197, 255, 0), rgba(50, 197, 255, 1), rgba(0, 251, 255, 0)) 1 1",
        //   // borderWidth: 1,
        // },
        // legend: {
        //   bottom: 20,
        //   textStyle: {
        //     color: "#ffffff",
        //   },
        // },
        grid: {
          right: 8,
          left: 42,
          top: 200,
        },
        radar: {
          // shape: 'circle',
          center: ["50%", "50%"],
          radius: "55%",
          nameGap: 5,
          indicator: [
            { name: "老虎湾", max: 20, axisLabel: { show: true } },
            { name: "灯头坞", max: 20 },
            { name: "姚坞里", max: 20 },
            { name: "回车", max: 20 },
            { name: "杨梅坞", max: 20 },
            { name: "大官塘", max: 20 },
          ],
          name: {
            rich: {
              a: {
                color: "#FFFFFF",
              },
              b: {
                color: "#47E6F4",
                lineHeight: 20,
              },
              c: {
                color: "#FFFFFF",
                padding: [0, 0, 0, 60],
                lineHeight: 20,
              },
              d: {
                color: "#47E6F4",
                padding: [0, 0, 0, 50],
              },
            },
            formatter: function (value, indicator) {
              let data = list.find((item) => item.label == value);
              if ((data.label == "老虎湾")) {
                return `{c|${data.label}}\n{d|${data.value}次}`;
              } else {
                return `{a|${data.label}}\n{b|${data.value}次}`;
              }
            },
            color: "#428BD4",
          },
          axisLine: {
            lineStyle: {
              color: "#E9E9E9",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#E9E9E9",
            },
          },
          splitArea: {
            show: false,
          },
          splitNumber: 4,
        },
        series: [
          {
            name: "Budget vs spending",
            type: "radar",
            data: [
              {
                value: [20, 16, 15, 15, 16, 17],
              },
            ],
            symbol: "none",
            lineStyle: {
              color: "#47E6F4",
            },
            areaStyle: {
              color: "rgba(71, 230, 244, 0.15)",
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
#radarMain {
  width: 100%;
  height: 100%;
}
</style>