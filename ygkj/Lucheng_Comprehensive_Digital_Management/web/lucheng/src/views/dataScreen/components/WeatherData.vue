<!--
 * @Author: hanyu
 * @LastEditTime: 2021-01-25 16:59:28
 * @Description: 大屏左下角天气
 * @FilePath: /FloodDisasterDispatch/src/views/dataScreen/components/WeatherData.vue
-->
<template>
  <div class="weather_wrap">
    <div class="weather_tab">
      <div
        :class="['weather_tab_item', weatherActive == i ? 'active' : '']"
        v-for="i in weatherTabArr"
        :key="i"
        @click="handleWeatherTab(i)"
      ></div>
    </div>
    <div class="weather_top">
      <template v-if="weatherActive == 1 || weatherActive == 0">
        <i class="icon-screen-weather1"></i>
        <div>
          <span>{{ weatherText }}</span>
          <p>{{ weatherVal }}</p>
        </div>
      </template>
      <template v-if="weatherActive == 2 || weatherActive == 3">
        <div class="ring-box">
          <ve-ring
            :data="chartData"
            :settings="chartSettings"
            :extend="chartExtend"
            :legend="{ show: false }"
            :tooltip="tooltip"
            height="100%"
            width="100%"
          ></ve-ring>
        </div>
        <div class="ring-wrap">
          <i class="ring-icon"></i>
          <span class="ring-text">{{ ringText }}</span>
          <p class="ring-num">{{ ringNum }}</p>
        </div>
      </template>
    </div>
    <div class="weather_mid">
      <i
        :class="
          weatherActive == 1 || weatherActive == 0
            ? 'icon-screen-weather2'
            : 'icon-level-warn-chart'
        "
      ></i>
      <span>{{ weatherChartText }}</span>
    </div>
    <div id="myChart"></div>
  </div>
</template>

<script>
import echarts from "echarts";
import { weatherPredict, currentWeather } from "@/api/system";
export default {
  name: "WeatherData",
  props: {},
  components: {},
  data() {
    this.chartExtend = {
      series: {
        center: ["50%", "50%"],
        hoverAnimation: false,
      },
      title: {
        show: true,
        left: "center",
        top: "18%",
        text: "",
        subtext: "75%",
        textStyle: {
          color: "#289DFB",
        },
        subtextStyle: {
          color: "#289DFB",
        },
      },
      color: ["#15365D", "#289DFB "],
    };
    this.chartSettings = {
      radius: [20, 30],
      labelLine: {
        show: false,
      },
      label: {
        show: false,
      },
    };
    this.tooltip = {
      position: "right",
    };
    return {
      chartData: {
        columns: ["水位", "数量"],
        rows: [
          { 水位: "异常水位", 数量: 25 },
          { 水位: "正常水位", 数量: 75 },
        ],
      },
      weatherTabArr: [0, 1, 2, 3],
      weatherActive: 0,
      weatherText: "多云",
      weatherVal: "",
      weatherChartText: "",
      rainfallArr: [],
      weatherArr: [],
      currentTemperature: "",
      currentRainfall: "",
      ringText: "正常水位",
      ringNum: 256,
    };
  },
  mounted() {
    // this.initChart([23, 17, 14, 16, 16, 13, 19], "预计温度", "℃");
    this.getWeatherPredict();
    this.getCurrentWeather();
  },
  methods: {
    handleWeatherTab(index) {
      // 0 气象 1降水 2水位 3工情
      if (index == 0) {
        this.weatherText = "多云";
        this.weatherVal = this.currentTemperature;
        this.weatherChartText = "未来7天天气预报";
        this.initChart(
          [
            this.$moment(new Date()).add(1, "days").format("MM-DD"),
            this.$moment(new Date()).add(2, "days").format("MM-DD"),
            this.$moment(new Date()).add(3, "days").format("MM-DD"),
            this.$moment(new Date()).add(4, "days").format("MM-DD"),
            this.$moment(new Date()).add(5, "days").format("MM-DD"),
            this.$moment(new Date()).add(6, "days").format("MM-DD"),
            this.$moment(new Date()).add(7, "days").format("MM-DD"),
          ],
          this.weatherArr,
          "预计温度",
          "℃",
          "line"
        );
      } else if (index == 1) {
        this.weatherText = "当前降雨";
        this.weatherVal = this.currentRainfall;
        this.weatherChartText = "未来7天降雨趋势";
        this.initChart(
          [
            this.$moment(new Date()).add(1, "days").format("MM-DD"),
            this.$moment(new Date()).add(2, "days").format("MM-DD"),
            this.$moment(new Date()).add(3, "days").format("MM-DD"),
            this.$moment(new Date()).add(4, "days").format("MM-DD"),
            this.$moment(new Date()).add(5, "days").format("MM-DD"),
            this.$moment(new Date()).add(6, "days").format("MM-DD"),
            this.$moment(new Date()).add(7, "days").format("MM-DD"),
          ],
          this.rainfallArr,
          "预计降雨",
          "mm",
          "line"
        );
      } else if (index == 2) {
        this.weatherChartText = "各区域水位超警数量";
        this.initChart(
          ["永嘉县", "乐清市", "龙湾区", "瓯海区", "瑞安市", "平阳县"],
          [5, 10, 13, 12, 4, 2],
          "超警数量",
          "个",
          "bar"
        );
      } else if (index == 3) {
        this.initChart(
          [
            "1月",
            "2月",
            "3月",
            "4月",
            "5月",
            "6月",
            "7月",
            "8月",
            "9月",
            "10月",
            "11月",
            "12月",
          ],
          [5, 10, 13, 12, 4, 2, 5, 10, 13, 12, 4, 2],
          "报警数量",
          "个",
          "bar"
        );
        this.weatherChartText = "每月报警数量";
      }
      this.weatherActive = index;
    },
    async getCurrentWeather() {
      let res = await currentWeather();
      if (res.code == 0) {
        this.currentRainfall = res.data.currentRainfall + "mm";
        this.currentTemperature = res.data.currentTemperature + "℃";
        this.weatherVal = this.currentTemperature;
      }
    },
    async getWeatherPredict() {
      let res = await weatherPredict("温州市");
      if (res.code == 0) {
        let arr = res.data;
        this.rainfallArr = [];
        this.weatherArr = [];
        for (let i in arr) {
          this.rainfallArr.push(arr[i].rainfall ? arr[i].rainfall : 0);
          this.weatherArr.push(
            ((Number(arr[i].maxTemp) + Number(arr[i].minTemp)) / 2).toFixed(1)
          );
        }
        this.handleWeatherTab(0);
      }
    },
    initChart(xdata, ydata, text, unit, type, color) {
      let myChart = echarts.init(document.getElementById("myChart"));
      myChart.setOption({
        color: ["#23D864", "#FF4D51"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: "{b}:<br/>" + text + " {c} " + unit,
        },
        grid: {
          top: "10",
          left: "0",
          right: "16",
          bottom: "0",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          boundaryGap: true,
          data: xdata,
          axisLabel: {
            // inside: true,
            textStyle: {
              color: "#3697FF",
            },
          },
          axisTick: {
            alignWithLabel: true, //类目轴中在 boundaryGap 为 true 的时候有效，
          },
          axisLine: {
            show: false,
          },
          min: 0,
        },
        yAxis: {
          type: "value",
          boundaryGap: false,
          name: unit,
          nameTextStyle: {
            color: "#3697FF",
            padding: [0, 27, -18, 0],
          },
          // axisTick: {
          //   show: false
          // },
          axisLine: {
            show: false,
          },
          axisLabel: {
            // inside: true,
            textStyle: {
              color: "#3697FF",
            },
          },
          splitLine: {
            //网格线
            lineStyle: {
              color: "#3588DA",
              opacity: 0.3,
            },
          },
        },
        series: [
          {
            type: type,
            smooth: true,
            symbol: "none",
            // symbolSize: 5,
            // sampling: 'average',
            itemStyle: {
              color:
                type == "bar"
                  ? new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      {
                        offset: 0,
                        color: "#F8B91D",
                      },
                      {
                        offset: 1,
                        color: "#F66B53",
                      },
                    ])
                  : "#3588DA",
            },
            stack: "a",
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#3588DA",
                },
                {
                  offset: 1,
                  color: "#091b2e",
                },
              ]),
            },
            barWidth: 20,
            data: ydata,
          },
        ],
      });
    },
  },
};
</script>

