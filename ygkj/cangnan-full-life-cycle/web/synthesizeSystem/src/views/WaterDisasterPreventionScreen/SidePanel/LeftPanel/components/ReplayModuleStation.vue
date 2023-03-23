<script>
import { mapActions, mapGetters } from "vuex";
const chartColor1 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#E00000",
    },
    {
      offset: 1,
      color: "#5A0404",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor2 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#DD4692",
    },
    {
      offset: 1,
      color: "#84015B",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor3 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#B61DD2",
    },
    {
      offset: 1,
      color: "#D599FF",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor4 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#EF101E",
    },
    {
      offset: 1,
      color: "#FE5F70",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor5 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#E35C26",
    },
    {
      offset: 1,
      color: "#FBA070",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor6 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#FFCA6D",
    },
    {
      offset: 1,
      color: "#F8F5AC",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor7 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#62AF32",
    },
    {
      offset: 1,
      color: "#4FF050",
    },
  ],
  global: false, // 缺省为 false
};
const chartColor8 = {
  type: "linear",
  x: 0,
  y: 0,
  x2: 1,
  y2: 1,
  colorStops: [
    {
      offset: 0,
      color: "#2D88EC",
    },
    {
      offset: 1,
      color: "#B2F4F8",
    },
  ],
  global: false, // 缺省为 false
};
const chartOption = {
  grid: { top: "12%", bottom: "1%", containLabel: true },
  settings: { labelLine: { show: false }, label: { show: false } },
  extend: {
    "textStyle.color": "#fff",
    legend: {
      icon: "circle",
      itemWidth: 16,
      selectedMode: false,
      orient: "vertical",
      right: "0%",
      textStyle: { color: "#FFF" },
      textStyle: {
        rich: {
          l: { fontSize: 16, width: 100 },
          n: {
            fontSize: 18,
            color: "#1CFFF5",
            width: 50,
            align: "right",
          },
          u: { fontSize: 16, width: 20, align: "left" },
        },
      },
    },
    series: {
      center: ["25%", "50%"],
      radius: ["45%", "70%"],
      color: [
        chartColor1,
        chartColor2,
        chartColor3,
        chartColor4,
        chartColor5,
        chartColor6,
        chartColor7,
        chartColor8,
      ],
    },
  },
};
export default {
  name: "StationStat",
  methods: {
    afterConfig(options) {
      let data = options.series[0].data;
      options.legend.formatter = (legend) => {
        let val = data.find((item) => item.name == legend).value;
        return `{l|${legend}} {n|${val}}{u|个}`;
      };
      return options;
    },
  },
  computed: {
    ...mapGetters(["stationStat"]),
  },
  render() {
    let ringProps = {
      props: {
        afterConfig: this.afterConfig,
        height: "230px",
        data: {
          columns: ["name", "value"],
          rows: this.stationStat,
        },
        ...chartOption,
      },
    };
    return (
      <div>
        <div class="panel-item-subtitle">站点统计</div>
        <ve-ring {...ringProps} />
      </div>
    );
  },
};
</script>
