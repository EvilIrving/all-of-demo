<template>
  <div class="information">
    <typhoon-info :data="typhoonData" @change="onChartChange" />
    <table-title :title="chartName" hide-line />
    <ve-line
      ref="charts"
      :settings="chartSettings"
      :data="chartData"
      :after-config="afterConfig"
      :loading="loading"
      :data-empty="chartData.rows.length == 0"
      :extend="chartsExtend"
      judge-width
      height="300px"
    />
  </div>
</template>
<script>
import "@/style/v-charts/style.css";
import { TableTitle } from "../../components.js";
import TyphoonInfo from "../TyphoonInfo.vue";
export default {
  name: "TyphoonInformation",
  props: { typhoonData: Object },
  components: { TyphoonInfo, TableTitle },
  data() {
    return {
      loading: false,
      chartSettings: {
        axisSite: { right: ["风力"] },
        yAxisType: ["normal", "normal"],
        yAxisName: ["m/s", "级"],
      },
      chartsExtend: {
        yAxis(item) {
          return item.map((item) => ({
            ...item,
            nameTextStyle: { color: "#fff" },
            axisLabel: {
              textStyle: {
                fontSize: 14,
                color: "rgba(255,255,255,0.6)",
              },
            },
          }));
        },
      },
      chartName: "中心气压",
      // 将props里的typhoonData的点位数据带进去 <TyphoonInfo /> 组件中的内容就会变化
      currentTyphoonData: {},
    };
  },
  // inject from ~/cards/TyphoonCard.vue
  inject: ["typhoonCard"],
  watch: {
    "typhoonCard.pageName": {
      immediate: true,
      handler(val, oldVal) {
        if (val == "currentTyphoon") {
          this.$refs.charts.resize();
        }
      },
    },
  },
  computed: {
    computedChartData() {
      return JSON.parse(this.typhoonData?.points || "[]");
    },
    chartDataWind() {
      return {
        columns: ["时间", "风速", "风力"],
        rows: this.computedChartData.map((item) => ({
          时间: item.time,
          风速: item.speed,
          风力: item.power,
        })),
      };
    },
    chartPressure() {
      return {
        columns: ["时间", "中心气压"],
        rows: this.computedChartData.map((item) => ({
          时间: item.time,
          中心气压: item.pressure,
        })),
      };
    },
    chartMoveSpeed() {
      return {
        columns: ["时间", "移动速度"],
        rows: this.computedChartData.map((item) => ({
          时间: item.time,
          移动速度: item.move_speed,
        })),
      };
    },
    chartData() {
      const chartDataMap = {
        中心气压: this.chartPressure,
        风力风速: this.chartDataWind,
        移动速度: this.chartMoveSpeed,
      };

      return chartDataMap[this.chartName];
    },
  },
  methods: {
    onChartChange(name) {
      if (name == "中心位置") return;
      this.chartName = name;
    },
    afterConfig(options) {
      options.legend = {
        ...options.legend,
        selectedMode: false,
        icon: "line",
        textStyle: {
          color: "#ffffff",
        },
      };
      options.grid = {
        top: "20%",
        left: "3%",
        right: "5%",
        bottom: "3%",
        containLabel: true,
      };
      options.tooltip = {
        trigger: "axis",
        axisPointer: {
          type: "cross",
          crossStyle: { color: "#999" },
        },
      };
      options.xAxis[0] = {
        ...options.xAxis[0],
        axisLabel: {
          textStyle: { fontSize: 14, color: "#fff" },
        },
      };
      options.series = options.series.map((item, index) => ({
        ...item,
        symbol: "none",
        itemStyle: {
          normal: {
            color: ["#eec80b", "#00f5ff"][index],
            lineStyle: {
              color: ["#eec80b", "#00f5ff"][index],
            },
          },
        },
      }));

      options.darkMode = true;
      return options;
    },
  },
};
</script>
<style lang="scss">
.v-charts-data-empty {
  background-color: #1414144f;
}
</style>