<style lang='scss' scoped>
.weather_wrap {
  height: 100%;
  width: 100%;
  .ring-box {
    height: 60px;
    width: 60px;
  }
  .ring-wrap {
    .ring-icon {
      display: inline-block;
      height: 6px;
      width: 6px;
      border-radius: 3px;
      background-image: linear-gradient(0deg, #0a65f5 0%, #3dc6ff 100%);
      box-shadow: 0 0 8px 0 #289dfb;
      margin: 0 8px;
    }
    .ring-text {
      font-size: 12px;
      color: #3998f8;
      letter-spacing: 1px;
      font-weight: 400;
    }
    .ring-num {
      font-size: 16px;
      color: #ffffff;
      letter-spacing: 1.33px;
      text-shadow: 0 0 8px #289dfb;
      font-weight: 600;
      color: #3998f8;
      margin-left: 22px;
    }
  }

  .weather_tab {
    display: flex;
    height: 14px;
    margin-left: 16px;
    margin-bottom: 16px;
    &_item {
      height: 100%;
      width: 12px;
      background: #0b1113;
      border: 1px solid #3f8ae7;
      box-shadow: 0 0 4px 0 rgba(63, 138, 231, 0.8);
      border-radius: 1px;
      margin-right: 6px;
      cursor: pointer;
      &.active {
        background: #3f8ae7;
        box-shadow: 0 0 4px 0 rgba(63, 138, 231, 0.8);
      }
    }
  }
  .weather_top {
    @include flexbox();
    @include flexAC();
    i {
      width: 50px;
      height: 50px;
      margin-right: 20px;
    }
    span {
      font-size: 16px;
      color: #469eff;
    }
    p {
      padding-top: 8px;
      font-size: 24px;
      color: #bbfdff;
    }
  }
  .weather_mid {
    @include flexbox();
    @include flexAC();
    font-size: 14px;
    color: #ffffff;
    font-weight: 500;
    padding-top: 14px;
    i {
      width: 18px;
      height: 18px;
      margin-right: 8px;
    }
  }
  #myChart,
  .bar-box {
    margin-top: 12px;
    height: 86px;
    width: 424px;
  }
}
</style>
