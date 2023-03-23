<script>
import CapsuleTabs from "../../../components/CapsuleTabs.vue";
import PrecipitionMaxList from "../../../components/PrecipitionMaxList.vue";
import { mapGetters } from "vuex";
const chartOption = {
  grid: {
    top: "12%",
    bottom: "1%",
    containLabel: true,
  },
  settings: {
    yAxisName: ["mm"],
    itemStyle: { color: "#00C1FF" },
    label: {
      show: true,
      fontSize: 18,
      fontWeight: "bold",
      position: "top",
      color: "#fff",
      formatter: ({ value }) => Number(value).toFixed(1),
    },
  },
  extend: {
    "textStyle.color": "#fff",
    barWidth: 15,
    xAxis: {
      axisLabel: {
        interval: 0,
      },
    },
    yAxis: {
      nameTextStyle: { color: "#ffffff99" },
      axisLine: {
        show: false,
      },
      axisLabel: {
        color: "#fff",
      },
      splitLine: {
        lineStyle: {
          color: "#00C1FF",
          opacity: 0.5,
        },
      },
      max: (value) => {
        return value.max > 1 ? null : 1;
      },
    },
  },
};
export default {
  name: "Rainfall",
  data() {
    return {
      currentIndex: 0,
      tabsList: ["行政区划", "流域"],
    };
  },
  computed: { ...mapGetters(["rainfallStat", "rainfallMax"]) },
  methods: {
    renderPreMaxList() {
      let { maxBasin, maxArea, maxPoint, totalRainfall } = this.rainfallMax;
      let maxList = [
        { label: "温州市累计面雨量", data: { value: totalRainfall }, disabled: true },
        { label: "最大降雨点", data: maxPoint, disabled: true },
        { label: "最大降雨区域", data: maxArea, disabled: true },
        { label: "最大降雨流域", data: maxBasin, disabled: true },
      ];
      return <PrecipitionMaxList maxList={maxList} />;
    },
    afterConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#2886c6", "#50bfda", "#89e3ec"];
      let color = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[1],
          },
          {
            offset: 1,
            color: colorArr[1],
          },
        ],
      };
      let barWidth = 30;
      let constData = [];
      let showData = [];
      data.filter(function (item) {
        if (item) {
          constData.push(1);
          showData.push(item);
        } else {
          constData.push(0);
          showData.push({
            value: 1,
            itemStyle: {
              normal: {
                borderColor: "rgba(0,0,0,0)",
                borderWidth: 2,
                color: "rgba(0,0,0,0)",
              },
            },
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: "数据",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: data,
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 18,
            fontWeight: "bold",
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        {
          z: 2,
          name: "数据",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: ["0%", "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "数据",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: ["0%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      return options;
    },
    rainfallData() {
      let rows = this.rainfallStat(this.tabsList[this.currentIndex]);
      return {
        columns: ["name", "value"],
        rows,
      };
    },
  },
  render() {
    let histogramProps = {
      props: {
        height: "230px",
        data: this.rainfallData(),
        ...chartOption,
        afterConfig: this.afterConfig,
        tooltipVisible: false,
        legendVisible: false,
      },
      style: "margin-bottom:12px",
    };
    let preMaxList = this.renderPreMaxList();
    return (
      <div>
        <div class="panel-item-subtitle">
          面雨量
          <div style="margin-left: auto;">
            <CapsuleTabs
              value={this.currentIndex}
              onChange={(value) => {
                this.currentIndex = value;
              }}
              tabs={this.tabsList}
            />
          </div>
        </div>
        <ve-histogram {...histogramProps} />
        {preMaxList}
      </div>
    );
  },
};
</script>
