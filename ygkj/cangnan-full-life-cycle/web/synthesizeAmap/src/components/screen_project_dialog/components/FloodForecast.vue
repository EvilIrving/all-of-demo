<script>
import ProjectItem from "./ProjectItem";
import echarts from "echarts";
export default {
  name: "FloodForecast",
  data() {
    return {};
  },
  components: { ProjectItem },
  /**
   * 使用jsx
   */
  render() {
    const chartStyle = { width: "100%", height: "450px" };
    return (
      <div class="flood-forecast">
        <project-item hide-line title="预报分析图">
          <div id="FloodForecast" style={chartStyle} />
        </project-item>
      </div>
    );
  },
  mounted() {
    this.drawForecastChart();
  },
  methods: {
    drawForecastChart() {
      const el = document.querySelector("#FloodForecast");
      let charts = echarts.init(el);
      let option = {
        tooltip: {
          trigger: "axis",
        },
        textStyle: {
          color: "#01c7fc",
        },
        legend: {
          bottom: "bottom",
          textStyle: {
            color: "#01c7fc",
          },
          data: ["雨量", "水位", "流量"],
          itemGap: 140,
          selectedMode: false,
        },
        grid: {
          left: "3%",
          right: "3%",
          bottom: "10%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: ["04-11", "04-12", "04-13", "04-14", "04-15", "04-16", "04-17"],
        },
        yAxis: [
          {
            axisLine: {
              lineStyle: {
                color: "#01c9fe",
              },
            },
            splitLine: {
              lineStyle: {
                color: "#01c7fc33",
              },
            },
            type: "value",
            name: "水位",
            position: "left",
            offset: 60,
            axisLabel: {
              formatter: "{value} m",
            },
          },
          {
            axisLine: {
              lineStyle: {
                color: "#51fcbd",
              },
            },
            splitLine: {
              show: false,
            },
            type: "value",
            name: "雨量",
            nameTextStyle: {
              color: "#51fcbd",
            },
            position: "left",
            axisLabel: {
              formatter: "{value} mm",
            },
          },
          {
            axisLine: {
              lineStyle: {
                color: "#e6b11e",
              },
            },
            splitLine: {
              show: false,
            },
            type: "value",
            name: "流量",
            nameTextStyle: {
              color: "#e6b11e",
            },
            position: "right",
            axisLabel: {
              formatter: "{value} m³/s",
            },
          },
        ],
        series: [
          {
            name: "雨量",
            type: "bar",
            barWidth: 6,
            itemStyle: {
              color: "#51febf",
            },
            data: [120, 132, 101, 134, 90, 230, 210],
            yAxisIndex: 0,
          },
          {
            name: "水位",
            type: "line",
            itemStyle: {
              color: "#01c9fe",
            },
            lineStyle: {
              width: 1,
              type: "dashed",
            },
            data: [30, 28, 20, 23, 29, 43, 41],
            yAxisIndex: 1,
            symbol: "circle",
            showSymbol: false,
            emphasis: {
              showSymbol: true,
            },
          },
          {
            name: "流量",
            type: "line",
            itemStyle: { color: "#e6b11e" },
            lineStyle: {
              width: 1,
              type: "dashed",
            },
            data: [7.2, 7.2, 7.2, 7.2, 7.2, 7.2, 7.2],
            yAxisIndex: 2,
            symbol: "circle",
            showSymbol: false,
            emphasis: {
              showSymbol: true,
            },
          },
        ],
      };
      charts.setOption(option);
    },
  },
};
</script>