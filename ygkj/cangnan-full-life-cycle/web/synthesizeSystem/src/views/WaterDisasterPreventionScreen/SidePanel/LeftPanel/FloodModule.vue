<template>
  <div>
    <!-- 防御态势 -->
    <div class="collapse-panel__border" style="width: 490px">
      <el-carousel trigger="click" arrow="never" :interval="6000">
        <el-carousel-item>
          <div class="collapse-panel__content">
            <!-- 气象预警 六天预报 -->
            <panel-weather-meteor />
            <!-- 短临预报降雨 1h, 3h, 6h -->
            <panel-item title="短临预报" hasSwitch>
              <!-- 短临预报降雨 标题右侧图标点击事件 -->
              <template slot="title-switch">
                <!-- <i class="icon-yinzhang-1 icon-sty" /> -->
                <i
                  class="icon-liebiao icon-sty"
                  @click="openDetailsDialog('短临预报')"
                />
                <!-- <i class="icon-yizhangtu icon-sty" /> -->
              </template>
              <!-- 1h, 3h, 6h 降雨量数据 -->
              <card-tabs
                :tabs-active="cardSelect == 'forecast'"
                v-model="forecastPreCardSelect"
                :list="forecastPreCardList"
                @change="precipitationCardClick"
              />
              <!-- 翻页 -->
              <div class="sidebar-list switch-list">
                <el-carousel
                  height="200px"
                  trigger="click"
                  arrow="never"
                  :autoplay="false"
                >
                  <el-carousel-item>
                    <!-- 短临预报降雨极值列表 -->
                    <precipition-max-list
                      :active="maxSelect == 'forecast'"
                      :max-list="forecastMaxList"
                      @change="forecastMaxListChange"
                      @active="forecastMaxListActive"
                    />
                  </el-carousel-item>
                  <el-carousel-item>
                    <!-- 一小时面雨量 -->
                    <div class="areal-rainfall-sty">
                      <div>
                        <i class="icon-diamond icon-sty" />
                        <span>{{
                          `${forecastPreCardList[forecastPreCardSelect].label}面雨量`
                        }}</span>
                      </div>
                      <capsule-tabs
                        v-model="forecastPreChartTab"
                        :tabs="['行政区划', '流域']"
                        @change="forecastChartTabChange"
                      />
                    </div>
                    <!-- 图表信息 -->
                    <ve-histogram
                      :loading="forecastChartLoading"
                      height="180px"
                      :judge-width="true"
                      :data="forecastChartData"
                      :settings="forecastChartOption.setting"
                      :extend="forecastChartOption.extend"
                      :grid="forecastChartOption.grid"
                      :after-config="forecastConfig"
                    />
                  </el-carousel-item>
                </el-carousel>
              </div>
            </panel-item>
            <!-- 山洪 -->
            <panel-mountain-torrents />
          </div>
        </el-carousel-item>
        <el-carousel-item>
          <div class="collapse-panel__content">
            <!-- 1h, 3h, 6h, 12h, 24h, 72h 实时降雨量 -->
            <panel-item title="实时降雨" hasSwitch>
              <!-- 预报降雨量 标题右侧图标点击事件 -->
              <template slot="title-switch">
                <i
                  v-if="cardSelect == 'realtime'"
                  title="地图落点"
                  @click="handleRainStationChange('ylbtn')"
                  :class="[
                    ylbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                    'icon-sty',
                  ]"
                />
                <i
                  style="margin: 0 10px"
                  title="查看详表"
                  class="icon-liebiao icon-sty"
                  @click="openDetailsDialog('实时降雨')"
                />
                <i
                  @click="goToAmap('雨量')"
                  title="一张图"
                  class="icon-yizhangtu icon-sty"
                />
              </template>
              <!-- 雨量站 点位站 -->
              <div class="station-list">
                <div class="statistic">
                  雨量站
                  <span class="number">
                    {{ realtimePreList.totalStationNumber || "0" }}
                  </span>
                  个
                </div>
                <div class="statistic">
                  降雨站
                  <span class="number">
                    {{ realtimePreList.hasRainfallStationNumber || "0" }}
                  </span>
                  个
                </div>
              </div>
              <!-- 1h, 3h, 6h, 12h, 24h, 72h 实时降雨量数据 -->
              <card-tabs
                v-model="realtimePreCardSelect"
                :tabs-active="cardSelect == 'realtime'"
                :list="realtimePreCardList"
                @change="realtimePreCardClick"
              />
              <!-- 1小时超过 30mm -->
              <div class="prediction-sty">
                <el-row>
                  <!-- 影响人口 -->
                  <el-col :span="12">
                    <div class="early-warning-sty">
                      <p>1小时超过30mm</p>
                      <p>
                        <span>
                          {{ realtimePreList["1hourBeyond30"] || "0" }}
                          <span>个</span>
                        </span>
                      </p>
                    </div>
                  </el-col>
                  <!-- 3小时超过60mm -->
                  <el-col :span="12">
                    <div class="early-warning-style">
                      <p>3小时超过60mm</p>
                      <p>
                        <span>
                          {{ realtimePreList["3hourBeyond60"] || "0" }}
                          <span>个</span>
                        </span>
                      </p>
                    </div>
                  </el-col>
                </el-row>
              </div>
              <!-- 实时降雨极值列表 -->
              <precipition-max-list
                :active="maxSelect == 'realtime'"
                :max-list="realtimeMaxList"
                @change="realtimeMaxListChange"
                @active="realtimeMaxListActive"
              />
              <!-- 一小时面雨量图表 -->
              <div class="areal-rainfall-sty">
                <div>
                  <i class="icon-diamond icon-sty" />
                  <span>{{
                    `${realtimePreCardList[realtimePreCardSelect].label}面雨量`
                  }}</span>
                </div>
                <capsule-tabs
                  v-model="realtimePreChartTab"
                  :tabs="realtimePreChartTabList"
                  @change="realtimePreChartTabChange"
                />
              </div>
              <ve-histogram
                height="370px"
                :judge-width="true"
                :loading="realtimeChartLoading"
                :data="realtimeChartData"
                :settings="realtimeChartOption.setting"
                :extend="realtimeChartOption.extend"
                :grid="realtimeChartOption.grid"
                :tooltip-visible="false"
                :legend-visible="false"
                :after-config="realtimeConfig"
              />
            </panel-item>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div
      class="collapse-panel__border"
      style="width: 490px; margin-left: 0.5rem"
    >
      <typhoon-module :typhoonList="typhoonData" v-if="!typhoonShowBool" />
      <three-typhoo-module :typhoonList="typhoonData" v-if="typhoonShowBool" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "v-charts/lib/style.css";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import PanelItem from "../../components/PanelItem";
