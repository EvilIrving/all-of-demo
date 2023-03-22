<template>
  <div class="wraperBox">

    <section class="one">
      <echart-capsule ref="chartCapsule" :series-data="capsuleData" :extra-option="extraOption" @clickItem="getCountryData">
      </echart-capsule>
    </section>

    <section class="two">
      <echart-solid-pie ref="chartSolid" :series-data="EchartSolidData" :extra-option="extraOption" @clickItem="getCountryData">
      </echart-solid-pie>
    </section>000

    <section class="three">
      <echart3dbar ref="chart3dbar" :series-data="Echart3dbarData" :extra-option="extra3dOption" @clickItem="getCountryData">
      </echart3dbar>
    </section>

    <section class="four">
      <echart-instrument ref="chartinstrument" :series-data="EchartInstrumentData" :extra-option="extraInstrumentOption" @clickItem="getCountryData">
      </echart-instrument>
    </section>

    <section class="five">
      <makeapie />
    </section>

    <section class="six">
      <v-chart-bar 
      height="100%" 
      width="100%" 
      ref="rehearsalChart" 
      :barWidth='20' 
      :barData="barData" 
      :colorArr="ranksColor" 
      :labelColor="ranksLabelColor" 
      :barName="ranksBarName" 
      :unit="rankUnit" 
      @barClick="getCountryData">
      </v-chart-bar>
    </section>

  </div>
</template>

<script>
import EchartCapsule from './charts/echart_capsule/EchartCapsule.vue'
import EchartSolidPie from './charts/echart_solidPie/EchartPie.vue'
import Echart3dbar from './charts/echart_3dbar/EchartBar.vue'
import EchartInstrument from './charts/echart_instrument/EchartInstrument.vue'
import Makeapie from './charts/makeapie/Makeapie.vue'
import VChartBar from './charts/vchart_3dbar/VchartBar.vue'
import { generateDataZoom } from "./charts/chart";
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  components: {
    EchartCapsule,
    Echart3dbar,
    EchartInstrument,
    EchartSolidPie,
    VChartBar,
    Makeapie,
  },
  data() {
    return {
      extraOption: {},
      extra3dOption: {},
      extraLineOption: {
        'series': [{
          data: [12, 18, 111, 56, 15, 20],
          type: "line",
          smooth: true,
          name: "预报值",
          smooth: false, //关键点，为true是不支持虚线的，实线就用true
          itemStyle: {
            normal: {
              lineStyle: {
                width: 2,
                type: "dashed"
              }
            }
          }
        },
        {
          data: [123, 181, 111, 561, 150, 20],
          type: "line",
          smooth: true,
          name: "实时值"
        },
        {
          data: [143, 151, 121, 361, 190, 200],
          type: "line",
          smooth: true,
          name: "警戒值",
          lineStyle: {
            color: "#FF0000",
            type: "dashed"
          },
          itemStyle: {
            opacity: 0
          }
        }]
      },
      extraInstrumentOption: {
        tooltip: {
          show: true,
          formatter: function () {
            return '<span style="font-size:18px">温州市总蓄水量万m³</span>';
          },
          confine: true
        }
      },

      capsuleData: [12, 18, 111, 56, 15, 20], //横向胶囊统计图
      EchartSolidData: [{ data: [{ value: 100, name: '红色' }, { value: 222, name: '白色' }, { value: 600, name: '绿色' }, { value: 300, name: '猫色' }, { value: 120, name: '修色' }] }],
      Echart3dbarData: [],
      EchartInstrumentData: [12, 18, 111, 56, 15, 20],
      EchartPieData: [{ name: '报警吧', value: 12 }, { name: '跑哪去', value: 23 }, { name: 'lizhi', value: 56 },],

      ranksLabelColor: '#1EF8F2',
      ranksBarName: '',
      ranksColor: ['#15E2DB', '#9DFDFB', '#A6FFFF'],
      barData: {
        columns: ['name', 'date'],
        rows: [
          { name: '正常', date: '12' },
          { name: '超蓝色', date: '44' },
          { name: '超黄色', date: '223' },
          { name: '超橙色', date: '12' },
          { name: '超红色', date: '210' },
        ]
      },
      rankUnit: 'mm',

    }
  },

  mounted() {
    this.getChartData()
  },

  methods: {
    getCountryData(item) {
      console.log(item, '-------')
    },

    getChartData() {
      let chartData = {
        name: ['2017', '2018', '2019', '2020', '2021', '2022', '2023'],
        now: [36.25, 23.22, 34.11, 56.89, 36.25, 23.22, 23.22,],
        pre: [36.25, 23.22, 34.11, 56.89, 36.25, 23.22, 23.22,],
      };


      let botData = [];
      let colorArr = ["#2886c6", "#50bfda", "#89e3ec"];
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
      let iconPath1 = require("@/assets/images/xushuiliang-1.png");
      let iconPath2 = require("@/assets/images/xushuiliang-dotted-1.png");
      this.extraOption = {
        dataZoom: generateDataZoom(6, chartData.name.length),
        grid: {
          left: "5%",
          top: "10%",
          bottom: "10%",
        },
        yAxis: {
          name: "亿m³",
          nameTextStyle: {
            color: "#ffffff99",
            fontSize: "14",
          },
          max: 25,
        },
        legend: {
          selectedMode: false,
          textStyle: {
            color: "#fff",
          },
          itemGap: 20,
          data: [
            {
              name: "需水量",
              icon: "image://" + iconPath1,
            },
            {
              name: "预测需水量",
              icon: "image://" + iconPath2,
            },
          ],
        },
      }
      chartData.now.forEach(() => botData.push(0));


      this.Echart3dbarData = [
        {
          z: 1,
          name: "需水量",
          type: "bar",
          barWidth: 30,
          barGap: "0%",
          data: chartData.now,
          markLine: {
            symbol: "none",
            data: [
              {
                silent: false,
                lineStyle: {
                  type: "solid",
                  color: "#E89100",
                  width: 1,
                },
                yAxis: 21,
                label: {
                  color: "#fff",
                  show: true,
                  position: "insideEndTop",
                  formatter: function () {
                    return "十四五用水总量控制";
                  },
                },
              },
            ],
          },
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
            color: "00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        {
          z: 2,
          name: "",
          type: "pictorialBar",
          data: botData,
          symbol: "diamond",
          symbolOffset: ["-35%", "50%"],
          symbolSize: [30, 10],
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
          name: "",
          type: "pictorialBar",
          symbolPosition: "end",
          data: chartData.now,
          symbol: "diamond",
          symbolOffset: ["-40%", "-50%"],
          symbolSize: [26, (10 * (30 - 4)) / 30],
          itemStyle: {
            normal: {
              borderColor: "#89e3ec",
              borderWidth: 2,
              color: "#89e3ec",
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          name: "预测需水量",
          type: "bar",
          data: chartData.pre,
          barWidth: 20,
          itemStyle: {
            borderColor: "#1EF8F2",
            borderType: "dashed",
            borderWidth: 2,
            color: function (params) {
              let color = "rgba(0,0,0,0)";
              if (params.value && params.value > 4) {
                color = "#E89100";
              }
              return color;
            },
          },
          label: {
            show: true,
            fontSize: 18,
            fontWeight: "bold",
            position: "top",
            color: "#E89100",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
      ];
      this.extra3dOption = {
        ...this.extra3dOption,
        xAxis: {
          data: chartData.name,
        },
      };
      this.$refs["chart3dbar"].updateChartView();
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
}
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.wraperBox {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  section {
    width: 30%;
    height: 300px;
  }
}
</style>
