<template>
  <div style="height: 100%; width: 100%">
    <ve-pie
      :data="chartData"
      :extend="pieChartExtend"
      :colors="chartColor"
      height="100%"
    ></ve-pie>
  </div>
</template>
<script>
export default {
  name: "PieChart",
  props: {
    chartData: Object,
  },
  data() {
    this.pieChartExtend = {
      legend: {
        type: "scroll", //设置超出部分滚动
        orient: "vertical", //设置竖向排列
        show: true, //是否显示，默认开启
        right: 40, //设置位置
        top: 14,
        bottom: 20,
        textStyle: {
          //文字样式
          fontSize: 14,
          color: "#fff",
          lineHeight: 40,
        },
        // formatter: function (name) {
        //   //超出部分隐藏显示
        //   return name.length > 12 ? name.slice(0, 12) + "..." : name;
        // },
      },
      series: {
        minAngle: 10,
        radius: 90,
        center: ["34%", "50%"],
        labelLine: {
          normal: {
            show: false,
          },
        },
        label: {
          normal: {
            show: false,
          },
        },
      },
    };
    return {
      chartColor: ["#4B83FF", "#2ED47A", "#FFB946", "#F7685B"],
    };
  },
  components: {},
  async mounted() {},
  methods: {},
};
</script>

<style lang="scss" scoped>
</style>
<style lang="scss">
</style>