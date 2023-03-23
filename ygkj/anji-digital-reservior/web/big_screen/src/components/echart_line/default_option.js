// 这里的数据会被深度合并
const BASIC_OPTION = {
  grid: {
    top: 40,
    left: 28,
    right: 10,
    bottom: 20,
  },
  xAxis: {
    boundaryGap: true,
    type: "category",
    axisLabel: {
      show: true,
      textStyle: {
        color: "#fff",
        fontSize: "12",
      },
    },
    data: [],
    axisTick: {
      show: false,
    },
    axisLine: {
      lineStyle: {
        color: "#fff",
        fontSize: "12",
      },
    },
  },
  yAxis: {
    type: "value",
    axisTick: {
      show: false,
    },
    axisLine: {
      show: false,
    },
    axisLabel: {
      color: "#00C1FF",
      fontSize: "12",
    },
    splitLine: {
      lineStyle: {
        color: "rgba(255,255,255,0.30)",
        opacity: 0.3,
      },
    },
    name: "m",
    nameTextStyle: {
      color: "#fff",
      fontSize: "12",
    },
  },
  tooltip: {
    show: true,
    trigger: "axis",
  },
  legend: {
    show: true,
    top: "2",
    right: "10",
    orient: "horizontal",
    textStyle: {
      color: "#fff",
      fontSize: "14",
    },
    icon: "circle",
    itemWidth: 12,
    nameTextStyle: {
      color: "#00C1FF",
      fontSize: "14",
    },
  },
  series: [],
  color: ["#33FFFF", "#5DFF30", "#FF0000"],
};

const COLOR_ARRAY = ['#33FFFF', '#5DFF30', '#FF0000'];

export { BASIC_OPTION, COLOR_ARRAY };