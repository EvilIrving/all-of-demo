<!--
 * @Author: dyn
 * @Date: 2021-11-15 10:58:50
 * @LastEditTime: 2021-12-16 20:44:40
 * @LastEditors: dtb
 * @Description: 环形图
 * @FilePath: \lucheng-app\src\echarts\Ring.vue
-->
<template>
  <div class="chart-wrap">
    <div class="myChart" ref="Ring"></div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    chartData: {
      type: Array,
      default: () => [],
    },
    total: Number,
    colorList: {
      type: Array,
      default: () => ["#35C966", "#FB9D38", "#38A0FB", "#FBD214"],
    },
    labelLineShow: {
      type: Boolean,
      default: true,
    },
    legendShow: {
      type: Boolean,
      default: false,
    },
    clickEvent: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  data() {
    return {
      myChart: null,
    };
  },
  mounted() { },
  methods: {
    initChart() {
      this.myChart && this.myChart.dispose();
      this.myChart = this.$echarts.init(this.$refs.Ring, null, { renderer: 'svg' });
      let option = {
        title: {
          show: false,
        },
        color: this.colorList,
        tooltip: {
          trigger: "item",
        },
        legend: {
          show: this.legendShow,
          orient: "horizontal",
          right: "center",
          top: "bottom",
          icon: "rect",
          textStyle: {
            lineHeight: 20,
            fontSize: 20,
            color: "#64676B",
          },
          itemWidth: 16, // 设置宽度
          itemHeight: 16, // 设置高度
          // itemGap: 16,
          itemStyle: {
            normal: {
              borderColor: "#fff",
              borderWidth: 2,
              color: function (params) {
                return this.colorList[params.dataIndex];
              },
            },
          },
        },
        graphic: [
          {
            type: "text",
            left: "center",
            top: "39%",
            style: {
              color: "rgba(0,0,0,0.88)",
              text: this.total,
              fontSize: 32,
            },
          },
          {
            type: "text",
            left: "center",
            top: "56%",
            style: {
              text: "总数",
              fontSize: 14,
              color: "rgba(0,0,0,0.48)",
            },
          },
        ],
        series: [
          {
            name: "健康统计",
            type: "pie",
            center: ["50%", "50%"],
            radius: ["50%", "80%"],
            data: this.chartData,
            itemStyle: {
              normal: {
                borderColor: "#fff",
                borderWidth: 2,
              },
            },
            label: {
              //饼图图形上的文本标签
              normal: {
                show: true,
                position: this.labelLineShow ? "" : "inner", //标签的位置
                textStyle: {
                  fontWeight: 300,
                  fontSize: 14, //文字的字体大小
                  color: this.labelLineShow ? "#333333" : "#fff",
                  lineHeight: 18,
                },
                formatter: this.labelLineShow ? "{b}\n{c}" : "{c}",
              },
            },
            //牵引线条颜色
            labelLine: {
              normal: {
                show: this.labelLineShow,
                length: 10,
              },
            },
          },
        ],
      };
      this.myChart.setOption(option);
      this.myChart.on("click", this.clickItem);
    },
    //图标点击事件
    clickItem(param) {
      this.$emit("clickItem", param);
    },
  },
};
</script>

<style lang="scss" scoped>
.chart-wrap {
  width: 100%;
  height: 100%;
  .myChart {
    width: 100%;
    height: 100%;
  }
}
</style>