import areaWzBorder from "../../../../assets/water_data/areaWzBorder.json";
import CardTabs from "../../components/CardTabs.vue";
import CapsuleTabs from "../../components/CapsuleTabs.vue";
import PrecipitionMaxList from "../../components/PrecipitionMaxList.vue";
import PanelWeatherMeteor from "../components/PanelWeatherInfo.vue";
import PanelMountainTorrents from "../components/PanelMountainTorrents.vue";
import TyphoonModule from "../TyphoonModule.vue";
import ThreeTyphooModule from "../ThreeTyphooModule.vue";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "LeftSildePanelDefenseModule",
  props: ["typhoonData", "typhoonShowBool"],
  components: {
    PanelItem,
    CapsuleTabs,
    CardTabs,
    PrecipitionMaxList,
    PanelWeatherMeteor,
    PanelMountainTorrents,
    TyphoonModule,
    ThreeTyphooModule,
  },
  data() {
    return {
      //两个卡片选择中
      cardSelect: "",
      maxSelect: "",
      //短临预报降雨图表tab
      forecastPreChartTabList: ["行政区划", "流域"],
      forecastPreChartTab: 0,
      forecastAreaRainfallChart: [],
      forecastBasinRainfallChart: [],
      //短临预报降雨卡片
      forecastPreCardSelect: 0,
      //短临预报降雨数据
      forecastPreList: {},
      //短临预报降雨极值
      forecastPreMaxList: {},
      //短临预报降雨图表
      forecastChartLoading: false,
      forecastChartData: {
        columns: ["name", "min", "max", "avg"],
        rows: [
          { name: "永嘉县", min: 136, max: 178 },
          { name: "乐清市", min: 136, max: 178 },
          { name: "瓯海区", min: 136, max: 178 },
          { name: "鹿城区", min: 136, max: 178 },
          { name: "洞头区", min: 136, max: 178 },
        ],
      },
      forecastChartOption: {
        grid: {
          top: "30%",
          bottom: "0%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["mm", ""],
          labelMap: { min: "最小值", max: "最大值", avg: "平均值" },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        extend: {
          legend: {
            right: "0%",
            icon: "circle",
            itemWidth: 10,
            textStyle: { color: "#FFF" },
          },
          "textStyle.color": "#fff",
          barWidth: 15,
          yAxis: {
            nameTextStyle: { color: "#ffffff99" },
            max: (value) => {
              return value.max > 1 ? null : 1;
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
          },
          "series.0.itemStyle": { color: "#00C1FF" },
          "series.1.itemStyle": { color: "#EEC80B" },
        },
      },
      //实时降雨图表tab
      realtimePreChartTabList: ["行政区划", "流域", "水库"],
      realtimePreChartTab: 0,
      //实时降雨
      realtimePreCardSelect: 0,
      realtimePreCardLoading: false,
      //短临预报降雨数据
      realtimePreList: {},
      //实时降雨极值
      realtimePreMaxList: {},
      //实时降雨图表
      realtimeChartLoading: true,
      realtimeChartData: {
        columns: ["name", "data"],
        rows: [],
      },
      realtimeChartOption: {
        grid: {
          top: "10%",
          bottom: "1%",
          containLabel: true,
        },
        setting: {
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
      },

      //预报降雨量
      show: false,
      rainList: [], //雨量测站列表
      ylbtn: false, //雨量单位展示标记
      realdzmbtn: false, //实时等值面开关标记
    };
  },
  filters: {
    numFilter(val) {
      let num = val ? Number(val).toFixed(1) : 0;
      return Number(num);
    },
  },
  computed: {
    ...mapGetters(["ScreenMap", "currentSelectArea"]),
    rainfallPointNum() {
      let map = { 0: "newsest_1", 1: "1", 2: "3", 3: "6", 4: "24", 5: "72" };
      let num =
        this.realtimePreList[`rainfall_${map[this.realtimePreCardSelect]}_num`];
      return num;
    },
    isBigScrren() {
      return this.$route.query.large == "true";
    },
    /** 短临预报卡片数据 */
    forecastPreCardList() {
      let list = [
        this.forecastPreList.rainfall_1,
        this.forecastPreList.rainfall_3,
        this.forecastPreList.rainfall_6,
      ];
      return [
        {
          label: "1小时",
          data: { number: list[0], unit: "mm" },
        },
        {
          label: "3小时",
          data: { number: list[1], unit: "mm" },
        },
        {
          label: "6小时",
          data: { number: list[2], unit: "mm" },
        },
      ];
    },
    /** 预报极值列表数据 */
    forecastPreMaxStation() {
      let name = this.forecastPreMaxList.maxRainfallStationName;
      let max = Number(this.forecastPreMaxList.maxRainfall).toFixed(1) || "0";
      let min = Number(this.forecastPreMaxList.minRainfall).toFixed(1) || "0";
      let lng = this.forecastPreMaxList.maxRainfallLongitude;
      let lat = this.forecastPreMaxList.maxRainfallLatitude;
      return {
        name: name || "",
        value: `${max}` || "0",
        type: "station",
        lng,
        lat,
      };
    },
    forecastPreMaxArea() {
      let name = this.forecastPreMaxList.maxRainfallAreaName;
      let max =
        Number(this.forecastPreMaxList.areaMaxRainfall).toFixed(1) || "0";
      let min =
        Number(this.forecastPreMaxList.areaMinRainfall).toFixed(1) || "0";
      return {
        name: name || "",
        value: `${max}` || "0",
        type: "area",
      };
    },
    forecastPreMaxBasin() {
      let name = this.forecastPreMaxList.maxRainfallBasinName;
      let max =
        Number(this.forecastPreMaxList.maxBasinRainfall).toFixed(1) || "0";
      let min =
        Number(this.forecastPreMaxList.minBasinRainfall).toFixed(1) || "0";
      return {
        name: name || "",
        value: `${max}` || "0",
        type: "basin",
      };
    },
    forecastMaxList() {
      let time = this.forecastPreCardList[this.forecastPreCardSelect].label;
      return [
        {
          label: `预计${time}最大降雨`,
          data: this.forecastPreMaxStation,
        },
        {
          label: `预计${time}最大降雨区域`,
          data: this.forecastPreMaxArea,
        },
        {
          label: `预计${time}最大降雨流域`,
          data: this.forecastPreMaxBasin,
        },
      ];
    },
    /** 实时降雨卡片数据 */
    realtimePreCardList() {
      let list = [
        this.realtimePreList.rainfall_newsest_1,
        this.realtimePreList.rainfall_1,
        this.realtimePreList.rainfall_3,
        this.realtimePreList.rainfall_6,
        this.realtimePreList.rainfall_24,
        this.realtimePreList.rainfall_72,
      ];
      return [
        {
          label: "近1小时",
          data: { number: list[0], unit: "mm" },
        },
        {
          label: "前1小时",
          data: { number: list[1], unit: "mm" },
        },
        {
          label: "前3小时",
          data: { number: list[2], unit: "mm" },
        },
        {
          label: "前6小时",
          data: { number: list[3], unit: "mm" },
        },
        {
          label: "前24小时",
          data: { number: list[4], unit: "mm" },
        },
        {
          label: "前72小时",
          data: { number: list[5], unit: "mm" },
        },
      ];
    },
    /** 实时极值列表数据 */
    realtimeMaxPoint() {
      const lat = this.realtimePreMaxList.maxPointStaInfo
        ? this.realtimePreMaxList.maxPointStaInfo.stLat
        : "";
      const lng = this.realtimePreMaxList.maxPointStaInfo
        ? this.realtimePreMaxList.maxPointStaInfo.stLong
        : "";
      return {
        areaName: this.realtimePreMaxList.maxRainfallAreaName,
        name: this.realtimePreMaxList.maxPointName || "",
        value: Number(this.realtimePreMaxList.maxPointRainfall).toFixed(1) || 0,
        type: "station",
        lat: lat,
        lng: lng,
      };
    },
    realtimeMaxArea() {
      return {
        name: this.realtimePreMaxList.maxRainfallAreaName || "",
        value: Number(this.realtimePreMaxList.areaMaxRainfall).toFixed(1) || 0,
        type: "area",
      };
    },
    realtimeMaxBasin() {
      return {
        name: this.realtimePreMaxList.maxRainfallBasinName || "",
        value: Number(this.realtimePreMaxList.basinMaxRainfall).toFixed(1) || 0,
        type: "basin",
      };
    },
    realtimeMaxList() {
      let time = this.realtimePreCardList[this.realtimePreCardSelect].label;
      return [
        {
          label: `${time}最大降雨点`,
          data: this.realtimeMaxPoint,
        },
        {
          label: `${time}最大降雨区域`,
          data: this.realtimeMaxArea,
        },
        {
          label: `${time}最大降雨流域`,
          data: this.realtimeMaxBasin,
        },
      ];
    },
  },
  watch: {
    currentSelectArea(val) {
      this.getRainfallData(val);
    },
  },
  mounted() {
    this.getRainfallData();
  },
  methods: {
    ...mapActions([
      "changeArea",
      "changeBasin",
      "getForecastRainfallLayer",
      "setCollapseLeft",
      "changeScreenLoading",
    ]),
    handleLeftPanelchange(status) {
      this.setCollapseLeft(status);
    },
    /** 短临预报极值点击 */
    forecastMaxListActive() {
      this.maxSelect = "forecast";
      this.ScreenMap.drawPoint([], {}, "maxPointLayer");
      this.ScreenMap.fullMap();
    },
    forecastMaxListChange(status, data) {
      let type = data.type;
      if (type == "station") {
        let point = [
          {
            lng: data.lng,
            lat: data.lat,
            no_pointermove: false,
            props: { 测站: `${(data.name, data.value)}mm` },
          },
        ];
        let style = {
          src: require("@/assets/images/WaterDisastersImage/torrent-flag.png"),
          anchor: [0.5, 1],
          anchorXUnits: "fraction",
          anchorYUnits: "fraction",
        };
        if (status) {
          this.ScreenMap.drawPoint(point, style, "maxPointLayer");
          this.ScreenMap.flyPoint([data.lng, data.lat], 12);
        } else {
          this.ScreenMap.drawPoint([], {}, "maxPointLayer");
          this.ScreenMap.fullMap();
        }
      } else if (type == "area") {
        status
          ? this.ScreenMap.showAreaLine(data.name, true)
          : this.ScreenMap.clearAreaLayer();
      } else if (type == "basin") {
        status
          ? this.ScreenMap.basinCommand(`${data.name}流域`)
          : this.ScreenMap.basinCommand("全流域");
      }
    },
    /** 实时降雨极值点击 */
    realtimeMaxListActive() {
      this.maxSelect = "realtime";
      this.ScreenMap.drawPoint([], {}, "maxPointLayer");
      this.ScreenMap.fullMap();
    },
    realtimeMaxListChange(status, data) {
      let type = data.type;
      if (type == "station") {
        let point = [
          {
            lng: data.lng,
            lat: data.lat,
            no_pointermove: false,
            name: `${data.name}测站`,
            props: { 雨量: `${(data.name, data.value)}mm` },
          },
        ];

        let style = {
          src: require("@/assets/images/WaterDisastersImage/torrent-flag.png"),
          anchor: [0.5, 1],
          anchorXUnits: "fraction",
          anchorYUnits: "fraction",
        };
        if (status) {
          this.ScreenMap.drawPoint(point, style, "maxPointLayer");
          this.ScreenMap.flyPoint([data.lng, data.lat], 12);
        } else {
          this.ScreenMap.drawPoint([], {}, "maxPointLayer");
          this.ScreenMap.fullMap();
        }
      } else if (type == "area") {
        status
          ? this.ScreenMap.showAreaLine(data.name, true)
          : this.ScreenMap.clearAreaLayer();
      } else if (type == "basin") {
        status
          ? this.ScreenMap.basinCommand(`${data.name}流域`)
          : this.ScreenMap.basinCommand("全流域");
      }
    },
    forecastConfig(options) {
      const minData = options.series[0].data;
      const maxData = options.series[1].data;
      const avgData = options.series[2].data;
      let colorArr = ["#15ddd7", "#7dfefa", "#9dfffc"];
      let colorArr2 = ["#2886c6", "#50bfda", "#89e3ec"];

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
      let color2 = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[1],
          },
          {
            offset: 1,
            color: colorArr2[1],
          },
        ],
      };
      let barWidth = 20;
      let constData = [];
      let showData = [];

      /** 最小柱形 */
      constData = minData.map((item) => (item ? 1 : 0));
      showData = minData.map((item) =>
        item
          ? item
          : {
              value: 1,
              itemStyle: {
                normal: {
                  borderColor: "rgba(0,0,0,0)",
                  borderWidth: 2,
                  color: "rgba(0,0,0,0)",
                },
              },
            }
      );
      const minSeries = [
        {
          z: 1,
          name: "最小值",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: minData,
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
          name: "最小值",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: [(-1 * barWidth) / 2, "50%"],
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
          name: "最小值",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: [(-1 * barWidth) / 2, "-50%"],
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
      /** 最大柱形 */
      constData = maxData.map((item) => (item ? 1 : 0));
      showData = maxData.map((item) =>
        item
          ? item
          : {
              value: 1,
              itemStyle: {
                normal: {
                  borderColor: "rgba(0,0,0,0)",
                  borderWidth: 2,
                  color: "rgba(0,0,0,0)",
                },
              },
            }
      );
      const maxSeries = [
        {
          z: 1,
          name: "最大值",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: maxData,
          itemStyle: {
            normal: {
              color: color2,
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
          name: "最大值",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: [barWidth / 2, "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color2,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "最大值",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: [barWidth / 2, "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr2[2],
              borderWidth: 2,
              color: colorArr2[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      /** 平均 */
      options.series = [
        ...minSeries,
        ...maxSeries,
        {
          name: "平均值",
          smooth: false,
          type: "line",
          data: avgData,
        },
      ];

      options.legend.data = [...options.legend.data, "平均值"];
      options.legend.selectedMode = false;
      return options;
    },
    realtimeConfig(options) {
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
    openDetailsDialog(val) {
      this.$emit("open-analyse-dialog", val);
    },
    /** 初始化雨量数据 */
    getRainfallData(areaName) {
      this.realtimePreCardLoading = true;
      let opt = {
        areaName: "",
      };
      if (areaName && areaName != "温州市") {
        opt.areaName = areaName;
      } else {
        opt.areaName = "";
      }
      disasterPreventionApi.predictRainfallByAreaName(opt).then((res) => {
        if (res.code == 0) {
          this.forecastPreList = res.data;
        }
      });
      disasterPreventionApi.realtimeRainfallByAreaName(opt).then((res) => {
        if (res.code == 0) {
          this.realtimePreList = res.data;
          this.realtimePreCardLoading = false;
        }
        this.rainList = this.realtimePreList["rainfall_newsest_1_data"];
      });
      this.getForecastPreMaxValue(1);
      this.getRealtimePreMaxValue(0);
    },
    /** 获得预报极值数据 */
    getForecastPreMaxValue(hour) {
      this.forecastChartLoading = true;
      disasterPreventionApi.rainfallMaxValuePredict({ hour }).then((res) => {
        this.forecastPreMaxList = { ...res.data };
      });
      if (
        this.forecastAreaRainfallChart.length == 0 &&
        this.forecastBasinRainfallChart.length == 0
      ) {
        disasterPreventionApi
          .rainfallPredictGrid({ type: "流域" })
          .then((res) => {
            this.forecastBasinRainfallChart = res.data;
          });
        disasterPreventionApi
          .rainfallPredictGrid({ type: "行政区划" })
          .then((res) => {
            this.forecastAreaRainfallChart = res.data;
            this.forecastChartTabChange(this.forecastPreChartTab, hour);
          });
      }
      this.forecastChartTabChange(this.forecastPreChartTab, hour);
      this.forecastChartLoading = false;
    },
    /** 获得实时极值数据 */
    getRealtimePreMaxValue(hour) {
      this.realtimeChartLoading = true;
      disasterPreventionApi
        .rainfallMaxValueAndGridRealtime({ hour })
        .then((res) => {
          this.realtimePreMaxList = res.data;
          disasterPreventionApi
            .grandTotalReservoirRainfall({ hour })
            .then((res) => {
              this.realtimePreMaxList.resRainfallChart = res.data;
              this.realtimePreChartTabChange(0);
              this.realtimeChartLoading = false;
            });
        });
    },
    /** 选中1，3，6小时降雨卡片降切换 */
    precipitationCardClick(val) {
      if (val == null) {
        this.ScreenMap.removeLayerByName("rainfall");
        this.ScreenMap.clearTextLayer();
        this.$emit("legend-change", "实时降雨量", {});
        this.$emit("legend-change", "预报降雨量", {});
        return;
      }
      let valMap = [1, 3, 6];
      this.cardSelect = "forecast";
      let data = this.forecastPreList[`rainfall_${valMap[val]}_data`];
      if (data) {
        let arr = [];
        for (let item of data) {
          if (item.rainfall > 0) {
            arr.push(item);
          }
        }
        if (arr.length > 0) {
          this.changeScreenLoading(true);
          let params = this.formatRainfallParam(arr, "rainfall");
          this.ScreenMap._showTestPoint(params.features);
          this.getForecastRainfallLayer({
            opt: params,
            type: false,
            area: this.currentSelectArea,
            hour: valMap[val],
          }).then((result) => {
            let mapImage = result.mapImage;
            this.changeScreenLoading(false);
            this.getIsohyetOpacity(
              mapImage.xmin,
              mapImage.ymin,
              mapImage.xmax,
              mapImage.ymax,
              result.jobId,
              "forecast"
            );
          });
        } else {
          this.changeScreenLoading(false);
          this.ScreenMap.removeLayerByName("rainfall");
          this.ScreenMap.clearTextLayer();
        }
        // console.log(params,3)
        /* this.getIsohyetArea(params); */
      }
      this.getForecastPreMaxValue(valMap[val]);
      this.$emit("legend-change", "实时降雨量", {});
      this.$emit("legend-change", "预报降雨量", {
        label: "预报降雨量",
        children: [
          {
            label: ">70",
            icon: "forecast-100",
          },
          { label: "50~70", icon: "forecast-70" },
          { label: "20~50", icon: "forecast-50" },
          { label: "10~20", icon: "forecast-25" },
          { label: "3~10", icon: "forecast-10" },
          { label: "1~3", icon: "forecast-1" },
          { label: "<1", icon: "forecast-0" },
        ],
      });
    },
    //实时等值面开关点击事件
    clickRealtimeRainfull(btnType) {
      // console.log(this.rainList);
      this[btnType] = !this[btnType];
      this.showRealTimeRainfull();
    },
    // 展示实时面雨量
    showRealTimeRainfull() {
      if (this.realdzmbtn) {
        if (this.rainList.length > 0) {
          this.changeScreenLoading(true);
          let params = this.formatRainfallParam(this.rainList, "drp");
          if (params.features.length > 0) {
            this.getIsohyetArea(params, true);
          } else {
            this.changeScreenLoading(false);
            this.ScreenMap.clearTextLayer();
            this.ScreenMap.removeLayerByName("rainfall");
          }
        }
        this.$emit("legend-change", "预报降雨量", {});
        this.$emit("legend-change", "实时降雨量", {
          label: "实时降雨量",
          children: [
            { label: ">250", icon: "realtime-250" },
            { label: "100~250", icon: "realtime-100" },
            { label: "50~100", icon: "realtime-50" },
            { label: "25~50", icon: "realtime-25" },
            { label: "10~25", icon: "realtime-10" },
            { label: "0.1~10", icon: "realtime-0_1" },
          ],
        });
      } else {
        this.ScreenMap.removeLayerByName("rainfall");
        this.$emit("legend-change", "实时降雨量", {});
        this.$emit("legend-change", "预报降雨量", {});
        return;
      }
    },
    /** 实时降雨1，3，6，12，24，72小时卡片降雨切换 */
    realtimePreCardClick(val) {
      this.ScreenMap.clearTextLayer();
      this.realtimePreChartTab = 0;
      if (val == null) {
        this.realdzmbtn = false;
        this.ScreenMap.removeLayerByName("rainfall");
        this.$emit("legend-change", "实时降雨量", {});
        this.$emit("legend-change", "预报降雨量", {});
        return;
      }

      let valMap = [0, 1, 3, 6, 24, 72];
      this.cardSelect = "realtime";
      let data =
        this.realtimePreList[
          `rainfall_${valMap[val] != 0 ? valMap[val] : "newsest_1"}_data`
        ];
      this.rainList = data;
      //如果等值面开关是开的 加载面雨量图层
      if (this.rainList && this.realdzmbtn) {
        this.showRealTimeRainfull();
      }
      // if (data) {
      //   this.changeScreenLoading(true);
      //   let params = this.formatRainfallParam(data, "drp");
      //   if (params.features.length > 0) {
      //     this.getIsohyetArea(params, true);
      //   } else {
      //     this.changeScreenLoading(false);
      //     this.ScreenMap.clearTextLayer();
      //     this.ScreenMap.removeLayerByName("rainfall");
      //   }
      // }
      this.getRealtimePreMaxValue(valMap[val]);
      // this.$emit("legend-change", "预报降雨量", {});
      // this.$emit("legend-change", "实时降雨量", {
      //   label: "实时降雨量",
      //   children: [
      //     { label: ">250", icon: "realtime-250" },
      //     { label: "100~250", icon: "realtime-100" },
      //     { label: "50~100", icon: "realtime-50" },
      //     { label: "25~50", icon: "realtime-25" },
      //     { label: "10~25", icon: "realtime-10" },
      //     { label: "0.1~10", icon: "realtime-0_1" },
      //   ],
      // });
    },
    /** 实时降雨图表tabs切换 */
    realtimePreChartTabChange(index) {
      let tab = this.realtimePreChartTabList[index];
      let data = {
        行政区划: this.realtimePreMaxList.areaRainfallChart || [],
        流域: this.realtimePreMaxList.basinRainfallChart || [],
        水库: this.realtimePreMaxList.resRainfallChart || [],
      };
      if (!data[tab]) return;
      data[tab].sort((a, b) => b.value - a.value);
      this.realtimeChartData.rows = [];
      let rows = [];
      for (let item of data[tab]) {
        if (rows.length < 5) rows.push({ name: item.key, data: item.value });
        else break;
      }
      this.realtimeChartData.rows = rows;
    },
    /** 短临预报降雨图表tabs切换 */
    forecastChartTabChange(index, hour) {
      hour = hour || [1, 3, 6][this.forecastPreCardSelect];
      let tab = this.forecastPreChartTabList[index];
      let data = {
        行政区划: this.forecastAreaRainfallChart || [],
        流域: this.forecastBasinRainfallChart || [],
      };
      if (!data[tab]) return;
      this.forecastChartData.rows = [];
      let rows = [];
      const sortData = data[tab].sort(
        (a, b) => b[`rainfall_${hour}`] - a[`rainfall_${hour}`]
      );
      for (let item of sortData) {
        if (rows.length < 5) {
          rows.push({
            name: item.key,
            max: item[`${hour}hourMaxRainfall`],
            min: item[`${hour}hourMinRainfall`],
            avg: item[`rainfall_${hour}`],
          });
        } else break;
      }
      this.forecastChartData.rows = rows;
    },
    /** 雨量数据转换成等雨量面参数格式 */
    formatRainfallParam(list, paramName) {
      let geoJson = {
        features: [],
      };
      let featuresArr = [];
      list.forEach((item) => {
        // let drp = item.drp > 0 ? item.drp : 0;
        // if (item[paramName] != "-" && item[paramName] > 0) {
        let obj = {
          attributes: {
            x: Number(item.longitude),
            y: Number(item.latitude),
            value: item[paramName],
          },
          geometry: {
            x: Number(item.longitude),
            y: Number(item.latitude),
          },
        };
        featuresArr.push(obj);
        // }
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    /** 获取等雨量面 type为true时 实时等雨量面 展示数字 */
    async getIsohyetArea(opt, type) {
      this.ScreenMap.clearTextLayer();
      // if(!type){
      //  this.ScreenMap._showTestPoint(opt.features);
      // }
      let params = new URLSearchParams();
      params.append("station", JSON.stringify(opt));
      params.append("f", "json");
      params.append("Extent", JSON.stringify(areaWzBorder));
      let _this = this;
      let areaUrl =
        "http://117.149.227.112:6080/arcgis/rest/services/gps/PointTRaster/GPServer/PointTRaster/submitJob";
      if (type) {
        areaUrl =
          "http://112.17.127.75:8008/arcgis/rest/services/gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/submitJob";
      }
      await axios({
        url: areaUrl,
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then(async (res) => {
        let interVal = setInterval(async () => {
          let url =
            "http://117.149.227.112:6080/arcgis/rest/services/gps/PointTRaster/GPServer/PointTRaster/jobs/" +
            res.data.jobId +
            "/results/Idwres?f=pjson";
          if (type) {
            url =
              "http://112.17.127.75:8008/arcgis/rest/services/gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/jobs/" +
              res.data.jobId +
              "/results/Idwres?f=pjson";
          }
          await axios({
            url: url,
            method: "get",
          }).then(async (result) => {
            if (!result.data.value.mapImage.error) {
              clearInterval(interVal);
              //加载透明等雨量面
              let mapImage = result.data.value.mapImage.extent;
              _this.getIsohyetOpacity(
                mapImage.xmin,
                mapImage.ymin,
                mapImage.xmax,
                mapImage.ymax,
                res.data.jobId,
                type
              );
            }
          });
        }, 1000);
      });
    },
    async getTextPoint(jobId) {
      let url = `http://117.149.227.112:6080/arcgis/rest/services/gps/DZPIDWFurLabel/MapServer/jobs/${jobId}/1/query?where=1=1&outFields=*&f=json`;
      await axios({
        url: url,
        method: "get",
      }).then(async (result) => {
        this.ScreenMap._showTestPoint(result.data.features);
      });
    },
    async getIsohyetOpacity(xmin, ymin, xmax, ymax, jobId, type) {
      //type用来区分预报和实时等值面服务 forecast为预报 预报请求112 实时请求117
      let bbox = xmin + "," + ymin + "," + xmax + "," + ymax;
      let params = new URLSearchParams();
      params.append("bbox", bbox);
      params.append("f", "pjson");
      params.append("transparent", true);
      let url = `http://112.17.127.75:8008/arcgis/rest/services/gps/dengzhiPIDWReal0605/MapServer/jobs/${jobId}/export`;
      if (type == "forecast") {
        url = `http://112.17.127.75:8008/arcgis/rest/services/gps/PointTRaster/MapServer/jobs/${jobId}/export`;
      }
      await axios({
        url: url,
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then((res) => {
        this.changeScreenLoading(false);
        this.ScreenMap.showIsohyetLayer(res.data);
      });
    },
    /**
     * 雨量点位开关
     * @param {number} index 开关的序号
     */
    handleRainStationChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        anchor: [5, 5],
        anchorXUnits: "pixels",
      };
      let pointList = [];
      for (let point of this.rainList) {
        let imgSrc = require("@/assets/images/legend/rain_0.png");
        if (point.drp > 0) {
          imgSrc = require("@/assets/images/legend/rain_10.png");
        }
        if (point.drp > 10) {
          imgSrc = require("@/assets/images/legend/rain_10_25.png");
        }
        if (point.drp > 25) {
          imgSrc = require("@/assets/images/legend/rain_25_50.png");
        }
        if (point.drp > 50) {
          imgSrc = require("@/assets/images/legend/rain_50_100.png");
        }
        if (point.drp > 100) {
          imgSrc = require("@/assets/images/legend/rain_100_250.png");
        }
        if (point.drp >= 250) {
          imgSrc = require("@/assets/images/legend/rain_250.png");
        }
        pointList.push({
          name: point.stationName,
          type: "雨量",
          lng: point.longitude,
          lat: point.latitude,
          code: point.stcd,
          props: { 雨量: `${point.drp}mm`, 位置: `${point.stLoc || "-"}` },
          src: imgSrc,
        });
      }
      if (this[btnType]) {
        this.ScreenMap.drawPoint(pointList, style, "rainLayer");
      } else {
        this.ScreenMap.drawPoint([], {}, "rainLayer");
      }
    },
    /**
     * 跳转一张图
     */
    goToAmap(type) {
      this.$router.push({
        path: "/hydraulicAmap",
        query: { selectType: type },
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "../../style/SildePanel.scss";
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}
.typhoon-search {
  width: 360px;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  > p {
    padding: 8px 0 12px 0;
    color: #fff;
  }
  .search-row {
    margin-bottom: 12px;
    line-height: 44px;
    .search-label {
      color: #fff;
      height: 100%;
      @include flexbox;
      @include flexAC;
      @include flexJC(center);
    }
  }
  .primary-btn {
    color: #fff;
    margin: 6px 0;
  }
  .typhoon-list {
    height: 270px;
  }
}

.prediction-sty {
  text-align: center;
  color: #ffffff;
  padding-top: 15px;
  padding-bottom: 15px;
  .border-sty {
    border-left: 1px solid rgba(35, 140, 239, 0.4);
    border-right: 1px solid rgba(35, 140, 239, 0.4);
  }
  .rainfall-sty {
    font-size: 20px;
    color: #1cfff5;
  }
}

.areal-rainfall-sty {
  display: flex;
  justify-content: space-between; /* 横向中间自动空间 */
  > div:nth-child(1) {
    vertical-align: middle;
    > i {
      vertical-align: middle;
    }
    > span {
      color: #fff;
    }
  }
  > div:nth-child(2) {
    line-height: 21px;
    .tab-switch-sty {
      padding: 2px 10px;
      border-radius: 15px;
      background: #00c1ff;
    }
    > span {
      color: #fff;
      margin-left: 15px;
      font-size: 15px;
    }
  }
}

.early-warning-sty {
  font-size: 20px;
  font-weight: bold;
  padding: 15px 20px;
  background-image: linear-gradient(
    to right,
    rgba(117, 124, 52, 0.3),
    rgba(0, 0, 0, 0)
  );
  > p {
    color: #eec80b;
  }
  > p:nth-child(2) {
    margin-top: 16px;
    > span {
      color: #eec80b;
      > span {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}
.early-warning-style {
  font-size: 20px;
  font-weight: bold;
  padding: 15px 20px;
  background-image: linear-gradient(
    to right,
    rgba(192, 110, 63, 0.3),
    rgba(0, 0, 0, 0)
  );
  > p {
    color: #ff7144;
  }
  > p:nth-child(2) {
    margin-top: 16px;
    > span {
      color: #ff7144;

      > span {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}

.switch-list {
  ::v-deep.el-carousel--horizontal {
    overflow-x: hidden;
    overflow-y: hidden;
  }
}

.sidebar-list {
  .warning-style {
    font-size: 16px;
    color: #ffffff;
    line-height: 24px;
    font-weight: 400;
  }
  ::v-deep.el-carousel--horizontal {
    overflow-x: hidden;
    overflow-y: hidden;
    height: unset;
  }
}
</style>
<style lang="scss" scoped>
::v-deep.el-carousel {
  height: 100%;
  .el-carousel__container {
    height: 100%;
  }
}
</style>
